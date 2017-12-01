package io.github.yoshikawaa.gfw.app.sample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sample/mtext")
public class MultiLineTextController {

    @GetMapping
    public String view(Model model) {
        model.addAttribute("text1", "test\rsuccess");
        model.addAttribute("text2", "test\nsuccess");
        model.addAttribute("text3", "test\r\nsuccess");
        return "sample/mtext";
    }
}
