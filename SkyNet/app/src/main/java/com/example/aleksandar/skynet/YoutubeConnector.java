package com.example.aleksandar.skynet;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *Class {@link YoutubeConnector} connects to Youtube Data API
 * Developer must enter his own key in string KEY
 *
 * @author SkyNet team
 */
public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;

    public static final String KEY = "AIzaSyCkFnPCbKAH_EnRnTk2bLr3RQJ5E2CGVmQ";

    /**
     * Method creates HttpRequest and defines request fields in JSON result
      * @param context
     */
    public YoutubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try{
            query = youtube.search().list("id,snippet");
            query.setMaxResults((long)25);
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url)");
        }catch(IOException e){
            Log.d("YC", "Could not initialize: " + e);
        }
    }

    /**
     * Method gets the list of {@link VideoItem} by keyword
     * @param keywords input string for search
     * @return fetched list {@link VideoItem}
     */
    public List<VideoItem> search(String keywords){
        query.setQ(keywords);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            List<VideoItem> items = new ArrayList<VideoItem>();
            for(SearchResult result:results){
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                items.add(item);
            }
            return items;
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }
}
