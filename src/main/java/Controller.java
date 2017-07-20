import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;


public class Controller {

    private static final String UPLOADED_FILE = Constants.UPLOADED_FILE;
    private static final String UPLOAD_DIR = Constants.UPLOAD_DIR;

    private static final Parser parser = new Parser();

    private static Logger LOG = LoggerFactory.getLogger(Controller.class);

    public static void main(String[] args) {

        File uploadDir = new File(UPLOAD_DIR);
        uploadDir.mkdir();

        staticFiles.externalLocation(UPLOAD_DIR);

        get("/", (req, res) ->
            "<form method='post' enctype='multipart/form-data'>" // note the enctype
                    + "    <input type='file' name='" + UPLOADED_FILE + "' accept='.csv'>"
                    + "    <button>Upload CSV</button>"
                    + "</form>"
        );

        post("/", (req, res) -> {
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            List<String[]> lines = parser.parse(req);

            return prettyPrintCsv(lines);
            //TODO send messages with v2
            //TODO remove files from uploaded folder
        });
    }


    public static void logInfo(Request req) throws IOException, ServletException {
        String fileName = req.raw().getPart(UPLOADED_FILE).getSubmittedFileName();
        LOG.info("Uploaded file '" + fileName +"'");
    }

    public static String readFile(InputStream stream) throws IOException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream))){
            return reader.lines().collect(Collectors.joining("\n"));
        }
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
