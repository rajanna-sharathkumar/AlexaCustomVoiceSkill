import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import handlers.CancelIntentHandler;
import handlers.FunnyIntentHandler;
import handlers.GeneralIntentHandler;
import handlers.GenerateReportIntentHandler;
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
                         new SimpleHelloIntentHandler(),
                         new FunnyIntentHandler(),
                         new GenerateReportIntentHandler(),
                         new GeneralIntentHandler()
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
