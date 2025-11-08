package mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores information set in the draft email popup, which currently consists of mail and cc recipients.
 * @param recipient The main recipient.
 * @param additionalRecipients All other recipients (in CC).
 */
public record MailInformation(String recipient, List<String> additionalRecipients) {

    // override relevant methods to avoid storing / giving external list references

    /**
     * Creates a new Mail Information Record. Creates a shallow copy of the cc recipient list.
     * @param recipient The main recipient.
     * @param additionalRecipients All other recipients (in CC).
     */
    public MailInformation(String recipient, List<String> additionalRecipients) {
        this.recipient = recipient;
        this.additionalRecipients = new ArrayList<>(additionalRecipients);
    }

    /**
     * Gets a copy of the list of additional recipients.
     * @return copy of the cc recipients.
     */
    public List<String> additionalRecipients() {
        return new ArrayList<>(additionalRecipients);
    }
}
