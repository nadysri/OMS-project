package com.example.oms.admin.model;

public class SaleModel {


    private String rank;
    private String username;
    private String totalSale;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(String totalSale) {
        this.totalSale = totalSale;
    }

    public SaleModel(String rank, String username, String totalSale) {
        this.rank = rank;
        this.username = username;
        this.totalSale = totalSale;
    }
}
