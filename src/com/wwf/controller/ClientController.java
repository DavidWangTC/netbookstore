package com.wwf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wwf.common.Cart;
import com.wwf.common.CartItem;
import com.wwf.common.Page;
import com.wwf.domain.Book;
import com.wwf.domain.Category;
import com.wwf.domain.Customer;
import com.wwf.domain.Manager;
import com.wwf.domain.Order;
import com.wwf.domain.OrderItem;
import com.wwf.mapper.BookMapper;
import com.wwf.mapper.CategoryMapper;
import com.wwf.mapper.CustomerMapper;
import com.wwf.mapper.OrderMapper;
import com.wwf.utils.Constants;
import com.wwf.utils.IdGenertor;

@Controller
@RequestMapping("/client")
public class ClientController {
	private ApplicationContext ac = new ClassPathXmlApplicationContext("config/applicationContext.xml");
	private BookMapper bookMapper = ac.getBean(BookMapper.class);
	private CategoryMapper categoryMapper = ac.getBean(CategoryMapper.class);
	private OrderMapper orderMapper = ac.getBean(OrderMapper.class);
	private CustomerMapper customerMapper = ac.getBean(CustomerMapper.class);

	// 返回购书主页
	@RequestMapping("/home")
	private void home(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher("/client/showPageBooks").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		;
	}

	// 展示所有书籍
	@RequestMapping("/showPageBooks")
	private String showPageBooks(@RequestParam(required = false) String num, Model mod) {
		// 查询所有分类
		List<Category> cs = categoryMapper.findAllCategories();
		mod.addAttribute("cs", cs);
		// 查询所有书籍：分页
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
		page.setUrl("/client/showPageBooks");
		mod.addAttribute("page", page);
		return "/client/listBooks";
	}

