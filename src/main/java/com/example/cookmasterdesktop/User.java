package com.example.cookmasterdesktop;

public class User {

    public String id,name, email, address, postalCode, city, country, phone, created_at;

    public User(String id, String name, String email, String address, String postalCode, String city, String country, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.created_at = created_at;
    }

    public String toString() {
        return "User => {" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", address=" + address +
                ", postalCode=" + postalCode +
                ", city=" + city +
                ", country=" + country +
                ", phone=" + phone +
                ", created_at=" + created_at +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public void setId(String id) { this.id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
