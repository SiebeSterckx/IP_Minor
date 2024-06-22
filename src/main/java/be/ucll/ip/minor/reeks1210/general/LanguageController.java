package be.ucll.ip.minor.reeks1210.general;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Controller
public class LanguageController implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("ln");
        registry.addInterceptor(interceptor);
    }

    @GetMapping("/language")
    public String changeLanguage(HttpServletRequest request, @RequestParam("ln") String ln) {
        request.getSession().setAttribute("locale", new Locale(ln));

        // Get the URL of the previous page from the Referer header
        String previousPageUrl = request.getHeader("Referer");

        // If the Referer header is not present or empty, redirect to the root URL
        if (previousPageUrl == null || previousPageUrl.isEmpty()) {
            return "redirect:/";
        }

        // Redirect to the previous page
        return "redirect:" + previousPageUrl;
    }


}

