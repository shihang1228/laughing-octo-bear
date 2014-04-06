package com.bodejidi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthServlet extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        if(isNotLogin(req))
        {
            showLoginPage(req,resp);
        }
        else
        {
            resp.sendRedirect(req.getContextPath());
        }
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        login(req,resp);
    }
    
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String userName = req.getParameter("user_name");
        String password = req.getParameter("password");
      
        if(userName.equals("bai")&&password.equals("shi"))
        {
            HttpSession session = req.getSession();
            session.setAttribute("memberId", 0L);
            showLoginSuccess(req,resp);                 
        }
        else
        {
            showLoginFailed(req,resp);
        }
    }
    
    public void showLoginFailed(HttpServletRequest req, HttpServletResponse resp)throws IOException,ServletException
    {
        PrintWriter out = resp.getWriter();
        
        out.println("login failed!!!");
        out.println("<htm l><head><meta http-equiv=\"refresh\" content=\"5;");        
        out.println("URL="+req.getRequestURI()+"\"></head><body>");
        out.println("</br>please wait for 5 seconds, if not redirect ,please click ");
        out.println("<a href=\"" + req.getRequestURI() + "\">here</a></body></html>");
        
    }
    
    public void showLoginSuccess(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException
    {
        Integer timeout = 5;
        PrintWriter out = resp.getWriter();              
        out.println("<html><head>");
        out.println("<meta http-equiv=\"refresh\" content=\"" + timeout + ";url=" 
                    + req.getContextPath()+"/member?action=list\"></head><body>");
        out.println("login success!</br>");
        out.println("please wait for " + timeout + "seconds,if not redirct,please click");
        out.println("<a href=\"?action=list\">here</a></br>");
        out.println("<a href=\"?action=logout\">logout</a>");   
        out.println("</body></html>");
    }
    
    public void showLoginPage(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        PrintWriter out = resp.getWriter();
        out.println("<html><head><title>登录</title></head><body><form action=\"" + req.getRequestURI() + "\" method=\"POST\">");
        out.println("<label>UserName:<input type=\"text\" name=\"user_name\"></label>");
        out.println("<label>Password:<input type=\"password\" name=\"password\"></label>");
        out.println("<input type=\"submit\" name=\"action\" value=\"login\">");
        out.println("</form></body></html>");
    }
     public boolean isNotLogin(HttpServletRequest req) throws ServletException
    { 
        HttpSession session = req.getSession();
        Long memberId = (Long)session.getAttribute("memberId");
        return null == memberId;
    }
}