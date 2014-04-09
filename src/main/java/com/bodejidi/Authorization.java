package com.bodejidi;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;

public class Authorization implements Filter
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
        
    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
    throws java.io.IOException, ServletException
    {
        chain.doFilter(req,resp);
    }
    public void destroy()
    {
        
    }
}
