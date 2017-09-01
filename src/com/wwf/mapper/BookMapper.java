package com.wwf.mapper;

import java.util.List;
import java.util.Map;

import com.wwf.common.Page;
import com.wwf.domain.Book;
import com.wwf.domain.Category;

public interface BookMapper {

	void save(Map<String, Object> map);

	/**
	 * 把书籍对应的分类也查询出来
	 * 
	 * @param bookId
	 * @return
	 */
	Book findBookById(String bookId);

	/**
	 * 获取书籍的总记录数
	 * 
	 * @return
	 */
	int getTotalRecordsNum();
	
	int getCategoryTotalRecordsNum(String categoryId);

	Page findBookPageRecords(String num);
	Page findBookPageRecords(String num, String categoryId);
	
	/**
	 * 分页查询:还要把书籍对应的分类查询出来
	 * 
	 * @param map包含两个Entry：<"startIndex",Integer>、<"pageSize",Integer>
	 * @return
	 */
	List<Book> findPageRecords(Map<String, Object> map);

	List<Book> findPageRecords(int startIndex, int pageSize, String categoryId);

	/**
	 * 更新一本书的内容，但其id作为主键不可更改；其它项根据传入对象中的非空项进行更新
	 * @param book
	 */
	void updateBookById(Map map);

	void deleteBookById(String bookId);

	List<Book> findPageRecordsByCategory(Map<String, Object> map);

}
