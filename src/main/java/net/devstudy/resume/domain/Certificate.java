package net.devstudy.resume.domain;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.devstudy.resume.annotation.constraints.EnglishLanguage;
import net.devstudy.resume.annotation.constraints.NotEmptyFile;

public class Certificate implements Serializable, ProfileCollectionField, Comparable<Certificate> {
	
	private static final long serialVersionUID = -6718545401459519784L;

	@EnglishLanguage
	@Size(min = 1)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String description;

	@JsonIgnore
	private String img;

	@JsonIgnore
	private String imgSmall;

	@Transient
	@NotEmptyFile
	@JsonIgnore
	private MultipartFile file;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImgSmall() {
		return imgSmall;
	}

	public void setImgSmall(String imgSmall) {
		this.imgSmall = imgSmall;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((img == null) ? 0 : img.hashCode());
		result = prime * result + ((imgSmall == null) ? 0 : imgSmall.hashCode());
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
		Certificate other = (Certificate) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (img == null) {
			if (other.img != null)
				return false;
		} else if (!img.equals(other.img))
			return false;
		if (imgSmall == null) {
			if (other.imgSmall != null)
				return false;
		} else if (!imgSmall.equals(other.imgSmall))
			return false;
		return true;
	}

	@Override
	public int compareTo(Certificate other) {
		return this.description.compareTo(other.description);
	}
}
