package tmall.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.HtmlUtils;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.bean.PropertyValue;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.comparator.ProductAllComparator;
import tmall.comparator.ProductDateComparator;
import tmall.comparator.ProductPriceComparator;
import tmall.comparator.ProductReviewComparator;
import tmall.comparator.ProductSaleCountComparator;
import tmall.dao.CategoryDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;
import tmall.util.Page;

public class ForeServlet extends BaseForeServlet {
	public String home(HttpServletRequest request,HttpServletResponse response,Page page) {
		List<Category> cs = new CategoryDAO().list();
		//为这些分类填充产品集合
		new ProductDAO().fill(cs);
		
		//为这些分类填充推荐产品集合，即为每个Category对象，设置productsByRow属性
		new ProductDAO().fillByRow(cs);
		
		request.setAttribute("cs", cs);
		return "home.jsp";
	}
	
	//注册方法
	public String register(HttpServletRequest request,HttpServletResponse response,Page page) {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		name = HtmlUtils.htmlEscape(name);
		
		System.out.println(name);
		
		boolean exsit = userDAO.isExist(name);
		if(exsit) {
			request.setAttribute("msg", "用户名已经被使用");
			return "register.jsp";
		}
		User user=new User();
		user.setName(name);
		user.setPassword(password);
		userDAO.add(user);
		
		return "@registerSuccess.jsp";
	}
	//登陆方法
	public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
	    String name = request.getParameter("name");
	    name = HtmlUtils.htmlEscape(name);
	    String password = request.getParameter("password");    
	     
	    User user = userDAO.get(name,password);
	      
	    if(null==user){
	        request.setAttribute("msg", "账号密码错误");
	        return "login.jsp";
	    }
	    request.getSession().setAttribute("user", user);
	    return "@forehome";
	}  
	//登出方法
	public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
	    request.getSession().removeAttribute("user");
	    return "@forehome";
	}  
	
	//产品详情页
	public String product(HttpServletRequest request,HttpServletResponse response,Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.get(pid);
		
		List<ProductImage> productSingleImages = productImageDAO.list(p, ProductImageDAO.type_single);
		List<ProductImage> productDetailImages = productImageDAO.list(p, ProductImageDAO.type_detail);
		p.setProductSingleImages(productSingleImages);
		p.setProductDetailImages(productDetailImages);
		
		List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
		
		List<Review> reviews = reviewDAO.list(p.getId());
		
		productDAO.setSaleAndReviewNumber(p);
		
		request.setAttribute("p", p);
		request.setAttribute("pvs", pvs);
		
		return "product.jsp";
	}
	
	//检查登陆状态
	public String checkLogin(HttpServletRequest request,HttpServletResponse response,Page page) {
		User user = (User)request.getSession().getAttribute("user");
		if(null!=user)
			return "%success";
		return "%fail";		
	}
	
	//异步调用的登陆状态验证方法
	public String loginAjax(HttpServletRequest request,HttpServletResponse response,Page page) {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		User user = userDAO.get(name, password);
		
		if(null==user) {
			return "%fail";
		}
		//将用户存入session
		request.getSession().setAttribute("user", user);
		return "%success";
		
	}
	
	//分类页
	public String category(HttpServletRequest request,HttpServletResponse response,Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = new CategoryDAO().get(cid);
		//为c填充包含的铲产品
		new ProductDAO().fill(c);
		new ProductDAO().setSaleAndReviewNUmber(c.getProducts());
		
		//如果sort==null，即不排序
		//如果sort!=null，则根据sort的值，从5个Comparator比较器中选择一个对应的排序器进行排序
		String sort = request.getParameter("sort");
		if(null!=sort) {
			switch (sort) {
			case "review":
				Collections.sort(c.getProducts(), new ProductReviewComparator());
				break;
			case "date":
				Collections.sort(c.getProducts(), new ProductDateComparator());
				break;
			case "saleCount":
				Collections.sort(c.getProducts(), new ProductSaleCountComparator());
				break;
			case "price":
				Collections.sort(c.getProducts(), new ProductPriceComparator());
				break;
			case "all":
				Collections.sort(c.getProducts(), new ProductAllComparator());
				break;
			default:
				break;
			}
		}
		request.setAttribute("c", c);
		return "category.jsp";	
	}
	
	//搜索功能实现
	public String search(HttpServletRequest request,HttpServletResponse response,Page page) {
		String keyword = request.getParameter("keyword");
		List<Product> ps = new ProductDAO().search(keyword, 0, 20);
		productDAO.setSaleAndReviewNUmber(ps);
		request.setAttribute("ps", ps);
		return "searchResult.jsp";
		
	}
}


















