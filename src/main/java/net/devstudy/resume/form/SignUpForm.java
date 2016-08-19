package net.devstudy.resume.form;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import net.devstudy.resume.annotation.constraints.EnglishLanguage;
import net.devstudy.resume.annotation.constraints.FieldMatch;

@FieldMatch(firstField = "password", secondField = "confirm")
public class SignUpForm extends RecaptchaForm implements Serializable {
	
	private static final long serialVersionUID = 5105334805427908062L;

	public SignUpForm() {
		super();
	}

	@Size(min = 1)
	@Email
	private String email;

	@Size(min = 1)
	@EnglishLanguage
	private String password;

	private String confirm;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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
