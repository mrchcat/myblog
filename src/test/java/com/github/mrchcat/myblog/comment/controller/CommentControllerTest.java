package com.github.mrchcat.myblog.comment.controller;

import com.github.mrchcat.myblog.comment.dto.NewCommentDto;
import com.github.mrchcat.myblog.comment.repository.CommentRepository;
import com.github.mrchcat.myblog.configuration.TestDataSourceConfiguration;
import com.github.mrchcat.myblog.configuration.TestWebConfiguration;
import com.github.mrchcat.myblog.post.repository.PostRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringJUnitConfig(classes = {TestDataSourceConfiguration.class, TestWebConfiguration.class})
@ActiveProfiles("test")
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
class CommentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void cleanUp() {
        jdbc.execute("DELETE FROM poststags");
        jdbc.execute("DELETE FROM comments");
        jdbc.execute("DELETE FROM tags");
        jdbc.execute("DELETE FROM posts");
    }

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
        long commentId = commentRepository.addComment(new NewCommentDto(commentText, postId));
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

    private long getFirstPostId() throws NotFoundException {
        return postRepository.getFeed(Pageable.ofSize(1))
                .stream().findFirst()
                .orElseThrow(() -> new NotFoundException("пост не найден")).getId();
    }

}