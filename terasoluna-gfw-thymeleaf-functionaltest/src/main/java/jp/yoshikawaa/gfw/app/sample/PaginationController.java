package jp.yoshikawaa.gfw.app.sample;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sample/pagination")
public class PaginationController {

    private static final int total = 1000;

    @GetMapping
    public String pagination(Model model, Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = pageable.getOffset();
        List<Integer> content = IntStream.rangeClosed(offset, offset + size + 1).boxed().collect(Collectors.toList());

        Page<Integer> page = new PageImpl<Integer>(content, pageable, total);
        model.addAttribute("page", page);
        model.addAttribute("pagination", page);
        return "sample/pagination";
    }

    @GetMapping("{page}")
    public String paginationByPath(Model model, Pageable pageable, @PathVariable("page") int page) {
        return pagination(model, new PageRequest(page, pageable.getPageSize()));
    }
}
