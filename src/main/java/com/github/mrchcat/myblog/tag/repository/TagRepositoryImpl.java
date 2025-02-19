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

//    @Override
//    public List<Tag> saveTagsForPost(List<String> tagNames, long postId) {
//        String getQueryByName = """
//                SELECT id,name
//                FROM tags AS t
//                WHERE name=?
//                """;
//        List<Tag> result = new ArrayList<>();
//        List<Tag> newTags = new ArrayList<>();
//        Tag tag;
//        for (String tagName : tagNames) {
//            try {
//                tag = jdbc.queryForObject(getQueryByName, tagRowMapper, tagName);
//                result.add(tag);
//            } catch (EmptyResultDataAccessException ignored) {
//                tag = getTagById(insert(tagName));
//                result.add(tag);
//                newTags.add(tag);
//            }
//        }
//        linkTagsToPost(newTags, postId);
//        return result;
//    }

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

//    private Tag getTagById(long tagId) {
//        String query = """
//                SELECT id,name
//                FROM tags
//                WHERE id=?
//                """;
//        return jdbc.queryForObject(query, tagRowMapper, tagId);
//    }

//    private long insert(String tagName) {
//        String insertQuery = """
//                INSERT INTO tags(name)
//                VALUES (?) ON CONFLICT (NAME) DO NOTHING
//                """;
//        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbc.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
//            ps.setObject(1, tagName);
//            return ps;
//        }, keyHolder);
//        Integer id = keyHolder.getKeyAs(Integer.class);
//        if (id != null) {
//            return id;
//        } else {
//            throw new InternalException("Data was not saved!");
//        }
//    }


    @Override
    public void deleteUnusedTags(List<Long> tagIds) {
        String query = """
                DELETE FROM tags
                WHERE id=? AND NOT EXISTS
                    (SELECT tag_id
                    FROM poststags
                    WHERE tag_id=?)
                """;
        List<Object[]> params = new ArrayList<>();
        for (long tagId : tagIds) {
            params.add(new Object[]{tagId, tagId});
        }
        jdbc.batchUpdate(query, params);
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
