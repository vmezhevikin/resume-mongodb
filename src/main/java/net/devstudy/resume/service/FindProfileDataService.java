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
import net.devstudy.resume.domain.Skill;

public interface FindProfileDataService {
	
	@Nonnull List<Skill> findListSkill(String idProfile);

	@Nonnull List<Language> findListLanguage(String idProfile);

	@Nonnull List<Experience> findListExperience(String idProfile);

	@Nonnull List<Education> findListEducation(String idProfile);

	@Nonnull List<Course> findListCourse(String idProfile);

	@Nonnull List<Certificate> findListCertificate(String idProfile);

	@Nonnull Contact findContact(String idProfile);

	@Nonnull List<Hobby> findListHobby(String idProfile);
}
