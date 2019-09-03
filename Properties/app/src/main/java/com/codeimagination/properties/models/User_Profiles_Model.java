package com.codeimagination.properties.models;

public class User_Profiles_Model {

    private String user_unique_id, username, fullname, phone_number, email_address, office_address, state, occupation, sex, profileImageUrl, coverImageUrl;

    public User_Profiles_Model() {

    }

    public User_Profiles_Model(String user_unique_id, String username, String fullname, String phone_number, String email_address, String office_address, String state, String occupation, String sex, String profileImageUrl, String coverImageUrl) {
        this.user_unique_id = user_unique_id;
        this.username = username;
        this.fullname = fullname;
        this.phone_number = phone_number;
        this.email_address = email_address;
        this.office_address = office_address;
        this.state = state;
        this.occupation = occupation;
        this.sex = sex;
        this.profileImageUrl = profileImageUrl;
        this.coverImageUrl = coverImageUrl;
    }

    public String getUser_unique_id() {
        return user_unique_id;
    }

    public void setUser_unique_id(String user_unique_id) {
        this.user_unique_id = user_unique_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getOffice_address() {
        return office_address;
    }

    public void setOffice_address(String office_address) {
        this.office_address = office_address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
}
