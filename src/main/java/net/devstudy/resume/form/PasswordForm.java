package net.devstudy.resume.form;

import java.io.Serializable;

import javax.validation.constraints.Size;

import net.devstudy.resume.annotation.constraints.EnglishLanguage;
import net.devstudy.resume.annotation.constraints.FieldMatch;

@FieldMatch(firstField = "password", secondField = "confirm")
public class PasswordForm implements Serializable {
	
	private static final long serialVersionUID = 2776455703501721852L;

	public PasswordForm() {
		super();
	}
	
	@Size(min = 1)
	@EnglishLanguage
	private String password;

	private String confirm;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
}
