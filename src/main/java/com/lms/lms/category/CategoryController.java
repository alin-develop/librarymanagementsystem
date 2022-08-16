package com.lms.lms.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/category")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping(path = "/get/children/{categoryId}")
    public List<Category> getChildren(@PathVariable("categoryId") Long categoryId){
        return categoryService.getCategoryChildren(categoryId);
    }


    @PostMapping(path = "/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        return new ResponseEntity<>(categoryService.addCategory(category), HttpStatus.OK);
    }

    @PutMapping(path = "/rename/{categoryId}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Category> renameCategory(@PathVariable("categoryId") Long categoryId, @RequestParam String name){
        return new ResponseEntity<>(categoryService.renameCategory(categoryId, name), HttpStatus.OK);
    }

    @PutMapping(path = "/addchilren")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Category> addCategoryChild(@RequestParam Long categoryId, @RequestParam Long childId){
        return new ResponseEntity<>(categoryService.addCategoryChild(categoryId, childId), HttpStatus.OK);
    }

    @DeleteMapping(path = "delete/{categoryId}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Category> deleteCategory(@PathVariable("categoryId") Long categoryId){
        categoryService.removeCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
