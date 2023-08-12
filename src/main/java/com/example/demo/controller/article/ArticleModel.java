package com.example.demo.controller.article;

import com.example.demo.controller.user.ProfileModel;
import com.example.demo.model.article.Article;
import com.example.demo.model.article.tag.Tag;
import com.example.demo.model.category.Category;
import lombok.Value;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Value
class ArticleModel {

    ArticleModelNested article;

    static ArticleModel fromArticle(Article article) {
        return new ArticleModel(ArticleModelNested.fromArticle(article));
    }

    @Value
    static class ArticleModelNested {
        String slug;
        String title;
        String description;
        String body;
        Set<String> tagList;
        String createdAt;
        String updatedAt;
        boolean favorited;
        int favoritesCount;
        ProfileModel.ProfileModelNested author;
        String imageUrl;
        Set<String> category;
        int view;
        String timeRead;
        List<String> mucluc;

        static ArticleModelNested fromArticle(Article article) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final var contents = article.getContents();
            final var titleFromArticle = contents.getTitle();
            final var category = article.getCategory();
            return new ArticleModelNested(
                    titleFromArticle.getSlug(),
                    titleFromArticle.getTitle(),
                    contents.getDescription(),
                    contents.getBody(),
                    contents.getTags().stream().map(Tag::toString).collect(toSet()),
                    dateFormat.format(article.getCreatedAt()),
                    dateFormat.format(article.getUpdatedAt()),
                    article.isFavorited(),
                    article.getFavoritedCount(),
                    ProfileModel.ProfileModelNested.fromProfile(article.getAuthor().getProfile()),
                    contents.getImageUrl(),
                    article.getCategory().stream().map(Category::toString).collect(toSet()),
                    article.getViews(),
                    article.getTimeRead(),
                    article.getMucluc()
            );
        }
    }


    @Value
    static class ArticleModelNestedList {
        String slug;
        String title;
        String description;
        Set<String> tagList;
        String createdAt;
        String imageUrl;
        Set<String> category;
        int view;
        static ArticleModelNestedList fromArticleList(Article article) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final var contents = article.getContents();
            final var titleFromArticle = contents.getTitle();
            final var category = article.getCategory();
            return new ArticleModelNestedList(
                    titleFromArticle.getSlug(),
                    titleFromArticle.getTitle(),
                    contents.getDescription(),
                    contents.getTags().stream().map(Tag::toString).collect(toSet()),
                    dateFormat.format(article.getCreatedAt()),
                    contents.getImageUrl(),
                    article.getCategory().stream().map(Category::toString).collect(toSet()),
                    article.getViews()
            );
        }
    }
}
