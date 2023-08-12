package com.example.demo.model.article;

import com.example.demo.model.article.comment.Comment;
import com.example.demo.model.category.Category;
import com.example.demo.model.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "articles")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = EAGER)
    private User author;

    @Embedded
    private ArticleContents contents;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "views")
    private int views;

    @Column(name = "timeRead")
    private String timeRead;


    @JoinTable(name = "article_favorites",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false))
    @ManyToMany(fetch = EAGER, cascade = PERSIST)
    private Set<User> userFavorited = new HashSet<>();

    @OneToMany(mappedBy = "article", cascade = {PERSIST, REMOVE})
    private Set<Comment> comments = new HashSet<>();


    @JoinTable(name = "article_category",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false))
    @ManyToMany(fetch = EAGER, cascade = PERSIST)
    private Set<Category> category= new HashSet<>();

    @Transient
    private boolean favorited = false;
    @Transient
    private List<String> mucluc = new ArrayList<>();

    public Article(User author, ArticleContents contents) {
        this.author = author;
        this.contents = contents;
        this.createdAt= new Date();
        this.updatedAt= new Date();

    }

    protected Article() {
    }

    public Article afterUserFavoritesArticle(User user) {
        userFavorited.add(user);
        return updateFavoriteByUser(user);
    }

    public Article afterUserUnFavoritesArticle(User user) {
        userFavorited.remove(user);
        return updateFavoriteByUser(user);
    }

    public Comment addComment(User author, String body) {
        final var commentToAdd = new Comment(this, author, body);
        comments.add(commentToAdd);
        return commentToAdd;
    }

    public void removeCommentByUser(User user, long commentId) {
        final var commentsToDelete = comments.stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        if (!user.equals(author) || !user.equals(commentsToDelete.getAuthor())) {
            throw new IllegalAccessError("Not authorized to delete comment");
        }
        comments.remove(commentsToDelete);
    }

    public void updateArticle(ArticleUpdateRequest updateRequest) {
        contents.updateArticleContentsIfPresent(updateRequest);
    }

    public Article updateFavoriteByUser(User user) {
        favorited = userFavorited.contains(user);
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public ArticleContents getContents() {
        return contents;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getFavoritedCount() {
        return userFavorited.size();
    }

    public boolean isFavorited() {
        return favorited;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var article = (Article) o;
        return author.equals(article.author) && contents.getTitle().equals(article.contents.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, contents.getTitle());
    }

    public Set<Category> getCategory() {
        return category;
    }

    public void setCategory(Set<Category> category) {
        this.category = category;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getTimeRead() {
        return timeRead;
    }

    public void setTimeRead(String timeRead) {
        this.timeRead = timeRead;
    }

    public List<String> getMucluc() {
        return mucluc;
    }

    public void setMucluc(List<String> mucluc) {
        this.mucluc = mucluc;
    }
}
