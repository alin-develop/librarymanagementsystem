package com.lms.lms.category;

import com.lms.lms.error.CategoryExistsException;
import com.lms.lms.error.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock private CategoryRepository categoryRepository;

    private CategoryService underTest;

    Category all_categories = new Category("All Categories", null);
    Category fiction = new Category("Fiction", all_categories);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new CategoryService(categoryRepository);
    }

    @Test
    void addCategory_givenCategoryAlreadyExists() {
        //given
        given(categoryRepository.findCategoryByNameAndParent(any(),any())).willReturn(Optional.of(mock(Category.class)));
        //when
        //then
        assertThrows(CategoryExistsException.class, () -> underTest.addCategory(all_categories));
    }

    @Test
    void addCategory_givenCategoryIsRootCategory() {
        //given
        given(categoryRepository.findCategoryByNameAndParent(any(),any())).willReturn(Optional.empty());
        //when
        underTest.addCategory(all_categories);
        //then
        then(categoryRepository).should().save(any());
        assertEquals(all_categories, all_categories.getParent());
    }

    @Test
    void addCategory_givenCategoryHasParent() {
        //given
        given(categoryRepository.findCategoryByNameAndParent(any(),any())).willReturn(Optional.empty());
        //when
        underTest.addCategory(fiction);
        //then
        then(categoryRepository).should().save(any());
        assertTrue(fiction.getParent().getChildren().contains(fiction));
    }

    @Test
    void findCategoryById() {
        Optional<Category> mock = Optional.of(mock(Category.class));
        given(categoryRepository.findById(any())).willReturn(mock);
        Category categoryById = underTest.findCategoryById(any());
        assertEquals(mock.get(), categoryById);
    }

    @Test
    void findCategoryById_ifCategoryDoesntExist() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> underTest.findCategoryById(any()));
    }

    @Test
    void renameCategory_ifCategoryDoesntExist() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> underTest.renameCategory(any(), "name"));
    }

    @Test
    void renameCategory() {
        fiction.setName("fiction");
        given(categoryRepository.findById(any())).willReturn(Optional.ofNullable(fiction));
        Category result = underTest.renameCategory(any(), "Fiction");
        assertEquals("Fiction", result.getName());
    }

    @Test
    void addCategoryChild_ifCategoryNotFound() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> underTest.addCategoryChild(any(), 1L) );
    }

    @Test
    void addCategoryChild_ifChildNotFound() {
        given(categoryRepository.findById(2L)).willReturn(Optional.of(mock(Category.class)));
        given(categoryRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> underTest.addCategoryChild(2L, 1L) );
    }

    @Test
    void addCategoryChild_ifChildExistsInCategory() {
        all_categories.setChildren(Collections.singletonList(fiction));
        given(categoryRepository.findById(2L)).willReturn(Optional.of(all_categories));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(fiction));

        assertThrows(CategoryExistsException.class, () -> underTest.addCategoryChild(2L, 1L) );
    }

    @Test
    void addCategoryChild_ifValid() {
        all_categories.setChildren(List.of());
        given(categoryRepository.findById(2L)).willReturn(Optional.of(all_categories));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(fiction));

        underTest.addCategoryChild(2L, 1L);

        assertTrue(all_categories.getChildren().contains(fiction));
    }

    @Test
    void removeCategory_ifNotFound() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> underTest.removeCategory(1L) );
    }

    @Test
    void removeCategory() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(mock(Category.class)));
        underTest.removeCategory(2L);
        then(categoryRepository).should().deleteById(2L);
    }

    @Test
    void getCategoryChildren() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(all_categories));
        List<Category> categoryChildren = underTest.getCategoryChildren(all_categories.getId());
        assertEquals(all_categories.getChildren(), categoryChildren);
    }

}