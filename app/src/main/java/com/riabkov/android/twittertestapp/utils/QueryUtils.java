package com.riabkov.android.twittertestapp.utils;

import android.support.v7.widget.SearchView;

public class QueryUtils {
    private static String mPreviousQuery = "";

    public static String getQuery(SearchView searchView){

        if(searchView == null){
            return Constants.BASE_QUERY;
        }
        String view_query = searchView.getQuery().toString();
        if(view_query.isEmpty()){
            return  Constants.BASE_QUERY;
        }if(view_query.length() < 2){
            if(!mPreviousQuery.isEmpty()){
                return mPreviousQuery;
            }
            return Constants.BASE_QUERY;
        } else{
            mPreviousQuery = view_query;
            return view_query;
        }
    }
}
