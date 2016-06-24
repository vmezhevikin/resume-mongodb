package net.devstudy.resume.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.devstudy.resume.domain.Certificate;
import net.devstudy.resume.domain.Contact;
import net.devstudy.resume.domain.Course;
import net.devstudy.resume.domain.Education;
import net.devstudy.resume.domain.Experience;
import net.devstudy.resume.domain.Hobby;
import net.devstudy.resume.domain.Language;
import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.domain.ProfileDomain;
import net.devstudy.resume.domain.ProfileRestore;
import net.devstudy.resume.domain.Skill;
import net.devstudy.resume.exception.CantCompleteClientRequestException;
import net.devstudy.resume.form.ChangePasswordForm;
import net.devstudy.resume.form.SignUpForm;
import net.devstudy.resume.model.UploadImageResult;
import net.devstudy.resume.repository.search.ProfileSearchRepository;
import net.devstudy.resume.repository.storage.ProfileRepository;
import net.devstudy.resume.repository.storage.ProfileRestoreRepository;
import net.devstudy.resume.service.EditProfileService;
import net.devstudy.resume.service.ImageProcessorService;
import net.devstudy.resume.service.NotificationManagerService;
import net.devstudy.resume.util.DataUtil;

@Service
public class EditProfileServiceImpl implements EditProfileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EditProfileServiceImpl.class);

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private ProfileSearchRepository profileSearchRepository;
	
	@Autowired
	private ProfileRestoreRepository profileRestoreRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private NotificationManagerService notificationManagerService;

	@Autowired
	private ImageProcessorService imageProcessorService;

	@Value("${generate.uid.alphabet}")
	private String generateUidAlphabet;

	@Value("${generate.uid.suffix.length}")
	private int generateUidSuffixlength;

	@Value("${generate.uid.max.try.count}")
	private int generateUidMaxTryCount;

	@Value("${email.restorelink.address}")
	private String emailRestorelinkAddress;

	@Override
	public Profile createNewProfile(SignUpForm form) {
		LOGGER.debug("Creating new profile");
		Profile profile = new Profile();
		profile.setUid(generateProfileUid(form.getFirstName(), form.getLastName()));
		profile.setFirstName(DataUtil.capitailizeName(form.getFirstName()));
		profile.setLastName(DataUtil.capitailizeName(form.getLastName()));
		profile.setPassword(passwordEncoder.encode(form.getPassword()));
		profile.setActive(false);
		profileRepository.save(profile);
		registerIndexAfterCreateProfile(profile);
		return profile;
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

	@SuppressWarnings("unchecked")
	private void registerIndexAfterCreateProfile(final Profile profile) {
		LOGGER.info("New profile created: {}", profile.getUid());
		profile.setCertificate(Collections.EMPTY_LIST);
		profile.setCourse(Collections.EMPTY_LIST);
		profile.setEducation(Collections.EMPTY_LIST);
		profile.setExperience(Collections.EMPTY_LIST);
		profile.setHobby(Collections.EMPTY_LIST);
		profile.setLanguage(Collections.EMPTY_LIST);
		profile.setSkill(Collections.EMPTY_LIST);
		profileSearchRepository.save(profile);
		LOGGER.info("New profile index created: {}", profile.getUid());
	}

	@Override
	public void updateSkill(String idProfile, List<Skill> editedList) {
		LOGGER.debug("Updating profile skills {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		List<Skill> currentList = profile.getSkill();
		if (CollectionUtils.isEqualCollection(editedList, currentList)) {
			LOGGER.debug("Updating profile skills: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile skills: profile skills have been changed");
			removeEmptyItems(editedList);
			profile.getSkill().clear();
			profile.getSkill().addAll(editedList);
			profileRepository.save(profile);
			updateIndexAfterEditSkill(idProfile, editedList);
		}
	}
	
	private void removeEmptyItems(List<? extends ProfileDomain> list)
	{
		Iterator<? extends ProfileDomain> iterator = list.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().hasNullSubstantionalFields()) {
				iterator.remove();
			}
		}
	}

	@Override
	public void addSkill(String idProfile, Skill newSkill) {
		LOGGER.debug("Updating profile skills, adding: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profile.getSkill().add(newSkill);
		profileRepository.save(profile);
		updateIndexAfterEditSkill(idProfile, profile.getSkill());
	}

	private void updateIndexAfterEditSkill(final String idProfile, final List<Skill> updatedList) {
		LOGGER.info("Profile skills updated: {}", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profile.getSkill().clear();
		profile.getSkill().addAll(updatedList);
		profileSearchRepository.save(profile);
		LOGGER.info("Profile skills index updated: {}", idProfile);
	}

	@Override
	public void updateLanguage(String idProfile, List<Language> editedList) {
		LOGGER.debug("Updating profile languages: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		List<Language> currentList = profile.getLanguage();
		if (CollectionUtils.isEqualCollection(editedList, currentList)) {
			LOGGER.debug("Updating profile languages: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile languages: profile languages have been changed");
			removeEmptyItems(editedList);
			profile.getLanguage().clear();
			profile.getLanguage().addAll(editedList);
			profileRepository.save(profile);
			updateIndexAfterEditLanguage(idProfile, editedList);
		}
	}

	@Override
	public void addLanguage(String idProfile, Language newLanguage) {
		LOGGER.debug("Updating profile languages, adding: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profile.getLanguage().add(newLanguage);
		profileRepository.save(profile);
		updateIndexAfterEditLanguage(idProfile, profile.getLanguage());
	}

	private void updateIndexAfterEditLanguage(final String idProfile, final List<Language> updatedList) {
		LOGGER.info("Profile languages updated: {}", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profile.getLanguage().clear();
		profile.getLanguage().addAll(updatedList);
		profileSearchRepository.save(profile);
		LOGGER.info("Profile languages index updated: {}", idProfile);
	}

	@Override
	public void updateExperience(String idProfile, List<Experience> editedList) {
		LOGGER.debug("Updating profile experience: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		List<Experience> currentList = profile.getExperience();
		if (CollectionUtils.isEqualCollection(editedList, currentList)) {
			LOGGER.debug("Updating profile experience: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile experience: profile experience has been changed");
			removeEmptyItems(editedList);
			profile.getExperience().clear();
			profile.getExperience().addAll(editedList);
			profileRepository.save(profile);
			updateIndexAfterEditExperience(idProfile, editedList);
		}
	}

	@Override
	public void addExperience(String idProfile, Experience newExperience) {
		LOGGER.debug("Updating profile experience, adding: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profile.getExperience().add(newExperience);
		profileRepository.save(profile);
		updateIndexAfterEditExperience(idProfile, profile.getExperience());
	}

	private void updateIndexAfterEditExperience(final String idProfile, final List<Experience> updatedList) {
		LOGGER.info("Profile experience updated: {}", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profile.getExperience().clear();
		profile.getExperience().addAll(updatedList);
		profileSearchRepository.save(profile);
		LOGGER.info("Profile experience index updated: {}", idProfile);
	}

	@Override
	public void updateEducation(String idProfile, List<Education> editedList) {
		LOGGER.debug("Updating profile education");
		Profile profile = profileRepository.findById(idProfile);
		List<Education> currentList = profile.getEducation();
		if (CollectionUtils.isEqualCollection(editedList, currentList)) {
			LOGGER.debug("Updating profile education: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile education: profile education has been changed");
			removeEmptyItems(editedList);
			profile.getEducation().clear();
			profile.getEducation().addAll(editedList);
			profileRepository.save(profile);
			LOGGER.info("Profile education updated: {}", idProfile);
		}
	}

	@Override
	public void addEducation(String idProfile, Education newEducation) {
		LOGGER.debug("Updating profile education, adding: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profile.getEducation().add(newEducation);
		profileRepository.save(profile);
		LOGGER.info("Profile education updated: {}", idProfile);
	}

	@Override
	public void updateCourse(String idProfile, List<Course> editedList) {
		LOGGER.debug("Updating profile courses: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		List<Course> currentList = profile.getCourse();
		if (CollectionUtils.isEqualCollection(editedList, currentList)) {
			LOGGER.debug("Updating profile courses: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile courses: profile courses have been changed");
			removeEmptyItems(editedList);
			profile.getCourse().clear();
			profile.getCourse().addAll(editedList);
			profileRepository.save(profile);
			updateIndexAfterEditCourse(idProfile, editedList);
		}
	}

	@Override
	public void addCourse(String idProfile, Course newCourse) {
		LOGGER.debug("Updating profile courses, adding: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profile.getCourse().add(newCourse);
		profileRepository.save(profile);
		updateIndexAfterEditCourse(idProfile, profile.getCourse());
	}

	private void updateIndexAfterEditCourse(final String idProfile, final List<Course> updatedList) {
		LOGGER.info("Profile courses updated: {}", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profile.getCourse().clear();
		profile.getCourse().addAll(updatedList);
		profileSearchRepository.save(profile);
		LOGGER.info("Profile courses index updated: {}", idProfile);
	}

	@Override
	public void updateCertificate(String idProfile, List<Certificate> editedList) {
		LOGGER.debug("Updating profile certificates: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		List<Certificate> currentList = profile.getCertificate();
		List<String> currentImagesList = getImagesList(currentList);
		if (CollectionUtils.isEqualCollection(editedList, currentList)) {
			LOGGER.debug("Updating profile certificates: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile certificates: profile certificates have been changed, updating certificates");
			removeEmptyItems(editedList);			
			List<String> editedImagesList = getImagesList(editedList);
			profile.getCertificate().clear();
			profile.getCertificate().addAll(editedList);
			profileRepository.save(profile);
			updateIndexAfterEditCertificate(idProfile, editedList);
			removeCertificatesImages(currentImagesList, editedImagesList);
		}
	}

	@Override
	public void addCertificate(String idProfile, Certificate newCertificate) {
		LOGGER.debug("Updating profile certificates, adding: {}", idProfile);
		if (newCertificate.getFile().isEmpty()) {
			LOGGER.debug("Updating profile certificates: no file uploaded");
		}
		else {
			LOGGER.debug("Updating profile certificates: updating");
			UploadImageResult uploadResult = imageProcessorService.processProfileCertificate(newCertificate.getFile());
			Profile profile = profileRepository.findById(idProfile);
			newCertificate.setImg(uploadResult.getLargeImageLink());
			newCertificate.setImgSmall(uploadResult.getSmallImageLink());
			profile.getCertificate().add(newCertificate);
			profileRepository.save(profile);
			updateIndexAfterEditCertificate(idProfile, profile.getCertificate());
		}
	}

	private void updateIndexAfterEditCertificate(final String idProfile, final List<Certificate> updatedList) {
		LOGGER.info("Profile certificates updated: {}", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profile.getCertificate().clear();
		profile.getCertificate().addAll(updatedList);
		profileSearchRepository.save(profile);
		LOGGER.info("Profile certificates index updated: {}", idProfile);
	}

	private List<String> getImagesList(List<Certificate> currentList) {
		List<String> list = new LinkedList<>();
		for (Certificate certificate : currentList) {
			list.add(certificate.getImg());
			list.add(certificate.getImgSmall());
		}
		return list;
	}

	private void removeCertificatesImages(final List<String> currentList, final List<String> editedList) {
		List<String> imagesToRemove = new LinkedList<>();
		imagesToRemove.addAll(currentList);
		imagesToRemove.removeAll(editedList);
		for (String image : imagesToRemove) {
			imageProcessorService.removeProfileCertificate(image);
		}
		LOGGER.info("Profile certificate images have been removed");
	}

	@Override
	public void updateGeneralInfo(String idProfile, Profile editedProfile) {
		LOGGER.debug("Updating profile general: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		boolean profileWasActiveBeforeEdit = profile.getActive();
		if (!profileShouldBeUpdated(profile, editedProfile)) {
			LOGGER.debug("Updating profile general: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile general: profile has been changed");
			checkEmailAddressIsUnique(idProfile, editedProfile.getEmail(), editedProfile.getPhone());
			MultipartFile file = editedProfile.getFile();
			if (!file.isEmpty()) {
				UploadImageResult uploadResult = imageProcessorService.processProfilePhoto(file);
				imageProcessorService.removeProfilePhoto(profile.getPhoto());
				imageProcessorService.removeProfilePhoto(profile.getPhotoSmall());
				profile.setPhoto(uploadResult.getLargeImageLink());
				profile.setPhotoSmall(uploadResult.getSmallImageLink());
			}
			profile.setBirthday(DataUtil.generateDateFromString(editedProfile.getBirthdayString()));
			profile.setCountry(editedProfile.getCountry());
			profile.setCity(editedProfile.getCity());
			profile.setEmail(editedProfile.getEmail());
			profile.setPhone(editedProfile.getPhone());
			profile.setObjective(editedProfile.getObjective());
			profile.setSummary(editedProfile.getSummary());
			profile.setActive(true);
			profileRepository.save(profile);
			updateIndexAfterEditGeneralInfo(idProfile, profile);
			if (!profileWasActiveBeforeEdit) {
				sendProfileActivatedNotification(profile);
			}
		}
	}

	private boolean profileShouldBeUpdated(Profile profile, Profile editedProfile) {
		if (!editedProfile.getFile().isEmpty())
			return true;
		if (!profile.getBirthday().equals(editedProfile.getBirthday()))
			return true;
		if (!profile.getCountry().equals(editedProfile.getCountry()))
			return true;
		if (!profile.getCity().equals(editedProfile.getCity()))
			return true;
		if (!profile.getEmail().equals(editedProfile.getEmail()))
			return true;
		if (!profile.getPhone().equals(editedProfile.getPhone()))
			return true;
		if (!profile.getObjective().equals(editedProfile.getObjective()))
			return true;
		if (!profile.getSummary().equals(editedProfile.getSummary()))
			return true;
		return false;
	}

	private void checkEmailAddressIsUnique(String idProfile, String email, String phone) {
		Profile profile = profileRepository.findByEmail(email);
		if (profile != null) {
			if (!profile.getId().equals(idProfile)) {
				throw new CantCompleteClientRequestException("Profile with email " + email + " already exist. Can't create profile");
			}
		}
		profile = profileRepository.findByPhone(phone);
		if (profile != null) {
			if (!profile.getId().equals(idProfile)) {
				throw new CantCompleteClientRequestException("Profile with phone " + phone + " already exist. Can't create profile");
			}
		}
	}

	private void updateIndexAfterEditGeneralInfo(final String idProfile, final Profile updatedProfile) {
		LOGGER.info("Profile general updated: {}", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profileSearchRepository.delete(profile);
		profileSearchRepository.save(updatedProfile);
		LOGGER.info("Profile general index updated: {}", idProfile);
	}

	private void sendProfileActivatedNotification(final Profile profile) {
		LOGGER.info("Profile {} has been activated", profile.getId());
		notificationManagerService.sendProfileActive(profile);
	}

	@Override
	public void updateAdditionalInfo(String idProfile, Profile editedProfile) {
		LOGGER.debug("Updating profile additional: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		makeEmptyAdditionalNulls(editedProfile);
		if (!profileShouldBeUpdated(profile.getAdditionalInfo(), editedProfile.getAdditionalInfo())) {
			LOGGER.debug("Updating profile additional: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile additional: profile has been changed");
			profile.setAdditionalInfo(editedProfile.getAdditionalInfo());
			profileRepository.save(profile);
		}
		LOGGER.info("Profile additional updated: {}", idProfile);
	}

	private void makeEmptyAdditionalNulls(Profile form) {
		if (form != null) {
			if (StringUtils.isBlank(form.getAdditionalInfo())) {
				form.setAdditionalInfo(null);
			}
		}
	}

	private boolean profileShouldBeUpdated(String currentAdditionalInfo, String editedAdditionalInfo) {
		if (currentAdditionalInfo == null && editedAdditionalInfo == null) {
			return false;
		}
		else if (currentAdditionalInfo == null || editedAdditionalInfo == null) {
			return true;
		}
		else {
			return !currentAdditionalInfo.equals(editedAdditionalInfo);
		}
	}

	@Override
	public void updateContact(String idProfile, Contact newContact) {
		LOGGER.debug("Updating profile contacts: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		Contact contact = profile.getContact();
		makeEmptyFieldsNulls(newContact);
		if (!profileShouldBeUpdated(contact, newContact)) {
			LOGGER.debug("Updating profile contacts: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile contacts: contacts have been changed");
			profile.setContact(newContact);
			profileRepository.save(profile);
		}
		LOGGER.info("Profile contacts updated: {}", idProfile);
	}

	private void makeEmptyFieldsNulls(Contact form) {
		if (form != null) {
			if (StringUtils.isBlank(form.getSkype())) {
				form.setSkype(null);
			}
			if (StringUtils.isBlank(form.getVkontakte())) {
				form.setVkontakte(null);
			}
			if (StringUtils.isBlank(form.getFacebook())) {
				form.setFacebook(null);
			}
			if (StringUtils.isBlank(form.getLinkedin())) {
				form.setLinkedin(null);
			}
			if (StringUtils.isBlank(form.getGithub())) {
				form.setGithub(null);
			}
			if (StringUtils.isBlank(form.getStackoverflow())) {
				form.setStackoverflow(null);
			}
		}
	}

	private boolean profileShouldBeUpdated(Contact currentContact, Contact editedContact) {
		if (currentContact == null && editedContact == null) {
			return false;
		}
		else if (currentContact == null || editedContact == null) {
			return true;
		}
		else {
			return !currentContact.equals(editedContact);
		}
	}

	@Override
	public void updateHobby(String idProfile, List<String> editedList) {
		LOGGER.debug("Updating profile hobbies: {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		List<Hobby> currentList = profile.getHobby();
		if (!profileShouldBeUpdated(currentList, editedList)) {
			LOGGER.debug("Updating profile hobbies: nothing to update");
		}
		else {
			LOGGER.debug("Updating profile hobbies: profile hobbies have been changed");
			List<Hobby> listHobby = updateListOfHobbiesFromSource(currentList, editedList);
			profile.getHobby().clear();
			profile.getHobby().addAll(listHobby);
			profileRepository.save(profile);
		}
		LOGGER.info("Profile hobbies updated: {}", idProfile);
	}

	private boolean profileShouldBeUpdated(List<Hobby> currentList, List<String> editedList) {
		if (currentList.size() == 0 && editedList == null) {
			return false;
		}
		if (currentList.size() != 0 && editedList == null) {
			return true;
		}
		if (currentList.size() != editedList.size()) {
			return true;
		}
		for (Hobby hobby : currentList) {
			if (!editedList.contains(hobby.getDescription())) {
				return true;
			}
		}
		return false;
	}

	private List<Hobby> updateListOfHobbiesFromSource(List<Hobby> currentList, List<String> listFromForm) {
		List<Hobby> listHobby = new ArrayList<>();
		for (String hoobyDes : listFromForm) {
			boolean addedFromCurrentList = false;
			for (Hobby hobbyFromCurrent : currentList) {
				if (hoobyDes.equals(hobbyFromCurrent.getDescription())) {
					addedFromCurrentList = listHobby.add(hobbyFromCurrent);
				}
			}				
			if (!addedFromCurrentList) {
				Hobby hoobyNew = new Hobby();
				hoobyNew.setDescription(hoobyDes);
				listHobby.add(hoobyNew);
			}
		}
		return listHobby;
	}

	@Override
	public void removeProfile(String idProfile) {
		LOGGER.debug("Removing profile {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profileRepository.delete(profile);
		removeProfileImages(profile);
		updateIndexAfterRemoveProfile(idProfile);
	}

	private void removeProfileImages(final Profile profile) {
		imageProcessorService.removeProfilePhoto(profile.getPhoto());
		imageProcessorService.removeProfilePhoto(profile.getPhotoSmall());
		for (Certificate certificate : profile.getCertificate()) {
			imageProcessorService.removeProfileCertificate(certificate.getImg());
			imageProcessorService.removeProfileCertificate(certificate.getImgSmall());
		}
		LOGGER.info("Profile images have been removed");
	}

	private void updateIndexAfterRemoveProfile(final String idProfile) {
		LOGGER.info("Profile removed: {}", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profileSearchRepository.delete(profile);
		LOGGER.info("Profile index removed: {}", idProfile);
	}

	/*@Override
	public void removeCourse(String idCourse) {
		LOGGER.debug("Removing course {}", idCourse);
		Course removingCourse = courseRepository.findOne(idCourse);
		Profile profile = removingCourse.getProfile();
		profile.getCourse().remove(removingCourse);
		profileRepository.save(profile);
		updateIndexAfterEditCourse(profile.getId(), profile.getCourse());
	}

	@Override
	public void removeEducation(long idEducation) {
		LOGGER.debug("Removing education {}", idEducation);
		Education removingEducation = educationRepository.findOne(idEducation);
		Profile profile = removingEducation.getProfile();
		profile.getEducation().remove(removingEducation);
		profileRepository.save(profile);
		LOGGER.info("Education {} removed", idEducation);
	}

	@Override
	public void removeExperience(long idExperience) {
		LOGGER.debug("Removing experience {}", idExperience);
		Experience removingExperience = experienceRepository.findOne(idExperience);
		Profile profile = removingExperience.getProfile();
		profile.getExperience().remove(removingExperience);
		profileRepository.save(profile);
		updateIndexAfterEditExperience(profile.getId(), profile.getExperience());
	}*/

	@Override
	public void addRestoreToken(String idProfile, String token) {
		LOGGER.debug("Creating restore token for profile {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		ProfileRestore restore = new ProfileRestore();
		restore.setId(profile.getId());
		restore.setProfile(profile);
		restore.setToken(token);
		profileRestoreRepository.save(restore);
		sendRestoreLinkNotification(profile, emailRestorelinkAddress + token);
	}

	private void sendRestoreLinkNotification(final Profile profile, final String restoreLink) {
		LOGGER.info("Restore link for profile {} has been created", profile.getId());
		notificationManagerService.sendRestoreAccessLink(profile, restoreLink);
	}

	@Override
	public void removeRestoreToken(String idProfile) {
		LOGGER.debug("Removing restore token for profile {}", idProfile);
		ProfileRestore restore = profileRestoreRepository.findByProfileId(idProfile);
		profileRestoreRepository.delete(restore);
	}

	@Override
	public void updatePassword(String idProfile, ChangePasswordForm form) {
		LOGGER.debug("Updating password for profile {}", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profile.setPassword(passwordEncoder.encode(form.getPassword()));
		profileRepository.save(profile);
		sendPasswordChangedNotification(profile);
	}

	private void sendPasswordChangedNotification(final Profile profile) {
		LOGGER.info("Password for profile {} has been changed", profile.getId());
		notificationManagerService.sendPasswordChanged(profile);
	}
}