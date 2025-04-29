package Group2.example.UserCasePoint.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class ThymeleafControllerAdvice {

    @ModelAttribute("request")
    public HttpServletRequest requestAttribute(HttpServletRequest request) {
        return request;
    }

    @ModelAttribute("response")
    public HttpServletResponse responseAttribute(HttpServletResponse response) {
        return response;
    }

    @ModelAttribute("session")
    public HttpSession sessionAttribute(HttpServletRequest request) {
        return request.getSession();
    }

    @ModelAttribute("servletContext")
    public Object servletContextAttribute(HttpServletRequest request) {
        return request.getServletContext();
    }
}
