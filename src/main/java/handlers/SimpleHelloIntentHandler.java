package handlers;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import util.ChatGptAccessor;

public class SimpleHelloIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("HelloWorldIntent") &&
            intentRequest.getIntent().getSlots() != null;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        final String speechText = intentRequest.getIntent().getSlots().get("userQuestion").getValue();
        System.out.println("Customer Request: " + speechText);
        final String response = ChatGptAccessor.getResponseFromChatGpt(speechText);
        System.out.println("ChatGPT Response: " + response);
        return handlerInput.getResponseBuilder()
                           .withSpeech(response)
                           .withReprompt(speechText)
                           .build();
    }

}