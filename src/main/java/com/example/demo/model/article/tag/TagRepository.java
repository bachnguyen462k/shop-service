package com.example.demo.model.article.tag;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface TagRepository extends Repository<Tag, Long> {

    List<Tag> findAll();

    Optional<Tag> findFirstByValue(String value);
}
