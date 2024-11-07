package com.finance.transaction.dto;

public class IncomeDepositDTO {
    private String periodLabel;
    private double totalDepositsAmount;
    private double totalWithdrawalsAmount;

    // Constructor
    public IncomeDepositDTO(String periodLabel, double totalDepositsAmount, double totalWithdrawalsAmount) {
        this.periodLabel = periodLabel;
        this.totalDepositsAmount = totalDepositsAmount;
        this.totalWithdrawalsAmount = totalWithdrawalsAmount;
    }

    // Getters and Setters
    public String getPeriodLabel() {
        return periodLabel;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public double getTotalDepositsAmount() {
        return totalDepositsAmount;
    }

    public void setTotalDepositsAmount(double totalDepositsAmount) {
        this.totalDepositsAmount = totalDepositsAmount;
    }

    public double getTotalWithdrawalsAmount() {
        return totalWithdrawalsAmount;
    }

    public void setTotalWithdrawalsAmount(double totalWithdrawalsAmount) {
        this.totalWithdrawalsAmount = totalWithdrawalsAmount;
    }
}
