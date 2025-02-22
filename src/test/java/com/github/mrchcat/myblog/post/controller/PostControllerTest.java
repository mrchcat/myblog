package com.github.mrchcat.myblog.post.controller;

import com.github.mrchcat.myblog.configuration.TestDataSourceConfiguration;
import com.github.mrchcat.myblog.configuration.TestWebConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@SpringJUnitConfig(classes = {TestDataSourceConfiguration.class, TestWebConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        jdbcTemplate.execute("DELETE FROM poststags");
//        jdbcTemplate.execute("DELETE FROM comments");
//        jdbcTemplate.execute("DELETE FROM tags");
//        jdbcTemplate.execute("DELETE FROM posts");
    }

//    @Test
//    void addNewPost() throws Exception {
//        String postName = "Название поста";
//        String postText = "Содержание поста";
//        String tag1 = "тег1";
//        String tag2 = "тег2";
//        String tag3 = "длинный тег3";
////        String postTags = tag1 + ";" + tag2 + ";  ;" + tag3;
////        mockMvc.perform(
////                post("/feed/post)")
////                        .param("name", postName)
////                        .param("text", postText)
////                        .param("tags", postTags))
////                .andExpect(status().isOk())
////                .andExpect(content().contentType("text/html;charset=UTF-8"))
////                .andExpect(view().name("redirect:/feed"));
//    }


}