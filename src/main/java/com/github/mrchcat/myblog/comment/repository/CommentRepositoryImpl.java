package com.github.mrchcat.myblog.comment.repository;

import com.github.mrchcat.myblog.comment.domain.Comment;
import com.github.mrchcat.myblog.comment.dto.NewCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final JdbcTemplate jdbc;
    private final CommentRowMapper commentRowMapper;


    @Override
    public List<Comment> getCommentsByPost(long postId) {
        String query = """
                SELECT id,text, post_id
                FROM comments
                WHERE post_id=?
                """;
        return jdbc.query(query, commentRowMapper, postId);
    }

    @Override
    public void deleteComment(long commentId) {
        String query = """
                DELETE FROM comments
                WHERE id=?
                """;
        jdbc.update(query, commentId);
    }

    @Override
    public void addComment(NewCommentDto comment) {
        String query = """
                INSERT INTO comments(text,post_id)
                VALUES (?,?)
                """;
        jdbc.update(query, comment.getText(), comment.getPostId());
    }
}
