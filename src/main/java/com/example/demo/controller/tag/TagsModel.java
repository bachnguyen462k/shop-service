package com.example.demo.controller.tag;

import lombok.Getter;

import java.util.Set;

@Getter
class TagsModel {

    private final Set<String> tags;

    TagsModel(Set<String> tags) {
        this.tags = tags;
    }
}
