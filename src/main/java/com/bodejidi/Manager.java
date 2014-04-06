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
import java.util.List;
import java.util.ArrayList;

public class Manager extends HttpServlet
{
    static final String jdbcUrl = "jdbc:mysql://localhost/test?" + "user=root" + "&password=";
    static final String jdbcDriver = "com.mysql.jdbc.Driver";
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
        PrintWriter out = resp.getWriter();
        String action = req.getParameter("action");   

        if(isNotLogin(req))
        {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        if(action==null || "".equals(action))
        {
            action = "list";
        }        
        
        switch(action.toLowerCase())
        {
            case "logout":
                logout(req,resp);break;
            case "create":
                create(req, resp); break;
            case "show":
                show(req,resp); break;
            case "list":
                list(req,resp); break;
            default:
                resp.sendError(HttpServletResponse.SC_FORBIDDEN,"connot find!");
        }
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException
    {
        String paraAction = req.getParameter("action");           
        PrintWriter out = resp.getWriter();
       
        switch(paraAction)
        {
            case "update":
                update(req, resp); break;
            case "delete":
                delete(req, resp); break;
            case "save":
                save(req, resp); break;
            default:
                resp.sendError(HttpServletResponse.SC_FORBIDDEN,"connot find!");
        }
    }

    public void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        session.removeAttribute("memberId");
        resp.sendRedirect("member?action=login");
    }

