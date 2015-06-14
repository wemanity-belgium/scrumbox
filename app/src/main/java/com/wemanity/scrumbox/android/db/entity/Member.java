package com.wemanity.scrumbox.android.db.entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.util.Arrays;

/**
 * Entity mapped to table MEMBER.
 */
public class Member implements Entity {

    private Long id;
    private String nickname;

    public Member() {
    }

    public Member(Long id) {
        this.id = id;
    }

    public Member(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public Member(String nickname) {
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Object getProperty(String propertyName) {
        switch (propertyName){
            case "nickname":
                return nickname;
            default:
                return null;
        }
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
}
