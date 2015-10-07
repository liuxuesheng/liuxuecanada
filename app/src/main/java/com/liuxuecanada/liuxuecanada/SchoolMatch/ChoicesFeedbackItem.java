package com.liuxuecanada.liuxuecanada.SchoolMatch;


import java.io.Serializable;

public class ChoicesFeedbackItem implements Serializable {

    private String title;
    private String desc;
    private int photoId;

    public ChoicesFeedbackItem(String name, String age, int photoId){
        this.title = name;
        this.desc = age;
        this.photoId = photoId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getDesc() {
        return desc;
    }

    public int getPhotoId() {
        return photoId;
    }

    public String getTitle() {
        return title;
    }
}
