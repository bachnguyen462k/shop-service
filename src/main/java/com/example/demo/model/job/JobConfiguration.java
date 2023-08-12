package com.example.demo.model.job;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "jobRunService")
@Entity
@Data
public class JobConfiguration {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(name = "jobName")
    private String jobName = "";

    @Column(name = "status")
    private String status = "";

    @Column(name = "runItems")
    private String runItems = "";

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "runNow")
    private int runNow ;

}
