package com.example.oninonSpring.infrastructure.SpringApp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.example.oninonSpring.infrastructure.SpringApp.DataSource.conf.TenantContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class ShardsInterceptor extends HandlerInterceptorAdapter {

	private static final String TENANT_HEADER_NAME = "X-TenantID";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String tenantId = request.getHeader(TENANT_HEADER_NAME);
//		TenantContext.setCurrentTenant(tenantId);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model) throws Exception {
//		TenantContext.clear();
	}
	
}
