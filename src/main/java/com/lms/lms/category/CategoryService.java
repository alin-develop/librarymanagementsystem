package com.lms.lms.category;

import com.lms.lms.error.CategoryExistsException;
import com.lms.lms.error.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category addCategory(Category category) {
       Optional<Category> categoryExists =
               Optional.ofNullable(categoryRepository.findCategoryByNameAndParent(category.getName(), category.getParent()));
       if (categoryExists.isPresent()) throw new CategoryExistsException(category.getId());

       if (category.getParent()!=null){
            category.getParent().addChild(category);
       }

       if(category.getName().equalsIgnoreCase("All Categories")){
           category.setParent(category);
       }


       return categoryRepository.save(category);
    }

    public List<Category> findCategoryByName(String name){
        return categoryRepository.findByName(name);
    }


    public Category findCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    public Category renameCategory(Long categoryId, String name) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException(categoryId) );

        category.setName(name);
        return category;
    }

    @Transactional
    public Category addCategoryChild(Long categoryId, Long childId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
        Category child = categoryRepository.findById(childId).orElseThrow(() -> new CategoryNotFoundException(childId));

        List<Category> children = category.getChildren();
        if (children.contains(child)) throw new CategoryExistsException(childId);

        child.setParent(category);
        category.addChild(child);

        return category;
    }

    @Transactional
    public void removeCategory(Long categoryId) {
        if (categoryRepository.findById(categoryId).isEmpty()) throw new CategoryNotFoundException(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    public List<Category> getCategoryChildren(Long categoryId) {
        Category categoryById = findCategoryById(categoryId);
        return categoryById.getChildren();
    }
}
