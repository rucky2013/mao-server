package com.guan.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.guan.domain.Comment;
import com.guan.domain.Post;
import com.guan.dto.CommentDto;
import com.guan.dto.PostDto;
import com.guan.service.PostService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "http://localhost:8000", methods={RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/post")
public class PostController {
   private Logger LOGGER = LoggerFactory.getLogger(PostController.class);

   @Autowired
   private PostService service;
   
   @RequestMapping(method = RequestMethod.POST)
   @ApiOperation(value = "Save a post under the assgined category")
   public PostDto createPost(@RequestBody PostDto dto) {
      Post post = service.createPost(dto);
      return new PostDto(post);
   }
   
   @RequestMapping(method = RequestMethod.GET)
   public List<PostDto> getAllPosts() {
       return service.getAllPosts().stream().map(PostDto::new).collect(Collectors.toList());
   }
   
   @RequestMapping(value = "/{id}", method = RequestMethod.GET)
   public PostDto getPost(@PathVariable("id") String id) {
      return new PostDto(service.getPost(id));
   }
   
   @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
   @ApiOperation(value = "Update a post, id and category cannot be changed")
   public PostDto updatePost(@PathVariable("id") String id, @RequestBody PostDto dto) {
      return new PostDto(service.updatePost(id, dto));
   }
   
   @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
   @ApiOperation(value = "Remove record completely")
   public ResponseEntity<String> deletePost(@PathVariable("id") String id) {
      service.deletePost(id);
      return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
   }
   
   @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
   public List<CommentDto> getCommentsOf(@PathVariable("id") String id) {
      return service.getCommentsOfPost(id).stream().map(c -> new CommentDto(c)).collect(Collectors.toList());
   }
   
   @RequestMapping(value = "/comment/new", method = RequestMethod.POST)
   @ApiOperation(value = "Add a comment under the post")
   public CommentDto createComment (@RequestBody CommentDto dto) {
      return new CommentDto(service.addComment(dto));
   }
   
   @RequestMapping(value = "/comment/{id}", method = RequestMethod.PUT)
   @ApiOperation(value = "Owner replies user's comment, only once")
   public CommentDto replyComment (@PathVariable("id") String id, @RequestBody String reply) {
      return new CommentDto(service.addReply(id, reply));
   }
   
}
