package com.cryomate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import com.cryomate.model.User;
import com.cryomate.utils.JDBCUtils;



public class JDBCTest {

    @Test
    public void testUpdate(){
        //曾
        String sql = "insert into user (id,first_name, last_name) values (1,'wangba','131')";
        //删
        //String sql = "delete from employee where id = 1";
        //改
        //String sql = "update employee set name = 'fuck' where id = 2";
        //查
        String sqlQuery = "select * from user";
        update(sql);
        testQueryObject(sqlQuery);
    }
    
    public void testQueryObject(String sql){
        User employee = null;
        List<User> list = new ArrayList();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            //创建连接
            conn = JDBCUtils.getConnetions();
            //创建prepareStatement对象，用于执行SQL
            ps = conn.prepareStatement(sql);
            //获取查询结果集
            result = ps.executeQuery();
            while(result.next()){
                //employee = new User(result.getInt(1),result.getString(2),result.getString(3));
            	employee = new User();
            	employee.setId(result.getInt(1));
            	employee.setFirstName(result.getString(2));
            	employee.setLastName(result.getString(3));
                list.add(employee);
            }
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.release(result, ps, conn);
        }
    }
    
    public void update(String sql){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //创建数据库连接
            conn = JDBCUtils.getConnetions();
            //创建执行SQL的prepareStatement对象
            ps = conn.prepareStatement(sql);
            //用于增删改操作
            int result = ps.executeUpdate();
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("出现异常1="+e.toString());
        }finally{
            JDBCUtils.release(ps, conn);
        }

        
    }
}