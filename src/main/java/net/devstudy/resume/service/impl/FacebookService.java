package net.devstudy.resume.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restfb.types.User;

import net.devstudy.resume.component.TranslitConverter;
import net.devstudy.resume.domain.Education;
import net.devstudy.resume.domain.Experience;
import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.exception.CantCompleteClientRequestException;
import net.devstudy.resume.repository.search.ProfileSearchRepository;
import net.devstudy.resume.repository.storage.ProfileRepository;
import net.devstudy.resume.service.SocialService;
import net.devstudy.resume.util.DataUtil;
import net.devstudy.resume.util.SecurityUtil;

@Service
public class FacebookService implements SocialService<User> {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacebookService.class);

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private ProfileSearchRepository profileSearchRepository;

	@Autowired
	private TranslitConverter translitConverter;

	@Value("${generate.uid.alphabet}")
	private String generateUidAlphabet;

	@Value("${generate.uid.suffix.length}")
	private int generateUidSuffixlength;

	@Value("${generate.uid.max.try.count}")
	private int generateUidMaxTryCount;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Profile findProfileViaSocailNetwork(User model) {
		String email = model.getEmail();
		if (StringUtils.isNotBlank(email)) {
			return profileRepository.findByEmail(email);
		}
		else {
			return null;
		}
	}

	@Override
	public Profile createNewProfileViaSocailNetwork(User user) {
		LOGGER.info("Creating new profile via Facebook");

		checkEmailAddressIsUnique(user.getEmail());

		Profile profile = new Profile();
		String firstName = translitConverter.translit(user.getFirstName().replace(" ", ""));
		String lastName = translitConverter.translit(user.getFirstName().replace(" ", ""));
		profile.setUid(generateProfileUid(firstName, lastName));
		profile.setFirstName(DataUtil.capitailizeName(firstName));
		profile.setLastName(DataUtil.capitailizeName(lastName));
		profile.setPassword(passwordEncoder.encode(SecurityUtil.generatePassword()));
		profile.setActive(false);
		setCountryAndCityFromUser(profile, user);
		profile.setBirthday(user.getBirthdayAsDate());
		profile.setEmail(user.getEmail());
		profile.setAdditionalInfo(user.getRelationshipStatus());
		setEducationsFromUser(profile, user);
		setExperienceFromUser(profile, user);
		profileRepository.save(profile);
		registerIndexAfterCreateProfileViaFacebook(profile);
		return profile;
	}

	private void checkEmailAddressIsUnique(String email) {
		if (StringUtils.isBlank(email)) {
			throw new CantCompleteClientRequestException("Email is blank. Can't create user via Facebook");
		}
		else {
			if (profileRepository.countByEmail(email) != 0) {
				throw new CantCompleteClientRequestException("Profile with email " + email + " already exist. Can't create profile via Facebook");
			}
		}
	}

	private String generateProfileUid(String firstName, String lastName) {
		String baseUid = DataUtil.generateProfileUid(firstName, lastName);
		String uid = baseUid;
		for (int i = 0; profileRepository.countByUid(uid) > 0; i++) {
			uid = DataUtil.regenerateUidWithRandomSuffix(baseUid, generateUidAlphabet, generateUidSuffixlength);
			if (i >= generateUidMaxTryCount) {
				throw new CantCompleteClientRequestException("Can't generate unique uid for profile: " + baseUid + ": maxTryCountToGenerateUid detected");
			}
		}
		return uid;
	}

	private void setCountryAndCityFromUser(Profile profile, User user) {
		if (user.getHometown() != null) {
			String[] location = user.getHometown().getName().split(",");
			profile.setCountry(location[1].trim());
			profile.setCity(location[0].trim());
		}
	}

	private void setEducationsFromUser(Profile profile, User user) {
		List<Education> educationsFromFacebook = new ArrayList<>();
		for (com.restfb.types.User.Education educationFacebook : user.getEducation()) {
			Education education = createEducationFromFacebook(educationFacebook);
			educationsFromFacebook.add(education);
		}
		if (!educationsFromFacebook.isEmpty()) {
			profile.setEducation(educationsFromFacebook);
		}
	}

	private void setExperienceFromUser(Profile profile, User user) {
		List<Experience> worksFromFacebook = new ArrayList<>();
		for (com.restfb.types.User.Work workFacebook : user.getWork()) {
			Experience experience = createExperienceFromFacebook(workFacebook);
			worksFromFacebook.add(experience);
		}
		if (!worksFromFacebook.isEmpty()) {
			profile.setExperience(worksFromFacebook);
		}
	}

	private Education createEducationFromFacebook(com.restfb.types.User.Education educationFacebook) {
		Education education = new Education();
		education.setDepartment("Department");

		if (educationFacebook.getSchool() != null) {
			education.setUniversity(educationFacebook.getSchool().getName());
		}
		else {
			education.setUniversity("University");
		}

		if (educationFacebook.getDegree() != null) {
			education.setSpeciality(educationFacebook.getDegree().getName());
		}
		else {
			education.setSpeciality("Speciality");
		}

		if (educationFacebook.getYear() != null) {
			education.setStartingYear(Integer.parseInt(educationFacebook.getYear().getName()));
			education.setCompletionYear(Integer.parseInt(educationFacebook.getYear().getName()));
		} else {
			education.setStartingYear(DataUtil.getCurrentYear());
			education.setCompletionYear(DataUtil.getCurrentYear());
		}

		return education;
	}

	private Experience createExperienceFromFacebook(com.restfb.types.User.Work workFacebook) {
		Experience experience = new Experience();

		if (workFacebook.getEmployer() != null) {
			experience.setCompany(workFacebook.getEmployer().getName());
		}
		else {
			experience.setCompany("Company");
		}

		if (workFacebook.getStartDate() != null) {
			experience.setStartingDate(workFacebook.getStartDate());
		}
		else {
			experience.setStartingDate(new LocalDate().toDate());
		}

		if (workFacebook.getEndDate() != null) {
			experience.setCompletionDate(workFacebook.getEndDate());
		}
		else {
			experience.setCompletionDate(new LocalDate().toDate());
		}

		if (workFacebook.getPosition() != null) {
			experience.setPosition(workFacebook.getPosition().getName());
		}
		else {
			experience.setPosition("Position");
		}

		if (workFacebook.getDescription() != null) {
			experience.setResponsibility(workFacebook.getDescription());
		}
		else {
			experience.setResponsibility("Responsibilities");
		}

		return experience;
	}

	@SuppressWarnings("unchecked")
	private void registerIndexAfterCreateProfileViaFacebook(final Profile profile) {
		LOGGER.info("New profile via Facebook created: {}", profile.getUid());
		profile.setCertificate(Collections.EMPTY_LIST);
		profile.setCourse(Collections.EMPTY_LIST);
		if (profile.getEducation() == null) {
			profile.setEducation(Collections.EMPTY_LIST);
		}
		if (profile.getExperience() == null) {
			profile.setExperience(Collections.EMPTY_LIST);
		}
		profile.setHobby(Collections.EMPTY_LIST);
		profile.setLanguage(Collections.EMPTY_LIST);
		profile.setSkill(Collections.EMPTY_LIST);
		profileSearchRepository.save(profile);
		LOGGER.info("New profile index created: {}", profile.getUid());
	}
}