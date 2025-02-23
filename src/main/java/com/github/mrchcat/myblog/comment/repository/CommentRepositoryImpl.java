package com.github.mrchcat.myblog.comment.repository;

import com.github.mrchcat.myblog.comment.domain.Comment;
import com.github.mrchcat.myblog.comment.dto.NewCommentDto;
import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
    public long addComment(NewCommentDto comment) {
        String query = """
                INSERT INTO comments(text,post_id)
                VALUES (?,?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getText());
            ps.setLong(2, comment.getPostId());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new InternalException("Комментарий не добавлен");
        }
        return key.longValue();
    }

    @Override
    public void incrementCommentCounter(long postId) {
        String query = """
                UPDATE posts
                SET comment_counter=comment_counter+1
                WHERE id=?;
                """;
        jdbc.update(query, postId);
    }

    @Override
    public void decrementCommentCounter(long postId) {
        String query = """
                UPDATE posts
                SET comment_counter=comment_counter-1
                WHERE id=?;
                """;
        jdbc.update(query, postId);
    }

}
