package com.github.mrchcat.myblog.post.repository;

import com.github.mrchcat.myblog.post.domain.Post;
import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final JdbcTemplate jdbc;
    private final PostResultSetExtractor postResultSetExtractor;

    @Override
    public Optional<Post> getPost(long postId) {
        String query = """
                SELECT p.id,p.name,text,picture,p.likes,p.comment_counter, pt.tag_id, t.name AS tag_name
                FROM posts AS p
                LEFT JOIN  poststags AS pt ON p.id=pt.post_id
                LEFT JOIN tags AS t ON t.id=pt.tag_id
                WHERE p.id=?
                """;
        try {
            Collection<Post> result = jdbc.query(query, postResultSetExtractor, postId);
            if (result == null || result.isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(result.iterator().next());
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public void addLike(long postId) {
        String query = """
                UPDATE posts
                SET likes=likes+1
                WHERE id=?;
                """;
        jdbc.update(query, postId);
    }

    @Override
    public void deletePost(long postId) {
        String query = "DELETE FROM posts WHERE id=?";
        jdbc.update(query, postId);
    }

    @Override
    public long updatePost(Post post) {
        String query = """
                UPDATE posts
                SET name=?,
                    text=?,
                    picture=?,
                    likes=?,
                    comment_counter=?
                WHERE id=?
                RETURNING id
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,post.getName());
            ps.setString(2,post.getText());
            ps.setString(3,post.getBase64Jpeg());
            ps.setLong(4,post.getLikes());
            ps.setLong(5,post.getCommentCounter());
            ps.setLong(6,post.getId());
            return ps;
        }, keyHolder);
        Number key=keyHolder.getKey();
        if(key!=null){
            return key.longValue();
        } else {
            throw new InternalException("Пост не добавлен");
        }
    }

    @Override
    public long addNewPost(Post post) {
        String query = """
                INSERT INTO posts(name,text, picture,likes,comment_counter)
                VALUES (?,?,?,?,?)
                RETURNING id
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,post.getName());
            ps.setString(2,post.getText());
            ps.setString(3,post.getBase64Jpeg());
            ps.setLong(4,post.getLikes());
            ps.setLong(5,post.getCommentCounter());
            return ps;
        }, keyHolder);
        Number key=keyHolder.getKey();
        if(key!=null){
            return key.longValue();
        } else {
            throw new InternalException("Пост не добавлен");
        }
    }

    @Override
    public Collection<Post> getFeedByTag(long tagId) {
        String query = """
                SELECT p.id,p.name,text,picture,p.likes,p.comment_counter, pt.tag_id, t.name AS tag_name
                FROM posts AS p
                LEFT JOIN  poststags AS pt ON p.id=pt.post_id
                LEFT JOIN tags AS t ON t.id=pt.tag_id
                WHERE p.id IN (
                	SELECT p.id
                	FROM tags AS t
                	JOIN poststags AS pt ON t.id=pt.tag_id
                	JOIN posts AS p ON p.id=pt.post_id
                	WHERE t.id=?)
                """;
        try {
            Collection<Post> posts = jdbc.query(query, postResultSetExtractor, tagId);
            if (posts == null) {
                return Collections.emptyList();
            }
            return posts;
        } catch (EmptyResultDataAccessException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<Post> getFeed() {
        String query = """
                SELECT p.id,p.name,text,picture,p.likes,p.comment_counter, pt.tag_id, t.name AS tag_name
                FROM posts AS p
                LEFT JOIN  poststags AS pt ON p.id=pt.post_id
                LEFT JOIN tags AS t ON t.id=pt.tag_id
                """;
        try {
            Collection<Post> posts = jdbc.query(query, postResultSetExtractor);
            if (posts == null) {
                return Collections.emptyList();
            }
            return posts;
        } catch (EmptyResultDataAccessException ignored) {
            return Collections.emptyList();
        }
    }


}