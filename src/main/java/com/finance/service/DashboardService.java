package com.finance.service;

import com.finance.dto.DashboardSummaryResponse;
import com.finance.dto.FinancialRecordResponse;
import com.finance.entity.FinancialRecord;
import com.finance.entity.User;
import com.finance.repository.FinancialRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private FinancialRecordRepository recordRepository;

    @Autowired
    private FinancialRecordService recordService;

    public DashboardSummaryResponse getSummary(User user) {
        List<FinancialRecord> records = recordRepository.findByUserAndNotDeleted(user);

        // Calculate totals
        BigDecimal totalIncome = records.stream()
            .filter(r -> r.getType() == FinancialRecord.RecordType.INCOME)
            .map(FinancialRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = records.stream()
            .filter(r -> r.getType() == FinancialRecord.RecordType.EXPENSE)
            .map(FinancialRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        // Category wise totals
        Map<String, BigDecimal> categoryWiseTotals = records.stream()
            .collect(Collectors.groupingBy(
                FinancialRecord::getCategory,
                Collectors.mapping(
                    FinancialRecord::getAmount,
                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                )
            ));

        // Recent activity (last 10 records)
        List<FinancialRecordResponse> recentActivity = records.stream()
            .sorted((a, b) -> b.getRecordDate().compareTo(a.getRecordDate()))
            .limit(10)
            .map(FinancialRecordResponse::new)
            .collect(Collectors.toList());

        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpenses(totalExpenses);
        response.setNetBalance(netBalance);
        response.setCategoryWiseTotals(categoryWiseTotals);
        response.setRecentActivity(recentActivity);
        response.setRecordCount(records.size());
        response.setPeriod("ALL_TIME");

        return response;
    }

    public DashboardSummaryResponse getMonthlySummary(User user, int year, int month) {
        List<FinancialRecord> records = recordRepository.findByUserAndNotDeleted(user);

        // Filter by year and month
        YearMonth yearMonth = YearMonth.of(year, month);
        List<FinancialRecord> monthlyRecords = records.stream()
            .filter(r -> YearMonth.from(r.getRecordDate()).equals(yearMonth))
            .collect(Collectors.toList());

        // Calculate totals
        BigDecimal totalIncome = monthlyRecords.stream()
            .filter(r -> r.getType() == FinancialRecord.RecordType.INCOME)
            .map(FinancialRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = monthlyRecords.stream()
            .filter(r -> r.getType() == FinancialRecord.RecordType.EXPENSE)
            .map(FinancialRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        // Category wise totals
        Map<String, BigDecimal> categoryWiseTotals = monthlyRecords.stream()
            .collect(Collectors.groupingBy(
                FinancialRecord::getCategory,
                Collectors.mapping(
                    FinancialRecord::getAmount,
                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                )
            ));

        // Recent activity
        List<FinancialRecordResponse> recentActivity = monthlyRecords.stream()
            .sorted((a, b) -> b.getRecordDate().compareTo(a.getRecordDate()))
            .map(FinancialRecordResponse::new)
            .collect(Collectors.toList());

        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpenses(totalExpenses);
        response.setNetBalance(netBalance);
        response.setCategoryWiseTotals(categoryWiseTotals);
        response.setRecentActivity(recentActivity);
        response.setRecordCount(monthlyRecords.size());
        response.setPeriod(String.format("%d-%02d", year, month));

        return response;
    }
}
