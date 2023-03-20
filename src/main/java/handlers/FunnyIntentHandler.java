package handlers;

import static org.slf4j.LoggerFactory.getLogger;
import static util.Constants.LIST_EXPERIENCE_SESSION_KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import util.ChatGptAccessor;

public class FunnyIntentHandler implements IntentRequestHandler {
    private static Logger LOG = getLogger(FunnyIntentHandler.class);
    private static final String REQUEST_PREFIX = "Give response that is playful for 5 year old kid in less than 30 words and make it funny. "
        + "Give knowledge or fun fact in less than 30 words. Do not answer adult questions. Ask a follow up question or start a new topic.";


    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("FunnyIntent") &&
            intentRequest.getIntent().getSlots() != null;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

        AttributesManager attributesManager = handlerInput.getAttributesManager();

        Map<String, Object> currentSessionAttributes = attributesManager.getSessionAttributes();

        List<Map<String, String>> context = new ArrayList<>();

        if (currentSessionAttributes.containsKey(LIST_EXPERIENCE_SESSION_KEY)) {
            LOG.info(currentSessionAttributes.get(LIST_EXPERIENCE_SESSION_KEY).toString());
            context = (List<Map<String, String>>) currentSessionAttributes.get(LIST_EXPERIENCE_SESSION_KEY);
        }

        final String speechText = intentRequest.getIntent().getSlots().get("userQuestion").getValue();
        System.out.println("Customer Request: " + speechText);
        String response = ChatGptAccessor.getResponseFromChatGpt(REQUEST_PREFIX, speechText, context);
        System.out.println("ChatGPT Response: " + response);

        addToContextList(context, speechText, response);

        currentSessionAttributes.put(LIST_EXPERIENCE_SESSION_KEY, context);

        attributesManager.setSessionAttributes(currentSessionAttributes);

        return handlerInput.getResponseBuilder()
                           .withSpeech(response)
                           .withReprompt(speechText)
                           .build();
    }

    private void addToContextList(final List<Map<String, String>> context,
            String request,
            String response){

        Map<String, String> mapRequest = new HashMap<>();
        Map<String, String> mapResponse = new HashMap<>();

        mapRequest.put("role", "user");
        mapRequest.put("content", request);

        mapResponse.put("role", "assistant");
        mapResponse.put("content", response);

        context.add(mapRequest);
        context.add(mapResponse);
    }
}