package com.github.mrchcat.myblog.comment.repository;

import com.github.mrchcat.myblog.comment.domain.Comment;
import com.github.mrchcat.myblog.comment.dto.NewCommentDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    @Override
    public List<Comment> getCommentsByPost(long postId) {
        Comment comment1 = new Comment(1, "comment 1");
        Comment comment2 = new Comment(1, "comment 2");
        Comment comment3 = new Comment(1, "comment 3");
        return List.of(comment1, comment2, comment3);
    }

    @Override
    public void deleteComment(long commentId) {

    }

    @Override
    public void addComment(NewCommentDto newCommentDto) {

    }
}
