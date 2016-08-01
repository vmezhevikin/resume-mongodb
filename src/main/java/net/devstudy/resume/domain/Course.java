package net.devstudy.resume.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.devstudy.resume.annotation.constraints.EnglishLanguage;

public class Course implements Serializable, ProfileCollectionField {
	
	private static final long serialVersionUID = -7509905830407382879L;

	@EnglishLanguage
	@Size(min = 1)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String description;

	@EnglishLanguage
	@Size(min = 1)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@JsonIgnore
	private String school;

	@JsonIgnore
	private Date completionDate;

	@Transient
	@JsonIgnore
	private Integer completionMonth;

	@Transient
	@JsonIgnore
	private Integer completionYear;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	@Transient
	public String getCompletionDateString() {
		LocalDate date = new LocalDate(completionDate);
		return date.toString("MMM yyyy");
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	@Transient
	public Integer getCompletionMonth() {
		if (completionDate == null)
			return null;
		LocalDate date = new LocalDate(completionDate);
		return date.getMonthOfYear();
	}

	@Transient
	public void setCompletionMonth(Integer completionMonth) {
		this.completionMonth = completionMonth;
		setupCompletionDate();
	}

	@Transient
	public Integer getCompletionYear() {
		if (completionDate == null)
			return null;
		LocalDate date = new LocalDate(completionDate);
		return date.getYear();
	}

	@Transient
	public void setCompletionYear(Integer completionYear) {
		this.completionYear = completionYear;
		setupCompletionDate();
	}

	private void setupCompletionDate() {
		if (completionYear != null && completionMonth != null) {
			DateTime dateTime = new DateTime(completionYear, completionMonth, 1, 0, 0);
			Date date = new Date(dateTime.getMillis());
			setCompletionDate(date);
		} else
			setCompletionDate(null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((completionDate == null) ? 0 : completionDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((school == null) ? 0 : school.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (school == null) {
			if (other.school != null)
				return false;
		} else if (!school.equals(other.school))
			return false;
		if (completionDate == null) {
			if (other.completionDate != null)
				return false;
		} else if (!completionDate.equals(other.completionDate))
			return false;
		return true;
	}
}
