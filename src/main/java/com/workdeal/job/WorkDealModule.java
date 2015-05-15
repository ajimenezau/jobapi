package com.workdeal.job;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.netflix.governator.guice.LifecycleInjector;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.netflix.governator.guice.lazy.LazySingleton;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;

import java.time.Clock;
import javax.ws.rs.client.Client;


public class WorkDealModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionFactory.class).toProvider(hibernateBundle::getSessionFactory);
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
    }

    @Provides
    @LazySingleton
    public final Client provideClient (Environment environment, WorkDealConfiguration configuration) {
        return new JerseyClientBuilder(environment)
                .using(configuration.getClient())
                .build("test client");
    }

    public final GuiceBundle<WorkDealConfiguration> getGuiceBundle() {
        return guiceGovernatorBundle;
    }

    public final HibernateBundle<WorkDealConfiguration> getHibernateBundle() {
        return hibernateBundle;
    }

    public final MigrationsBundle<WorkDealConfiguration> getMigrationBundle() {
        return migrationsBundle;
    }

    private final HibernateBundle<WorkDealConfiguration> hibernateBundle =
        new ScanningHibernateBundle<WorkDealConfiguration>("com.workdeal.job.model") {
            @Override
            public DataSourceFactory getDataSourceFactory(WorkDealConfiguration configuration) {
                return configuration.getDataSource();
            }
        };

    private final MigrationsBundle<WorkDealConfiguration> migrationsBundle =
        new MigrationsBundle<WorkDealConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(WorkDealConfiguration configuration) {
                return configuration.getDataSource();
            }
        };

    private final GuiceBundle<WorkDealConfiguration> guiceGovernatorBundle = GuiceBundle.<WorkDealConfiguration>newBuilder()
        .addModule(this)
        .enableAutoConfig("com.workdeal.job","trunk.dropwizard.newrelic")
        .setConfigClass(WorkDealConfiguration.class)
        .setInjectorFactory((stage, modules) -> LifecycleInjector.builder()
            .inStage(stage)
            .withModules(modules)
            .build()
            .createInjector())
        .build();

}
