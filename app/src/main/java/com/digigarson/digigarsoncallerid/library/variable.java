package com.digigarson.digigarsoncallerid.library;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

public class variable {

    // Convert ArrayObject To String
    public static String get_post_data_string(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator itr = params.keys();

        while(itr.hasNext()){

            String key = (String) itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
