package com.example.demo.model.article.comment;

import com.example.demo.model.article.Article;
import com.example.demo.model.article.ArticleFindService;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserFindService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.springframework.data.util.Optionals.mapIfAllPresent;
@Lazy
@Service
public class CommentService {

    private final UserFindService userFindService;
    private final ArticleFindService articleFindService;

    CommentService(UserFindService userFindService, ArticleFindService articleFindService) {
        this.userFindService = userFindService;
        this.articleFindService = articleFindService;
    }

    @Transactional
    public Comment createComment(long userId, String slug, String body) {
        return mapIfAllPresent(userFindService.findById(userId), articleFindService.getArticleBySlug(slug),
                (user, article) -> user.writeCommentToArticle(article, body))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Set<Comment> getComments(long userId, String slug) {
        return mapIfAllPresent(userFindService.findById(userId), articleFindService.getArticleBySlug(slug),
                User::viewArticleComments)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Set<Comment> getComments(String slug) {
        return articleFindService.getArticleBySlug(slug)
                .map(Article::getComments)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void deleteCommentById(long userId, String slug, long commentId) {
        final var articleContainsComments = articleFindService.getArticleBySlug(slug)
                .orElseThrow(NoSuchElementException::new);
        userFindService.findById(userId)
                .ifPresentOrElse(user -> user.deleteArticleComment(articleContainsComments, commentId),
                        () -> {throw new NoSuchElementException();});
    }
}
