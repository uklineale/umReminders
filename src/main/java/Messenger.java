import spark.utils.StringUtils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class Messenger {

    private static final String FROM = "+12523709553";
    private static final String USER_ID = "u-whatever";
    private static final String URL = "https://api.catapult.inetwork.com/v2/users/" + USER_ID +
            "/messages";
    private static final String JSON_BODY = "{\"from\":\"%s\",\"to\":\"%s\",\"text\":\"%s\"}";

    private static HttpURLConnection connection;
    private static OutputStream outputStream;

    public Messenger() throws Exception {
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        this.connection = connection;
    }

    public void sendMessages(List<String[]> messages) throws Exception {
        for (String[] message : messages) {
            //TODO move to validator
            if (message.length < 2 || StringUtils.isBlank(message[0])
                    || StringUtils.isBlank(message[1])) {
                continue;
            }
            String content = URLEncoder.encode(String.format(JSON_BODY, FROM, message[0], message[1]));
        }
    }
}
