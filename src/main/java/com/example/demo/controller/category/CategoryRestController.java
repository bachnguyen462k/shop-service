package com.example.demo.controller.category;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/category")
@RestController
public class CategoryRestController {
//    private final TagService tagService;
//
//    TagRestController(TagService tagService) {
//        this.tagService = tagService;
//    }
//
//    @GetMapping
//    public TagsModel getTags() {
//        return new TagsModel(tagService.findAll().stream()
//                .map(Objects::toString)
//                .collect(toSet()));
//    }
}
