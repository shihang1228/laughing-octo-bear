package com.bodejidi;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Authorization implements Filter
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
        
    }
    
    public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain)
    throws java.io.IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest)request;
        if(!req.getRequestURI().startsWith(req.getContextPath()+"/auth/") && isNotLogin(req))
        {
            ((HttpServletResponse)resp).sendRedirect(req.getContextPath()+"/auth/login");
            return;
        }
        chain.doFilter(req,resp);
    }
    public void destroy()
    {
        
    }
    public boolean isNotLogin(HttpServletRequest req)
    {
        HttpSession session = req.getSession();
        Long memberId = (Long)session.getAttribute("memberId");
        return memberId == null;
    }
}
