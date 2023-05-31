package com.digigarson.digigarsoncallerid.services.web;

public class values {
    public static String url(){
        return "https://digigarson.net";
    }

    public static String url_app(){
        return String.format("%s/caller_id", url());
    }
}
