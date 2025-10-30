package mail;

import ui.TemplateFormatter;
import ui.UserInterface;
import ui.json.JSONHandler;
import ui.json.UISettings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.URIParameter;

public class MailtoLinkBuilder {

    private static final String LINK_TEMPLATE = "mailto:%s";
    private static final String LINK_CONTENT_TEMPLATE = "%s?subject=%s";

    private final UserInterface parentUi;

    /**
     * Constructs a new Mailto link builder.
     * The given UI will provide the month/year for the mail subject.
     * @param parentUi The relevant ui instance.
     */
    public MailtoLinkBuilder(UserInterface parentUi) {
        this.parentUi = parentUi;
    }

    public String constructLink() {
        return LINK_TEMPLATE.formatted(encodeURIComponent(constructPlaintextLink()));
    }

    private String constructPlaintextLink() {
        UISettings settings = JSONHandler.getUISettings();
        String formattedSubject = TemplateFormatter.formatTemplate(settings.getMailSubjectFormat(), parentUi);
        return LINK_CONTENT_TEMPLATE.formatted(settings.getMailRecipient(), formattedSubject);
    }

    private static String encodeURIComponent(String plaintextLink) {
        return URLEncoder.encode(plaintextLink, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }
}
