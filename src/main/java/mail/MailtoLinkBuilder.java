/* Licensed under MIT 2025. */
package mail;

import ui.TemplateFormatter;
import ui.UserInterface;
import ui.json.JSONHandler;
import ui.json.UISettings;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MailtoLinkBuilder {

	private static final String LINK_TEMPLATE = "mailto:%s?%ssubject=%s";
	private static final String CC_RECIPIENT_TEMPLATE = "cc=%s&";
	private static final String CC_ENCODED_DELIMITER = "%2C%20";

	private final UserInterface parentUi;

	/**
	 * Constructs a new Mailto link builder. The given UI will provide the
	 * month/year for the mail subject.
	 * 
	 * @param parentUi The relevant ui instance.
	 */
	public MailtoLinkBuilder(UserInterface parentUi) {
		this.parentUi = parentUi;
	}

	public String constructLink() {
		UISettings settings = JSONHandler.getUISettings();
		String formattedSubject = TemplateFormatter.formatTemplate(settings.getMailSubjectFormat(), parentUi);
		String additionalTags = buildAdditionalTags(settings.getMailRecipientsCC());
		return LINK_TEMPLATE.formatted(settings.getMailRecipient(), additionalTags, encodeURIComponent(formattedSubject));
	}

	private String buildAdditionalTags(List<String> mailRecipientsCC) {
		return CC_RECIPIENT_TEMPLATE.formatted(String.join(CC_ENCODED_DELIMITER, mailRecipientsCC));
	}

	private static String encodeURIComponent(String plaintextLink) {
		return URLEncoder.encode(plaintextLink, StandardCharsets.UTF_8).replace("+", "%20");
	}
}
