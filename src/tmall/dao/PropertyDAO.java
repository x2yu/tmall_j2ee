package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.DBUtil;

//产品属性
public class PropertyDAO {
	
	//获取某个产品的属性条数，根据cid
	
	public int getTotal(int cid) {
		int total = 0;
		try (Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
			String sql = "select count(*) from property where cid = " + cid;
			
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return total;
	}
	
	//增加产品属性
	
	public void add(Property bean) {
		String sql = "insert into property values(null,?,?)";
		try(Connection c = DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
			ps.setInt(1, bean.getCategory().getId());
			ps.setString(2, bean.getName());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			
			while(rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//更新修改产品属性
	  public void update(Property bean) {
		  
	        String sql = "update property set cid= ?, name=? where id = ?";
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	 
	            ps.setInt(1, bean.getCategory().getId());
	            ps.setString(2, bean.getName());
	            ps.setInt(3, bean.getId());
	            ps.execute();
	  
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	  
	    }
	
	
	//删除产品属性
	public void delect(int id) {
		try (Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
			String sql = "delete from property where id = " + id;
			s.execute(sql);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//根据id获取产品属性
	public Property get(int id) {
		Property bean = new Property();
		
		try (Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
			String sql = "select * from property where id = " + id;
			
			ResultSet rs = s.executeQuery(sql);
			
			while(rs.next()) {
				String name = rs.getString("name");
				int cid = rs.getInt("cid");
				bean.setName(name);
				Category category = new CategoryDAO().get(cid);
				bean.setCategory(category);
				bean.setId(id);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return bean;
		
	}
	
	//调用下面的分类查询方法，0是页数
	//，Short,Max_VALUE是每页显示数据的个数，加起来就是用分类查询显示成一页，所有的数据。
	public List<Property> list(int cid){
		return list(cid,0,Short.MAX_VALUE);
	}
	//查询方法
	public List<Property> list(int cid,int start,int count){
		List <Property> beans = new ArrayList<Property>();
		
		String sql = "select * from property where cid = ? order by id desc limit ?,?";
		
		try (Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Property bean = new Property();
				int id = rs.getInt(1);
				String name = rs.getString("name");
				bean.setName(name);
				Category category = new CategoryDAO().get(cid);
				bean.setCategory(category);
				bean.setId(id);
				
				beans.add(bean);
			}
			
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return beans;
		
	}
	
	
}
