package net.devstudy.resume.form;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import net.devstudy.resume.annotation.constraints.EnglishLanguage;
import net.devstudy.resume.domain.Profile;

public class EmailForm implements Serializable {
	
	private static final long serialVersionUID = 2776455703501721852L;

	public EmailForm() {
		super();
	}
	
	public EmailForm(Profile profile) {
		super();
		this.email = profile.getEmail();
	}
	
	@Size(min = 1)
	@EnglishLanguage
	@Email
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
