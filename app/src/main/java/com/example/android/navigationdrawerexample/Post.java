package com.example.android.navigationdrawerexample;

import android.widget.ImageView;

/**
 * Created by NaveenT on 6/19/14.
 */
public class Post {
    private String time;
    private String username;
    private String profilePicUrl;
    private String name;
    private String location;
    private String postText;
    private String postImageUrl;
    private boolean hasPostImage;
    private int commentsCount;
    private int favoritesCount;
    private String type;
    private ImageView img ;

    public Post(){
        time= "0sec ago";
        username="";
        name="";
        profilePicUrl="";
        location="";
        postImageUrl="";
        hasPostImage=false;
        commentsCount=0;
        favoritesCount=0;
        postText="";
        type="";
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public boolean isHasPostImage() {
        return hasPostImage;
    }

    public void setHasPostImage(boolean hasPostImage) {
        this.hasPostImage = hasPostImage;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
