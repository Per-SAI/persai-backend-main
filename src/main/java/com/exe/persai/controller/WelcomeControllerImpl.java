package com.exe.persai.controller;

import com.exe.persai.controller.api.WelcomeController;
import com.exe.persai.model.request.ScheduleDowngradeSubReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WelcomeControllerImpl implements WelcomeController {

    @Override
    public String welcomeUser(String username) {
        return "Welcome user " + username + " to PerSAI web service";
    }

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirect(@RequestParam("name") String name) {
        log.info(name);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://www.persai.space")).build();
    }

//    @GetMapping("/call-cron-job")
//    public Boolean callCronJobServer(@RequestParam("time") Instant time) throws URISyntaxException {
//        WebClient client = WebClient.create();
//        ScheduleDowngradeSubReq scheduleDowngradeSubReq = new ScheduleDowngradeSubReq(
//                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkNST05KT0JfU0VSVkVSX1NFQ1JFVF9LRVkiLCJpYXQiOjE1MTYyMzkwMjJ9.f-4qT-1jSTBFzSbAr_HnLZIDR3HY0ys3qSOn_P3Mlio",
//                UUID.randomUUID(),
//                time
//        );
//        ResponseEntity<Boolean> response = client.post()
//                .uri(new URI("http://localhost:8081/api/v1/subscription/schedule-downgrade"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue(scheduleDowngradeSubReq))
//                .retrieve()
//                .toEntity(Boolean.class)
//                .block();
//        if (response.getBody().equals(Boolean.FALSE)) {
//            log.error("Failed to call cron job scheduler server");
//            return false;
//        }
//        log.info("Successfully call cron job scheduler server");
//        return true;
//    }
}
