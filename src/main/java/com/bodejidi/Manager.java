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
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().println("<html><head><title>登录</title></head><body><form action=\"member\" method=\"POST\">");
                resp.getWriter().println("<label>UserName:<input type=\"text\" name=\"user_name\"></label>");
                resp.getWriter().println("<label>Password:<input type=\"password\" name=\"password\"></label>");
                resp.getWriter().println("<input type=\"submit\" name=\"submit_login\" value=\"login\">");
                resp.getWriter().println("</form></body></html>");
                return;
            }
            if (pid == null)
            {
                resp.getWriter().println("<html><head><title>会员管理</title></head><body><h1>会员列表</h1><table border=\"2\">");
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                resp.getWriter().println("<tr><th>ID</th><th>Name</th></tr>");
                while(rs.next())
                {
                    Long id = rs.getLong("ID");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    resp.getWriter().println("<tr><td><a href=\"?id=" + id + "\" >"+ id + "</a></td><td>" + firstName + lastName + "</td></tr>");
           
                }
                resp.getWriter().println("</table><a href=\".\">Add Member</a></body></html>");
            }
            else
            {
                resp.getWriter().println("<html><head><title>指定会员</title></head><body><h1>指定会员</h1><form action=\"member\" method=\"POST\">");
                sql = sql + " WHERE ID =" + pid;
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                resp.getWriter().println("<table border=\"2\"><tr><th>ID</th><th>First Name</th><th>Last Name</th></tr>");
                
                rs.next();
                Long id = rs.getLong("ID");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                resp.getWriter().println("<tr><td>" + id + "</td><td><input type=\"text\" name=\"first_name\" value=\"" + firstName + "\">" 
                                       + "</td><td><input type=\"text\" name=\"last_name\" value=\"" + lastName + "\"></td></tr></table></br>");
                resp.getWriter().println("<input type=\"submit\" name=\"submit_update\" value=\"update\">");
                resp.getWriter().println("<input type=\"submit\" name=\"submit_delete\" value=\"delete\">");
                resp.getWriter().println("<input type=\"hidden\" name=\"hidden_update\" value=\"" + id + "\">");
                resp.getWriter().println("</form><a href=\".\">Add Member</a></body></html>");
            }
        } 
        catch(SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            resp.getWriter().println("Error!");
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
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String paraId = req.getParameter("hidden_update");
        String paraUpdate = req.getParameter("submit_update");
        String paraDelete = req.getParameter("submit_delete");
        resp.setContentType("text/html;charset=utf-8");
        
        String paraLogin = req.getParameter("submit_login");
        
        
        
        
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
            
            if("login".equals(paraLogin))
            {
                String userName = req.getParameter("user_name");
                String password = req.getParameter("password");
                if(userName.equals("bai")&&password.equals("shi"))
                {
                    HttpSession session = req.getSession();
                    session.setAttribute("memberId", 0L);
                    resp.getWriter().println("login success!");
                    resp.getWriter().println("<a href=\"member\">member list</a>");
                    resp.getWriter().println("<a href=\"?action=logout\">logout</a>");
                    
                }
                else
                {
                    resp.getWriter().println("login failed");
                }
                return;
            }
        
            else if("update".equals(paraUpdate))
            {
                String sql = "update shihang set first_name='" + firstName + "' ,last_name='" + lastName +"' where ID=" + paraId;
                System.out.println("SQL: " + sql);
                stmt.execute(sql);
                resp.getWriter().println("Update " + firstName + " " + lastName + " Success!");
                resp.getWriter().println("<a href=\"member\">Member List</a>");
            }
            else if("delete".equals(paraDelete))
            {
                String sql = "delete from shihang where id=" + paraId;
                System.out.println("SQL: " + sql);
                stmt.execute(sql);
                resp.getWriter().println("delete " + firstName + " " + lastName + " Success!");
                resp.getWriter().println("<a href=\"member\">Member List</a>");
            }
            else
            {
                String sql = "INSERT INTO shihang(first_name,last_name,date_created,last_updated) VALUES('"
                            + firstName + "','" + lastName +"',now(),now())";
                System.out.println("SQL: " + sql);
                stmt.execute(sql);
                resp.getWriter().println("Add " + firstName + " " + lastName + " Success!");
                resp.getWriter().println("<a href=\"member\">Member List</a>");
            }
            
        }
        catch(SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            resp.getWriter().println("Error!");
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