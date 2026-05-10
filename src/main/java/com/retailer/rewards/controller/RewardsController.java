package com.retailer.rewards.controller;

import com.retailer.rewards.dto.RewardResponse;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import com.retailer.rewards.service.RewardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RewardsController {

    private final RewardsService rewardsService;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public RewardsController(RewardsService rewardsService,
                             CustomerRepository customerRepository,
                             TransactionRepository transactionRepository) {
        this.rewardsService = rewardsService;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/rewards")
    public ResponseEntity<List<RewardResponse>> getAllRewards() {
        return ResponseEntity.ok(rewardsService.getAllRewards());
    }

    @GetMapping("/rewards/{customerId}")
    public ResponseEntity<RewardResponse> getRewardsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(rewardsService.getRewardsForCustomer(customerId));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }
}
