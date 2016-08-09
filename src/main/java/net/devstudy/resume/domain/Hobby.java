package net.devstudy.resume.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;

public class Hobby implements Serializable, ProfileCollectionField, Comparable<Hobby> {
	
	private static final long serialVersionUID = 4900586647321986730L;

	private String description;
	
	@Transient
	private String checked;
	
	@Transient
	private String icon;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		Hobby other = (Hobby) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}

	@Override
	public int compareTo(Hobby other) {
		return this.description.compareTo(other.description);
	}
}