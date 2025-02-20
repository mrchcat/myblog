package com.github.mrchcat.myblog.tag.repository;

import com.github.mrchcat.myblog.post.domain.Post;
import com.github.mrchcat.myblog.tag.domain.Tag;
import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.nCopies;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private final JdbcTemplate jdbc;
    private final TagRowMapper tagRowMapper;

    @Override
    public List<Tag> getAllTagsByPost(long postId) {
        String query = """
                SELECT id,name
                FROM tags AS t
                JOIN poststags AS pt ON t.id=pt.tag_id
                WHERE pt.post_id=?
                """;
        return jdbc.query(query, tagRowMapper, postId);
    }

    private void linkTagsToPost(List<Tag> tags, long postId) {
        String query = """
                INSERT INTO poststags
                VALUES (?,?)
                """;

        List<Object[]> params = new ArrayList<>();
        for (Tag tag : tags) {
            params.add(new Object[]{postId, tag.getId()});
        }
        jdbc.batchUpdate(query, params);
    }

    @Override
    public void deleteSingleTagsOfPost(long postId) {
        String query = """
                DELETE FROM tags
                WHERE id IN (
                	SELECT tag_id
                	FROM poststags
                	WHERE tag_id IN (
                		SELECT tag_id FROM poststags WHERE post_id=?
                	)
                	GROUP BY tag_id
                	HAVING COUNT(tag_id)<=1
                )
                """;
        jdbc.update(query, postId);
    }

    @Override
    public List<Tag> saveTagsForPost(Set<String> tagNames, long postId) {
        List<Object[]> params = new ArrayList<>();
        for (String name : tagNames) {
            params.add(new Object[]{name});
        }
        String insertQuery = """
                INSERT INTO tags(name)
                VALUES (?) ON CONFLICT DO NOTHING
                """;
        jdbc.batchUpdate(insertQuery, params);
        String inSql = String.join(",", nCopies(tagNames.size(), "?"));
        String getQuery = """
                SELECT id,name
                FROM tags
                WHERE name IN (%s)
                """;
        List<Tag> tags = jdbc.query(String.format(getQuery, inSql), tagRowMapper, tagNames.toArray());
        linkTagsToPost(tags, postId);
        return tags;
    }
}
