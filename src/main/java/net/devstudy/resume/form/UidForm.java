package net.devstudy.resume.form;

import java.io.Serializable;

import javax.validation.constraints.Size;

import net.devstudy.resume.annotation.constraints.EnglishLanguage;
import net.devstudy.resume.domain.Profile;

public class UidForm implements Serializable {
	
	private static final long serialVersionUID = 2776455703501721852L;

	public UidForm() {
		super();
	}
	
	public UidForm(Profile profile) {
		super();
		this.uid = profile.getUid();
	}
	
	@Size(min = 1)
	@EnglishLanguage(withPunctuations = false, withSpecSymbols = false)
	private String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
