package com.example.cookmasterdesktop;

public class Subscription {

    public String id, user_id, subscription_type, start_date, end_date;
    public Integer price_per_month, annual_price;


    public Subscription(String id, String user_id, String subscription_type, Integer price_per_month, Integer annual_price, String start_date, String end_date) {
        this.id = id;
        this.user_id = user_id;
        this.subscription_type = subscription_type;
        this.price_per_month = price_per_month;
        this.annual_price = annual_price;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUser_id() { return user_id; }

    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getSubscription_type() { return subscription_type; }

    public void setSubscription_type(String subscription_type) { this.subscription_type = subscription_type; }

    public Integer getPrice_per_month() { return price_per_month; }

    public void setPrice_per_month(Integer price_per_month) { this.price_per_month = price_per_month; }

    public Integer getAnnual_price() { return annual_price; }

    public void setAnnual_price(Integer annual_price) { this.annual_price = annual_price; }

    public String getStart_date() { return start_date; }

    public String setStart_date(String start_date) { this.start_date = start_date;
        return start_date;
    }

    public String getEnd_date() { return end_date; }

    public void setEnd_date(String end_date) { this.end_date = end_date; }

}



