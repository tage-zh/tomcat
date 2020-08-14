package com.tage.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
@WebFilter(urlPatterns = {"/*"})
public class MyFilter implements Filter {

  public void init(FilterConfig filterConfig) throws ServletException {
    System.out.println("MyFilter====init");
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    System.out.println("MyFilter====doFilter");
    filterChain.doFilter(servletRequest,servletResponse);
  }

  public void destroy() {
    System.out.println("MyFilter====destroy");
  }
}
