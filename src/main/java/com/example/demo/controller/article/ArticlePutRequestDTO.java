package com.example.demo.controller.article;

import com.example.demo.model.article.ArticleTitle;
import com.example.demo.model.article.ArticleUpdateRequest;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

import static com.example.demo.model.article.ArticleUpdateRequest.builder;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static java.util.Optional.ofNullable;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Value
class ArticlePutRequestDTO {

    String title;
    String description;
    String body;

    ArticleUpdateRequest toUpdateRequest() {
        return builder().titleToUpdate(ofNullable(title).map(ArticleTitle::of).orElse(null))
                .descriptionToUpdate(description)
                .bodyToUpdate(body)
                .build();
    }
}
