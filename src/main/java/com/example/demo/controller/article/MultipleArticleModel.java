package com.example.demo.controller.article;

import com.example.demo.model.article.Article;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
class MultipleArticleModel {

    List<ArticleModel.ArticleModelNested> articles;
    int articlesCount;

    static MultipleArticleModel fromArticles(Page<Article> articles) {
        final var articlesCollected = articles.map(ArticleModel.ArticleModelNested::fromArticle)
                .stream().collect(toList());
        return new MultipleArticleModel(articlesCollected, articlesCollected.size());
    }

}

@Value
class MultipleArticleModelList {

    List<ArticleModel.ArticleModelNestedList> articles;
    int articlesCount;


    static MultipleArticleModelList fromArticleList(Page<Article> articles) {
        final var articlesCollectedList = articles.map(ArticleModel.ArticleModelNestedList::fromArticleList)
                .stream().collect(toList());
        return new MultipleArticleModelList(articlesCollectedList, articlesCollectedList.size());
    }
}