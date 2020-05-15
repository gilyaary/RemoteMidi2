package web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SequencerController {

    @GetMapping("/sequencer")
    public String index() {
        return "Greetings from Sequencer!";
    }


}
