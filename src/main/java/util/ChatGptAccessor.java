package util;

import static java.lang.String.format;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGptAccessor {

    private static final String REQUEST_PREFIX = "Answer this playfully for 5-13 year old kids in less than 30 words and do not answer adult questions. ";

    /**
     * Age buckets
     * Funny and Regular
     * Parental control
     * Interactivity
     */

    public static void main(String[] args) {
        getResponseFromChatGpt("Rhinos");
    }

    public static String getResponseFromChatGpt(final String request) {
        URL url = null;
        String kidFriendlyRequest = REQUEST_PREFIX + request;
        try {
            url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");

            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Authorization", "Bearer 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");


            JSONObject messageBody = new JSONObject();
            messageBody.put("role", "user");
            messageBody.put("content", kidFriendlyRequest);

            JSONObject messageBody1 = new JSONObject();
            messageBody1.put("role", "assistant");
            messageBody1.put("content", "Which Animal do you like to know about?");

            List<JSONObject> messages = new ArrayList<>();
            messages.add(messageBody);
            messages.add(messageBody1);

            System.out.println(messages);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("temperature", 0.6);
            requestBody.put("stream", true);
            requestBody.put("messages", messages);

            httpConn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(requestBody.toString());
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";

            System.out.println(response);
            String alexaResponse = parseGptResponse(response);
            System.out.println(alexaResponse);
            return alexaResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "I don't know. I am sorry, I failed you master";
    }

    private static String parseGptResponse(String gptResponse) {
        String[] splitByContent = gptResponse.split("content");
        StringBuilder resp = new StringBuilder();

        try {
            if (splitByContent.length>1) {
                Pattern pattern = Pattern.compile("\"([^\"]*)\"");
                for (int i=2; i<splitByContent.length; i++) {
                    String secondSplit = splitByContent[i].split("}")[0];
                    System.out.println(secondSplit);
                    String thirdSplit = secondSplit.split(":")[1];
                    thirdSplit = thirdSplit.replaceAll("\"", "");
                    System.out.println("Matcher: " + thirdSplit.trim());
                    resp.append(thirdSplit);
                }
            }

            return resp.toString();
        } catch (Exception e) {
            return resp.toString();
        }
    }
}
