package com.retailer.rewards.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class RewardsServiceTest {

    private RewardsService rewardsService;

    @BeforeEach
    void setUp() {
        rewardsService = new RewardsService(null, null);
    }

    @Test
    void calculatePoints_120_returns90() {
        // $120 = 2x$20 + 1x$50 = 90 points (example from problem statement)
        assertEquals(90, rewardsService.calculatePoints(120));
    }

    @Test
    void calculatePoints_200_returns250() {
        // $200 = 2x$100 + 1x$50 = 250 points
        assertEquals(250, rewardsService.calculatePoints(200));
    }

    @Test
    void calculatePoints_100_returns50() {
        // $100 = 0 (nothing over $100) + 1x$50 (between $50-$100) = 50 points
        assertEquals(50, rewardsService.calculatePoints(100));
    }

    @Test
    void calculatePoints_75_returns25() {
        // $75 = 0 + 1x$25 = 25 points
        assertEquals(25, rewardsService.calculatePoints(75));
    }

    @Test
    void calculatePoints_50_returns0() {
        // $50 = 0 (not over $50, the range is between $50 and $100 exclusive of $50)
        assertEquals(0, rewardsService.calculatePoints(50));
    }

    @Test
    void calculatePoints_51_returns1() {
        // $51 = 1 point (1 dollar over $50)
        assertEquals(1, rewardsService.calculatePoints(51));
    }

    @Test
    void calculatePoints_30_returns0() {
        // Below $50 threshold = 0 points
        assertEquals(0, rewardsService.calculatePoints(30));
    }

    @Test
    void calculatePoints_0_returns0() {
        assertEquals(0, rewardsService.calculatePoints(0));
    }

    @Test
    void calculatePoints_101_returns52() {
        // $101 = 2x$1 + 1x$50 = 52 points
        assertEquals(52, rewardsService.calculatePoints(101));
    }

    @Test
    void calculatePoints_300_returns450() {
        // $300 = 2x$200 + 1x$50 = 450 points
        assertEquals(450, rewardsService.calculatePoints(300));
    }

    @Test
    void calculatePoints_withCents_truncatesDown() {
        // $55.99 → truncated to $55 → 1x$5 = 5 points
        assertEquals(5, rewardsService.calculatePoints(55.99));
    }
}
