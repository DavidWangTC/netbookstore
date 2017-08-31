package com.wwf.mapper;

import java.util.List;
import java.util.Map;

import com.wwf.domain.Book;
import com.wwf.domain.Category;

public interface CategoryMapper {

	void save(Category c);

	List<Category> findAll();

	Category findById(String categoryId);
	
	void updateCategoryById(Category c);

	void deleteCategoryById(String categoryId);
	
	Category findCategoryById(String categoryId);
	
	List<Category> findAllCategories();

	void insertCategory(Category category);

	int getTotalRecordsNum();

	List<Category> findPageRecords(Map<String, Object> map);

}
