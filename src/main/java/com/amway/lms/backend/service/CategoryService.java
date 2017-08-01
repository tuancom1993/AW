package com.amway.lms.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Category;

public interface CategoryService {

    public ResponseEntity<?> searchByName(String name);

    public ResponseEntity<?> getCategoryList(int firstItem, int maxItem);

    public ResponseEntity<?> addCategory(Category category);

    public ResponseEntity<?> deleteCategories(List<Category> categories);

    public ResponseEntity<?> deleteCategory(int id);

    public ResponseEntity<?> editCategory(Category category);

    public ResponseEntity<?> getCategoryById(int id);
}
