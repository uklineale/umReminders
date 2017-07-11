import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Main {
    private static final String UPLOAD_DIR = "upload";
    private static final String UPLOADED_FILE = "uploaded_file";
    private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        File uploadDir = new File(UPLOAD_DIR);
        uploadDir.mkdir();

        staticFiles.externalLocation(UPLOAD_DIR);

//        get("/hello", (req, res) -> "Hello World" );

        get("/", (req, res) ->
            "<form method='post' enctype='multipart/form-data'>" // note the enctype
                    + "    <input type='file' name='" + UPLOADED_FILE + "' accept='.csv'>"
                    + "    <button>Upload CSV</button>"
                    + "</form>"
        );

        post("/", (req, res) -> {
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            //Do csv parse
            Part requestPart = req.raw().getPart(UPLOADED_FILE);
            InputStream inputStream = requestPart.getInputStream();

            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

            List<String[]> lines = reader.readAll();
            return prettyPrintCsv(lines);
            //TODO send messages with v2
            //TODO remove files from uploaded folder
        });
    }


    // methods used for logging
    private static void logInfo(Request req) throws IOException, ServletException {
        String fileName = req.raw().getPart(UPLOADED_FILE).getSubmittedFileName();
        LOG.info("Uploaded file '" + fileName +"'");
    }

    private static String readFile(InputStream stream) throws IOException {
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
