package com.github.mrchcat.myblog.comment.controller;

import com.github.mrchcat.myblog.post.repository.PostRepository;
import com.sun.jdi.InternalException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CommentControllerTest {

    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = "classpath:test-data/add-post.sql")
    void addNewComment() throws Exception {
        long postId = getFirstPostId();
        String newComment = "new test comment";
        mockMvc.perform(post("/post/" + postId + "/comment")
                        .param("_method", "add")
                        .param("text", newComment))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId))
                .andExpect(view().name("redirect:/post/" + postId));

        mockMvc.perform(get("/post/" + postId))
                .andExpect(xpath("//section[@class='comment-content'][normalize-space(text()) = '"
                        + newComment + "']").exists());
    }

    @Test
    @Sql(scripts = "classpath:test-data/add-post.sql")
    void deleteComment() throws Exception {
        long postId = getFirstPostId();
        String commentText = "comment to delete " + (int) (Math.random() * 1000);
        long commentId = addCommentAndGetId(commentText,postId);
        mockMvc.perform(get("/post/" + postId))
                .andExpect(xpath("//section[@class='comment-content'][normalize-space(text()) = '"
                        + commentText + "']").exists());
        String url = String.format("/post/%s/comment/%s", postId, commentId);
        mockMvc.perform(post(url)
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId))
                .andExpect(view().name("redirect:/post/" + postId));

        mockMvc.perform(get("/post/" + postId))
                .andExpect(xpath("//section[@class='comment-content'][normalize-space(text()) = '"
                        + commentText + "']").doesNotExist());
    }

    private long getFirstPostId() throws NoSuchElementException {
        return postRepository.getFeed(Pageable.ofSize(1))
                .stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("пост не найден")).getId();
    }

    private long addCommentAndGetId(String commentText,long postId){
        String query = """
                INSERT INTO comments(text,post_id)
                VALUES (?,?)
                RETURNING id
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, commentText);
            ps.setLong(2, postId);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new InternalException("Пост не добавлен");
        }
        return key.longValue();
    }
}