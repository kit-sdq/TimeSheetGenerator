package mail;

import ui.TemplateFormatter;
import ui.UserInterface;
import ui.json.JSONHandler;
import ui.json.UISettings;

public class MailtoLinkBuilder {

    private static final String LINK_TEMPLATE = "mailto:%s?subject=%s";

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
        UISettings settings = JSONHandler.getUISettings();
        String formattedSubject = TemplateFormatter.formatTemplate(settings.getMailSubjectFormat(), parentUi);
        return LINK_TEMPLATE.formatted(settings.getMailReceiver(), formattedSubject);
    }
}
