package tmall.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import tmall.bean.Category;
import tmall.bean.OrderItem;
import tmall.bean.User;
import tmall.dao.CategoryDAO;
import tmall.dao.OrderItemDAO;



public class ForeServletFilter implements Filter {


    public ForeServletFilter() {
        // TODO Auto-generated constructor stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}


	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String contextPath = request.getServletContext().getContextPath();
		request.getServletContext().setAttribute("contextPath", contextPath);
		
		User user = (User)request.getSession().getAttribute("user");
		int cartTotalItemNumber = 0;
		if(null!=user) {
			List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
			for(OrderItem oi: ois) {
				cartTotalItemNumber+=oi.getNumber();
			}
		}
		request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);//设置购物车商品数
		
		List<Category> cs = (List<Category>) request.getAttribute("cs");
		if(null==cs) {
			cs = new CategoryDAO().list();
			request.setAttribute("cs", cs);
		}
		
		String uri = request.getRequestURI();
		uri = StringUtils.remove(uri,contextPath);
		if(uri.startsWith("/fore")&&!uri.startsWith("/foreServlet")) {
			String method = StringUtils.substringAfterLast(uri, "/fore");
			request.setAttribute("method", method);
			req.getRequestDispatcher("/foreServlet").forward(request, response);
			return;
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
