package com.example.demo.model.article;

import com.example.demo.model.article.tag.Tag;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;

@Embeddable
public class ArticleContents {

    @Embedded
    private ArticleTitle title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String status;



    @JoinTable(name = "articles_tags",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false))
    @ManyToMany(fetch = EAGER, cascade = PERSIST)
    private Set<Tag> tags = new HashSet<>();

    public ArticleContents(String description, ArticleTitle title, String body, Set<Tag> tags,String imageUrl,String category) {
        this.description = description;
        this.title = title;
        this.body = body;
        this.tags = tags;
        this.imageUrl = imageUrl;
        this.status="Successful";
    }

    protected ArticleContents() {
    }

    public ArticleTitle getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    void updateArticleContentsIfPresent(ArticleUpdateRequest updateRequest) {
        updateRequest.getTitleToUpdate().ifPresent(titleToUpdate -> title = titleToUpdate);
        updateRequest.getDescriptionToUpdate().ifPresent(descriptionToUpdate -> description = descriptionToUpdate);
        updateRequest.getBodyToUpdate().ifPresent(bodyToUpdate -> body = bodyToUpdate);
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public void setBody(String body) {
        this.body = body;
    }
}
