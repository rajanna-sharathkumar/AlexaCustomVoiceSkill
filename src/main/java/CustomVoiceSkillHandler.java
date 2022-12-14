import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazonaws.services.lambda.runtime.Context;

import handlers.CancelIntentHandler;
import handlers.GenericExceptionHandler;
import handlers.HelpIntentHandler;
import handlers.LaunchHandler;
import handlers.NoIntentHandler;
import handlers.SimpleHelloIntentHandler;

public class CustomVoiceSkillHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                     .addRequestHandlers(
                         new LaunchHandler(),
                         new CancelIntentHandler(),
                         new HelpIntentHandler(),
                         new NoIntentHandler(),
                         new SimpleHelloIntentHandler()
                     )
                     .addExceptionHandlers(new GenericExceptionHandler())
                     // Add your skill id below
                     //.withSkillId("")
                     .build();
    }

    public CustomVoiceSkillHandler() {
        super(getSkill());
    }

}
