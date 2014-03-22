package com.bodejidi;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Manager extends HttpServlet
{
    public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
        resp.getWriter().println("hello manager");
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException
    {
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_names");
        
        resp.getWriter().println("Add " + firstName + " " + lastName + " Success!");
    }
}