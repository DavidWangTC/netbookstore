package com.wwf.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFormat.Encoding;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wwf.common.Page;
import com.wwf.domain.Book;
import com.wwf.domain.Category;
import com.wwf.domain.Manager;
import com.wwf.mapper.BookMapper;
import com.wwf.mapper.CategoryMapper;
import com.wwf.mapper.ManagerMapper;
import com.wwf.utils.Constants;
import com.wwf.utils.IdGenertor;

@Controller
@RequestMapping("/manage")
public class ManageController {
	private static final Log logger = LogFactory.getLog(ManageController.class);
	private ApplicationContext ac = new ClassPathXmlApplicationContext("config/applicationContext.xml");
	private BookMapper bookMapper = ac.getBean(BookMapper.class);
	private CategoryMapper categoryMapper = ac.getBean(CategoryMapper.class);
	private ManagerMapper managerMapper = ac.getBean(ManagerMapper.class);
	
	
	//展示书籍所有的类型
	@RequestMapping("/showPageCategorys")
	private String showPageCategorys(@RequestParam(required=false) String num, HttpServletRequest request, Model mod) {
		if(!loginValidate(request)) {
			return "mlogin";
		}
		int pageNum = 1;
		if (num != null && !num.equals("")) {
			pageNum = Integer.parseInt(num);
		}
		int totalRecordsNum = categoryMapper.getTotalRecordsNum();
		Page page = new Page(pageNum, totalRecordsNum);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startIndex", page.getStartIndex());
		map.put("pageSize", page.getPageSize());
		List<Category> records = categoryMapper.findPageRecords(map);
		page.setRecords(records);
		page.setUrl("/manage/showPageCategorys");
		mod.addAttribute("page", page);
		
		return "listCategory";
	}
	
	//修改一本书提交
	@RequestMapping("/updateBook")
	private void updateBook(HttpServletRequest request, HttpServletResponse response) {
		// 判断表单是不是multipart/form-data类型的
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			throw new RuntimeException("The form is not multipart/form-data");
		}
		// 解析请求内容
		// DiskFileItemFactory是FileItem对象工厂，可以盛放FileItem文件项；一个FileItem就代表一个上传的文件
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// ServletFileUpload接收上传文件，暂存在DiskFileItemFactory中
		ServletFileUpload sfu = new ServletFileUpload(factory);
		List<FileItem> items = new ArrayList<FileItem>();
		try {
			items = sfu.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

		Book book = new Book();// 空对象
		System.out.println("newbook.path=" + book.getPath() + " newbook.filename=" + book.getFilename());
		for (FileItem item : items) {
			// 普通字段：把数据封装到Book对象中
			if (item.isFormField()) {
				processFormFiled(item, book);
			} else {
				// 上传字段：上传
				if(item!=null) processUploadFiled(request, item, book);
			}
		}
		System.out.println("newbook.path=" + book.getPath() + " newbook.filename=" + book.getFilename());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", book.getId());
		map.put("name", book.getName());
		map.put("author", book.getAuthor());
		map.put("price", book.getPrice());
		map.put("path", book.getPath());
		map.put("filename", book.getFilename());
		map.put("description", book.getDescription());
		map.put("categoryId", book.getCategory().getId());
		
		// 把书籍信息保存到数据库中
		bookMapper.updateBookById(map);
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("书籍修改成功！2秒后返回");
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/manage/showPageBooks");
	}
	
	//修改一本书申请
	@RequestMapping("/editBook")
	private String editBook(@RequestParam String id, Model mod) {
		List<Category> cs = categoryMapper.findAllCategories();
		mod.addAttribute("cs", cs);
		Book book = bookMapper.findBookById(id);
		mod.addAttribute("book", book);
	if(book.getCategory()==null) {
		System.out.println("book.category==null");
	}
		return "editBook";
	}
	
	//删除一本书
	@RequestMapping("/deleteBook")
	private void deleteBook(@RequestParam String id, 
			HttpServletRequest request, HttpServletResponse response) {
		bookMapper.deleteBookById(id);
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("书籍删除成功！2秒后返回");
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/manage/showPageBooks");
	}
	
	//展示书籍
	@RequestMapping("/showPageBooks")
	private String showPageBooks(String num, HttpServletRequest request, Model mod) {
		if(!loginValidate(request)) {
			return "mlogin";
		}
		int pageNum = 1;
		if (num != null && !num.equals("")) {
			pageNum = Integer.parseInt(num);
		}
		int totalRecordsNum = bookMapper.getTotalRecordsNum();
		Page page = new Page(pageNum, totalRecordsNum);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startIndex", page.getStartIndex());
		map.put("pageSize", page.getPageSize());
		List<Book> records = bookMapper.findPageRecords(map);
		page.setRecords(records);
		page.setUrl("/manage/showPageBooks");
		mod.addAttribute("page", page);
		return "listBooks";
	}
	
