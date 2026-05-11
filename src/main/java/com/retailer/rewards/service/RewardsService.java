package com.retailer.rewards.service;

import com.retailer.rewards.dto.RewardResponse;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidTransactionAmountException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardsService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public RewardsService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Calculate reward points for a given purchase amount.
     * 2 points for every dollar spent over $100,
     * 1 point for every dollar spent between $50 and $100.
     */
    public int calculatePoints(double amount) {
        if (amount < 0) {
            throw new InvalidTransactionAmountException(amount);
        }

        int points = 0;
        int dollars = (int) amount;

        if (dollars > 100) {
            points += 2 * (dollars - 100);
        }
        if (dollars > 50) {
            points += Math.min(dollars, 100) - 50;
        }
        return points;
    }

    public RewardResponse getRewardsForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        return buildRewardResponse(customer, transactions);
    }

    public List<RewardResponse> getAllRewards() {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream().map(customer -> {
            List<Transaction> transactions = transactionRepository.findByCustomerId(customer.getId());
            return buildRewardResponse(customer, transactions);
        }).collect(Collectors.toList());
    }

    private RewardResponse buildRewardResponse(Customer customer, List<Transaction> transactions) {
        Map<String, Integer> monthlyRewards = new LinkedHashMap<>();
        int totalRewards = 0;

        // Group transactions by YearMonth
        Map<YearMonth, List<Transaction>> grouped = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getTransactionDate()),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        // Sort by YearMonth and calculate points
        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    YearMonth ym = entry.getKey();
                    String monthLabel = ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                            + " " + ym.getYear();

                    int monthPoints = entry.getValue().stream()
                            .mapToInt(t -> calculatePoints(t.getAmount()))
                            .sum();

                    monthlyRewards.put(monthLabel, monthPoints);
                });

        totalRewards = monthlyRewards.values().stream().mapToInt(Integer::intValue).sum();

        return new RewardResponse(customer.getId(), customer.getName(), monthlyRewards, totalRewards);
    }
}
