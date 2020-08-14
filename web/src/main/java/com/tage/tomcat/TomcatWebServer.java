package com.tage.tomcat;

import static java.nio.charset.Charset.forName;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

public class TomcatWebServer {

  public static void main(String[] args) throws LifecycleException {
    Tomcat tomcat = new Tomcat();
    tomcat.setHostname("localhost");
    tomcat.setPort(8090);
    final Context context = tomcat.addContext("/myweb",null);
    HttpServlet servlet = new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        System.out.println("doGet");
        resp.getWriter().write("hello, tiger");
      }
    };
    tomcat.addServlet(context,"dispatch",servlet);
    context.addServletMappingDecoded("/hello","dispatch");
    Filter filter = new Filter() {
      public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
      }

      public void destroy() {
        System.out.println("destroy");
      }

      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
        System.out.println("do filter");
        chain.doFilter(request,response);
      }
    };
    FilterDef filterDef = new FilterDef();
    filterDef.setFilter(filter);
    filterDef.setFilterName("myfilter");

    FilterMap filterMap = new FilterMap();
    filterMap.addURLPatternDecoded("/*");
    filterMap.addServletName("*");
    filterMap.setFilterName("myfilter");
    filterMap.setCharset(forName("UTF-8"));
    context.addFilterDef(filterDef);
    context.addFilterMap(filterMap);
    try {
      tomcat.init();
      tomcat.start();
      tomcat.getServer().await();
    }catch (LifecycleException e){
      e.printStackTrace();
      tomcat.destroy();
    }
  }

}
