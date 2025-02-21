package com.github.mrchcat.myblog.post.service;

import com.github.mrchcat.myblog.comment.dto.CommentDto;
import com.github.mrchcat.myblog.comment.service.CommentService;
import com.github.mrchcat.myblog.post.domain.Post;
import com.github.mrchcat.myblog.post.dto.NewPostDto;
import com.github.mrchcat.myblog.post.dto.PostDto;
import com.github.mrchcat.myblog.post.dto.ShortPostDto;
import com.github.mrchcat.myblog.post.mapper.PostMapper;
import com.github.mrchcat.myblog.post.repository.PostRepository;
import com.github.mrchcat.myblog.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final TagService tagService;

    @Override
    public PostDto getPostDto(long postId) {
        Post post = postRepository.getPost(postId).orElseThrow(() -> new NoSuchElementException("Post not found"));
        List<CommentDto> commentDtos = commentService.getCommentsByPost(post.getId());
        return PostMapper.toDto(post, commentDtos);
    }

    @Override
    public List<ShortPostDto> getFeed() {
        Collection<Post> postList = postRepository.getFeed();
        return PostMapper.toShortDto(postList);
    }

    @Override
    public void addLike(long postId) {
        postRepository.addLike(postId);
    }

    @Override
    public void deletePost(long postId) {
        tagService.unlinkTagsFromPost(postId);
        tagService.deleteSingleTagsOfPost(postId);
        postRepository.deletePost(postId);
    }

    @Override
    public PostDto editPost(long postId, NewPostDto newPostDto) {
        Post post = PostMapper.toPost(newPostDto);
        post.setId(postId);

        tagService.unlinkTagsFromPost(postId);
        tagService.deleteSingleTagsOfPost(postId);
        tagService.saveTags(newPostDto.getTags(), postId);

        long updatedPostId = postRepository.updatePost(post);
        Post updatedPost=postRepository.getPost(updatedPostId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));
        List<CommentDto> commentDtos = commentService.getCommentsByPost(updatedPostId);
        log.debug(updatedPost.toString());
        return PostMapper.toDto(updatedPost, commentDtos);
    }

    @Override
    public ShortPostDto addNewPost(NewPostDto newPostDto) {
        Post post = PostMapper.toPost(newPostDto);
        long savedPostId = postRepository.addNewPost(post);
        tagService.saveTags(newPostDto.getTags(), savedPostId);
        Post savedPost=postRepository.getPost(savedPostId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));
        return PostMapper.toShortDto(savedPost);
    }
}
