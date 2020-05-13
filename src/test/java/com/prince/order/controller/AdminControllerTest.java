package com.prince.order.controller;

import com.prince.order.model.response.AgentStatusResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Test
    void assignOrder() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
    }

    @Test
    void getStatus() {
        headers.add("userId", "9170775458");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<AgentStatusResponse> response = restTemplate.exchange(createURLWithPort("/admin/agent/status?agentId" +
                "=9170775458"), HttpMethod.GET, entity, AgentStatusResponse.class);
        assertEquals("9170775458", Objects.requireNonNull(response.getBody()).getAgentId());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}