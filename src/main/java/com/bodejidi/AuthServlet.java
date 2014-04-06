package com.bodejidi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthServlet extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        showLoginPage(req,resp);
    }
    public void showLoginPage(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        PrintWriter out = resp.getWriter();
        out.println("<html><head><title>登录</title></head><body><form action=\"member\" method=\"POST\">");
        out.println("<label>UserName:<input type=\"text\" name=\"user_name\"></label>");
        out.println("<label>Password:<input type=\"password\" name=\"password\"></label>");
        out.println("<input type=\"submit\" name=\"action\" value=\"login\">");
        out.println("</form></body></html>");
    }
}