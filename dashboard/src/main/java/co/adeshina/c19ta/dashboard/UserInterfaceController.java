package co.adeshina.c19ta.dashboard;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserInterfaceController {

    @Value("${data.api.url}")
    private String dataApiBaseUrl;

    @GetMapping("/dashboard")
    public String home(Model model) {
        model.addAttribute("dataApiUrl", dataApiBaseUrl.concat("/data-stream"));
        return "dashboard";
    }
}
