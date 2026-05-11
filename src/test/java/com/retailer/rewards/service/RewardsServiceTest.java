package com.retailer.rewards.service;

import com.retailer.rewards.dto.RewardResponse;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidTransactionAmountException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardsService rewardsService;

    @Nested
    @DisplayName("calculatePoints")
    class CalculatePointsTests {

        @Test
        @DisplayName("$120 purchase returns 90 points (problem statement example)")
        void calculatePoints_120_returns90() {
            assertEquals(90, rewardsService.calculatePoints(120));
        }

        @Test
        @DisplayName("$200 purchase returns 250 points")
        void calculatePoints_200_returns250() {
            assertEquals(250, rewardsService.calculatePoints(200));
        }

        @Test
        @DisplayName("$100 purchase returns 50 points (boundary: nothing over $100)")
        void calculatePoints_100_returns50() {
            assertEquals(50, rewardsService.calculatePoints(100));
        }

        @Test
        @DisplayName("$75 purchase returns 25 points")
        void calculatePoints_75_returns25() {
            assertEquals(25, rewardsService.calculatePoints(75));
        }

        @Test
        @DisplayName("$50 purchase returns 0 points (boundary: not over $50)")
        void calculatePoints_50_returns0() {
            assertEquals(0, rewardsService.calculatePoints(50));
        }

        @Test
        @DisplayName("$51 purchase returns 1 point")
        void calculatePoints_51_returns1() {
            assertEquals(1, rewardsService.calculatePoints(51));
        }

        @Test
        @DisplayName("$30 purchase returns 0 points (below threshold)")
        void calculatePoints_belowThreshold_returns0() {
            assertEquals(0, rewardsService.calculatePoints(30));
        }

        @Test
        @DisplayName("$0 purchase returns 0 points")
        void calculatePoints_zero_returns0() {
            assertEquals(0, rewardsService.calculatePoints(0));
        }

        @Test
        @DisplayName("$101 purchase returns 52 points (boundary: just over $100)")
        void calculatePoints_101_returns52() {
            assertEquals(52, rewardsService.calculatePoints(101));
        }

        @Test
        @DisplayName("$300 purchase returns 450 points")
        void calculatePoints_300_returns450() {
            assertEquals(450, rewardsService.calculatePoints(300));
        }

        @Test
        @DisplayName("Fractional dollar amounts are truncated before calculation")
        void calculatePoints_withCents_truncatesDown() {
            assertEquals(5, rewardsService.calculatePoints(55.99));
        }

        @Test
        @DisplayName("Negative amount throws InvalidTransactionAmountException")
        void calculatePoints_negativeAmount_throwsException() {
            assertThrows(InvalidTransactionAmountException.class,
                    () -> rewardsService.calculatePoints(-10));
        }
    }

    @Nested
    @DisplayName("getRewardsForCustomer")
    class GetRewardsForCustomerTests {

        @Test
        @DisplayName("Returns correct reward breakdown for existing customer")
        void getRewardsForCustomer_existingCustomer_returnsRewards() {
            Customer customer = new Customer("Alice Johnson");
            customer.setId(1L);

            List<Transaction> transactions = Arrays.asList(
                    new Transaction(customer, 120.00, LocalDate.of(2026, 1, 10)),
                    new Transaction(customer, 75.00, LocalDate.of(2026, 1, 25)),
                    new Transaction(customer, 200.00, LocalDate.of(2026, 2, 15))
            );

            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(transactionRepository.findByCustomerId(1L)).thenReturn(transactions);

            RewardResponse response = rewardsService.getRewardsForCustomer(1L);

            assertEquals(1L, response.getCustomerId());
            assertEquals("Alice Johnson", response.getCustomerName());
            assertEquals(115, response.getMonthlyRewards().get("January 2026"));
            assertEquals(250, response.getMonthlyRewards().get("February 2026"));
            assertEquals(365, response.getTotalRewards());
        }

        @Test
        @DisplayName("Throws CustomerNotFoundException for non-existent customer")
        void getRewardsForCustomer_nonExistentCustomer_throwsException() {
            when(customerRepository.findById(999L)).thenReturn(Optional.empty());

            CustomerNotFoundException exception = assertThrows(
                    CustomerNotFoundException.class,
                    () -> rewardsService.getRewardsForCustomer(999L)
            );

            assertTrue(exception.getMessage().contains("999"));
        }

        @Test
        @DisplayName("Returns zero rewards for customer with no transactions")
        void getRewardsForCustomer_noTransactions_returnsZeroRewards() {
            Customer customer = new Customer("New Customer");
            customer.setId(5L);

            when(customerRepository.findById(5L)).thenReturn(Optional.of(customer));
            when(transactionRepository.findByCustomerId(5L)).thenReturn(Collections.emptyList());

            RewardResponse response = rewardsService.getRewardsForCustomer(5L);

            assertEquals(5L, response.getCustomerId());
            assertEquals("New Customer", response.getCustomerName());
            assertTrue(response.getMonthlyRewards().isEmpty());
            assertEquals(0, response.getTotalRewards());
        }
    }

    @Nested
    @DisplayName("getAllRewards")
    class GetAllRewardsTests {

        @Test
        @DisplayName("Returns rewards for all customers")
        void getAllRewards_returnsAllCustomerRewards() {
            Customer alice = new Customer("Alice");
            alice.setId(1L);
            Customer bob = new Customer("Bob");
            bob.setId(2L);

            when(customerRepository.findAll()).thenReturn(Arrays.asList(alice, bob));
            when(transactionRepository.findByCustomerId(1L)).thenReturn(
                    List.of(new Transaction(alice, 120.00, LocalDate.of(2026, 1, 10)))
            );
            when(transactionRepository.findByCustomerId(2L)).thenReturn(
                    List.of(new Transaction(bob, 150.00, LocalDate.of(2026, 2, 15)))
            );

            List<RewardResponse> responses = rewardsService.getAllRewards();

            assertEquals(2, responses.size());
            assertEquals("Alice", responses.get(0).getCustomerName());
            assertEquals("Bob", responses.get(1).getCustomerName());
        }

        @Test
        @DisplayName("Returns empty list when no customers exist")
        void getAllRewards_noCustomers_returnsEmptyList() {
            when(customerRepository.findAll()).thenReturn(Collections.emptyList());

            List<RewardResponse> responses = rewardsService.getAllRewards();

            assertTrue(responses.isEmpty());
        }
    }
}
