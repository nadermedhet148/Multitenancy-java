package demoTest.infrastructure.SpringApp.RestServices.interceptors.tent;


import demoTest.infrastructure.SpringApp.tenatManger.MultiTenantManager;
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
       multiTenantManager.setCurrentTenant(tentName);
        return true;
    }

}