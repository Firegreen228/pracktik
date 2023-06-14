package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloWorldController {
    @GetMapping("/")
    public Mono<String> helloWorld(@RequestParam("message") String message) {
        return Mono.just("Hello, World! " + message);
    }
}
