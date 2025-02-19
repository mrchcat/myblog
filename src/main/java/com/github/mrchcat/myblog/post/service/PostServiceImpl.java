package com.github.mrchcat.myblog.post.service;

import com.github.mrchcat.myblog.comment.dto.CommentDto;
import com.github.mrchcat.myblog.comment.service.CommentService;
import com.github.mrchcat.myblog.post.domain.Post;
import com.github.mrchcat.myblog.post.dto.NewPostDto;
import com.github.mrchcat.myblog.post.dto.PostDto;
import com.github.mrchcat.myblog.post.mapper.PostMapper;
import com.github.mrchcat.myblog.post.repository.PostRepository;
import com.github.mrchcat.myblog.tag.dto.TagDto;
import com.github.mrchcat.myblog.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final TagService tagService;

    @Override
    public PostDto getPostDto(long postId) {
        Post post = postRepository.getPost(postId).orElseThrow(() -> new NoSuchElementException("Post not found"));
        PostDto postDto = PostMapper.toDto(post);
        addCommentToDto(postDto);
        List<TagDto> tags = tagService.getAllTagsByPost(postId);
        postDto.setTagsDto(tags);
        return postDto;
    }

    @Override
    public void addLike(long postId) {
        postRepository.addLike(postId);
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deletePost(postId);
    }

    @Override
    public PostDto editPost(long postId, NewPostDto newPostDto) {
        Post post = PostMapper.toPost(newPostDto);
        post.setId(postId);
        postRepository.savePost(post);

        PostDto postDto = PostMapper.toDto(post);

        List<TagDto> tagDto = tagService.saveTags(newPostDto.getTags(), postId);
        postDto.setTagsDto(tagDto);

        addCommentToDto(postDto);

        return postDto;
    }

    private void addCommentToDto(PostDto postDto) {
        List<CommentDto> comments = commentService.getCommentsByPost(postDto.getId());
        postDto.setCommentsDto(comments);
    }
}
