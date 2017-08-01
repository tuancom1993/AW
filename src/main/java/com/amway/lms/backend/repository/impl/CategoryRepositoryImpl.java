package com.amway.lms.backend.repository.impl;

/* 
 * Hung (Charles) V. PHAM
 * Last Modify: 06-Feb-2017  
 * CategoryDaoImpl - Ren 40
 */

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Category;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.CategoryRepository;

@Repository
public class CategoryRepositoryImpl extends AbstractRepository<Serializable, Category>
        implements CategoryRepository {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRepositoryImpl.class);

    @Override
    public List<Category> getCategoryList(int firstItem, int maxItem) {
        logger.info("CategoryRepositoryImpl*********getCategoryList");
        Query query = createNamedQuery("getCategoryList", firstItem, maxItem);
        return (List<Category>) query.list();
    }
    

    @Override
    public void addCategory(Category category) {
        category.setCreatedAt(Utils.getCurrentTime());
        persist(category);
        logger.info("Category saved successfully, Category Details=" + category);
    }

    @Override
    public void deleteCategory(Category category) {
        delete(category);
        logger.info("Category deleted successfully, Category Details=" + category);

    }

    @Override
    public void editCategory(Category category) {
        category.setUpdatedAt(Utils.getCurrentTime());
        update(category);
        logger.info("Category edited successfully, Category Details=" + category);
    }

    @Override
    public Category getCategoryById(int id) {
        return getByKey(id);

    }

    @Override
    public List<Category> searchByName(String name) {
        logger.info("CategoryRepositoryImpl*********searchByName");
        String searchStr = "%" + name + "%";
        Query query = createNamedQuery("searchByName", -1, -1, searchStr);
        return (List<Category>) query.list();
    }

}
