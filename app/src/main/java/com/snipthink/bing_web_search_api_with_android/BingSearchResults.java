package com.snipthink.bing_web_search_api_with_android;

/**
 * Created by SnipThinK on 12/4/2017.
 */

public class BingSearchResults {

    public ResultsContent d;

    public static class ResultsContent {
        public Result[] results;
        public String __next;
    }

    public static class Result {
        public String ID;
        public String Title;
        public String Description;
        public String DisplayUrl;
        public String Url;
        public Metadata __metadata;

    }

    public static class Metadata {
        public String uri;
        public String type;
    }

    public Result[] getResults(){
        if (d == null)
            return null;
        return d.results;
    }

    public String getNextUrl(){
        if (d == null)
            return null;
        return d.__next;
    }

    public boolean isEmpty(){
        return (d == null || d.results == null || d.results.length == 0);
    }

    public int size(){
        if (d == null || d.results == null)
            return 0;
        return d.results.length;
    }
}