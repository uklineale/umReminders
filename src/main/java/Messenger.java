import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.utils.StringUtils;

import java.io.IOException;
import java.util.List;


public class Messenger {

    private static Logger LOG = LoggerFactory.getLogger(Messenger.class);
    private String fromNumber;
    private String apiToken;
    private String apiSecret;
    private String applicationId;
    private String url = "https://api.catapult.inetwork.com/v2/users/%s/messages";
    private String requestBin = "https://requestb.in/yqi4f6yq";

    public Messenger(String fromNumber, String userId, String apiToken, String apiSecret, String applicationId) {
        this.fromNumber = fromNumber;
        this.apiToken = apiToken;
        this.apiSecret = apiSecret;
        this.applicationId = applicationId;
        this.url = String.format(url, userId);

        Unirest.setObjectMapper( new com.mashape.unirest.http.ObjectMapper() {
            private ObjectMapper jacksonObjectMapper = new ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void sendMessages(List<String[]> messages) throws Exception {
        for (String[] message : messages) {
            //TODO move to validator
            if (message.length < 2 || StringUtils.isBlank(message[0])
                    || StringUtils.isBlank(message[1])) {
                continue;
            }

            LOG.info("Sending message to " + message[0]);
            LOG.info(String.format("Stats: fromNumber - %s apiToken %s apiSecret %s applicationId %s url %s",
                    fromNumber, apiToken, apiSecret, applicationId, url));

            Message toSend = new Message(fromNumber, message[0], message[1], applicationId);


            HttpResponse<String> response = Unirest.post(url)
                    .header("Content-type", "application/json")
                    .basicAuth(apiToken, apiSecret)
                    .body(toSend)
                    .asString();

            System.out.print(response.getBody().toString());
        }
    }
}
