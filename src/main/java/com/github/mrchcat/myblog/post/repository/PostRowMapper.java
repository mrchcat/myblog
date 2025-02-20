package com.github.mrchcat.myblog.post.repository;

import com.github.mrchcat.myblog.post.domain.Post;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostRowMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Post.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .text(rs.getString("text"))
                .base64Jpeg(rs.getString("picture"))
                .likes(rs.getLong("likes"))
                .commentCounter(rs.getLong("comment_counter"))
                .build();
    }
}
