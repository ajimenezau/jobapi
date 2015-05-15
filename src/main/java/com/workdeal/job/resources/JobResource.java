package com.workdeal.job.resources;

import javax.ws.rs.Consumes;
import com.wordnik.swagger.annotations.*;

import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;
import com.workdeal.job.db.JobDAO;
import com.workdeal.job.model.Job;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.*;

@Path("/job")
@Api(value = "/job", description = "Operation on Job")
@Produces(MediaType.APPLICATION_JSON)
public class JobResource {

    private final JobDAO jobDAO;

    @Inject
    public JobResource(final JobDAO jobDAO) {
        this.jobDAO = jobDAO;
    }

    @POST
    @Timed
    @UnitOfWork
    @Consumes(APPLICATION_JSON)
    @ApiOperation(value = "Create Job", response = Job.class)
    @ApiResponses(value = {@ApiResponse(code = 422, message = "Job already exists")})
    public Response createJob(@Valid Job job) {
        Job created = jobDAO.create(job);
        return status(CREATED).entity(created).build();
    }

    @GET
    @Timed
    @UnitOfWork
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
    @ApiOperation(value = "Get all Job", response = List.class)
    public List<Job> getAllJob() {
        return jobDAO.readAll();
    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
    @ApiOperation(value = "Get Job by Id", response = Job.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Job not found")})
    public Optional<Job> getJobById(@ApiParam(required = true) @PathParam("id") final Long id ) {
        return jobDAO.readById(id);
    }

}
