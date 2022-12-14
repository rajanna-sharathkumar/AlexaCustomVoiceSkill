package util;

import static java.lang.String.format;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class ChatGptAccessor {

    private static final String REQUEST_BODY_TEMPLATE =
        "{\n  \"model\": \"text-davinci-003\",\n  \"prompt\": \"%s\",\n  \"temperature\": 0.0,\n  \"max_tokens\": 50,\n  \"top_p\": 1,\n  \"frequency_penalty\": 0,\n  \"presence_penalty\": 0\n}";

    public static String getResponseFromChatGpt(final String request) {
        URL url = null;
        try {
            url = new URL("https://api.openai.com/v1/completions");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");

            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Authorization", "Bearer <your-API-key>");

            httpConn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(format(REQUEST_BODY_TEMPLATE, request));
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";

            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getJSONArray("choices").getJSONObject(0).get("text").toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "I don't know. I am sorry, I failed you master";
    }
}
