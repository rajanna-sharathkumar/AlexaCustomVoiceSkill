package util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class LyricsHelper {

    private static final String GENIUS_API_KEY = "4NI8Kg2YZyBBW5C555FBE1inkXviRv_NnivPN9z7yDF-00waoRkLBMNQTf_sUOAB";
    private static final String GENIUS_API_BASE_URL = "https://api.genius.com";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        String songTitle = "Bohemian Rhapsody";
        String artistName = "Queen";

        try {
            String lyrics = fetchSongLyrics(songTitle, artistName);
            System.out.println(lyrics);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String fetchSongLyrics(String songTitle, String artistName) throws IOException {
        int songId = searchSong(songTitle, artistName);
        if (songId == -1) {
            return "Song not found";
        }
        return getLyrics(songId);
    }

    private static int searchSong(String songTitle, String artistName) throws IOException {
        HttpUrl url = HttpUrl.parse(GENIUS_API_BASE_URL + "/search").newBuilder()
                             .addQueryParameter("q", songTitle + " " + artistName)
                             .build();

        Request request = new Request.Builder()
            .header("Authorization", "Bearer " + GENIUS_API_KEY)
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonData = response.body().string();
            JsonElement jsonElement = JsonParser.parseString(jsonData);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject hits = jsonObject.getAsJsonObject("response").getAsJsonArray("hits").get(0).getAsJsonObject();
            JsonObject result = hits.getAsJsonObject("result");

            if (result != null) {
                return result.get("id").getAsInt();
            }
        }
        return -1;
    }

    private static String getLyrics(int songId) throws IOException {
        HttpUrl url = HttpUrl.parse(GENIUS_API_BASE_URL + "/songs/" + songId).newBuilder()
                             .addQueryParameter("text_format", "plain")
                             .build();

        Request request = new Request.Builder()
            .header("Authorization", "Bearer " + GENIUS_API_KEY)
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonData = response.body().string();
            JsonElement jsonElement = JsonParser.parseString(jsonData);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject song = jsonObject.getAsJsonObject("response").getAsJsonObject("song");
            String lyrics = song.get("lyrics").getAsString();

            return lyrics;
        }
    }
}
