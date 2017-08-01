package com.amway.lms.backend.rest;


import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.Category;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.CategoryService;

@RestController
@RequestMapping("/api/v1/")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;
    
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<?> getCategoryList()
            throws ObjectNotFoundException, Exception {
        logger.info("CategoryController********getCategoryList");
        return categoryService.getCategoryList(-1, -1);
    }
    
    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Integer id){
        logger.info("CategoryController********getCategoryById: id = " + id);
        return categoryService.getCategoryById(id);
    }

    @RequestMapping(value = "/categories/search", method = RequestMethod.GET)
    public ResponseEntity<?> searchByName(@RequestParam("name") String name)
            throws SQLException, ObjectNotFoundException, Exception {
        logger.info("CategoryController********searchByName: name = " + name);
        return categoryService.searchByName(name);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public ResponseEntity<?> addCategory(@RequestBody Category category)
            throws SQLException, AddObjectException, Exception {
        logger.info("CategoryController********addCategory: " + category);
        return this.categoryService.addCategory(category);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.PUT)
    public ResponseEntity<?> editCategory(@RequestBody Category category)
            throws SQLException, Exception {
        return this.categoryService.editCategory(category);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCategories(@RequestBody List<Category> categories)
            throws SQLException, Exception {
        return this.categoryService.deleteCategories(categories);
    }
    
    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Integer id)
            throws SQLException, Exception {
        return this.categoryService.deleteCategory(id);
    }

}
