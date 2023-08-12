package com.example.demo.model.article;

import com.example.demo.model.article.tag.TagService;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserFindService;
import com.example.demo.model.user.UserName;
import com.example.demo.service.CloudinaryService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static org.springframework.data.util.Optionals.mapIfAllPresent;
@Lazy
@Service
public class ArticleService implements ArticleFindService {

    private final UserFindService userFindService;
    private final TagService tagService;
    private final ArticleRepository articleRepository;
    private final CloudinaryService cloudinaryService;

    ArticleService(UserFindService userFindService, TagService tagService, ArticleRepository articleRepository,
                   CloudinaryService cloudinaryService) {
        this.userFindService = userFindService;
        this.tagService = tagService;
        this.articleRepository = articleRepository;
        this.cloudinaryService = cloudinaryService;

    }

    @Transactional
    public Article createNewArticle(long authorId, ArticleContents contents) {
        final var tagsReloaded = tagService.reloadAllTagsIfAlreadyPresent(contents.getTags());
        contents.setTags(tagsReloaded);
//        try {
//            contents.setBody(cloudinaryService.uploadString(contents.getBody()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return userFindService.findById(authorId)
                .map(author -> author.writeArticle(contents))
                .map(articleRepository::save)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Article> getFeedByUserId(long userId, Pageable pageable) {
        return userFindService.findById(userId)
                .map(user -> articleRepository.findAllByUserFavoritedContains(user, pageable)
                        .map(article -> article.updateFavoriteByUser(user)))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticleFavoritedByUsername(UserName username, Pageable pageable) {
        return userFindService.findByUsername(username)
                .map(user -> articleRepository.findAllByUserFavoritedContains(user, pageable)
                        .map(article -> article.updateFavoriteByUser(user)))
                .orElse(Page.empty());
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticlesByAuthorName(String authorName, Pageable pageable) {
        return articleRepository.findAllByAuthorProfileUserName(new UserName(authorName), pageable);
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticlesByTag(String tagValue, Pageable pageable) {
        return tagService.findByValue(tagValue)
                .map(tag -> articleRepository.findAllByContentsTagsContains(tag, pageable))
                .orElse(Page.empty());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Article> getArticleBySlug(String slug) {
        Optional<Article> data= articleRepository.findFirstByContentsTitleSlug(slug);
        if (data.isPresent()) {
            Article article= data.get();
            Document document = Jsoup.parse(article.getContents().getBody());

            Elements headings = document.select("h1, h2, h3,h4, h5, h6"); // Lựa chọn tất cả các thẻ h1, h2, h3

            for (Element heading : headings) {
//                String id = heading.text().toLowerCase().replaceAll("\\s+", "-"); // Tạo id từ nội dung của thẻ
//                heading.attr("id", id); // Chèn thuộc tính id vào thẻ
                article.getMucluc().add(heading.text().toLowerCase());
            }
            return Optional.of(article);
        }
        return data;
    }



    @Transactional
    public Article updateArticle(long userId, String slug, ArticleUpdateRequest request) {
        Article data= mapIfAllPresent(userFindService.findById(userId), getArticleBySlug(slug),
                (user, article) -> user.updateArticle(article, request))
                .orElseThrow(NoSuchElementException::new);
        return data;
    }

    @Transactional
    public Article favoriteArticle(long userId, String articleSlugToFavorite) {
        return mapIfAllPresent(
                userFindService.findById(userId), getArticleBySlug(articleSlugToFavorite),
                User::favoriteArticle)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Article unfavoriteArticle(long userId, String articleSlugToUnFavorite) {
        return mapIfAllPresent(
                userFindService.findById(userId), getArticleBySlug(articleSlugToUnFavorite),
                User::unfavoriteArticle)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void deleteArticleBySlug(long userId, String slug) {
        userFindService.findById(userId)
                .ifPresentOrElse(user -> articleRepository.deleteArticleByAuthorAndContentsTitleSlug(user, slug),
                        () -> {throw new NoSuchElementException();});
    }

    @Override
    public Optional<Article> getArticleByView() {
        return articleRepository.findFirstByOrderByViewsDesc();
    }


    @Transactional(readOnly = true)
    public Page<Article> getByCategory(Pageable pageable,String category) {
        return articleRepository.findByCategory(category,pageable);
    }
    @Transactional
    public void save(Article article) {
        articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticleBysearch(String slug, Pageable pageable) {
        return articleRepository.findByContentsBodyLike(slug,pageable);
    }
}
