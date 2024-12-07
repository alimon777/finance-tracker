package com.finance.ai.dto;

import java.util.HashMap;
import java.util.Map;

public class ExpenditureSummaryDTO {

    private ExpenditureDetail weekly;
    private ExpenditureDetail monthly;
    private ExpenditureDetail yearly;

    public ExpenditureDetail getWeekly() {
        return weekly;
    }

    public void setWeekly(ExpenditureDetail weekly) {
        this.weekly = weekly;
    }

    public ExpenditureDetail getMonthly() {
        return monthly;
    }

    public void setMonthly(ExpenditureDetail monthly){
        this.monthly = monthly;
    }

    public ExpenditureDetail getYearly() {
        return yearly;
    }

    public void setYearly(ExpenditureDetail yearly) {
        this.yearly = yearly;
    }

    // Nested static class with separate maps for DEPOSIT and WITHDRAW, and a withdraw total
    public static class ExpenditureDetail {

        private Map<String, Double> deposit;
        private Map<String, Double> withdraw;
        private double withdrawTotal;  // Total of all withdrawals for this period

        public ExpenditureDetail() {
            this.deposit= new HashMap<>();
            this.withdraw = new HashMap<>();
            this.withdrawTotal = 0.0;
        }

        public ExpenditureDetail(Map<String, Double> deposit, Map<String, Double> withdraw, double withdrawTotal) {
            this.deposit = deposit;
            this.withdraw = withdraw;
            this.withdrawTotal = withdrawTotal;
        }

        public Map<String, Double> getDeposit() {
            return deposit;
        }

        public void setDeposit(Map<String, Double> deposit) {
            this.deposit = deposit;
        }

        public Map<String, Double> getWithdraw() {
            return withdraw;
        }

        public void setWithdraw(Map<String, Double> withdraw) {
            this.withdraw = withdraw;
        }

        public double getWithdrawTotal() {
            return withdrawTotal;
        }

        public void setWithdrawTotal(double withdrawTotal) {
            this.withdrawTotal = withdrawTotal;
        }
    }
}
