package handlers;

import static util.Constants.LIST_EXPERIENCE_SESSION_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import util.ChatGptAccessor;

public class CancelIntentHandler implements IntentRequestHandler {
    private static final String REQUEST_PREFIX = "Give response that is playful for 5 year old kid in less than 30 words and make it funny.";

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("AMAZON.CancelIntent") ||
            intentRequest.getIntent().getName().equals("AMAZON.StopIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        AttributesManager attributesManager = handlerInput.getAttributesManager();

        Map<String, Object> currentSessionAttributes = attributesManager.getSessionAttributes();

        List<Map<String, String>> context = new ArrayList<>();

        if (currentSessionAttributes.containsKey(LIST_EXPERIENCE_SESSION_KEY)) {
            context = (List<Map<String, String>>) currentSessionAttributes.get(LIST_EXPERIENCE_SESSION_KEY);
        }

        final String speechText = "Greetings ending conversation";
        System.out.println("Customer Request: " + speechText);
        String response = ChatGptAccessor.getResponseFromChatGpt(REQUEST_PREFIX, speechText, context);
        System.out.println("ChatGPT Response: " + response);


        return handlerInput.getResponseBuilder()
                           .withSpeech(response)
                           .build();
    }
}
