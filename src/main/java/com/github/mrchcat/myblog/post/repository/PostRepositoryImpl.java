package com.github.mrchcat.myblog.post.repository;

import com.github.mrchcat.myblog.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final JdbcTemplate jdbc;
    private final PostRowMapper postRowMapper;


    @Override
    public Optional<Post> getPost(long posId) {
        String query = """
                SELECT id,name,text,picture,text,likes,comment_counter
                FROM posts
                WHERE id=?
                """;
        try {
            Post result = jdbc.queryForObject(query, postRowMapper, posId);
            return Optional.ofNullable(result);
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
    public long savePost(Post post) {
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
}
