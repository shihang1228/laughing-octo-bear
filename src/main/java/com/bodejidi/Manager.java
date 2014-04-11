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
    static final String FORM_FIRST_NAME = Constants.FORM_FIRST_NAME; 
    static final String FORM_LAST_NAME = Constants.FORM_LAST_NAME;
    static final String FORM_ID = Constants.FORM_ID;
    static final String FORM_ACTION = Constants.FORM_ACTION;
    static final String SHIHANG_FIRST_NAME = Constants.SHIHANG_FIRST_NAME;
    static final String SHIHANG_LAST_NAME = Constants.SHIHANG_LAST_NAME;
    static final String SHIHANG_ID = Constants.SHIHANG_ID;
    static final String SHIHANG_TABLE = Constants.SHIHANG_TABLE;
    static final String SHIHANG_DATE_CREATED = Constants.SHIHANG_DATE_CREATED;
    static final String SHIHANG_LAST_UPDATED = Constants.SHIHANG_LAST_UPDATED;

    
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
        String className = Manager.class.getName();
        System.out.println("[DEBUG] " + new Date() + " " + className + " " + str);
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
            Long pid = Long.valueOf(req.getParameter(FORM_ID));
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
    /**
     *@Deprecated
     */
    public Member getMemberById(String pid) throws SQLException
    {
        return getMemberById(Long.valueOf(pid));
    }
    public Member getMemberById(Long pid) throws SQLException
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
        Member member = new Member();
        member.setFirstName(req.getParameter(FORM_FIRST_NAME));
        member.setLastName(req.getParameter(FORM_LAST_NAME));
        
       
        DataBaseService ds = null;
        try
        {   
            save(member);
            req.setAttribute("flash_massage","Add " + member + " Success!");           
        }
        catch(Exception e)
        {
            req.setAttribute("flash_errorMessage", "Error: first name or last name cannot be empty!");
        }        
        /*finally
        {
            ds.close();
        }*/
        forward("result" , req, resp);
    }
    
    public Member save(Member member) throws Exception
    {
        String firstName = member.getFirstName();
        String lastName = member.getLastName();
        if(firstName == null || firstName.length() == 0 || lastName == null || lastName.length() == 0)
        {
            throw new Exception("Member validator error!!!");
        }
        DataBaseService ds = DataBaseService.newInstance();
        String sql = "INSERT INTO " + SHIHANG_TABLE + " ( " + SHIHANG_FIRST_NAME + ", " + SHIHANG_LAST_NAME + " ," 
                    + SHIHANG_DATE_CREATED + "," + SHIHANG_LAST_UPDATED + ") VALUES('"
                    + firstName + "','" + lastName +"',now(),now())";
        debug("SQL: " + sql);
        ds.execute(sql);
        return member;
    }
    
    public void delete(HttpServletRequest req,HttpServletResponse resp) 
                throws IOException ,ServletException ,SQLException         
    {
        String paraId = req.getParameter(FORM_ID);      
        deleteById(Long.valueOf(paraId));
        req.setAttribute("flash_massage","delete " + paraId + " Success!");
        forward("result", req,resp); 
    } 
    
    public void deleteById(Long paraId) throws SQLException
    {
        DataBaseService ds = DataBaseService.newInstance();
        String sql = "delete from " + SHIHANG_TABLE + " where " + SHIHANG_ID  + "=" + paraId;
        debug("SQL: " + sql);
        ds.execute(sql);
        ds.close();
    }
    public void update(HttpServletRequest req,HttpServletResponse resp)
                 throws IOException ,ServletException ,SQLException
    {       
        Member member = new Member();
        member.setFirstName(req.getParameter(FORM_FIRST_NAME));
        member.setLastName(req.getParameter(FORM_LAST_NAME));
        member.setId(Long.valueOf(req.getParameter(FORM_ID)));
        
        update(member);
        req.setAttribute("flash_massage","Update " + member + " Success!");
        forward("result" , req , resp);            
    }
    public Member update(Member member) throws SQLException
    {
        String firstName = member.getFirstName();
        String lastName = member.getLastName();
        Long paraId = member.getId();
        DataBaseService ds = DataBaseService.newInstance();
        String sql = "update " + SHIHANG_TABLE + " set " + SHIHANG_FIRST_NAME + "='" 
                    + firstName + "' , " + SHIHANG_LAST_NAME + "='" + lastName + "'where " + SHIHANG_ID + "=" + paraId;
        debug("SQL: " + sql);
        ds.execute(sql);       
        ds.close();
        return member;
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