package com.bodejidi;

import javax.servlet.http.HttpSession;
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
import java.lang.AutoCloseable;
import java.util.Date;

public class Manager extends HttpServlet
{
    static final String jdbcUrl = "jdbc:mysql://localhost/test?" + "user=root" + "&password=";
    static final String jdbcDriver = "com.mysql.jdbc.Driver";
    static final String contentType = "text/html; charset=UTF-8";
    static final String FORM_FIRST_NAME = "first_name"; 
    static final String FORM_LAST_NAME = "last_name";
    static final String FORM_ID = "id";
    static final String FORM_ACTION = "action";
    static final String SHIHANG_FIRST_NAME = "first_name";
    static final String SHIHANG_LAST_NAME = "last_name";
    static final String SHIHANG_ID = "ID";
    static final String SHIHANG_TABLE = "shihang";
    static final String SHIHANG_DATE_CREATED = "date_created";
    static final String SHIHANG_LAST_UPDATED = "last_updated";

    
    public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        
        resp.setContentType(contentType);
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        PrintWriter out = resp.getWriter();
        
        try
        { 
            conn = createConnection();
            String pid = req.getParameter(FORM_ID);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM " + SHIHANG_TABLE;
            String action = req.getParameter("action");
            HttpSession session = req.getSession();
            
            if("logout".equals(action))
            {
                session.removeAttribute("memberId");
            }
            
            Long memberId = (Long)session.getAttribute("memberId");
            
            if(memberId == null)
            {
                out.println("<html><head><title>登录</title></head><body><form action=\"member\" method=\"POST\">");
                out.println("<label>UserName:<input type=\"text\" name=\"user_name\"></label>");
                out.println("<label>Password:<input type=\"password\" name=\"password\"></label>");
                out.println("<input type=\"submit\" name=\"action\" value=\"login\">");
                out.println("</form></body></html>");
                return;
            }
            if (pid == null)
            {
                list(req,resp);
            }
            else
            {
                out.println("<html><head><title>指定会员</title></head><body><h1>指定会员</h1><form action=\"member\" method=\"POST\">");
                sql = sql + " " + " WHERE " + SHIHANG_ID + " = " + pid;
                debug(sql);
                rs = stmt.executeQuery(sql);
                out.println("<table border=\"2\"><tr><th>ID</th><th>First Name</th><th>Last Name</th></tr>");
                
                rs.next();
                Long id = rs.getLong(SHIHANG_ID);
                String firstName = rs.getString(SHIHANG_FIRST_NAME);
                String lastName = rs.getString(SHIHANG_LAST_NAME);          
                out.println("<tr><td>" + id + "</td><td><input type=\"text\" name=\"first_name\" value=\"" + firstName + "\">" 
                                       + "</td><td><input type=\"text\" name=\"last_name\" value=\"" + lastName + "\"></td></tr></table></br>");
                out.println("<input type=\"submit\" name=\"action\" value=\"update\">");
                out.println("<input type=\"submit\" name=\"action\" value=\"delete\">");
                out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\">");
                out.println("</form><a href=\".\">Add Member</a></body></html>");
            }
        } 
        catch(SQLException ex)
        {
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
        finally
        {
            close(conn);
            conn = null; 
          
            close(stmt);
            stmt = null;
            
            close(rs);
            rs = null;
        }
                
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException
    {
        resp.setContentType(contentType);
    
        String firstName = req.getParameter(FORM_FIRST_NAME);
        String lastName = req.getParameter(FORM_LAST_NAME);
        String paraId = req.getParameter(FORM_ID);
        String paraAction = req.getParameter("action");
           
        PrintWriter out = resp.getWriter();

        Connection conn = null;
        Statement stmt = null;
        try
        {
            conn = createConnection();                                   
            stmt = conn.createStatement();
            
            if("login".equals(paraAction))
            {
                String userName = req.getParameter("user_name");
                String password = req.getParameter("password");
                if(userName.equals("bai")&&password.equals("shi"))
                {
                    HttpSession session = req.getSession();
                    session.setAttribute("memberId", 0L);
                    out.println("login success!");
                    out.println("<a href=\"member\">member list</a>");
                    out.println("<a href=\"?action=logout\">logout</a>");
                    
                }
                else
                {
                    out.println("login failed");
                }
                return;
            }
        
            else if("update".equals(paraAction))
            {             
                String sql = "update " + SHIHANG_TABLE + " set " + SHIHANG_FIRST_NAME + "='" + firstName + "' , " + SHIHANG_LAST_NAME + "='" + lastName + "'where " + SHIHANG_ID + "=" + paraId;
                debug("SQL: " + sql);
                stmt.execute(sql);
                out.println("Update " + firstName + " " + lastName + " Success!");
                out.println("<a href=\"member\">Member List</a>");
            }
            else if("delete".equals(paraAction))
            {
                String sql = "delete from " + SHIHANG_TABLE + " where " + SHIHANG_ID  + "=" + paraId;
                debug("SQL: " + sql);
                stmt.execute(sql);
                out.println("delete " + firstName + " " + lastName + " Success!");
                out.println("<a href=\"member\">Member List</a>");
            }
            else
            {
                String sql = "INSERT INTO " + SHIHANG_TABLE + " ( " + SHIHANG_FIRST_NAME + ", " + SHIHANG_LAST_NAME + " ," +SHIHANG_DATE_CREATED + "," + SHIHANG_LAST_UPDATED + ") VALUES('"
                            + firstName + "','" + lastName +"',now(),now())";
                debug("SQL: " + sql);
                stmt.execute(sql);
                out.println("Add " + firstName + " " + lastName + " Success!");
                out.println("<a href=\"member\">Member List</a>");
            }
            
        }
        catch(SQLException ex)
        {
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
        finally
        {
            close(conn);
            conn = null; 
          
            close(stmt);
            stmt = null;
            
        }
    }
    protected Connection createConnection() throws SQLException
    {
        try
        {        
            Class.forName(jdbcDriver).newInstance();    
        }
        catch(Exception ex)
        {
            //ignore
        }
        return DriverManager.getConnection(jdbcUrl);
    }
    protected void close(AutoCloseable obj) 
    {
        try
        {
            obj.close();
        }
        catch(Exception ex)
        {
            //ignore;
        }
    }
    public void debug(String str)
    {
        System.out.println("[DEBUG] " + new Date() + " " + str);
    }
    public void list(HttpServletRequest req, HttpServletResponse resp)throws IOException,ServletException
    {   
        resp.setContentType(contentType);
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        PrintWriter out = resp.getWriter();
       
        try
        {
            conn = createConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM " + SHIHANG_TABLE;
            out.println("<html><head><title>会员管理</title></head><body><h1>会员列表</h1><table border=\"2\">");
            debug(sql);
            rs = stmt.executeQuery(sql);
            out.println("<tr><th>ID</th><th>Name</th></tr>");
            while(rs.next())
            {
                Long id = rs.getLong(SHIHANG_ID);
                String firstName = rs.getString(SHIHANG_FIRST_NAME);
                String lastName = rs.getString(SHIHANG_LAST_NAME);
                out.println("<tr><td><a href=\"?id=" + id + "\" >"+ id + "</a></td><td>" + firstName + lastName + "</td></tr>");         
            }
                out.println("</table><a href=\".\">Add Member</a></body></html>");
        }
        catch(SQLException ex)
        {
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
        finally
        {
            close(conn);
            conn = null; 
          
            close(stmt);
            stmt = null;
            
            close(rs);
            rs = null;
        }
    }
}