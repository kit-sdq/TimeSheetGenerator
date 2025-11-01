/* Licensed under MIT 2025. */
package mail;

import ui.TemplateFormatter;
import ui.UserInterface;
import ui.json.JSONHandler;
import ui.json.UISettings;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MailtoLinkBuilder {

	private static final String LINK_TEMPLATE = "mailto:%s?subject=%s";

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
		return LINK_TEMPLATE.formatted(settings.getMailRecipient(), encodeURIComponent(formattedSubject));
	}

	private static String encodeURIComponent(String plaintextLink) {
		return URLEncoder.encode(plaintextLink, StandardCharsets.UTF_8).replace("+", "%20");
	}
}
