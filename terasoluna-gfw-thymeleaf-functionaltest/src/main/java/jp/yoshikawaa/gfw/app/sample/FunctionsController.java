package jp.yoshikawaa.gfw.app.sample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sample/functions")
public class FunctionsController {

    @GetMapping
    public String view(Model model) {
        model.addAttribute("message", "<span>hoge</span>");
        return "sample/functions";
    }
}
