package util;

import static java.lang.String.format;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGptAccessor {

    private static final String REQUEST_PREFIX = "Give response that is playful for 5-13 year old kids in less than 30 words and make it funny. Give knowledge or fun fact in less than 30 words. Do not answer adult questions. Ask a follow up question or start a new topic.";

    /**
     * Age buckets
     * Funny and Regular
     * Parental control
     * Interactivity
     */

    public static void main(String[] args) {
        List<Map<String, String>> context = new ArrayList<>();
        Map<String, String> context0 = new HashMap<>();
        Map<String, String> context1 = new HashMap<>();
        Map<String, String> context2 = new HashMap<>();
        Map<String, String> context3 = new HashMap<>();
        Map<String, String> context4 = new HashMap<>();

        context0.put("role", "system");
        context0.put("content", "Give response that is playful for 5-13 year old kids in less than 30 words and do not answer adult questions. ");

        context1.put("role", "user");
        context1.put("content", "what is cat");

        context2.put("role", "assistant");
        context2.put("content", "A furry friend with whiskers, paws, and a love for naps and chasing toys. Meow! Do "
                + "you want to learn something new?");
//
//        context3.put("role", "user");
//        context3.put("content", "yes");
//
//        context4.put("role", "assistant");
//        context4.put("content", "Sure thing! What's your favorite color?");

//        context.add(context0);
        context.add(context1);
        context.add(context2);
//        context.add(context3);
//        context.add(context4);


        getResponseFromChatGpt("tell me something about American cat.", context);

    }

    public static String getResponseFromChatGpt(final String request, final List<Map<String, String>>  context) {
        URL url = null;
        String kidFriendlyRequest = request ;
        try {
            url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");

            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Authorization", "Bearer XXX");


            JSONObject messageStarter = new JSONObject();
            messageStarter.put("role", "system");
            messageStarter.put("content", REQUEST_PREFIX);

            JSONObject messageBody = new JSONObject();
            messageBody.put("role", "user");
            messageBody.put("content", kidFriendlyRequest);

            List<JSONObject> messages = new ArrayList<>();

            messages.add(messageStarter);

            for(Map<String, String> record : context){
                JSONObject messageBody1 = new JSONObject();
                messageBody1.put("role", record.get("role"));
                messageBody1.put("content", record.get("content"));
                messages.add(messageBody1);
            }

            messages.add(messageBody);

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
                for (int i=1; i<splitByContent.length; i++) {
                    System.out.println("......");
                    String secondSplit = splitByContent[i].split("}")[0];
                    System.out.println(secondSplit);
                    String thirdSplit = secondSplit.split(":")[1];
                    thirdSplit = thirdSplit.replaceAll("\"", "");
                    thirdSplit = thirdSplit.replace("\\n", " ");
                    thirdSplit = thirdSplit.replace("\\", " ");
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
