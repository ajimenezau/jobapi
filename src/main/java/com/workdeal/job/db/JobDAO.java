package com.workdeal.job.db;

import java.util.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import com.workdeal.job.model.Job;

import javax.inject.Inject;
import java.util.List;

public class JobDAO extends AbstractDAO<Job> {

    @Inject
    public JobDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Job create(Job job) {
        return persist(job);
    }

    public List<Job> readAll() {
        return list(currentSession().createCriteria(Job.class));
    }

    public Optional<Job> readById(Long id) {
        return Optional.ofNullable(get(id));
    }
}