	// 按指定类型展示图书
	@RequestMapping("/showPageBooksByCategory")
	private String showPageBooksByCategory(@RequestParam String categoryId, String num, Model mod) {
		// 查询所有分类
		List<Category> cs = categoryMapper.findAllCategories();
		mod.addAttribute("cs", cs);
		// 查询所有书籍：分页
		int pageNum = 1;
		if (num != null && !num.equals("")) {
			pageNum = Integer.parseInt(num);
		}
		int totalRecordsNum = bookMapper.getCategoryTotalRecordsNum(categoryId);
		Page page = new Page(pageNum, totalRecordsNum);
		if (totalRecordsNum == 0) {
			page.setTotalPageNum(1);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startIndex", page.getStartIndex());
		map.put("pageSize", page.getPageSize());
		map.put("categoryId", categoryId);
		List<Book> records = bookMapper.findPageRecordsByCategory(map);
		page.setRecords(records);
		page.setUrl("/client/showPageBooksByCategory?categoryId=" + categoryId);
		mod.addAttribute("page", page);
		return "/client/listBooksByCategory";
	}

	// 显示书籍详情页
	@RequestMapping("/showBookDetail")
	private String showBookDetail(@RequestParam String bookId, Model mod) {
		Book book = bookMapper.findBookById(bookId);
		mod.addAttribute("book", book);
		return "/client/bookDetail";
	}

	// 加入购物车
	@RequestMapping("/buy")
	private void buy(@RequestParam String bookId, HttpServletRequest request, HttpServletResponse response) {
		Book book = bookMapper.findBookById(bookId);
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute(Constants.HTTPSESSION_CART);
		if (cart == null) {
			cart = new Cart();
			session.setAttribute(Constants.HTTPSESSION_CART, cart);
		}
		cart.addBook(book);
		// 返回主页
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("已加入购物车！2秒后转向主页");
			response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/client/home");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//删除一个购物项
	@RequestMapping("/delOneItem")
	private String delOneItem(@RequestParam String bookId, HttpServletRequest request) {
		Cart cart = (Cart)request.getSession().getAttribute(Constants.HTTPSESSION_CART);
		cart.getItems().remove(bookId);
		return "/client/showCart";
	}
	
	//删除所有购物项
	@RequestMapping("/delAllItems")
	private void delAllItems(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		request.getSession().removeAttribute(Constants.HTTPSESSION_CART);
		request.getRequestDispatcher("/jsp/client/showCart.jsp").forward(request, response);
//		response.sendRedirect(request.getContextPath() + "/jsp/client/showCart.jsp");
//		return "/client/showCart";
	}

	//修改购物项的数量
	@RequestMapping("/changeNum")
	private String changeNum(@RequestParam String num, @RequestParam String bookId, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		Cart cart = (Cart)request.getSession().getAttribute(Constants.HTTPSESSION_CART);
		CartItem item = cart.getItems().get(bookId);
		item.setQuantity(Integer.parseInt(num));
		return "/client/showCart";
//		response.sendRedirect(request.getContextPath()+"/showCart.jsp");
	}
	
	//生成订单
	@RequestMapping("/genOrder")
	private void genOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//验证用户是否登录
		HttpSession session = request.getSession();
		Customer c = (Customer) session.getAttribute(Constants.CUSTOMER_LOGIN_FLAG);
		//没有登录：去登录
		if(c==null){
			response.sendRedirect(request.getContextPath()+"/jsp/client/login.jsp");
			return;
		}
		//登录：
		//取出购物车的信息-------->Order.订单号没搞
		//取出购物项的信息--------->OrderItem.id没搞
		Cart cart = (Cart) session.getAttribute(Constants.HTTPSESSION_CART);
		if(cart==null){
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("会话超时!");
			response.sendRedirect(request.getContextPath() + "/client/home");
			return;
		}
		Order order = new Order();
		order.setOrdernum(IdGenertor.genOrdernum());
		order.setQuantity(cart.getTotalQuantity());
		order.setMoney(cart.getTotalMoney());
		order.setCustomer(c);
		//搞定单项
		List<OrderItem> oItems = new ArrayList<OrderItem>();
		for(Map.Entry<String,CartItem> me:cart.getItems().entrySet()){
			CartItem cItem = me.getValue();//购物车中的购物项
			OrderItem oItem = new OrderItem();
			oItem.setId(IdGenertor.genGUID());
			oItem.setBook(cItem.getBook());//忘记会造成DAO异常
			oItem.setPrice(cItem.getMoney());
			oItem.setQuantity(cItem.getQuantity());
			oItems.add(oItem);
		}
		//建立订单项和订单的关系
		order.setItems(oItems);
		order.setStatus(0);
		//保存订单：跳转到在线支付页面
		orderMapper.genOrder(order,c.getId());
		request.setAttribute("order", order);
		request.setAttribute("address", c.getAddress());
		try {
			request.getRequestDispatcher("/jsp/client/pay.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	//客户注册
	@RequestMapping("/customerRegister")
	private void customerRegister(Customer customer, HttpServletRequest request, HttpServletResponse response) {
		customer.setId(IdGenertor.genGUID());
		customerMapper.add(customer);
		try {
			response.sendRedirect(request.getContextPath() + "/client/home");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//客户登录
	@RequestMapping("/customerLogin")
	private void customerLogin(@RequestParam String username, @RequestParam String password,
			HttpServletRequest request, HttpServletResponse response) {
		List<Customer> list = customerMapper.findByUsernameAndPassword(username, password);
		if(list.size()==0) {
			try {
				request.getRequestDispatcher("/jsp/client/login.jsp").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CUSTOMER_LOGIN_FLAG, list.get(0));
		try {
			response.sendRedirect(request.getContextPath() + "/client/home");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//客户注销
	@RequestMapping("/customerLogout")
	private void customerLogout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.removeAttribute(Constants.CUSTOMER_LOGIN_FLAG);
		try {
			response.sendRedirect(request.getContextPath() + "/client/home");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//查看订单
	@RequestMapping("/showAllOrders")
	private String showAllOrders(HttpServletRequest request, Model mod) {
		Customer customer = (Customer) request.getSession().getAttribute(Constants.CUSTOMER_LOGIN_FLAG);
		if(customer==null) {
			return "/client/login";
		}
		List<Order> orders = orderMapper.findOrderByCustomerid(customer.getId());
		mod.addAttribute("orders", orders);
		
		return "/client/listOrders";
	}
	
	//登录认证
	private boolean loginValidate(HttpServletRequest request) {
		Customer customer = (Customer) request.getSession().getAttribute(Constants.CUSTOMER_LOGIN_FLAG);
		if(customer==null) {
			return false;
		}
		return true;
	}
	
}
