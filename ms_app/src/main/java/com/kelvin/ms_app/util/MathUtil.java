package com.kelvin.ms_app.util;

import com.kelvin.ms_app.entity.Expense;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MathUtil {

    public Map<String, Map<String, Double>> addition(List<Expense> expenses) {
        // Group expenses by month
        Map<String, List<Expense>> expensesByMonth = expenses.stream()
                .collect(Collectors.groupingBy(expense -> {
                    LocalDate date = expense.getDate();
                    return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                }));

        // Create a map to store the totals for each month
        Map<String, Map<String, Double>> monthlyTotals = new HashMap<>();

        // Iterate through each month's expenses and calculate totals
        for (Map.Entry<String, List<Expense>> entry : expensesByMonth.entrySet()) {
            String month = entry.getKey();
            List<Expense> monthExpenses = entry.getValue();

            double totalFB = 0;
            double totalHL = 0;
            double totalTP = 0;
            double totalET = 0;

            for (Expense expense : monthExpenses) {
                switch (expense.getCategory()) {
                    case "Food & Beverages":
                        totalFB += expense.getAmount();
                        break;
                    case "Entertainment":
                        totalET += expense.getAmount();
                        break;
                    case "Household":
                        totalHL += expense.getAmount();
                        break;
                    case "Transportation":
                        totalTP += expense.getAmount();
                        break;
                }
            }

            // Create a map to store the totals for the current month
            Map<String, Double> totalsMap = new HashMap<>();
            totalsMap.put("fb", totalFB);
            totalsMap.put("et", totalET);
            totalsMap.put("hl", totalHL);
            totalsMap.put("tp", totalTP);

            // Add the current month's totals to the main map
            monthlyTotals.put(month, totalsMap);
        }

        return monthlyTotals;
    }
}
