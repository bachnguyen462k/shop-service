package com.example.demo.model.pathLog;

import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

interface PathLogRepository extends Repository<PathLog, Long> {

    PathLog save(PathLog data);


    void deleteById(Long id);

    List<PathLog> findAll();

    List<PathLog> findByPathStartingWithAndStatus(String s, int i);

    List<PathLog> findByStatusAndPathStartingWithAndPathNotLike(int i, String s, String s1);
}
