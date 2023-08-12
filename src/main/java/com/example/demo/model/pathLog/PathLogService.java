package com.example.demo.model.pathLog;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PathLogService {

    @Autowired
    private PathLogRepository pathLogRepository;

    @Transactional
    public void createNewPathLog(PathLog contents) {
        pathLogRepository.save(contents);
    }

    @Transactional
    public void deleteByCreatedAtBefore(Long id) {
        pathLogRepository.deleteById(id);
    }

    public List<PathLog> getData() {
      return   pathLogRepository.findAll();
    }

    public List<PathLog> getDataViews() {
        return   pathLogRepository.findByStatusAndPathStartingWithAndPathNotLike(200, "/articles/", "/comments%");
    }
}
