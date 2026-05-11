package com.retailer.rewards.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/rewards - returns all customer rewards")
    void getAllRewards_returnsAllCustomers() throws Exception {
        mockMvc.perform(get("/api/rewards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].customerId").isNumber())
                .andExpect(jsonPath("$[0].customerName").isString())
                .andExpect(jsonPath("$[0].monthlyRewards").isMap())
                .andExpect(jsonPath("$[0].totalRewards").isNumber());
    }

    @Test
    @DisplayName("GET /api/rewards/1 - returns rewards for specific customer")
    void getRewardsByCustomer_returnsSpecificCustomer() throws Exception {
        mockMvc.perform(get("/api/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Alice Johnson"))
                .andExpect(jsonPath("$.monthlyRewards").isMap())
                .andExpect(jsonPath("$.totalRewards").isNumber());
    }

    @Test
    @DisplayName("GET /api/rewards/1 - verifies Alice's monthly breakdown and total")
    void getRewardsByCustomer_alice_verifyMonthlyBreakdown() throws Exception {
        mockMvc.perform(get("/api/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyRewards['January 2026']").value(115))
                .andExpect(jsonPath("$.monthlyRewards['February 2026']").value(255))
                .andExpect(jsonPath("$.monthlyRewards['March 2026']").value(60))
                .andExpect(jsonPath("$.totalRewards").value(430));
    }

    @Test
    @DisplayName("GET /api/rewards/2 - verifies Bob's monthly breakdown and total")
    void getRewardsByCustomer_bob_verifyMonthlyBreakdown() throws Exception {
        mockMvc.perform(get("/api/rewards/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Bob Smith"))
                .andExpect(jsonPath("$.monthlyRewards['January 2026']").value(35))
                .andExpect(jsonPath("$.monthlyRewards['February 2026']").value(200))
                .andExpect(jsonPath("$.monthlyRewards['March 2026']").value(450))
                .andExpect(jsonPath("$.totalRewards").value(685));
    }

    @Test
    @DisplayName("GET /api/rewards/3 - verifies Charlie's monthly breakdown and total")
    void getRewardsByCustomer_charlie_verifyMonthlyBreakdown() throws Exception {
        mockMvc.perform(get("/api/rewards/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Charlie Davis"))
                .andExpect(jsonPath("$.monthlyRewards['January 2026']").value(110))
                .andExpect(jsonPath("$.monthlyRewards['February 2026']").value(10))
                .andExpect(jsonPath("$.monthlyRewards['March 2026']").value(200))
                .andExpect(jsonPath("$.totalRewards").value(320));
    }

    @Test
    @DisplayName("GET /api/rewards/999 - returns 404 for non-existent customer")
    void getRewardsByCustomer_invalidId_returns404() throws Exception {
        mockMvc.perform(get("/api/rewards/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Customer not found with id: 999"));
    }

    @Test
    @DisplayName("GET /api/customers - returns all customers")
    void getAllCustomers_returnsThreeCustomers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Alice Johnson"))
                .andExpect(jsonPath("$[1].name").value("Bob Smith"))
                .andExpect(jsonPath("$[2].name").value("Charlie Davis"));
    }

    @Test
    @DisplayName("GET /api/transactions - returns all 17 transactions")
    void getAllTransactions_returnsAllTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(17)));
    }

    @Test
    @DisplayName("GET /api/rewards - response contains monthly rewards as month-year keys")
    void getAllRewards_monthlyRewardsHaveCorrectKeyFormat() throws Exception {
        mockMvc.perform(get("/api/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyRewards['January 2026']").exists())
                .andExpect(jsonPath("$.monthlyRewards['February 2026']").exists())
                .andExpect(jsonPath("$.monthlyRewards['March 2026']").exists());
    }
}
