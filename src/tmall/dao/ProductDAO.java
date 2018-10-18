package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.RowSet;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ProductDAO {

	 public int getTotal(int cid) {
	        int total = 0;
	        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
	  
	            String sql = "select count(*) from product where cid = " + cid;
	  
	            ResultSet rs = s.executeQuery(sql);
	            while (rs.next()) {
	                total = rs.getInt(1);
	            }
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	        return total;
	    }
	  
	    public void add(Product bean) {
	 
	        String sql = "insert into product values(null,?,?,?,?,?,?,?)";
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	  
	            ps.setString(1, bean.getName());
	            ps.setString(2, bean.getSubTitle());
	            ps.setFloat(3, bean.getOrignalPrice());
	            ps.setFloat(4, bean.getPromotePrice());
	            ps.setInt(5, bean.getStock());
	            ps.setInt(6, bean.getCategory().getId());
	            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
	            ps.execute();
	  
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                int id = rs.getInt(1);
	                bean.setId(id);
	            }
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	    }
	  
	    public void update(Product bean) {
	 
	        String sql = "update product set name= ?, subTitle=?, orignalPrice=?,promotePrice=?,stock=?, cid = ?, createDate=? where id = ?";
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	 
	            ps.setString(1, bean.getName());
	            ps.setString(2, bean.getSubTitle());
	            ps.setFloat(3, bean.getOrignalPrice());
	            ps.setFloat(4, bean.getPromotePrice());
	            ps.setInt(5, bean.getStock());
	            ps.setInt(6, bean.getCategory().getId());
	            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
	            ps.setInt(8, bean.getId());
	            ps.execute();
	  
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	  
	    }
	  
	    public void delete(int id) {
	  
	        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
	  
	            String sql = "delete from product where id = " + id;
	  
	            s.execute(sql);
	  
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	    }
	  
	
	//根据id 获取产品信息
	public Product get(int id) {
		// TODO 自动生成的方法存根
		  Product bean = new Product();
		  
	        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
	  
	            String sql = "select * from product where id = " + id;
	  
	            ResultSet rs = s.executeQuery(sql);
	  
	            if (rs.next()) {
	 
	                String name = rs.getString("name");
	                String subTitle = rs.getString("subTitle");
	                float orignalPrice = rs.getFloat("orignalPrice");
	                float promotePrice = rs.getFloat("promotePrice");
	                int stock = rs.getInt("stock");
	                int cid = rs.getInt("cid");
	                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
	               
	                bean.setName(name);
	                bean.setSubTitle(subTitle);
	                bean.setOrignalPrice(orignalPrice);
	                bean.setPromotePrice(promotePrice);
	                bean.setStock(stock);
	                Category category = new CategoryDAO().get(cid);
	                bean.setCategory(category);
	                bean.setCreateDate(createDate);
	                bean.setId(id);
	                setFirstProductImage(bean);//设置大图
	            }
	  
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	        return bean;
	    }
		
		//分页显示根据分类id
	 public List<Product> list(int cid) {
	        return list(cid,0, Short.MAX_VALUE);
	    }
	  
	    public List<Product> list(int cid, int start, int count) {
	        List<Product> beans = new ArrayList<Product>();
	        Category category = new CategoryDAO().get(cid);
	        String sql = "select * from product where cid = ? order by id desc limit ?,? ";
	  
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	            ps.setInt(1, cid);
	            ps.setInt(2, start);
	            ps.setInt(3, count);
	  
	            ResultSet rs = ps.executeQuery();
	  
	            while (rs.next()) {
	                Product bean = new Product();
	                int id = rs.getInt(1);
	                String name = rs.getString("name");
	                String subTitle = rs.getString("subTitle");
	                float orignalPrice = rs.getFloat("orignalPrice");
	                float promotePrice = rs.getFloat("promotePrice");
	                int stock = rs.getInt("stock");
	                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
	 
	                bean.setName(name);
	                bean.setSubTitle(subTitle);
	                bean.setOrignalPrice(orignalPrice);
	                bean.setPromotePrice(promotePrice);
	                bean.setStock(stock);
	                bean.setCreateDate(createDate);
	                bean.setId(id);
	                bean.setCategory(category);
	                setFirstProductImage(bean);
	                beans.add(bean);
	            }
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	        return beans;
	    }
	    
	    //根据产品id分页显示
	    public List<Product> list() {
	        return list(0,Short.MAX_VALUE);
	    }
	    public List<Product> list(int start, int count) {
	        List<Product> beans = new ArrayList<Product>();
	 
	        String sql = "select * from product limit ?,? ";
	  
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	 
	            ps.setInt(1, start);
	            ps.setInt(2, count);
	  
	            ResultSet rs = ps.executeQuery();
	  
	            while (rs.next()) {
	                Product bean = new Product();
	                int id = rs.getInt(1);
	                int cid = rs.getInt("cid");
	                String name = rs.getString("name");
	                String subTitle = rs.getString("subTitle");
	                float orignalPrice = rs.getFloat("orignalPrice");
	                float promotePrice = rs.getFloat("promotePrice");
	                int stock = rs.getInt("stock");
	                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
	 
	                bean.setName(name);
	                bean.setSubTitle(subTitle);
	                bean.setOrignalPrice(orignalPrice);
	                bean.setPromotePrice(promotePrice);
	                bean.setStock(stock);
	                bean.setCreateDate(createDate);
	                bean.setId(id);
	 
	                Category category = new CategoryDAO().get(cid);
	                bean.setCategory(category);
	                beans.add(bean);
	            }
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	        return beans;
	    }   
	    
	    //为分类填充产品集合
	    public void fill(List<Category> cs) {
	        for (Category c : cs)
	            fill(c);
	    }
	    public void fill(Category c) {
	            List<Product> ps = this.list(c.getId());
	            c.setProducts(ps);
	    }
	    
	    //在页面上显示的时候，需要每8种产品，放在一列 为了显示的方便，把这40种产品，
	    //按照每8种产品方在一个集合里的方式，拆分成了5个小的集合，这5个小的集合里的每个元素是8个产品
	    public void fillByRow(List<Category> cs) {
	    	int productNumberEachRow = 8;
	    	for(Category c: cs) {
	    		List<Product> products = c.getProducts();
	    		List<List<Product>> productsByRow = new ArrayList<>();
	    		
	    		for(int i = 0;i < products.size();i+=productNumberEachRow) {
	    			int size = i+productNumberEachRow;
	    			 size= size>products.size()?products.size():size;
	                 List<Product> productsOfEachRow =products.subList(i, size);
	                 productsByRow.add(productsOfEachRow);
	    		}
	    		c.setProductsByRow(productsByRow);
	    	}
	    }
	    
	    //设置产品显示大图
	    public void setFirstProductImage(Product p) {
	    	 List<ProductImage> pis= new ProductImageDAO().list(p, ProductImageDAO.type_single);
	    	 if(!pis.isEmpty()) {
	    		 p.setFirstProductImage(pis.get(0));
	    	 }
	    }
	    
	    //为产品设置销售和评价数量
	    public void setSaleAndReviewNumber(Product p) {
	    	int saleCount = new OrderItemDAO().getSaleCount(p.getId());
	    	p.setSaleCount(saleCount);
	    	
	    	int reviewCount = new ReviewDAO().getCount(p.getId());
	    	p.setReviewCount(reviewCount);
	    }
	    //集合循环调用上方函数
	    
	    public void setSaleAndReviewNUmber(List<Product> products) {
	    	for(Product p: products) {
	    		setFirstProductImage(p);
	    	}
	    }
	    
	    //根据关键字查询产品
	    public List<Product> search(String keyword,int start,int count){
	    	List<Product> beans = new ArrayList<>();
	    	
	    	if(null==keyword||0==keyword.trim().length()) {
	    		return beans;
	    	}
	    	String sql = "select * from product where name like ? limit ?,?";
	    	
	    	try (Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
				ps.setString(1, "%"+keyword.trim()+"%");
				ps.setInt(2, start);
				ps.setInt(3, count);
				
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					Product bean = new Product();
					int id = rs.getInt(1);
					int cid = rs.getInt("cid");
					String name = rs.getString("name");
					String subTitle = rs.getString("subTitle");
					float orignalPrice = rs.getFloat("orignalPrice");
					float promotePrice = rs.getFloat("promotePrice");
					int stock = rs.getInt("stock");
					Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
					
					bean.setName(name);
					bean.setSubTitle(subTitle);
					bean.setOrignalPrice(orignalPrice);
					bean.setPromotePrice(promotePrice);
					bean.setStock(stock);
					bean.setCreateDate(createDate);
					bean.setId(id);
					
					Category category = new CategoryDAO().get(cid);
					bean.setCategory(category);
					setFirstProductImage(bean);
					beans.add(bean);
					
					
				}
			} catch (SQLException e) {
				// TODO: handle exception
			}
	    	return beans;
	    }
	    
	    
	}



























