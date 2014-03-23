package com.bodejidi;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;


public class Manager extends HttpServlet
{
    public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();           
        }
        catch(Exception ex)
        {
            //ignore
        }
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?"
                                               +"user=root"
                                               +"&password=");
            resp.setContentType("text/html;charset=utf-8");
            stmt = conn.createStatement();
            resp.getWriter().println("<html><head><title>会员管理</title></head><body><h1>会员列表</h1><table border=\"2\">");
            String sql = "SELECT * FROM shihang";
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            resp.getWriter().println("<tr><th>ID</th><th>Name</th></tr>");
            while(rs.next())
            {
                Long id = rs.getLong("ID");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                resp.getWriter().println("<tr><td>" + id + "</td><td>" + firstName + lastName + "</td></tr>");
           
            }
            resp.getWriter().println("</table><a href=\".\">Add Member</a></body></html>");
        }
        
        
        catch(SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            resp.getWriter().println("Error!");

        }
                
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException
    {
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        resp.setContentType("text/html;charset=utf-8");
        
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(Exception ex)
        {
            //ignore;
        }
        
        Connection conn = null;
        Statement stmt = null;
        try
        {
            conn =
                   DriverManager.getConnection("jdbc:mysql://localhost/test?"
                                               + "user=root"
                                               + "&password=");
            stmt = conn.createStatement();
            String sql = "INSERT INTO shihang(first_name,last_name,date_created,last_updated)"
                       + "VALUES('" + firstName + "','" + lastName + "',now(),now())";
            System.out.println("SQL: " + sql);
            stmt.execute(sql);
            resp.getWriter().println("Add " + firstName + " " + lastName + " Success!");
            resp.getWriter().println("<a href=\"member\">Member List</a>");
            
        }catch(SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            resp.getWriter().println("Error!");
        }finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }catch(SQLException ex)
                {
                    //ignore;
                }
            } 
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }catch(SQLException ex)
                {
                    //ignore;
                }
            }
        }
    }
}