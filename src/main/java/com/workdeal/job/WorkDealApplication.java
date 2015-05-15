package com.workdeal.job;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.reader.ClassReaders;
import com.workdeal.job.db.MongoManaged;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class WorkDealApplication extends Application<WorkDealConfiguration> {

    public static void main(String[] args) throws Exception {
        new WorkDealApplication().run(args);
    }

    @Override
    public void initialize(final Bootstrap<WorkDealConfiguration> bootstrap) {
        final WorkDealModule guiceModule = new WorkDealModule();
        bootstrap.addBundle(new Java8Bundle());
        bootstrap.addBundle(new TemplateConfigBundle());
        bootstrap.addBundle(guiceModule.getMigrationBundle());
        bootstrap.addBundle(guiceModule.getHibernateBundle());
        bootstrap.addBundle(guiceModule.getGuiceBundle());
        bootstrap.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void run(WorkDealConfiguration configuration, Environment environment) throws Exception {
        setUpSwagger(environment);
        setUpMongo(configuration, environment);
    }

    private void setUpMongo(final Configuration configuration, Environment environment) {
        MongoManaged mongoManaged = new MongoManaged(configuration.mongo);
        environment.lifecycle().manage(mongoManaged);
        environment.healthChecks().register("MongoHealthCheck", new MongoHealthCheck(mongoManaged));
    }

    private void setUpSwagger(final Environment environment) {
        // Swagger Resource
        environment.jersey().register(new ApiListingResourceJSON());

        // Swagger providers
        environment.jersey().register(new ApiDeclarationProvider());
        environment.jersey().register(new ResourceListingProvider());

        // Swagger Scanner, which finds all the resources for @Api Annotations
        ScannerFactory.setScanner(new DefaultJaxrsScanner());

        // Add the reader, which scans the resources and extracts the resource information
        ClassReaders.setReader(new DefaultJaxrsApiReader());

        // Set the swagger config options
        ApiInfo apiInfo= new ApiInfo(this.getName(), "description of service", null, "contact me", null, null );
        ConfigFactory.config().setApiInfo(apiInfo);
    }


}
