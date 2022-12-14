package handlers;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

public class CancelIntentHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("AMAZON.CancelIntent") ||
            intentRequest.getIntent().getName().equals("AMAZON.StopIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        final String speechText = "GoodBye";
        return handlerInput.getResponseBuilder()
                           .withSpeech(speechText)
                           .build();
    }
}
