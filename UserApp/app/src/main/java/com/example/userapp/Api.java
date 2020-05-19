package com.example.userapp;


public class Api {

    private static final String ROOT_URL = "http://192.168.0.103/USERAPI/v1/Api.php?apicall=";

    public static final String URL_CREATE_USER = ROOT_URL + "createuser";
    public static final String URL_READ_USERS = ROOT_URL + "getusers";
    public static final String URL_UPDATE_USER = ROOT_URL + "updateuser";
    public static final String URL_DELETE_USER= ROOT_URL + "deleteuser&id=";

}