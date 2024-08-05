package com.abc.bank.accountmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RateLimiterServiceTest {

    @Autowired
    RateLimiterService rateLimiterService;

    @BeforeEach
    public void setup() throws InterruptedException {
        //Wait 1 second to allow the bucket to refill before each test.
        Thread.sleep(1000);
    }
    @Test
    public void testTryConsumeWithinLimit() {
        assertTrue(rateLimiterService.tryConsume());
        assertTrue(rateLimiterService.tryConsume());
    }

    @Test
    public void testTryConsumeExceedingLimit() {
        assertTrue(rateLimiterService.tryConsume());
        assertTrue(rateLimiterService.tryConsume());

        // Third call will return false because the limit is exceeded.
        assertFalse(rateLimiterService.tryConsume());
    }

    @Test
    public void testTryConsumeAfterRefill() throws InterruptedException {
        assertTrue(rateLimiterService.tryConsume());
        assertTrue(rateLimiterService.tryConsume());

        // Third call will return false because the limit is exceeded.
        assertFalse(rateLimiterService.tryConsume());

         // Wait 1 second to allow the bucket to refill.
        Thread.sleep(1000);

        assertTrue(rateLimiterService.tryConsume());
        assertTrue(rateLimiterService.tryConsume());
    }
}