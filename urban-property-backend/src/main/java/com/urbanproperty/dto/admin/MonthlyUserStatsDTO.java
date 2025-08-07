// File: src/main/java/com/urbanproperty/dto/MonthlyUserStatsDTO.java
package com.urbanproperty.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MonthlyUserStatsDTO {
    private String month;

    @JsonProperty("Seller")
    private int sellerCount;

    @JsonProperty("Buyer")
    private int buyerCount;

    public MonthlyUserStatsDTO(String month, int sellerCount, int buyerCount) {
        this.month = month;
        this.sellerCount = sellerCount;
        this.buyerCount = buyerCount;
    }

    // Getters and Setters
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public int getSellerCount() { return sellerCount; }
    public void setSellerCount(int sellerCount) { this.sellerCount = sellerCount; }
    public int getBuyerCount() { return buyerCount; }
    public void setBuyerCount(int buyerCount) { this.buyerCount = buyerCount; }
}