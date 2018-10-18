package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;

public class OrderItemDAO {
	
	public int getTotal() {
		 int total = 0;
	        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
	  
	            String sql = "select count(*) from orderitem";
	  
	            ResultSet rs = s.executeQuery(sql);
	            while (rs.next()) {
	                total = rs.getInt(1);
	            }
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	        return total;
	}
	
	//添加订单
	  public void add(OrderItem bean) {
		  
	        String sql = "insert into orderitem values(null,?,?,?,?)";
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	  
	            ps.setInt(1, bean.getProduct().getId());
	             
	            //订单项在创建的时候，是没有订单信息的
	            if(null==bean.getOrder())
	                ps.setInt(2, -1);
	            else
	                ps.setInt(2, bean.getOrder().getId()); 
	             
	            ps.setInt(3, bean.getUser().getId());
	            ps.setInt(4, bean.getNumber());
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
	  
	  //更新订单信息
	  public void update(OrderItem bean) {
		  
	        String sql = "update orderitem set pid= ?, oid=?, uid=?,number=?  where id = ?";
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	 
	            ps.setInt(1, bean.getProduct().getId());
	            if(null==bean.getOrder())
	                ps.setInt(2, -1);
	            else
	                ps.setInt(2, bean.getOrder().getId());             
	            ps.setInt(3, bean.getUser().getId());
	            ps.setInt(4, bean.getNumber());
	             
	            ps.setInt(5, bean.getId());
	            ps.execute();
	  
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	  
	    }
	  //删除订单项
	  public void delete(int id) {
		  
	        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
	  
	            String sql = "delete from orderitem where id = " + id;
	  
	            s.execute(sql);
	  
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	    }
	  
	  //根据id获取订单项
	  public OrderItem get(int id) {
	        OrderItem bean = new OrderItem();
	  
	        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
	  
	            String sql = "select * from orderitem where id = " + id;
	  
	            ResultSet rs = s.executeQuery(sql);
	  
	            if (rs.next()) {
	                int pid = rs.getInt("pid");
	                int oid = rs.getInt("oid");
	                int uid = rs.getInt("uid");
	                int number = rs.getInt("number");
	                Product product = new ProductDAO().get(pid);
	                User user = new UserDAO().get(uid);
	                bean.setProduct(product);
	                bean.setUser(user);
	                bean.setNumber(number);
	                 
	                if(-1!=oid){
	                    Order order= new OrderDAO().get(oid);
	                    bean.setOrder(order);                  
	                }
	                 
	                bean.setId(id);
	            }
	  
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	        return bean;
	    }
	  
	  //查询某个用户的未生成订单的订单项(既购物车中的订单项)
	  public List<OrderItem> listByUser(int uid) {
	        return listByUser(uid, 0, Short.MAX_VALUE);
	    }
	  
	    public List<OrderItem> listByUser(int uid, int start, int count) {
	        List<OrderItem> beans = new ArrayList<OrderItem>();
	  
	        String sql = "select * from orderitem where uid = ? and oid=-1 order by id desc limit ?,? ";
	  
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	  
	            ps.setInt(1, uid);
	            ps.setInt(2, start);
	            ps.setInt(3, count);
	  
	            ResultSet rs = ps.executeQuery();
	  
	            while (rs.next()) {
	                OrderItem bean = new OrderItem();
	                int id = rs.getInt(1);
	 
	                int pid = rs.getInt("pid");
	                int oid = rs.getInt("oid");
	                int number = rs.getInt("number");
	                 
	                Product product = new ProductDAO().get(pid);
	                if(-1!=oid){
	                    Order order= new OrderDAO().get(oid);
	                    bean.setOrder(order);                  
	                }
	 
	                User user = new UserDAO().get(uid);
	                bean.setProduct(product);
	 
	                bean.setUser(user);
	                bean.setNumber(number);
	                bean.setId(id);               
	                beans.add(bean);
	            }
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	        return beans;
	    }
	    
	    //查询某种订单下所有的订单项
	    public List<OrderItem> listByOrder(int oid) {
	        return listByOrder(oid, 0, Short.MAX_VALUE);
	    }
	     
	    public List<OrderItem> listByOrder(int oid, int start, int count) {
	        List<OrderItem> beans = new ArrayList<OrderItem>();
	         
	        String sql = "select * from orderitem where oid = ? order by id desc limit ?,? ";
	         
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	             
	            ps.setInt(1, oid);
	            ps.setInt(2, start);
	            ps.setInt(3, count);
	             
	            ResultSet rs = ps.executeQuery();
	             
	            while (rs.next()) {
	                OrderItem bean = new OrderItem();
	                int id = rs.getInt(1);
	                 
	                int pid = rs.getInt("pid");
	                int uid = rs.getInt("uid");
	                int number = rs.getInt("number");
	                 
	                Product product = new ProductDAO().get(pid);
	                if(-1!=oid){
	                    Order order= new OrderDAO().get(oid);
	                    bean.setOrder(order);                  
	                }
	                 
	                User user = new UserDAO().get(uid);
	                bean.setProduct(product);
	                 
	                bean.setUser(user);
	                bean.setNumber(number);
	                bean.setId(id);               
	                beans.add(bean);
	            }
	        } catch (SQLException e) {
	             
	            e.printStackTrace();
	        }
	        return beans;
	    }
	    
