package com.example.demo.controller.article;

import com.example.demo.model.article.ArticleService;
import com.example.demo.model.jwt.UserJWTPayload;
import com.example.demo.model.user.UserName;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.of;

@RestController
class ArticleRestController {

    private final ArticleService articleService;

    ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    public ArticleModel postArticle(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                                    @Valid @RequestBody ArticlePostRequestDTO dto) {
        var articleCreated = articleService.createNewArticle(jwtPayload.getUserId(), dto.toArticleContents());
        return ArticleModel.fromArticle(articleCreated);
    }

    @GetMapping("/articles")
    public MultipleArticleModelList getArticles(Pageable pageable) {
        final var articles = articleService.getArticles(pageable);
        return MultipleArticleModelList.fromArticleList(articles);
    }

    @GetMapping(value = "/articles", params = {"author"})
    public MultipleArticleModel getArticlesByAuthor(@RequestParam String author, Pageable pageable) {
        final var articles = articleService.getArticlesByAuthorName(author, pageable);
        return MultipleArticleModel.fromArticles(articles);
    }

    @GetMapping(value = "/articles", params = {"tag"})
    public MultipleArticleModel getArticlesByTag(@RequestParam String tag, Pageable pageable) {
        final var articles = articleService.getArticlesByTag(tag, pageable);
        return MultipleArticleModel.fromArticles(articles);
    }

    @GetMapping(value = "/articles", params = {"favorited"})
    public MultipleArticleModel getArticleByFavoritedUsername(@RequestParam UserName favorited, Pageable pageable) {
        final var articles = articleService.getArticleFavoritedByUsername(favorited, pageable);
        return MultipleArticleModel.fromArticles(articles);
    }

    @GetMapping("/articles/feed")
    public MultipleArticleModel getFeed(@AuthenticationPrincipal UserJWTPayload jwtPayload, Pageable pageable) {
        final var articles = articleService.getFeedByUserId(jwtPayload.getUserId(), pageable);
        return MultipleArticleModel.fromArticles(articles);
    }

    @GetMapping("/articles/{slug}")
    public ResponseEntity<ArticleModel> getArticleBySlug(@PathVariable String slug) {
        Optional<ArticleModel> acArticleModel =articleService.getArticleBySlug(slug)
                .map(ArticleModel::fromArticle);
        return of(acArticleModel);
    }

    @PutMapping("/articles/{slug}")
    public ArticleModel putArticleBySlug(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                                         @PathVariable String slug,
                                         @RequestBody ArticlePutRequestDTO dto) {
        final var articleUpdated = articleService.updateArticle(jwtPayload.getUserId(), slug, dto.toUpdateRequest());
        return ArticleModel.fromArticle(articleUpdated);
    }

    @PostMapping("/articles/{slug}/favorite")
    public ArticleModel favoriteArticleBySlug(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                                              @PathVariable String slug) {
        var articleFavorited = articleService.favoriteArticle(jwtPayload.getUserId(), slug);
        return ArticleModel.fromArticle(articleFavorited);
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public ArticleModel unfavoriteArticleBySlug(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                                                @PathVariable String slug) {
        var articleUnfavored = articleService.unfavoriteArticle(jwtPayload.getUserId(), slug);
        return ArticleModel.fromArticle(articleUnfavored);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/articles/{slug}")
    public void deleteArticleBySlug(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                                    @PathVariable String slug) {
        articleService.deleteArticleBySlug(jwtPayload.getUserId(), slug);
    }


    @GetMapping("/articles/byView")
    public ResponseEntity<ArticleModel> articlesByView() {
        return of(articleService.getArticleByView()
                .map(ArticleModel::fromArticle));
    }

    @GetMapping("/articles/getByCategory")
    public MultipleArticleModel getByCategory( @PathVariable String category,Pageable pageable) {
        final var articles = articleService.getByCategory(pageable,category);
        return MultipleArticleModel.fromArticles(articles);
    }


    @GetMapping("/articles/search/{slug}")
    public MultipleArticleModelList getArticleBysearch(  @PathVariable String slug,Pageable pageable) {
        final var articles = articleService.getArticleBysearch(slug,pageable);
        return MultipleArticleModelList.fromArticleList(articles);
    }
}
