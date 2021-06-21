package com.wsd.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wsd.request.CreationRequest;
import com.wsd.service.Creator;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@CrossOrigin
@Controller
public class DiagramController {
    private static final Logger LOG = LoggerFactory.getLogger(DiagramController.class);
    private final Bucket bucket;

    public DiagramController() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(2, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping(value = "/diagram", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getDiagram(@Validated @RequestBody CreationRequest creationRequest) {
        LOG.info("Request received: " + creationRequest.toString());
        try {
            if (bucket.tryConsume(1)) {
                Creator creator = new Creator();
                byte[] response = creator.processRequest(creationRequest);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
            }
        } catch (Exception ex) {
            LOG.error("Error while processing: " + ex.getMessage());
            LOG.error(ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}
