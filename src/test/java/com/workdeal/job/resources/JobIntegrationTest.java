package com.workdeal.job.resources;

import org.junit.FixMethodOrder;
import org.junit.Test;
import com.workdeal.job.IntegrationTest;
import com.workdeal.job.model.Job;
import org.junit.runners.MethodSorters;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class JobIntegrationTest extends IntegrationTest {

    @Test
    public void createJob () {
        // when
        Response response = client.target(
                String.format("http://localhost:%d/job", RULE.getLocalPort()))
                .request()
                .post(Entity.json(givenJobWithId(null)), Response.class);

        // then
        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());

        Job job = response.readEntity(Job.class);
        assertThat(job.getId()).isNotNull();
        assertThat(job).isEqualToComparingFieldByField(givenJobWithId(1L));
    }

    @Test
    public void getAllJob () {
        // when
        List<Job> jobList = client.target(
                String.format("http://localhost:%d/job", RULE.getLocalPort()))
                .request()
                .get(List.class);

        // then
        assertThat(jobList).hasSize(1);
    }

    @Test
    public void getJobById () {
        // given
        Job job = readFromJson("fixtures/job.json", Job.class);

        // when
        Job JobResult = client.target(
                String.format("http://localhost:%d/job/1", RULE.getLocalPort()))
                .request()
                .get(Job.class);

        // then
        assertThat(JobResult).isEqualToIgnoringGivenFields(job, "id");
    }

}
