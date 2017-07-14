package jp.yoshikawaa.gfw.app.sample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

@Controller
@RequestMapping("sample/messages-panel")
public class MessagesPanelController {

    @GetMapping
    public String message(Model model) {
        model.addAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME, ResultMessages.success()
                .add(ResultMessage.fromCode("e.xx.fw.8001")).add(ResultMessage.fromText("</li><li>message</li>")));
        model.addAttribute("customMessage", ResultMessages.success().add(ResultMessage.fromText("custom message")));
        return "sample/messagesPanel";
    }

    @GetMapping(params = "non-message")
    public String nonMessage() {
        return "sample/messagesPanel";
    }

    @GetMapping(params = "business-error")
    public String businessError() {
        throw new BusinessException(
                ResultMessages.error().add(ResultMessage.fromText("error1")).add(ResultMessage.fromText("error2")));
    }
}
