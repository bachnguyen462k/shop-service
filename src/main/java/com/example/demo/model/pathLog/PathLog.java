package com.example.demo.model.pathLog;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "pathLog")
@Entity
@Data
public class PathLog {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "ipAddress")
    private String ipAddress;

    @Column(name = "path")
    private String path;

    @Column(name = "requestMethod")
    private String requestMethod;

    @Column(name = "sizeScreen")
    private String sizeScreen;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "platform")
    private String platform;

    @Column(name = "userAgent")
    private String userAgent;

    @Column(name = "requestId")
    private String requestId;

    @Column(name = "status")
    private int status;

    @Column(name = "referer")
    private String referer;

    @Column(name = "token")
    private String token;

    @Column(name = "role")
    private String role;
}
