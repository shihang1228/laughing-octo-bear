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

public class Manager extends HttpServlet
{
    static final String jdbcUrl = "jdbc:mysql://localhost/test?" + "user=root" + "&password=";
    static final String jdbcDriver = "com.mysql.jdbc.Driver";
    static final String contentType = "text/html; charset=UTF-8";
    static final String FORM_FIRST_NAME = "first_name"; 
    static final String FORM_LAST_NAME = "last_name";
    
    public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        
        resp.setContentType(contentType);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        PrintWriter out = resp.getWriter();
        try
        {        
            Class.forName(jdbcDriver).newInstance();    
        }
        catch(Exception ex)
        {
            //ignore
        }
        
        try
        {
            conn = DriverManager.getConnection(jdbcUrl);                                   
            String pid = req.getParameter("id");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM shihang";
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
                out.println("<input type=\"submit\" name=\"submit_login\" value=\"login\">");
                out.println("</form></body></html>");
                return;
            }
            if (pid == null)
            {
                out.println("<html><head><title>会员管理</title></head><body><h1>会员列表</h1><table border=\"2\">");
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                out.println("<tr><th>ID</th><th>Name</th></tr>");
                while(rs.next())
                {
                    Long id = rs.getLong("ID");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    out.println("<tr><td><a href=\"?id=" + id + "\" >"+ id + "</a></td><td>" + firstName + lastName + "</td></tr>");
           
                }
                out.println("</table><a href=\".\">Add Member</a></body></html>");
            }
            else
            {
                out.println("<html><head><title>指定会员</title></head><body><h1>指定会员</h1><form action=\"member\" method=\"POST\">");
                sql = sql + " WHERE ID =" + pid;
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                out.println("<table border=\"2\"><tr><th>ID</th><th>First Name</th><th>Last Name</th></tr>");
                
                rs.next();
                Long id = rs.getLong("ID");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");          
                out.println("<tr><td>" + id + "</td><td><input type=\"text\" name=\"first_name\" value=\"" + firstName + "\">" 
                                       + "</td><td><input type=\"text\" name=\"last_name\" value=\"" + lastName + "\"></td></tr></table></br>");
                out.println("<input type=\"submit\" name=\"submit_update\" value=\"update\">");
                out.println("<input type=\"submit\" name=\"submit_delete\" value=\"delete\">");
                out.println("<input type=\"hidden\" name=\"hidden_update\" value=\"" + id + "\">");
                out.println("</form><a href=\".\">Add Member</a></body></html>");
            }
        } 
        catch(SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
        finally
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
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException
    {
        String firstName = req.getParameter(FORM_FIRST_NAME);
        String lastName = req.getParameter(FORM_LAST_NAME);
        String paraId = req.getParameter("hidden_update");
        String paraUpdate = req.getParameter("submit_update");
        String paraDelete = req.getParameter("submit_delete");      
        resp.setContentType(contentType);
        
        String paraLogin = req.getParameter("submit_login");
        
        PrintWriter out = resp.getWriter();
        
        
        
        
        try
        {
            Class.forName(jdbcDriver).newInstance();
        }catch(Exception ex)
        {
            //ignore;
        }
        
        Connection conn = null;
        Statement stmt = null;
        try
        {
            conn = DriverManager.getConnection(jdbcUrl);                                   
            stmt = conn.createStatement();
            
            if("login".equals(paraLogin))
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
        
            else if("update".equals(paraUpdate))
            {             
                String sql = "update shihang set first_name='" + firstName + "' ,last_name='" + lastName + "'where ID=" + paraId;
                System.out.println("SQL: " + sql);
                stmt.execute(sql);
                out.println("Update " + firstName + " " + lastName + " Success!");
                out.println("<a href=\"member\">Member List</a>");
            }
            else if("delete".equals(paraDelete))
            {
                String sql = "delete from shihang where id=" + paraId;
                System.out.println("SQL: " + sql);
                stmt.execute(sql);
                out.println("delete " + firstName + " " + lastName + " Success!");
                out.println("<a href=\"member\">Member List</a>");
            }
            else
            {
                String sql = "INSERT INTO shihang(first_name,last_name,date_created,last_updated) VALUES('"
                            + firstName + "','" + lastName +"',now(),now())";
                System.out.println("SQL: " + sql);
                stmt.execute(sql);
                out.println("Add " + firstName + " " + lastName + " Success!");
                out.println("<a href=\"member\">Member List</a>");
            }
            
        }
        catch(SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
        finally
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