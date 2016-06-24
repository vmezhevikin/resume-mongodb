package net.devstudy.resume.service;

import java.util.List;

import javax.annotation.Nonnull;

import net.devstudy.resume.domain.Certificate;
import net.devstudy.resume.domain.Contact;
import net.devstudy.resume.domain.Course;
import net.devstudy.resume.domain.Education;
import net.devstudy.resume.domain.Experience;
import net.devstudy.resume.domain.Language;
import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.domain.Skill;
import net.devstudy.resume.form.ChangePasswordForm;
import net.devstudy.resume.form.SignUpForm;

public interface EditProfileService {
	
	@Nonnull Profile createNewProfile(@Nonnull SignUpForm form);

	void updateSkill(String idProfile, @Nonnull List<Skill> editedList);

	void addSkill(String idProfile, @Nonnull Skill newSkill);

	void updateLanguage(String idProfile, @Nonnull List<Language> editedList);

	void addLanguage(String idProfile, @Nonnull Language newLanguage);

	void updateExperience(String idProfile, @Nonnull List<Experience> editedList);

	void addExperience(String idProfile, @Nonnull Experience newExperience);

	void updateEducation(String idProfile, @Nonnull List<Education> editedList);

	void addEducation(String idProfile, @Nonnull Education newEducation);

	void updateCourse(String idProfile, @Nonnull List<Course> editedList);

	void addCourse(String idProfile, @Nonnull Course newCourse);

	void updateCertificate(String idProfile, @Nonnull List<Certificate> editedList);

	void addCertificate(String idProfile, @Nonnull Certificate newCertificate);

	void updateGeneralInfo(String idProfile, @Nonnull Profile editedProfile);

	void updateAdditionalInfo(String idProfile, @Nonnull Profile editedProfile);

	void updateContact(String idProfile, @Nonnull Contact newContact);

	void updateHobby(String idProfile, @Nonnull List<String> editedList);

	void removeProfile(String idProfile);

	void addRestoreToken(String idProfile, @Nonnull String token);

	void removeRestoreToken(String idProfile);

	void updatePassword(String idProfile, @Nonnull ChangePasswordForm form);
}
