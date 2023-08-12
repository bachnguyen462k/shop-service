package com.example.demo.model.property;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "property")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Property {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "CODE")
    private String code = "";
    @Column (name = "VALUE")
    private String value = "";
    @Column (name = "DESCRIPTION")
    private String description = "";
    @Column (name = "WEIGHT")
    private String weight = "";
    @Version
    @Column (name ="VERSION")
    private long version = 0;



}
