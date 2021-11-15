package com.kitesoft.tpkakaosearchapi;

public class UserInfoResponse {
    String resultcode;
    String message;
    User response;
}

class User{
    String id;
    String nickname;
    String profile_image;
    String email;
}
