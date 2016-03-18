package com.guan.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guan.domain.Category;
import com.guan.domain.Post;
import com.guan.repo.CategoryRepository;
@Service
public class CategoryService {
 Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

   @Autowired
   private CategoryRepository repo;

   public List<Post> getPostsUnderCategory(String categoryId) {
      return repo.findOne(categoryId).getPosts();
   }
   
   public List<Post> getPosts() {
      List<Post> posts = new ArrayList<>();
      for(Category ca : repo.findAll()) {
         posts.addAll(ca.getPosts());
      }
      return posts;
   }

   public List<Category> getCategories() {
      return repo.findAll();
   }

   public Category save(Category category) {
      return repo.save(category);
   }
}