package cn.rzwl.multidata.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author : championjing
 * @ClassName: MyFilter
 * @Description: 增加javax.servlet的过滤器，要相应的在配置类上增加@ServletComponentScan注解
 * @Date: 7/11/2019 9:44 AM
 */
@WebFilter(urlPatterns = "/*")
@Slf4j
public class MyFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化自定义过滤器");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String site = request.getParameter("source");
        if( DynamicDataSourceRouter.SourceName.TEST1.dbName.equals( site ) ){
            DynamicDataSourceRouter.dbStore.set( DynamicDataSourceRouter.SourceName.TEST1.dbName );
        } else {
            DynamicDataSourceRouter.dbStore.set( DynamicDataSourceRouter.SourceName.TEST2.dbName );
        }
        log.info("进入业务流程");
        chain.doFilter( request,response );
        log.info("完成处理");
        System.out.println("后续操作");
    }

    @Override
    public void destroy() {

    }
}
