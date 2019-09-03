package io.github.yoshikawaa.gfw.app.sample;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
        long offset = pageable.getOffset();
        List<Long> content = LongStream.rangeClosed(offset, offset + size + 1).boxed().collect(Collectors.toList());

        Page<Long> page = new PageImpl<Long>(content, pageable, total);
        model.addAttribute("page", page);
        model.addAttribute("pagination", page);
        model.addAttribute("query", Collections.singletonMap("item", "sample"));
        return "sample/pagination";
    }

    @GetMapping("{page}")
    public String paginationByPath(Model model, Pageable pageable, @PathVariable("page") int page) {
        return pagination(model, PageRequest.of(page, pageable.getPageSize()));
    }
}
