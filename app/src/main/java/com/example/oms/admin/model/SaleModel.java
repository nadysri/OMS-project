package com.example.oms.admin.model;

public class SaleModel {


    private String rank;
    private String username;
    private String totalSale;
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public SaleModel(String rank, String username, String totalSale, String image) {
        this.rank = rank;
        this.username = username;
        this.totalSale = totalSale;
        this.image = image;
    }
}
