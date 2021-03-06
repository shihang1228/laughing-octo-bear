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

import static com.bodejidi.Constants.FORM_FIRST_NAME; 
import static com.bodejidi.Constants.FORM_LAST_NAME;
import static com.bodejidi.Constants.FORM_ID;
import static com.bodejidi.Constants.FORM_ACTION;

public class Manager extends HttpServlet
{
    public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {                
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
        PrintWriter out = resp.getWriter();
        String paraAction = req.getParameter("action");           
        
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
            MemberService memberService = new MemberService();
            req.setAttribute("memberList",memberService.findAllMember());
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
            MemberService memberService = new MemberService();
            Member member = memberService.getMemberById(pid);
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

    public void  save(HttpServletRequest req,HttpServletResponse resp)
                throws IOException ,ServletException ,SQLException
    {       
        Member member = new Member();
        member.setFirstName(req.getParameter(FORM_FIRST_NAME));
        member.setLastName(req.getParameter(FORM_LAST_NAME));
        
       
        DataBaseService ds = null;
        try
        {   
            MemberService memberService = new MemberService();
            memberService.save(member);
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
    
    public void delete(HttpServletRequest req,HttpServletResponse resp) 
                throws IOException ,ServletException ,SQLException         
    {
        String paraId = req.getParameter(FORM_ID);      
        MemberService memberService = new MemberService();
        memberService.deleteById(Long.valueOf(paraId));
        req.setAttribute("flash_massage","delete " + paraId + " Success!");
        forward("result", req,resp); 
    } 
    
    public void update(HttpServletRequest req,HttpServletResponse resp)
                 throws IOException ,ServletException ,SQLException
    {       
        Member member = new Member();
        member.setFirstName(req.getParameter(FORM_FIRST_NAME));
        member.setLastName(req.getParameter(FORM_LAST_NAME));
        member.setId(Long.valueOf(req.getParameter(FORM_ID)));
        
        MemberService memberService = new MemberService();
        memberService.update(member);
        req.setAttribute("flash_massage","Update " + member + " Success!");
        forward("result" , req , resp);            
    }
   
    
    public void create(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {       
        forward("create", req, resp);
    }
    public void forward(String page, HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException
    {
        String jsp = "/WEB-INF/member/" + page + ".jsp";
        getServletContext().getRequestDispatcher(jsp).forward(req,resp);
    }
}