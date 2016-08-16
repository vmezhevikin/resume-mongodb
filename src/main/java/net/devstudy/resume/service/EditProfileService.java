package net.devstudy.resume.service;

import java.util.List;

import javax.annotation.Nonnull;

import net.devstudy.resume.domain.Certificate;
import net.devstudy.resume.domain.Contact;
import net.devstudy.resume.domain.Course;
import net.devstudy.resume.domain.Education;
import net.devstudy.resume.domain.Experience;
import net.devstudy.resume.domain.Hobby;
import net.devstudy.resume.domain.Language;
import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.domain.Skill;
import net.devstudy.resume.form.EmailForm;
import net.devstudy.resume.form.PasswordForm;
import net.devstudy.resume.form.SignUpForm;
import net.devstudy.resume.form.UidForm;

public interface EditProfileService {
	
	@Nonnull Profile createNewNotActiveProfile(@Nonnull SignUpForm form);

	void addConfirmRegistrtionToken(@Nonnull String idProfile, @Nonnull String token);

	void removeConfirmRegistrtionToken(@Nonnull String idProfile);
	
	@Nonnull Profile activateProfile(@Nonnull Profile profile);
	
	void updateLastVisitDate(@Nonnull String idProfile);
	
	void updateSkill(String idProfile, @Nonnull List<Skill> editedList);

	void updateLanguage(String idProfile, @Nonnull List<Language> editedList);

	void updateExperience(String idProfile, @Nonnull List<Experience> editedList);

	void updateEducation(String idProfile, @Nonnull List<Education> editedList);

	void updateCourse(String idProfile, @Nonnull List<Course> editedList);

	void updateCertificate(String idProfile, @Nonnull List<Certificate> editedList);

	void addCertificate(String idProfile, @Nonnull Certificate newCertificate);

	void updateGeneralInfo(String idProfile, @Nonnull Profile editedProfile);

	void updateAdditionalInfo(String idProfile, @Nonnull Profile editedProfile);

	void updateContact(String idProfile, @Nonnull Contact newContact);

	void updateHobby(String idProfile, @Nonnull List<Hobby> editedList);

	void removeProfile(String idProfile);

	void addRestoreToken(String idProfile, @Nonnull String token);

	void removeRestoreToken(String idProfile);

	void updatePassword(String idProfile, @Nonnull PasswordForm form);

	void addConfirmEmailToken(@Nonnull String idProfile, @Nonnull String token, @Nonnull EmailForm form);

	@Nonnull String removeConfirmEmailToken(@Nonnull String idProfile);

	void updateEmail(String idProfile, String email);

	void updateUid(String idProfile, @Nonnull UidForm form);
}