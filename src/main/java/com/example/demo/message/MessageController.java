package com.example.demo.message;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
//    @Autowired
//    RestTemplate restTemplate;
//
//    @PostMapping("/hitOpenaiAPi")
//    public String getOpenaiResponse(@RequestBody String prompt) {
//        ChatCompletionRequest chatCompletionRequest
//                = new ChatCompletionRequest("gpt-3.5-turbo", prompt);
//
//        ChatCompletionResponse response =
//                restTemplate.postForObject(
//                        "https://api.openai.com/v1/chat/completions",
//                        chatCompletionRequest,
//                        ChatCompletionResponse.class
//                );
//
//        assert response != null;
//        return response.getChoices().get(0).getMessage().getContent();
//    }
}
