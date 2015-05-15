package com.workdeal.job.model;

import javax.validation.constraints.NotNull;
import com.wordnik.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "job")
@ApiModel(value = "Representation of a Job")
public class Job {

    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonProperty
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }
}
