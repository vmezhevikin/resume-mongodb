package net.devstudy.resume.domain;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import net.devstudy.resume.annotation.constraints.EnglishLanguage;
import net.devstudy.resume.annotation.constraints.FirstFieldLessThanSecond;

@FirstFieldLessThanSecond(firstField = "startingYear", secondField = "completionYear", message = "Completion date must be after starting date")
public class Education implements Serializable, ProfileCollectionField {
	
	private static final long serialVersionUID = 8257785827490293025L;

	@EnglishLanguage
	@Size(min = 1)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String speciality;

	@EnglishLanguage
	@Size(min = 1)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String university;

	@EnglishLanguage
	@Size(min = 1)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String department;

	private Integer startingYear;

	private Integer completionYear;

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getStartingYear() {
		return startingYear;
	}

	public void setStartingYear(Integer startingYear) {
		this.startingYear = startingYear;
	}

	public Integer getCompletionYear() {
		return completionYear;
	}

	public void setCompletionYear(Integer completionYear) {
		this.completionYear = completionYear;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((completionYear == null) ? 0 : completionYear.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((startingYear == null) ? 0 : startingYear.hashCode());
		result = prime * result + ((university == null) ? 0 : university.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Education other = (Education) obj;
		if (completionYear == null) {
			if (other.completionYear != null)
				return false;
		} else if (!completionYear.equals(other.completionYear))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (startingYear == null) {
			if (other.startingYear != null)
				return false;
		} else if (!startingYear.equals(other.startingYear))
			return false;
		if (university == null) {
			if (other.university != null)
				return false;
		} else if (!university.equals(other.university))
			return false;
		return true;
	}
}