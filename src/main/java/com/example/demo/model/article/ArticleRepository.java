package com.example.demo.model.article;

import com.example.demo.model.article.tag.Tag;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

interface ArticleRepository extends Repository<Article, Long> {

    Article save(Article article);

    Page<Article> findAll(Pageable pageable);
    Page<Article> findAllByUserFavoritedContains(User user, Pageable pageable);
    Page<Article> findAllByAuthorProfileUserName(UserName authorName, Pageable pageable);
    Page<Article> findAllByContentsTagsContains(Tag tag, Pageable pageable);
    Optional<Article> findFirstByContentsTitleSlug(String slug);

    void deleteArticleByAuthorAndContentsTitleSlug(User author, String slug);

    Optional<Article> findFirstByOrderByViewsDesc();

    Page<Article> findByCategory(String category, Pageable pageable);

    Page<Article> findByContentsBodyLike(String keyword,Pageable pageable);

}
