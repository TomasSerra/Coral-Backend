package com.coral.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //TABLE_PER_CLASS o JOINED
@DiscriminatorColumn(name = "userType")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false)
    private boolean firstLogin;

    private String name;

    private byte[] profileImage;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private String description;
    private String location;
    private LocalDate initial_date;
    private String userTypeMin;

    public User(){}

    @ManyToMany(cascade=CascadeType.ALL)
    private List<Area> areas;

    @ManyToMany(cascade=CascadeType.ALL)
    private List<Post> taggedInPost;

    //Setters
    public void setProfileImage(byte[] base64) {
        this.profileImage = base64;
    }
    public void setUserId(long user_id) {
        this.userId = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setInitialDate(LocalDate initial_date) {
        this.initial_date = initial_date;
    }

    public void setAreas(List<Area> area) {
        this.areas = area;
    }
    //Getters
    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getInitialDate() {
        return initial_date;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public boolean getFirstLogin() {
        return firstLogin;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public String getProfileImageString(){
        if(profileImage != null){
            return new String(profileImage);
        }
        return null;
    }

    public void setUserTypeMin(String userType) {
        this.userTypeMin = userType;
    }

    public String getUserTypeMin() {
        return userTypeMin;
    }
  
    private String decodeImage(byte[] byteArray) {
        return new String(byteArray);
    }

    public List<Post> getTaggedInPost() {
        return taggedInPost;
    }
    public void setTaggedInPost(List<Post> taggedIn) {
        this.taggedInPost = taggedIn;
    }
}
