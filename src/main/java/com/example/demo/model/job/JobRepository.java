package com.example.demo.model.job;

import com.example.demo.model.user.User;
import org.springframework.data.repository.Repository;

import java.util.List;

interface JobRepository extends Repository<JobConfiguration, Long> {

    JobConfiguration save(JobConfiguration data);

    List<JobConfiguration> findAll();

    void deleteById(Long id);

    JobConfiguration findJobByJobName(String s);
}
