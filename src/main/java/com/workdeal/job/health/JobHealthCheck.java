package com.workdeal.job.health;

import com.hubspot.dropwizard.guice.InjectableHealthCheck;

public class JobHealthCheck extends InjectableHealthCheck {
    @Override
    public String getName() {
        return "Lazybones Health";
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
