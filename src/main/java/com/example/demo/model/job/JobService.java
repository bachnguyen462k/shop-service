package com.example.demo.model.job;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Lazy
@Service
public class JobService {

    private final JobRepository jobRepository;

    JobService(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }

    @Transactional
    public JobConfiguration createNewJobConfiguration(JobConfiguration contents) {
        return jobRepository.save(contents);
    }

    @Transactional(readOnly = true)
    public List<JobConfiguration> getPayJob() {
        return jobRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void deleteById(Long id) {
        jobRepository.deleteById(id);
    }


    public JobConfiguration findJobByJobName(String s) {
        return  jobRepository.findJobByJobName(s);
    }
}