	    // 为订单设置订单项集合
	    public void fill(List<Order> os) {
	        for (Order o : os) {
	            List<OrderItem> ois=listByOrder(o.getId());
	            float total = 0;
	            int totalNumber = 0;
	            for (OrderItem oi : ois) {
	                 total+=oi.getNumber()*oi.getProduct().getPromotePrice();
	                 totalNumber+=oi.getNumber();
	            }
	            o.setTotal(total);
	            o.setOrderItems(ois);
	            o.setTotalNumber(totalNumber);
	        }
	         
	    }
	 
	    public void fill(Order o) {
	        List<OrderItem> ois=listByOrder(o.getId());
	        float total = 0;
	        for (OrderItem oi : ois) {
	             total+=oi.getNumber()*oi.getProduct().getPromotePrice();
	        }
	        o.setTotal(total);
	        o.setOrderItems(ois);
	    }
	    
	    //根据产品显示订单项（搜索订单）
	    public List<OrderItem> listByProduct(int pid) {
	        return listByProduct(pid, 0, Short.MAX_VALUE);
	    }
	  
	    public List<OrderItem> listByProduct(int pid, int start, int count) {
	        List<OrderItem> beans = new ArrayList<OrderItem>();
	  
	        String sql = "select * from orderitem where pid = ? order by id desc limit ?,? ";
	  
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	  
	            ps.setInt(1, pid);
	            ps.setInt(2, start);
	            ps.setInt(3, count);
	  
	            ResultSet rs = ps.executeQuery();
	  
	            while (rs.next()) {
	                OrderItem bean = new OrderItem();
	                int id = rs.getInt(1);
	 
	                int uid = rs.getInt("uid");
	                int oid = rs.getInt("oid");
	                int number = rs.getInt("number");
	                 
	                Product product = new ProductDAO().get(pid);
	                if(-1!=oid){
	                    Order order= new OrderDAO().get(oid);
	                    bean.setOrder(order);                  
	                }
	 
	                User user = new UserDAO().get(uid);
	                bean.setProduct(product);
	 
	                bean.setUser(user);
	                bean.setNumber(number);
	                bean.setId(id);               
	                beans.add(bean);
	            }
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	        return beans;
	    }
	    
	    //获取某一种产品的销量。 产品销量就是这种产品对应的订单项OrderItem的number字段的总和
	    public int getSaleCount(int pid) {
	         int total = 0;
	            try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
	      
	                String sql = "select sum(number) from orderitem where pid = " + pid;
	      
	                ResultSet rs = s.executeQuery(sql);
	                while (rs.next()) {
	                    total = rs.getInt(1);
	                }
	            } catch (SQLException e) {
	      
	                e.printStackTrace();
	            }
	            return total;
	    }
}	



























