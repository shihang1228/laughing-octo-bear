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
import java.sql.DriverManager;


public class Manager extends HttpServlet
{
    public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        resp.getWriter().println("hello manager");
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException
    {
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        
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