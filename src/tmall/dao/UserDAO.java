package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.User;
import tmall.util.DBUtil;

public class UserDAO {
	//查询用户数量
	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection();Statement s =c.createStatement(); ){
			String sql = "select count(*) from User";
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
	
	//增加用户
	public void add(User bean) {
		String sql = "insert into user values(null,?,?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);){
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getPassword());
			
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
	//更新用户资料
	public void update(User bean) {
		String sql = "update user set name= ? , password = ? where id = ? ";
		try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getPassword());
			
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
	//根据id删除用户
	public void delect(int id) {
		try (Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
			String sql = "delect from User where id = " + id;
			s.execute(sql);
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	//根据id获取用户
	public User get(int id) {
		User bean = null;
		try (Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
			String sql = "select * from User where id = " + id;
			
			ResultSet rs = s.executeQuery(sql);
			
			while (rs.next()) {
				bean = new User();
				String name = rs.getString("name");
				bean.setName(name);
				String password = rs.getString("password");
				bean.setPassword(password);
				bean.setId(id);
				
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bean;
	}
	
	//用户集合查询返回
	public List<User> list(){
		return list(0,Short.MAX_VALUE);
	}
	
	public List<User> list(int start, int count){
		List<User> beans = new ArrayList<User>();
		
		String sql = "select * from User order by id desc limit ?,? ";
		try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);) {
			ps.setInt(1, start);
			ps.setInt(2, count);
			
			ResultSet rs = ps.executeQuery();
			
			while( rs.next()) {
				User bean = new User();
				int id = rs.getInt(1);
				
				String name = rs.getString("name");
				bean.setName(name);
				
				String password = rs.getString("password");
				bean.setPassword(password);
				
				bean.setId(id);
				beans.add(bean);
				
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return beans;
	}
	
	//定义一个boolean类型的函数来判断用户是否存在
	public boolean isExist(String name) {
		User user = get(name);
		return user!=null;
	}
	//根据用户名查询用户
	public User get(String name) {
		User bean = null;
		String sql = "select * from User where name = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);){
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				bean = new User();
				int id = rs.getInt("id");
				bean.setName(name);
				String password = rs.getString("password");
				bean.setPassword(password);
				bean.setId(id);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bean;
	}
	
	//根据用户名和密码获取用户
	public User get(String name,String password) {
		User bean = null;
		String sql = "select * from user where name = ? and password = ?";
		try (Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
			ps.setString(1, name);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				bean = new User();
				int id = rs.getInt("id");
				bean.setId(id);
				bean.setName(name);
				bean.setPassword(password);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bean;
	}
	
}
