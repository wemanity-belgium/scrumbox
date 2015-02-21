package com.wemanity.scrumbox.android.db.entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MEMBER.
 */
public class Member implements Entity {

    private Long id;
    private String nickname;
    private byte[] image;

    public Member() {
    }

    public Member(Long id) {
        this.id = id;
    }

    public Member(Long id, String nickname, byte[] image) {
        this.id = id;
        this.nickname = nickname;
        this.image = image;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
