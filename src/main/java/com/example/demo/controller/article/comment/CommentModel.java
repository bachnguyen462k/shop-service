package com.example.demo.controller.article.comment;

import com.example.demo.controller.user.ProfileModel;
import com.example.demo.model.article.comment.Comment;
import lombok.Value;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Value
class CommentModel {

    CommentModelNested comment;

    static CommentModel fromComment(Comment comment) {
        return new CommentModel(CommentModelNested.fromComment(comment));
    }

    @Value
    static class CommentModelNested {
        long id;
        String body;
        ZonedDateTime createdAt;
        ZonedDateTime updatedAt;
        ProfileModel.ProfileModelNested author;

        static CommentModelNested fromComment(Comment comment) {
            return new CommentModelNested(comment.getId(),
                    comment.getBody(),
                    comment.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")),
                    comment.getUpdatedAt().atZone(ZoneId.of("Asia/Seoul")),
                    ProfileModel.ProfileModelNested.fromProfile(comment.getAuthor().getProfile()));
        }
    }
}
