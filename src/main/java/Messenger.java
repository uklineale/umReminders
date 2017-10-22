import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.utils.StringUtils;
import java.util.List;


public class Messenger {

    private static Logger LOG = LoggerFactory.getLogger(Messenger.class);
    private String fromNumber;
    private String apiToken;
    private String apiSecret;
    private String applicationId;
    private String url = "https://api.catapult.inetwork.com/v2/users/%s/messages";

    public Messenger(String fromNumber, String userId, String apiToken, String apiSecret, String applicationId) {
        this.fromNumber = fromNumber;
        this.apiToken = apiToken;
        this.apiSecret = apiSecret;
        this.applicationId = applicationId;
        this.url = String.format(url, userId);
    }

    public void sendMessages(List<String[]> messages) throws Exception {
        for (String[] message : messages) {
            //TODO move to validator
            if (message.length < 2 || StringUtils.isBlank(message[0])
                    || StringUtils.isBlank(message[1])) {
                continue;
            }

            LOG.info("Sending message to " + message[0]);

            HttpResponse<JsonNode> response = Unirest.post(url)
                    .header("accept", "application/json")
                    .basicAuth(apiToken, apiSecret)
                    .field("from", fromNumber)
                    .field("to", message[0])
                    .field("text", message[1])
                    .field("applicationId", applicationId)
                    .asJson();

            System.out.print(response.getBody().toString());
        }
    }
}
