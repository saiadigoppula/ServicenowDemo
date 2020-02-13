package com.example.servicenowdemo;

public class ExampleItem {

    private String mCreator;
    private int mLikes;
    private String msys_id;

    public ExampleItem(String creator, int likes ,String sys_id) {

        mCreator = creator;
        mLikes = likes;
        msys_id = sys_id;
    }

    public String getCreator() {
        return mCreator;
    }

    public int getLikeCount() {
        return mLikes;
    }

    public String getSysid(){
        return msys_id;
    }
}