import com.bandwidth.sdk.BandwidthClient;
import com.bandwidth.sdk.model.Message;

import java.util.List;

public class Messenger {

    private static final String FROM = "+12523709553";

    public void sendMessages(List<String[]> messages) throws Exception {
        BandwidthClient client = BandwidthClient.getInstance();
        for (String[] message : messages) {
            Message.create(message[0], FROM, message[1]);
            
        }
    }
}
