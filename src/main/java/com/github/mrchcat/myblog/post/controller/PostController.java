package com.github.mrchcat.myblog.post.controller;

import com.github.mrchcat.myblog.post.dto.NewPostDto;
import com.github.mrchcat.myblog.post.dto.PostDto;
import com.github.mrchcat.myblog.post.dto.ShortPostDto;
import com.github.mrchcat.myblog.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping(value = {"/"})
    public String redirectToFeed() {
        return "redirect:feed";
    }

    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable(value = "postId") long postId, Model model) {
        if (postId <= 0) {
            throw new IllegalArgumentException("номер поста должен быть положительным числом");
        }
        PostDto postDto = postService.getPostDto(postId);
        model.addAttribute("postDto", postDto);
        return "post";
    }

    @PostMapping("/post/like/{postId}")
    public String addLike(@PathVariable(value = "postId") long postId) {
        if (postId <= 0) {
            throw new IllegalArgumentException("номер поста должен быть положительным числом");
        }
        postService.addLike(postId);
        return "redirect:/post/" + postId;
    }

    @PostMapping(value = "/post/{postId}", params = "_method=delete")
    public String deletePost(@PathVariable(value = "postId") long postId) {
        if (postId <= 0) {
            throw new IllegalArgumentException("номер поста должен быть положительным числом");
        }
        postService.deletePost(postId);
        return "feed";
    }

    @PostMapping("/post/{postId}")
    public String editPost(@PathVariable("postId") long postId,
                           @RequestParam("name") String name,
                           @RequestParam("image") MultipartFile image,
                           @RequestParam("text") String text,
                           @RequestParam("tags") String tags,
                           Model model) {
        if (postId <= 0) {
            throw new IllegalArgumentException("номер поста должен быть положительным числом");
        }
        NewPostDto newPostDto = NewPostDto.builder()
                .name(name)
                .image(image)
                .text(text)
                .tags(tags)
                .build();
        PostDto postDto = postService.editPost(postId, newPostDto);
        model.addAttribute("postDto", postDto);
        return "post";
    }

    @GetMapping(value = {"/feed/", "/feed"})
    public String getFeed(Model model) {
        List<ShortPostDto> shortPostDtoList = postService.getFeed();
        model.addAttribute("shortPostDtoList", shortPostDtoList);
        return "feed";
    }

    @GetMapping("/feed/tag/{tagId}")
    public String getFeedByTag(@PathVariable("tagId") long tagId, Model model) {
        List<ShortPostDto> shortPostDtoList = postService.getFeedByTag(tagId);
        model.addAttribute("shortPostDtoList", shortPostDtoList);
        return "feed";
    }

    @PostMapping("/feed/post")
    public String addNewPost(@RequestParam("name") String name,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("text") String text,
                             @RequestParam("tags") String tags,
                             Model model) {
        NewPostDto newPostDto = NewPostDto.builder()
                .name(name)
                .image(image)
                .text(text)
                .tags(tags)
                .build();
        postService.addNewPost(newPostDto);
        return "redirect:/feed";
    }


}
