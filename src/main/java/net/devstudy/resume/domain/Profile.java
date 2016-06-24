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
import net.devstudy.resume.annotation.constraints.Phone;

@Document(indexName = "profile")
@org.springframework.data.mongodb.core.mapping.Document(collection = "profile")
public class Profile implements Serializable {
	
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

	@Size(min = 1, message = "Don't leave it empty")
	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Html is not allowed")
	private String country;

	@Size(min = 1, message = "Don't leave it empty")
	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Html is not allowed")
	private String city;

	private Date birthday;

	@Size(min = 1, message = "Don't leave it empty")
	@DateFormat
	@JsonIgnore
	@Transient
	private String birthdayString;

	@Size(min = 1, message = "Don't leave it empty")
	@EnglishLanguage
	@Email(message = "Not an email address")
	@JsonIgnore
	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Html is not allowed")
	private String email;

	@Size(min = 1, message = "Don't leave it empty")
	@Phone
	@JsonIgnore
	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Html is not allowed")
	private String phone;

	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Html is not allowed")
	private String additionalInfo;

	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Html is not allowed")
	private String objective;

	@EnglishLanguage
	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Html is not allowed")
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

	//@JsonIgnore
	//private ProfileRestore profileRestore;

	@JsonIgnore
	private Contact contact;

	@JsonIgnore
	@Transient
	private MultipartFile file;

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
			if (h.getDescription().equals(description)) {
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

	/*public ProfileRestore getProfileRestore() {
		return profileRestore;
	}

	public void setProfileRestore(ProfileRestore profileRestore) {
		this.profileRestore = profileRestore;
	}*/

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
}