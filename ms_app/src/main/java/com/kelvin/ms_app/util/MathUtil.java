package com.kelvin.ms_app.util;

import com.kelvin.ms_app.entity.Expense;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MathUtil {

    public Map<String, Double> addition(List<Expense> expenses) {
        double totalFB = 0;
        double totalHL = 0;
        double totalTP = 0;
        double totalET = 0;

        // Iterate through the list and sum the amounts
        for (Expense expense : expenses) {
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

        // Create a map to store the totals
        Map<String, Double> totalsMap = new HashMap<>();
        totalsMap.put("fb", totalFB);
        totalsMap.put("et", totalET);
        totalsMap.put("hl", totalHL);
        totalsMap.put("tp", totalTP);

        return totalsMap;
    }
}
