package com.retailer.rewards.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllRewards_returnsAllCustomers() throws Exception {
        mockMvc.perform(get("/api/rewards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].customerName").value("Alice Johnson"))
                .andExpect(jsonPath("$[0].totalRewards").isNumber());
    }

    @Test
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
    void getRewardsByCustomer_alice_verifyMonthlyBreakdown() throws Exception {
        // Alice: Jan=115(90+25), Feb=255(250+5), Mar=60(0+60)
        mockMvc.perform(get("/api/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyRewards['January 2026']").value(115))
                .andExpect(jsonPath("$.monthlyRewards['February 2026']").value(255))
                .andExpect(jsonPath("$.monthlyRewards['March 2026']").value(60))
                .andExpect(jsonPath("$.totalRewards").value(430));
    }

    @Test
    void getAllCustomers_returnsThreeCustomers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void getAllTransactions_returnsAllTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(17)));
    }

    @Test
    void getRewardsByCustomer_invalidId_throwsException() {
        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/api/rewards/999"))
        );
    }
}
