package io.github.yoshikawaa.gfw.app.sample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

@Controller
@RequestMapping("sample/transaction")
public class TransactionTokenController {

    @GetMapping
    @TransactionTokenCheck(namespace = "sample", type = TransactionTokenType.BEGIN)
    public String begin() {
        return "sample/transaction";
    }

    @GetMapping(params = "non-token")
    @TransactionTokenCheck(namespace = "sample", type = TransactionTokenType.NONE)
    public String nonToken() {
        return "sample/transaction";
    }

    @PostMapping
    @TransactionTokenCheck(namespace = "sample", type = TransactionTokenType.IN)
    public String in(Model model) {
        model.addAttribute("result", "token check succeed.");
        return "sample/transaction";
    }
}
