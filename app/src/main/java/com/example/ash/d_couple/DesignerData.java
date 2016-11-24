package com.example.ash.d_couple;

/**
 * 리싸이클러 뷰에 담을 아이템 클래스
 */
public class DesignerData {
    String imageURL;
    String ID;
    String interest;

    public  DesignerData(){
    }
    public DesignerData(String imageURL, String ID, String interest) {
        this.imageURL = imageURL;
        this.ID = ID;
        this.interest = interest;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getID() {
        return ID;
    }

    public String getInterest() {
        return interest;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}