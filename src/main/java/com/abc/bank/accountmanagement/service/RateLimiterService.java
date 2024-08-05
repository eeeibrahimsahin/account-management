package com.abc.bank.accountmanagement.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {
    private final Bucket bucket;

    public RateLimiterService() {
        Bandwidth limit = Bandwidth.classic(2, Refill.greedy(2, Duration.ofSeconds(1)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }
}

