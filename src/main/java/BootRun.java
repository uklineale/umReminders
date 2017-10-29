public class BootRun {

    private static final Parser parser = new Parser();
    private static final Messenger messenger = new Messenger(Constants.FROM, Constants.USER_ID, Constants.API_TOKEN,
            Constants.API_SECRET, Constants.APPLICATION_ID);

    private static final Controller controller = new Controller(parser, messenger);

    public static void main(String[] args) {
        controller.handleRequests();
    }
}