	//添加书籍界面的提交
	@RequestMapping("/addBookSubmit")
	private void addBookSubmit(HttpServletRequest request, HttpServletResponse response) {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			throw new RuntimeException("The form is not multipart/form-data");
		}
		// 解析请求内容
		// DiskFileItemFactory是FileItem对象工厂，可以盛放FileItem文件项；一个FileItem就代表一个上传的文件
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// ServletFileUpload接收上传文件，暂存在DiskFileItemFactory中
		ServletFileUpload sfu = new ServletFileUpload(factory);
		List<FileItem> items = new ArrayList<FileItem>();
		try {
			items = sfu.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

		Book book = new Book();// 空对象
		book.setId(IdGenertor.genGUID());
		for (FileItem item : items) {
			// 普通字段：把数据封装到Book对象中
			if (item.isFormField()) {
				processFormFiled(item, book);
			} else {
				// 上传字段：上传
				processUploadFiled(request, item, book);
			}
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", book.getId());
		map.put("name", book.getName());
		map.put("author", book.getAuthor());
		map.put("price", book.getPrice());
		map.put("path", book.getPath());
		map.put("filename", book.getFilename());
		map.put("description", book.getDescription());
		map.put("categoryId", book.getCategory().getId());
		// 把书籍信息保存到数据库中
		bookMapper.save(map);

		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("书籍添加成功！2秒后返回");
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/manage/showPageBooks");
	}
	
	// 处理文件上传
	private void processUploadFiled(HttpServletRequest request, FileItem item, Book book) {
		String filename = item.getName(); // 如a.jpg
		if (filename == null || filename == "") return;
		// 存放路径：不要放在WEB-INF中
		String storeDirectory = request.getServletContext().getRealPath("/images");
		File rootDirectory = new File(storeDirectory);
		if (!rootDirectory.exists()) {
			rootDirectory.mkdirs();
		}
		// 生成文件名
		filename = IdGenertor.genGUID() + "." + FilenameUtils.getExtension(filename);// LKDSJFLKSFKS.jpg
		book.setFilename(filename);
		
		// 计算子目录
		String path = genChildDirectory(storeDirectory, filename);
		book.setPath(path);

		// 文件上传
		try {
			item.write(new File(rootDirectory, path + File.separator + filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	private String genChildDirectory(String realPath, String fileName) {
		int hashCode = fileName.hashCode();
		int dir1 = hashCode & 0xf;
		int dir2 = (hashCode & 0xf0) >> 4;

		String str = dir1 + File.separator + dir2;

		File file = new File(realPath, str);
		if (!file.exists()) {
			file.mkdirs();
		}
		return str;
	}

	// 把FileItem中的数据封装到Book中
	private void processFormFiled(FileItem item, Book book) {
		try {
			String fieldName = item.getFieldName();
			String fieldValue = item.getString("UTF-8");
			// 单独处理书籍所属的分类
			if ("categoryId".equals(fieldName)) {
				Category c = categoryMapper.findCategoryById(fieldValue);
				book.setCategory(c);
			}else {
				BeanUtils.setProperty(book, fieldName, fieldValue);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	//返回添加书籍的界面
	@RequestMapping("/addBookUI")
	private String addBookUI(HttpServletRequest request, Model mod) {
		if(!loginValidate(request)){
			return "mlogin";
		}
		List<Category> cs = categoryMapper.findAllCategories();
		mod.addAttribute("cs", cs);
		return "addBook";
	}
	
	//删除书籍类型
	@RequestMapping("/deleteCategory")
	private void deleteCategory(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
		categoryMapper.deleteCategoryById(id);
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("删除成功！2秒后返回类型页");
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/manage/showPageCategorys");
	}
	
	//修改书籍类型
	@RequestMapping("/editCategory")
	private void editCategory(Category category, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		//从jsp页面中表单提交到服务器的数据都放在request body中，其编码格式虽然可以在页面设置，
		//但有些流氓浏览器会将我们所设的忽略，故确保正确，需要我们在服务器端按想要的格式进行编码，
		//下面就是重新编码书籍类别的信息，以便正确存储和显示中文
		String name = category.getName();
		String description = category.getDescription();
		name = new String(name.getBytes("ISO-8859-1"),"UTF-8");
		description = new String(description.getBytes("ISO-8859-1"),"UTF-8");
		category.setName(name);
		category.setDescription(description);
		
		categoryMapper.updateCategoryById(category);
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("修改成功！2秒后返回类型页");
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/manage/showPageCategorys");
	}
	
	//返回后台主页
	@RequestMapping("/home")
	private String startPage() {
		return "mindex";
	}
	
	//登录认证
	private boolean loginValidate(HttpServletRequest request) {
		Manager manager = (Manager) request.getSession().getAttribute(Constants.MANAGER_LOGIN_FLAG);
		if(manager==null) {
			return false;
		}
		return true;
	}
	
	//管理员登录
	@RequestMapping("/managerLogin")
	private String managerLogin(@RequestParam String name, @RequestParam String password,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("name", name);
		map.put("password", password);
		List<Manager> list = managerMapper.findManagerByNameAndPassword(map);
		if(list.size()==0) {
			return "mlogin";
		}
		request.getSession().setAttribute(Constants.MANAGER_LOGIN_FLAG, list.get(0));
		return "mindex";
	}
	//管理员注销
	@RequestMapping("/managerLogout")
	private String managerLogout(HttpServletRequest request) {
		request.getSession().removeAttribute(Constants.MANAGER_LOGIN_FLAG);
		return "mindex";
	}
	
	//添加图书类别，验证是否已登录
	@RequestMapping("/addCategory")
	private String addCategory(HttpServletRequest request) {
		if(!loginValidate(request)){
			return "mlogin";
		}
		return "addCategory";
	}
	
	//添加书籍类别的表单提交处理
	@RequestMapping("/addCategorySubmit")
	private void addCategorySubmit(@RequestParam String name, @RequestParam(required=false) String description,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		name = new String(name.getBytes("ISO-8859-1"),"UTF-8");
		description = new String(description.getBytes("ISO-8859-1"),"UTF-8");
		Category category = new Category();
		category.setId(IdGenertor.genGUID());
		category.setName(name);
		category.setDescription(description);
		System.out.println("ID=" + category.getId() + " name=" + category.getName() + " desp=" + category.getDescription());
		categoryMapper.insertCategory(category);
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("图书类别添加成功！2秒后返回主页");
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/manage/showPageCategorys");
	}
	
	
	
}
