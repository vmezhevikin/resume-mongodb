package net.devstudy.resume.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.devstudy.resume.annotation.constraints.DateFormat;
import net.devstudy.resume.annotation.constraints.EnglishLanguage;
import net.devstudy.resume.annotation.constraints.FieldImageOrFileNotEmpty;
import net.devstudy.resume.annotation.constraints.Phone;

@Document(indexName = "profile")
@org.springframework.data.mongodb.core.mapping.Document(collection = "profile")
@FieldImageOrFileNotEmpty(imageField = "photo", fileField = "file")
public class Profile extends AbstractDomain<String> implements Serializable {
	
	private static final long serialVersionUID = 4419584168346691423L;

	@Id
	private String id;

	private String uid;

	@JsonIgnore
	private String password;

	private Boolean active;

	private String firstName;

	private String lastName;

	@JsonIgnore
	private String fullName;

	@Size(min = 1)
	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String country;

	@Size(min = 1)
	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String city;

	private Date birthday;

	@Size(min = 1)
	@DateFormat
	@JsonIgnore
	@Transient
	private String birthdayString;

	@Size(min = 1)
	@EnglishLanguage
	@Email
	@JsonIgnore
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String email;

	@Size(min = 1)
	@Phone
	@JsonIgnore
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String phone;

	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String additionalInfo;

	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String objective;

	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE)
	private String summary;

	@JsonIgnore
	private String photo;

	private String photoSmall;

	@JsonIgnore
	private Date created;

	private List<Certificate> certificate;

	private List<Course> course;

	@JsonIgnore
	private List<Education> education;

	private List<Experience> experience;

	@JsonIgnore
	private List<Hobby> hobby;

	private List<Language> language;

	private List<Skill> skill;

	@JsonIgnore
	private Contact contact;

	@JsonIgnore
	@Transient
	private MultipartFile file;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		if (fullName == null)
			fullName = firstName + " " + lastName;
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setBirthdayString(String birthdayString) {
		this.birthdayString = birthdayString;
	}

	public String getBirthdayString() {
		if (birthdayString == null && birthday != null) {
			LocalDate birthdate = new LocalDate(birthday);
			birthdayString = birthdate.toString("yyyy-MM-dd");
		}
		return birthdayString;
	}

	public int getAge() {
		LocalDate birthdate = new LocalDate(birthday);
		LocalDate now = new LocalDate();
		Years age = Years.yearsBetween(birthdate, now);
		return age.getYears();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhotoSmall() {
		return photoSmall;
	}

	public void setPhotoSmall(String photoSmall) {
		this.photoSmall = photoSmall;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public List<Certificate> getCertificate() {
		return certificate;
	}

	public void setCertificate(List<Certificate> certificate) {
		this.certificate = certificate;
	}

	public List<Course> getCourse() {
		return course;
	}

	public void setCourse(List<Course> course) {
		this.course = course;
	}

	public List<Education> getEducation() {
		return education;
	}

	public void setEducation(List<Education> education) {
		this.education = education;
	}

	public List<Experience> getExperience() {
		return experience;
	}

	public void setExperience(List<Experience> experience) {
		this.experience = experience;
	}

	public List<Hobby> getHobby() {
		return hobby;
	}

	public void setHobby(List<Hobby> hobby) {
		this.hobby = hobby;
	}

	public boolean hasHobby(String description) {
		if (hobby == null || hobby.size() == 0) {
			return false;
		}
		for (Hobby h : hobby) {
			if (description.equals(h.getDescription())) {
				return true;
			}
		}
		return false;
	}

	public List<Language> getLanguage() {
		return language;
	}

	public void setLanguage(List<Language> language) {
		this.language = language;
	}

	public List<Skill> getSkill() {
		return skill;
	}

	public void setSkill(List<Skill> skill) {
		this.skill = skill;
	}

	public Contact getContact() {
		if (contact == null) {
			contact = new Contact();
		}
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
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
		result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((objective == null) ? 0 : objective.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((photo == null) ? 0 : photo.hashCode());
		result = prime * result + ((photoSmall == null) ? 0 : photoSmall.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		Profile other = (Profile) obj;
		if (additionalInfo == null) {
			if (other.additionalInfo != null)
				return false;
		} else if (!additionalInfo.equals(other.additionalInfo))
			return false;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (objective == null) {
			if (other.objective != null)
				return false;
		} else if (!objective.equals(other.objective))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (photo == null) {
			if (other.photo != null)
				return false;
		} else if (!photo.equals(other.photo))
			return false;
		if (photoSmall == null) {
			if (other.photoSmall != null)
				return false;
		} else if (!photoSmall.equals(other.photoSmall))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}
}