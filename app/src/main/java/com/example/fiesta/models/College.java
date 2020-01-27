package com.example.fiesta.models;

import java.util.List;

public class College {

    private String collegeListName;
    private String collegeListImage;
    private String collegeListFestName;
    private String collegeListFestDetails;
    private String collegeContact;
    private String collegeListStartDate;
    private String collegeListEndDate;
    private int collegeListImg;
    private String userId;
    private String collegeId;

    public College() {}

    public String getCollegeListEndDate(){return collegeListEndDate;}

    public void setCollegeListEndDate(String collegeListEndDate){
        this.collegeListEndDate=collegeListEndDate;
    }

    public String getCollegeListFestName() {return collegeListFestName;}

    public void setCollegeListFestName(String collegeListFestName){
        this.collegeListFestName = collegeListFestName;
    }

    public String getCollegeListFestDetails(){
        return collegeListFestDetails;
    }

    public void setCollegeListFestDetails(String collegeListFestDetails){
        this.collegeListFestDetails = collegeListFestDetails;
    }

    public String getCollegeListStartDate(){ return collegeListStartDate;}

    public void setCollegeListStartDate(String collegeListStartDate){
        this.collegeListStartDate = collegeListStartDate;
    }

    public String getCollegeItemName() {
        return collegeListName;
    }

    public void setCollegeItemName(String collegeListName) {
        this.collegeListName = collegeListName;
    }

    public String getCollegeImage() {
        return collegeListImage;
    }

    public void setcollegeImage(String collegeListImage) {
        this.collegeListImage = collegeListImage;
    }

    public int getCollegeImg() {
        return collegeListImg;
    }

    public void setCollegeImg(int collegeListImg) {
        this.collegeListImg = collegeListImg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String _getCollegeId() {
        return collegeId;
    }

    public void _setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeContact() {
        return collegeContact;
    }

    public void setCollegeContact(String collegeContact) {
        this.collegeContact = collegeContact;
    }
}
