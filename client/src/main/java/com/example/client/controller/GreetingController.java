package com.example.client.controller;

import com.example.client.dto.Info;
import com.example.client.dto.Word;
import com.example.client.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class GreetingController {
    @Autowired
    private ConnectionService connectionService;

    private List<Word> words = Collections.singletonList(new Word(""));

    @GetMapping
    public String greeting(Map<String, Object> model) {
        return "main";
    }

    @GetMapping("returnWords")
    public String returnWords(Map<String, Object> model,
                              @RequestParam(name = "number") Integer number,
                              @RequestParam(name = "word") String word) {
        words = connectionService.returnWords(new Info(number, word));
        model.put("words", words);
        return "main";
    }
}
