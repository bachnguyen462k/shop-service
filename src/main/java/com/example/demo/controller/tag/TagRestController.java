package com.example.demo.controller.tag;

import com.example.demo.model.article.tag.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static java.util.stream.Collectors.toSet;

@RequestMapping("/api/tags")
@RestController
class TagRestController {

    private final TagService tagService;

    TagRestController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public TagsModel getTags() {
        return new TagsModel(tagService.findAll().stream()
                .map(Objects::toString)
                .collect(toSet()));
    }
}
