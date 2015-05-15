package com.workdeal.job.health;

import com.workdeal.job.db.MongoManaged;
import com.codahale.metrics.health.HealthCheck;

public class MongoHealthCheck extends HealthCheck {

    private MongoManaged mongo;

    public MongoHealthCheck(MongoManaged mongoManaged) {
        this.mongo = mongoManaged;
    }

    @Override
    protected Result check() throws Exception {
        mongo.getDb();
        return Result.healthy();
    }

}