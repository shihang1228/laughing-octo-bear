package com.bodejidi;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Manager extends HttpServlet
{
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

        if(action==null || "".equals(action))
        {
            action = "list";
        }        
        
        switch(action.toLowerCase())
        {
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
        
        try
        {
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
        catch(SQLException ex)
        {
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
        
    }
    
    public void debug(String str)
    {
        System.out.println("[DEBUG] " + new Date() + " " + str);
    }
    public void list(HttpServletRequest req, HttpServletResponse resp)throws IOException,ServletException
    {  
  
        PrintWriter out = resp.getWriter();       
        try
        {          
            req.setAttribute("memberList",findAllMember());
            forward("list", req, resp);
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
            req.setAttribute("member",member);
            forward("show", req, resp);          
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
    public String showLoginInfo(HttpServletRequest req)
    {
        return "welcome,admin!<a href=\""+req.getContextPath()+"/auth/logout\">logout</a>";
    }
     
    public void  save(HttpServletRequest req,HttpServletResponse resp)
                throws IOException ,ServletException ,SQLException
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
        finally
        {
            ds.close();
        }
    }
    public void delete(HttpServletRequest req,HttpServletResponse resp) 
                throws IOException ,ServletException ,SQLException         
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
        finally
        {
            ds.close();
        }
            
    } 
    public void update(HttpServletRequest req,HttpServletResponse resp)
                 throws IOException ,ServletException ,SQLException
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
        finally
        {
            ds.close();
        }
    }
    public void create(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        PrintWriter out = resp.getWriter();
        
        forward("create", req, resp);

    }
    public void forward(String page, HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException
    {
        String jsp = "/WEB-INF/member/" + page + ".jsp";
        getServletContext().getRequestDispatcher(jsp).forward(req,resp);
    }
}