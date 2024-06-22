package be.ucll.ip.minor.reeks1210.general;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());


            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                model.addAttribute("message", "error.401");
                model.addAttribute("status", 401);
                model.addAttribute("error", "error.Unauthorized");
                return "error";
            }
            else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("message", "error.403");
                model.addAttribute("status", 403);
                model.addAttribute("error", "error.Forbidden");
                return "error";
            }
            else if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("message", "error.404");
                model.addAttribute("status", 404);
                model.addAttribute("error", "error.NotFound");
                return "error";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("message", "error.500");
                model.addAttribute("status", 500);
                model.addAttribute("error", "error.InternalServerError");
                return "error";
            }
        }
        return "error";
    }

}
