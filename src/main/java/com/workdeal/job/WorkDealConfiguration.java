package com.workdeal.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class WorkDealConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory dataSource = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty
    private JerseyClientConfiguration client = new JerseyClientConfiguration();

    public DataSourceFactory getDataSource() {
        return dataSource;
    }

    public JerseyClientConfiguration getClient() {
        return client;
    }

    @Valid
    @NotNull
    public MongoConfiguration mongo = new MongoConfiguration();
}
