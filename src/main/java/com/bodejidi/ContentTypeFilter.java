package com.bodejidi;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.Filter;

public class ContentTypeFilter implements Filter
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
        
    }
    public void doFilter(ServletRequest req,ServletResponse resp,FilterChain chain)
                        throws java.io.IOException, ServletException
    {
        //req.setContentType("text/html");
        resp.setContentType("text/html");
        chain.doFilter(req,resp);
    } 
    public void destroy()
    {
        
    }
    
}