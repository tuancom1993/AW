package com.amway.lms.backend.repository;


import java.util.List;

import com.amway.lms.backend.entity.Category;

public interface CategoryRepository {
    
    public List<Category> getCategoryList(int firstItem, int maxItem);
    

    public void addCategory(Category category);

    public void deleteCategory(Category category);

    public void editCategory(Category category);

    public Category getCategoryById(int id);
    
    public List<Category> searchByName(String name);
    
}
