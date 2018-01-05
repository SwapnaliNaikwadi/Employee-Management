package com.example.a.userinfo;

/**
 * Created by A on 20/11/2017.
 */

public class Student {
    private int studId;
    private String studName;
    private String studPhone;
    private String studAddress;
    private String studHobbies;
    private String img;


    public Student(String studName, String studPhone, String studAddress, String studHobbies, String img) {
        this.studName = studName;
        this.studPhone = studPhone;
        this.studAddress = studAddress;
        this.studHobbies = studHobbies;
        this.img=img;

    }

    public Student() {
        super();
    }

    public int getStudId() {
        return studId;
    }

    public void setStudId(int studId) {
        this.studId = studId;
    }

    public String getStudName() {
        return studName;
    }

    public void setStudName(String studName) {
        this.studName = studName;
    }

    public String getStudPhone() {
        return studPhone;
    }

    public void setStudPhone(String studPhone) {
        this.studPhone = studPhone;
    }

    public String getStudAddress() {
        return studAddress;
    }

    public void setStudAddress(String studAddress) {
        this.studAddress = studAddress;
    }

    public String getStudHobbies() {
        return studHobbies;
    }

    public void setStudHobbies(String studHobbies) {
        this.studHobbies = studHobbies;
    }

    public String getImage(){
        return this.img;
    }

    //setting profile pic
    public void setImage(String b){
        this.img=b;
    }


}
