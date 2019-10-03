package io.github.yoshikawaa.gfw.app.common.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("common/error")
public class ErrorController {
    @RequestMapping("{view}")
    public String view(@PathVariable("view") String view) {
        return "/common/error/" + view;
    }
}
