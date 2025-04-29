package Group2.example.UserCasePoint.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebRequestConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(@org.springframework.lang.NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new WebRequestInterceptor());
    }

    static class WebRequestInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(@org.springframework.lang.NonNull HttpServletRequest request, @org.springframework.lang.NonNull HttpServletResponse response, @org.springframework.lang.NonNull Object handler) throws Exception {
            // Add the request object to the request attributes
            request.setAttribute("request", request);
            request.setAttribute("response", response);
            request.setAttribute("session", request.getSession());
            request.setAttribute("servletContext", request.getServletContext());
            
            return true;
        }
    }
}
