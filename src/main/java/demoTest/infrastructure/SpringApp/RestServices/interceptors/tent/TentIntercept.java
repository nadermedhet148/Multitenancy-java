package demoTest.infrastructure.SpringApp.RestServices.interceptors.tent;


import demoTest.DomianServices.exceptions.ExceptionBase;
import demoTest.infrastructure.SpringApp.tenatManger.MultiTenantManager;
import demoTest.infrastructure.SpringApp.tenatManger.TentNotExisted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TentIntercept implements HandlerInterceptor {

    @Autowired
    MultiTenantManager multiTenantManager;
    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       String tentName = request.getHeader("tentName");
       try {
           multiTenantManager.setTenant(tentName);
       }catch (Exception e ){
           if(e instanceof TentNotExisted){
               response.getWriter().write("tent is not existed");
               response.setStatus(503);
               return false;
           }
       }
        return true;
    }

}