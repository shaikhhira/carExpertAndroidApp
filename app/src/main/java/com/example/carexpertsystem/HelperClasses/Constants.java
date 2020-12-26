package com.example.carexpertsystem.HelperClasses;

public class Constants {

    private static final String ROOT_URL = "http://192.168.1.102/carproject/phpFolder/androdConn/";//chnage everytime by check IPconfig on cmd

    public static final String URL_REGISTER = ROOT_URL + "registerUser.php";
    public static final String URL_LOGIN = ROOT_URL + "loginUser.php";

    public static final String URL_BRAND = ROOT_URL + "brandName.php";
    public static final String URL_MODEL = ROOT_URL + "modelName.php?BRANDNAME=";

    public static final String URL_ISSUE = ROOT_URL + "fetchIssue.php?MODELNAME=";
    public static final String URL_ISSUE_ISSUENAME ="&ISSUENAME=";

    public static final String URL_SOLUTION=ROOT_URL+"fetchSolution.php?ISSUENAME=";
}