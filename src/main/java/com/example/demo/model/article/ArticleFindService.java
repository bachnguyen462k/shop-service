package com.example.demo.model.article;

import java.util.Optional;

public interface ArticleFindService {

    Optional<Article> getArticleBySlug(String slug);
    Optional<Article> getArticleByView();
}
