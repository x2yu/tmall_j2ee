package tmall.servlet;


import java.awt.image.BufferedImage;	
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.util.ImageUtil;
import tmall.util.Page;

public class CategoryServlet extends BaseBackServlet {
	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		//创建一个HashMap对象
		Map<String,String> params = new HashMap<>();
		
		//设置文件上传路径
		//String savePath = this.getServletContext().getRealPath("/img/category");
		
		//获取输入流
		InputStream is = super.parseUpload(request, params);
		
		String name= params.get("name");
		Category c = new Category();
		c.setName(name);
		categoryDAO.add(c);
		
		
		String savePath = this.getServletContext().getRealPath("/img/category");
		File file = new File(savePath);
		
		 if (!file.exists() && !file.isDirectory()) {
			 System.out.println(savePath+"目录不存在，需要创建");
			//创建目录
			 file.mkdir();
			}
		
		file = new File(savePath,c.getId()+".jpg");
		try {
			if(null!=is && 0!=is.available()){
			    try(FileOutputStream fos = new FileOutputStream(file)){
			        byte b[] = new byte[1024 * 1024];
			        int length = 0;
			        while (-1 != (length = is.read(b))) {
			            fos.write(b, 0, length);
			        }
			        fos.flush();
			        is.close();
			        fos.close();
			        //通过如下代码，把文件保存为jpg格式
			        //BufferedImage img = ImageUtil.change2jpg(file);
			        //ImageIO.write(img, "jpg", file);		
			    }
			    catch(Exception e){
			    	e.printStackTrace();
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return "@admin_category_list";
	}

	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		categoryDAO.delect(id);
		
		//获取图片储存的位置
		String savePath = this.getServletContext().getRealPath("/img/category");
		
		//System.out.println(savePath);
		
		File file = new File(savePath,id+ ".jpg");
		//如果文件存在进行删除
		if (file.exists()) {
			file.delete();
		}

		return "@admin_category_list";
	}

	
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Category c = categoryDAO.get(id);
		request.setAttribute("c", c);//把Category对象放入request
		return "admin/editCategory.jsp";		
	}

	
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		Map<String,String> params = new HashMap<>();
		InputStream is = super.parseUpload(request, params);
		
		System.out.println(params);
		String name= params.get("name");
		int id = Integer.parseInt(params.get("id"));

		Category c = new Category();
		c.setId(id);
		c.setName(name);
		categoryDAO.update(c);
		
		String savePath = this.getServletContext().getRealPath("/img/category");
		File file = new File(savePath,id + ".jpg");
				
		try {
			if(null!=is && 0!=is.available()){
				//如果文件存在进行删除
				if (file.exists()) {
					file.delete();
				}
				//重新写入文件		
			    try(FileOutputStream fos = new FileOutputStream(file)){
			        byte b[] = new byte[1024 * 1024];
			        int length = 0;
			        while (-1 != (length = is.read(b))) {
			            fos.write(b, 0, length);
			        }
			        fos.flush();
			        //通过如下代码，把文件保存为jpg格式
			        //BufferedImage img = ImageUtil.change2jpg(file);
			        //ImageIO.write(img, "jpg", file);		
			    }
			    catch(Exception e){
			    	e.printStackTrace();
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "@admin_category_list";

	}

	//分页获取数据
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<Category> cs = categoryDAO.list(page.getStart(),page.getCount());
		int total = categoryDAO.getTotal();
		page.setTotal(total);
		
		request.setAttribute("thecs", cs);
		request.setAttribute("page", page);
		
		return "admin/listCategory.jsp";
	}
}
