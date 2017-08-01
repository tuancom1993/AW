package com.amway.lms.backend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Category;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.repository.CategoryRepository;
import com.amway.lms.backend.service.CategoryService;

@Service
@Transactional
@EnableTransactionManagement
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory
            .getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryDao;

    @Override
    public ResponseEntity<?> getCategoryList(int firstItem, int maxItem) {
        try {
            List<Category> categories = categoryDao.getCategoryList(firstItem,
                    maxItem);
            if (categories == null || categories.size() == 0)
                throw new ObjectNotFoundException(
                        Message.MSG_CATEGORY_NOT_FOUND);
            return Utils.generateSuccessResponseEntity(categories);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCategoryList " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_ADD_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addCategory(Category category) {
        try {
            this.categoryDao.addCategory(category);
            return Utils.generateSuccessResponseEntity(category);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addCategory " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }

    }

    @Override
    public ResponseEntity<?> deleteCategories(List<Category> categories) {
        try {
            for (Category category : categories) {
                Category c = this.categoryDao.getCategoryById(category.getId());
                // Check if category doesn't exist in DB then throw Exception
                if (c == null)
                    throw new DeleteObjectException(
                            Message.MSG_CATEGORY_NOT_FOUND + "CategoryId="
                                    + category.getId());

                this.categoryDao.deleteCategory(c);
            }
            return Utils.generateSuccessResponseEntity(categories);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - deleteCategory " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> editCategory(Category category) {
        try {
            Category c = this.categoryDao.getCategoryById(category.getId());
            // Check if category doesn't exist in DB then throw Exception
            if (c == null) {
                logger.debug("editCategory***CategoryId=" + category.getId());
                throw new EditObjectException(Message.MSG_CATEGORY_NOT_FOUND
                        + "CategoryId=" + category.getId());
            }
            c.setName(category.getName());
            this.categoryDao.editCategory(c);
            return Utils.generateSuccessResponseEntity(category);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addCategory " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_EDIT_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getCategoryById(int id) {
        try {
            Category category = this.categoryDao.getCategoryById(id);
            if (category == null)
                throw new ObjectNotFoundException(
                        Message.MSG_CATEGORY_NOT_FOUND + " CategoryId=" + id);
            return Utils.generateSuccessResponseEntity(category);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCategoryById " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> searchByName(String name) {
        try {
            List<Category> categories = categoryDao.searchByName(name);
            if (categories == null || categories.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_CATEGORY_NOT_FOUND);
            return Utils.generateSuccessResponseEntity(categories);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - searchByName " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteCategory(int id) {
        try {
            Category c = this.categoryDao.getCategoryById(id);
            // Check if category doesn't exist in DB then throw Exception
            if (c == null) 
                throw new DeleteObjectException(Message.MSG_CATEGORY_NOT_FOUND + " CategoryId=" + id);
            this.categoryDao.deleteCategory(c);
            return Utils.generateSuccessResponseEntity(c);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - deleteCategory " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    e.getMessage());
        }
    }

}
