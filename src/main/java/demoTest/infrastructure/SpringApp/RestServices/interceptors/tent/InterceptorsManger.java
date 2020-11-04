package demoTest.infrastructure.SpringApp.RestServices.interceptors.tent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorsManger  implements WebMvcConfigurer {
        @Autowired
        TentIntercept tentIntercept;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tentIntercept);
    }

}
