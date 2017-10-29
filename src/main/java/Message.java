public class Message {
    private String from;
    private String to;
    private String text;
    private String applicationId;

    public Message(String from, String to, String text,
                   String applicationId) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.applicationId = applicationId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