    public void debug(String str)
    {
        System.out.println("[DEBUG] " + new Date() + " " + str);
    }
    public void list(HttpServletRequest req, HttpServletResponse resp)throws IOException,ServletException
    {  
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        PrintWriter out = resp.getWriter();       
        try
        {
     
            out.println("<html><head><title>会员管理</title></head><body>" + showLoginInfo() + "<h1>会员列表</h1><table border=\"2\">");
            out.println("<tr><th>ID</th><th>Name</th></tr>");
           
            for(Member member: findAllMember())
            {
                out.println("<tr><td><a href=\"?action=show&id=" + member.getId() + "\" >"+ member.getId() 
                          + "</a></td><td>" + member.getFirstName() + member.getLastName() 
                          + "</td></tr>");  
            } 
            out.println("</table><a href=\"?action=create\">Add Member</a></body></html>");
        }
        catch(SQLException ex)
        {
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
        
    }
    public void show(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException
    {
        
        PrintWriter out = resp.getWriter();
        try
        {
            String pid = req.getParameter(FORM_ID);
            Member member = getMemberById(pid);
            
            out.println("<html><head><title>指定会员</title></head><body>" + showLoginInfo() + "<h1>指定会员</h1><form action=\"member\" method=\"POST\">");
            out.println("<table border=\"2\"><tr><th>ID</th><th>First Name</th><th>Last Name</th></tr>");
            out.println("<tr><td>" + member.getId() + "</td><td><input type=\"text\" name=\"first_name\" value=\"" + member.getFirstName() + "\">" 
                       + "</td><td><input type=\"text\" name=\"last_name\" value=\"" + member.getLastName() + "\"></td></tr></table></br>");
            out.println("<input type=\"submit\" name=\"action\" value=\"update\">");
            out.println("<input type=\"submit\" name=\"action\" value=\"delete\">");
            out.println("<input type=\"hidden\" name=\"id\" value=\"" + member.getId() + "\">");
            out.println("</form><a href=\"?action=create\">Add Member</a></body></html>");
        }
        catch(SQLException ex)
        {
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
    }
    public Member getMemberById(String pid) throws SQLException
    {
        Member member = new Member();
        
        String sql = "SELECT * FROM " + SHIHANG_TABLE;
        sql = sql + " " + " WHERE " + SHIHANG_ID + " = " + pid;
        debug(sql);
        DataBaseService ds = null;
        try
        {
            ds = DataBaseService.newInstance();
            ResultSet rs = ds.executeQuery(sql);   
            rs.next();
            member.setId(rs.getLong(SHIHANG_ID));
            member.setFirstName(rs.getString(SHIHANG_FIRST_NAME));
            member.setLastName(rs.getString(SHIHANG_LAST_NAME));             
        }
        finally
        {
            ds.close();
        }
        return member;

        
    } 
    public List<Member> findAllMember() throws SQLException
    {
        List<Member> memberList = new ArrayList<Member>();
        DataBaseService ds = null;
        
        try
        {
            ds = DataBaseService.newInstance();
            String sql = "SELECT * FROM " + SHIHANG_TABLE;
            debug(sql);
            ResultSet rs = ds.executeQuery(sql);
            while(rs.next())
            {
                Member member = new Member();
                member.setId(rs.getLong(SHIHANG_ID));
                member.setFirstName(rs.getString(SHIHANG_FIRST_NAME));
                member.setLastName(rs.getString(SHIHANG_LAST_NAME));
                memberList.add(member);
                
            }
        }
        finally
        {
            ds.close();
        }
        return memberList;
    }
    public String showLoginInfo()
    {
        return "welcome,admin!<a href=\"?action=logout\">logout</a>";
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
    
    public void  save(HttpServletRequest req,HttpServletResponse resp) throws IOException ,ServletException 
    {
        PrintWriter out = resp.getWriter();            
        String firstName = req.getParameter(FORM_FIRST_NAME);
        String lastName = req.getParameter(FORM_LAST_NAME);
        DataBaseService ds = null;
        try
        {  

            ds = DataBaseService.newInstance();
            String sql = "INSERT INTO " + SHIHANG_TABLE + " ( " + SHIHANG_FIRST_NAME + ", " + SHIHANG_LAST_NAME + " ," +SHIHANG_DATE_CREATED + "," + SHIHANG_LAST_UPDATED + ") VALUES('"
                            + firstName + "','" + lastName +"',now(),now())";
            debug("SQL: " + sql);
            ds.execute(sql);
            out.println("Add " + firstName + " " + lastName + " Success!");
            out.println("<a href=\"?action=list\">Member List</a>");
        }
        catch (SQLException ex)
        {
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
        finally
        {
            ds.close();
        }
    }
    public void delete(HttpServletRequest req,HttpServletResponse resp) throws IOException ,ServletException 
    {
        String firstName = req.getParameter(FORM_FIRST_NAME);
        String lastName = req.getParameter(FORM_LAST_NAME);
        String paraId = req.getParameter(FORM_ID);
        PrintWriter out = resp.getWriter();
        DataBaseService ds = null;
        
        try
        {
            ds = DataBaseService.newInstance();
            String sql = "delete from " + SHIHANG_TABLE + " where " + SHIHANG_ID  + "=" + paraId;
            debug("SQL: " + sql);
            ds.execute(sql);
            out.println("delete " + firstName + " " + lastName + " Success!");
            out.println("<a href=\"?action=list\">Member List</a>");
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
            ds.close();
        }
            
    } 
    public void update(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        String firstName = req.getParameter(FORM_FIRST_NAME);
        String lastName = req.getParameter(FORM_LAST_NAME);
        String paraId = req.getParameter(FORM_ID);
        PrintWriter out = resp.getWriter();
        DataBaseService ds = null;
        
        try
        {
            ds = DataBaseService.newInstance();
            String sql = "update " + SHIHANG_TABLE + " set " + SHIHANG_FIRST_NAME + "='" + firstName + "' , " + SHIHANG_LAST_NAME + "='" + lastName + "'where " + SHIHANG_ID + "=" + paraId;
            debug("SQL: " + sql);
            ds.execute(sql);
            out.println("Update " + firstName + " " + lastName + " Success!");
            out.println("<a href=\"?action=list\">Member List</a>");
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
            ds.close();
        }
    }
    public void create(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>新增会员</title>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\"/>");	
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>新增会员</h1>");
        out.println("<form action=\"member\" method=\"POST\">");
        out.println("<label>firstName:<input type=\"text\" name=\"first_name\"/></label></br>");
        out.println("<label>LastName :<input type=\"text\" name=\"last_name\"/></label></br>");
        out.println("<input type=\"hidden\" name=\"action\" value=\"save\"/></label></br>");
        out.println("<input type=\"submit\" name=\"action\" value=\"提交\"/>");
        out.println("</form>");
        out.println("<a href=\"?action=list\">Member List</a>");
        out.println("</body>");
        out.println("</html>");

    }
    public boolean isNotLogin(HttpServletRequest req) throws ServletException
    { 
        HttpSession session = req.getSession();
        Long memberId = (Long)session.getAttribute("memberId");
        return null == memberId;
    }
}