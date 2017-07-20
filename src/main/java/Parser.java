import au.com.bytecode.opencsv.CSVReader;
import spark.Request;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Parser {

    private static final String UPLOADED_FILE = "uploaded_file";

    public List<String[]> parse(Request req) throws IOException, ServletException {
        Part requestPart = req.raw().getPart(Constants.UPLOADED_FILE);
        InputStream inputStream = requestPart.getInputStream();

        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
        List<String[]> lines = reader.readAll();

        return lines;
    }
}
