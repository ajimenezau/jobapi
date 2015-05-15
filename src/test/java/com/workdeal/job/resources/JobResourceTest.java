package com.workdeal.job.resources;

import java.util.Optional;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import com.workdeal.job.JsonFixtureTest;
import com.workdeal.job.db.JobDAO;
import com.workdeal.job.model.Job;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import io.dropwizard.java8.jersey.OptionalMessageBodyWriter;
import io.dropwizard.java8.jersey.OptionalParamFeature;

@RunWith(MockitoJUnitRunner.class)
public class JobResourceTest extends JsonFixtureTest {

    private final static JobDAO jobDAO = mock(JobDAO.class);

    @ClassRule
    public static ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new JobResource(jobDAO))
            .addProvider(OptionalMessageBodyWriter.class)
            .addProvider(OptionalParamFeature.class)
            .build();

    @Before
    public void setUp () {
    }

    @Test
    public void createJob () {
        // given
        when(jobDAO.create(any(Job.class))).thenReturn(givenJobWithId(1L));

        // when
        Response response = resources.client().target("/job").request().post(Entity.json(givenJobWithId(null)));

        // then
        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());

        Job job = response.readEntity(Job.class);
        assertThat(job.getId()).isNotNull();
        assertThat(job).isEqualToComparingFieldByField(givenJobWithId(1L));
    }


    @Test
    public void getJobById () {
        // given
        when(jobDAO.readById(1L)).thenReturn(Optional.of(givenJobWithId(1L)));

        // when
        Response response = resources.client().target("/job/1").request().get();

        // then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

        Job job = response.readEntity(Job.class);
        assertThat(job).isEqualToComparingFieldByField(givenJobWithId(1L));
    }


    @Test
    public void getAllJob () {
        // given
        when(jobDAO.readAll()).thenReturn(Arrays.<Job>asList(
            givenJobWithId(1L),
            givenJobWithId(2L)
        ));

        // when
        Response response = resources.client().target("/job").request().get();
        List<Job> result = resources.client().target("/job").request().get(List.class);

        // then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(result.size()).isEqualTo(2);
    }
}
