package com.tage.config;

import com.tage.filter.MyFilter;
import java.util.EnumSet;
import java.util.Enumeration;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MyWebApplicationInitializer implements WebApplicationInitializer {

  public void onStartup(ServletContext servletContext) throws ServletException {
    AnnotationConfigWebApplicationContext app = new AnnotationConfigWebApplicationContext();
    app.setServletContext(servletContext);
    app.register(AppConfig.class);
    app.refresh();

    DispatcherServlet ds = new DispatcherServlet(app);
    Dynamic app1 = servletContext.addServlet("app", ds);
    app1.setLoadOnStartup(1);
    app1.addMapping("/app/*");
    FilterRegistration.Dynamic aa = servletContext.addFilter("aa", new MyFilter());
//    aa.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true,"/*");
  }
}
