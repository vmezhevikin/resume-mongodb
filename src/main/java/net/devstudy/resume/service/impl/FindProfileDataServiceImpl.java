package net.devstudy.resume.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.devstudy.resume.domain.Certificate;
import net.devstudy.resume.domain.Contact;
import net.devstudy.resume.domain.Course;
import net.devstudy.resume.domain.Education;
import net.devstudy.resume.domain.Experience;
import net.devstudy.resume.domain.Hobby;
import net.devstudy.resume.domain.Language;
import net.devstudy.resume.domain.Skill;
import net.devstudy.resume.repository.storage.ProfileRepository;
import net.devstudy.resume.service.FindProfileDataService;

@Service
public class FindProfileDataServiceImpl implements FindProfileDataService {
	
	@Autowired
	private ProfileRepository profileRepository;

	@Override
	public List<Skill> findListSkill(String idProfile) {
		return profileRepository.findById(idProfile).getSkill();
	}

	@Override
	public List<Language> findListLanguage(String idProfile) {
		return profileRepository.findById(idProfile).getLanguage();
	}

	@Override
	public List<Experience> findListExperience(String idProfile) {
		return profileRepository.findById(idProfile).getExperience();
	}

	@Override
	public List<Education> findListEducation(String idProfile) {
		return profileRepository.findById(idProfile).getEducation();
	}

	@Override
	public List<Course> findListCourse(String idProfile) {
		return profileRepository.findById(idProfile).getCourse();
	}

	@Override
	public List<Certificate> findListCertificate(String idProfile) {
		return profileRepository.findById(idProfile).getCertificate();
	}

	@Override
	public Contact findContact(String idProfile) {
		return profileRepository.findById(idProfile).getContact();
	}

	@Override
	public List<Hobby> findListHobby(String idProfile) {
		return profileRepository.findById(idProfile).getHobby();
	}
}