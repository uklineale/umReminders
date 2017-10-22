import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.MultipartConfigElement;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;


public class Controller {

    private static final String UPLOADED_FILE = Constants.UPLOADED_FILE;

    private static final Parser parser = new Parser();
    private static final Messenger messenger = new Messenger(Constants.FROM, Constants.USER_ID, Constants.API_TOKEN,
            Constants.API_SECRET, Constants.APPLICATION_ID);

    private static Logger LOG = LoggerFactory.getLogger(Controller.class);

    public static void main(String[] args) {
        get("/", (req, res) ->
                //TODO: Make stylish page
            "<form method='post' enctype='multipart/form-data'>" // note the enctype
                    + "    <input type='file' name='" + UPLOADED_FILE + "' accept='.csv'>"
                    + "    <button>Upload CSV</button>"
                    + "</form>"
        );

        post("/", (req, res) -> {
            LOG.info("Got request");

            //TODO: look into security
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            List<String[]> lines = parser.parse(req);
            messenger.sendMessages(lines);

            return prettyPrintCsv(lines);
            //TODO send messages with v2
        });
    }

    private static String prettyPrintCsv(List<String[]> lines) {
        StringBuilder sb = new StringBuilder();

        for (String[] line : lines) {
            for (String s : line) {
                sb.append(s);
                sb.append(" ");
            }
            sb.append("\n");

        }

        return sb.toString();
    }
}
