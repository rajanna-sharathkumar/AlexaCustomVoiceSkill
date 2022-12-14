###How to create .jar from this package?
Run the following command 

```shell
mvn org.apache.maven.plugins:maven-assembly-plugin:2.6:assembly -DdescriptorId=jar-with-dependencies package
```

###Skill JSON Interaction Model

```json
{
  "interactionModel": {
    "languageModel": {
      "invocationName": "hi dad",
      "modelConfiguration": {
        "fallbackIntentSensitivity": {
          "level": "HIGH"
        }
      },
      "intents": [
        {
          "name": "AMAZON.CancelIntent",
          "samples": []
        },
        {
          "name": "AMAZON.HelpIntent",
          "samples": []
        },
        {
          "name": "AMAZON.StopIntent",
          "samples": []
        },
        {
          "name": "HelloWorldIntent",
          "slots": [
            {
              "name": "userQuestion",
              "type": "AMAZON.SearchQuery"
            }
          ],
          "samples": [
            "Hey Dad {userQuestion}"
          ]
        },
        {
          "name": "AMAZON.NavigateHomeIntent",
          "samples": []
        },
        {
          "name": "AMAZON.FallbackIntent",
          "samples": []
        }
      ],
      "types": []
    }
  }
}
```