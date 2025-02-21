package com.github.mrchcat.myblog.post.repository;

import com.github.mrchcat.myblog.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
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
                WHERE id=?;
                """;
        Object[] params = {
                post.getName(),
                post.getText(),
                post.getBase64Jpeg(),
                post.getLikes(),
                post.getCommentCounter(),
                post.getId()
        };
        long postId = jdbc.update(query, params);
        if (postId == 0) {
            throw new IllegalArgumentException("Data was not updated");
        }
        return postId;
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

    @Override
    public long addNewPost(Post post) {
        String query = """
                INSERT INTO posts(name,text, picture,likes,comment_counter)
                VALUES ('?','?',?,?,?)
                """;
        Object[] params = {
                post.getName(),
                post.getText(),
                post.getBase64Jpeg(),
                post.getLikes(),
                post.getCommentCounter(),
        };
        long postId = jdbc.update(query, params);
        if (postId == 0) {
            throw new IllegalArgumentException("Data was not updated");
        }
        return postId;
    }
}