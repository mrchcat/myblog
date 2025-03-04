package com.github.mrchcat.myblog.post.controller;

import com.github.mrchcat.myblog.post.domain.Post;
import com.github.mrchcat.myblog.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PostControllerTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getStartPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("feed"))
                .andExpect(view().name("redirect:feed"));
    }

    @Test
    @Sql(scripts = "classpath:test-data/add-three-posts.sql")
    void getFeed() throws Exception {
        mockMvc.perform(get("/feed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("postPage", "pageSize", "isFiltered"))
                .andExpect(xpath("//a[text()='Седина в шерсти']/@href").exists())
                .andExpect(xpath("//a[text()='Кошачий аппетит']/@href").exists())
                .andExpect(xpath("//a[text()='Лечим попугая']/@href").exists());
    }

    @Test
    @Sql(scripts = "classpath:test-data/add-three-posts.sql")
    void getPost() throws Exception {
        var posts = getAllPost();
        for (var entry : posts.entrySet()) {
            mockMvc.perform(get("/post/" + entry.getKey()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("post"))
                    .andExpect(model().attributeExists("postDto", "newPostDto"))
                    .andExpect(xpath("//span['" + entry.getKey() + "']").exists());
        }
    }

    @Test
    @Sql(scripts = "classpath:test-data/add-three-posts.sql")
    void deletePost() throws Exception {
        Post post = getFirstPost();
        long postId = post.getId();
        String postName = post.getName();

        mockMvc.perform(get("/feed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("postPage", "pageSize", "isFiltered"))
                .andExpect(xpath("//a[text()='" + postName + "']/@href").exists());


        mockMvc.perform(post("/post/" + postId).param("_method", "delete"))
                .andExpect(redirectedUrl("/feed"))
                .andExpect(view().name("redirect:/feed"));

        mockMvc.perform(get("/feed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("postPage", "pageSize", "isFiltered"))
                .andExpect(xpath("//a[text()='" + postName + "']/@href").doesNotExist());
    }

    @Test
    @Sql(scripts = "classpath:test-data/add-three-posts.sql")
    void addLike() throws Exception {
        Post post = getFirstPost();
        long postId = post.getId();
        long likes = post.getLikes();
        mockMvc.perform(post("/post/like/" + postId))
                .andExpect(redirectedUrl("/post/" + postId))
                .andExpect(view().name("redirect:/post/" + postId));
        mockMvc.perform(get("/post/" + postId))
                .andExpect(xpath("//span[@id='likes']").string(String.valueOf(++likes)));
    }

    private Post getFirstPost() throws NoSuchElementException {
        return postRepository.getFeed(Pageable.ofSize(1))
                .stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("пост не найден"));
    }

    private Map<Long, String> getAllPost() {
        return postRepository.getFeed(Pageable.ofSize(Integer.MAX_VALUE))
                .stream().collect(Collectors.toMap(Post::getId, Post::getText));
    }
}