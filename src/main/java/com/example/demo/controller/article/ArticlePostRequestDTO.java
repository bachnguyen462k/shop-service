package com.example.demo.controller.article;

import com.example.demo.model.article.ArticleContents;
import com.example.demo.model.article.ArticleTitle;
import com.example.demo.model.article.tag.Tag;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Value
class ArticlePostRequestDTO {

    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotBlank
    String body;
    @NotNull
    Set<Tag> tagList;
    @NotBlank
    String imageUrl="";

    @NotBlank
    String category="";

    ArticleContents toArticleContents() {
        return new ArticleContents(description, ArticleTitle.of(title), body, tagList,imageUrl,category);
    }
}
