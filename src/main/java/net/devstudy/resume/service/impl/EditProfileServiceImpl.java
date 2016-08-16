package net.devstudy.resume.service.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.devstudy.resume.domain.Certificate;
import net.devstudy.resume.domain.Contact;
import net.devstudy.resume.domain.Course;
import net.devstudy.resume.domain.Education;
import net.devstudy.resume.domain.Experience;
import net.devstudy.resume.domain.Hobby;
import net.devstudy.resume.domain.Language;
import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.domain.ProfileCollectionField;
import net.devstudy.resume.domain.ProfileConfirmEmail;
import net.devstudy.resume.domain.ProfileConfirmRegistration;
import net.devstudy.resume.domain.ProfileRestore;
import net.devstudy.resume.domain.Skill;
import net.devstudy.resume.exception.CantCompleteClientRequestException;
import net.devstudy.resume.form.EmailForm;
import net.devstudy.resume.form.PasswordForm;
import net.devstudy.resume.form.SignUpForm;
import net.devstudy.resume.form.UidForm;
import net.devstudy.resume.model.UploadImageResult;
import net.devstudy.resume.repository.search.ProfileSearchRepository;
import net.devstudy.resume.repository.storage.ProfileConfirmEmailRepository;
import net.devstudy.resume.repository.storage.ProfileConfirmRegistrationRepository;
import net.devstudy.resume.repository.storage.ProfileRepository;
import net.devstudy.resume.repository.storage.ProfileRestoreRepository;
import net.devstudy.resume.service.EditProfileService;
import net.devstudy.resume.service.ImageProcessorService;
import net.devstudy.resume.service.NotificationManagerService;
import net.devstudy.resume.util.DataUtil;
import net.devstudy.resume.util.ProfileDataUtil;
import net.devstudy.resume.util.SecurityUtil;
import net.devstudy.resume.util.UpdateLogicUtil;

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
	private ProfileConfirmRegistrationRepository profileRegistrationRepository;
	
	@Autowired
	private ProfileConfirmEmailRepository profileConfirmEmailRepository;

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

	@Override
	public Profile createNewNotActiveProfile(SignUpForm form) {
		LOGGER.info("Profile: creating new not active profile");
		Profile profile = new Profile();
		synchronized (this) {
			checkEmailIsUnique(form.getEmail());
			profile.setEmail(form.getEmail());
			profile.setPassword(passwordEncoder.encode(form.getPassword()));
			profile.setCreated(DataUtil.getCurrentDate());
			profile.setActive(false);
			profileRepository.save(profile);
		}
		return profile;
	}

	@Override
	public void addConfirmRegistrtionToken(String idProfile, String token) {
		LOGGER.info("Profile {}: creating confirm registration token", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		ProfileConfirmRegistration registration = new ProfileConfirmRegistration();
		registration.setId(profile.getId());
		registration.setProfile(profile);
		registration.setCreated(DataUtil.getCurrentDate());
		registration.setToken(token);
		profileRegistrationRepository.save(registration);
		sendConfirmRegistrationLinkNotification(profile, token);
	}

	private void sendConfirmRegistrationLinkNotification(final Profile profile, final String token) {
		notificationManagerService.sendConfirmRegistrationLink(profile, token);
		LOGGER.info("Profile {}: confirm registration token " + token + " has been send", profile.getId(), token);
	}

	@Override
	public void removeConfirmRegistrtionToken(String idProfile) {
		LOGGER.info("Profile {}: removing confirm registration token", idProfile);
		ProfileConfirmRegistration registration = profileRegistrationRepository.findByProfileId(idProfile);
		profileRegistrationRepository.delete(registration);
	}

	@Override
	public Profile activateProfile(Profile profile) {
		profile.setUid(generateProfileUid(profile.getId()));
		profile.setActive(true);
		ProfileDataUtil.setAllProfileCollectionsAsEmty(profile);
		profileRepository.save(profile);
		registerIndexAfterActivateProfile(profile);
		return profile;
	}

	private String generateProfileUid(String idProfile) {
		String uid = SecurityUtil.generateProfileUid();
		for (int i = 0; profileRepository.countByUid(uid) > 0; i++) {
			uid = SecurityUtil.generateProfileUid();
			if (i >= generateUidMaxTryCount) {
				throw new CantCompleteClientRequestException("Can't generate unique uid for profile: " + idProfile + ": maxTryCountToGenerateUid detected");
			}
		}
		return uid;
	}

	private void registerIndexAfterActivateProfile(final Profile profile) {
		LOGGER.info("Profile {}: has been activated", profile.getUid());
		ProfileDataUtil.setAllProfileCollectionsAsEmty(profile);
		profileSearchRepository.save(profile);
		LOGGER.info("Profile {}: index has been created", profile.getUid());
	}

	@Override
	public void updateLastVisitDate(String idProfile) {
		LOGGER.info("Profile {}: updating last visit", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profile.setLastVisit(DataUtil.getCurrentDate());
		profileRepository.save(profile);
		LOGGER.info("Profile {}: last visit has been updated", idProfile);
	}
	
	@Override
	public void updateSkill(String idProfile, List<Skill> editedList) {
		updateProfileCollection(idProfile, Skill.class, editedList);
	}
	
	@Override
	public void updateLanguage(String idProfile, List<Language> editedList) {
		updateProfileCollection(idProfile, Language.class, editedList);
	}
	
	@Override
	public void updateExperience(String idProfile, List<Experience> editedList) {
		updateProfileCollection(idProfile, Experience.class, editedList);
	}
	
	@Override
	public void updateCourse(String idProfile, List<Course> editedList) {
		updateProfileCollection(idProfile, Course.class, editedList);
	}
	
	@Override
	public void updateEducation(String idProfile, List<Education> editedList) {
		updateProfileCollection(idProfile, Education.class, editedList);
	}
	
	@Override
	public void updateCertificate(String idProfile, List<Certificate> editedList) {
		updateProfileCollection(idProfile, Certificate.class, editedList);
	}

	@Override
	public void updateHobby(String idProfile, List<Hobby> editedList) {
		updateProfileCollection(idProfile, Hobby.class, editedList);
	}
	
	@SuppressWarnings("unchecked")
	private <E extends ProfileCollectionField> void updateProfileCollection(String idProfile, Class<E> clazz, List<E> editedList) {
		LOGGER.info("Profile {}: updating {} collection", idProfile, clazz.getSimpleName());
		Profile profile = profileRepository.findById(idProfile);
		List<E> currentList = ProfileDataUtil.getProfileCollection(profile, clazz);
		List<String> currentImagesList = null, editedImagesList = null;
		if (UpdateLogicUtil.profileCollectionChanged(editedList, currentList)) {
			LOGGER.info("Profile {}: profile {} collection has been changed", idProfile, clazz.getSimpleName());	
			if ("Certificate".equals(clazz.getSimpleName())) {
				currentImagesList = getImagesList((List<Certificate>) currentList);
				editedImagesList = getImagesList((List<Certificate>) editedList);
			}
			sortCollection(editedList);
			currentList.clear();
			currentList.addAll(editedList);
			profileRepository.save(profile);
			if (!("Education".equals(clazz.getSimpleName()) || "Hobby".equals(clazz.getSimpleName()))) {
				updateIndexAfterEditCollection(idProfile, clazz, editedList);
			}
			if ("Certificate".equals(clazz.getSimpleName())) {
				LOGGER.info("Profile {}: old certificate images removing", idProfile);
				removeCertificatesImages(currentImagesList, editedImagesList);
			}
		} else {
			LOGGER.info("Updating profile {}: nothing to update", idProfile);
		}
	}
	
	private <E extends ProfileCollectionField> void updateIndexAfterEditCollection(String idProfile, Class<E> clazz, List<E> editedList) {
		LOGGER.info("Profile {}: profile {} collection has been updated", idProfile, clazz.getSimpleName());
		Profile profile = profileSearchRepository.findOne(idProfile);
		List<E> currentList = ProfileDataUtil.getProfileCollection(profile, clazz);
		currentList.clear();
		currentList.addAll(editedList);
		profileSearchRepository.save(profile);
		LOGGER.info("Profile {}: index has been updated", idProfile);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <E extends ProfileCollectionField> void sortCollection(List<E> editedList) {
		Collections.sort((List<? extends Comparable>) editedList);
	}
	
	@Override
	public void addCertificate(String idProfile, Certificate newCertificate) {
		LOGGER.info("Profile {}: adding new certificate", idProfile);
		UploadImageResult uploadResult = imageProcessorService.processProfileCertificate(newCertificate.getFile());
		Profile profile = profileRepository.findById(idProfile);
		newCertificate.setImg(uploadResult.getLargeImageLink());
		newCertificate.setImgSmall(uploadResult.getSmallImageLink());
		List<Certificate> editedList = profile.getCertificate();
		editedList.add(newCertificate);
		sortCollection(editedList);
		profileRepository.save(profile);
		updateIndexAfterEditCollection(idProfile, Certificate.class, profile.getCertificate());
	}

	private List<String> getImagesList(List<Certificate> certificateList) {
		List<String> imagesList = new LinkedList<>();
		for (Certificate certificate : certificateList) {
			imagesList.add(certificate.getImg());
			imagesList.add(certificate.getImgSmall());
		}
		return imagesList;
	}

	private void removeCertificatesImages(final List<String> currentList, final List<String> editedList) {
		List<String> imagesToRemove = new LinkedList<>();
		imagesToRemove.addAll(currentList);
		imagesToRemove.removeAll(editedList);
		for (String image : imagesToRemove) {
			imageProcessorService.removeProfileCertificate(image);
		}
	}

	@Override
	public void updateGeneralInfo(String idProfile, Profile editedProfile) {
		LOGGER.info("Profile {}; updating general info", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		if (UpdateLogicUtil.profileGeneralInfoChanged(profile, editedProfile)) {
			LOGGER.info("Profile {}: general info has been changed", idProfile);
			synchronized (this) {
				checkEmailIsUnique(idProfile, editedProfile.getEmail());
				checkPhoneIsUnique(idProfile, editedProfile.getPhone());
				ProfileDataUtil.copyGeneralFields(profile, editedProfile);
				if (StringUtils.isEmpty(editedProfile.getBirthdayString())) {
					profile.setBirthday(null);
				} else {
					profile.setBirthday(DataUtil.generateDateFromString(editedProfile.getBirthdayString()));
				}
				if (!editedProfile.getFile().isEmpty()) {
					UploadImageResult uploadResult = imageProcessorService.processProfilePhoto(editedProfile.getFile());
					imageProcessorService.removeProfilePhoto(profile.getPhoto());
					imageProcessorService.removeProfilePhoto(profile.getPhotoSmall());
					profile.setPhoto(uploadResult.getLargeImageLink());
					profile.setPhotoSmall(uploadResult.getSmallImageLink());
				}
				profileRepository.save(profile);
				updateIndexAfterEditGeneralInfo(idProfile, profile);
			}
		}
		else {
			LOGGER.info("Profile {}: nothing to update", idProfile);
		}
	}

	private void updateIndexAfterEditGeneralInfo(final String idProfile, final Profile updatedProfile) {
		LOGGER.info("Profile {}: general info has been updated", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profileSearchRepository.delete(profile);
		profileSearchRepository.save(updatedProfile);
		LOGGER.info("Profile {}: index has been updated", idProfile);
	}

	@Override
	public void updateAdditionalInfo(String idProfile, Profile editedProfile) {
		LOGGER.info("Profile {}: updating additional info", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		if (UpdateLogicUtil.profileAdditionalInfoChanged(profile, editedProfile)) {
			LOGGER.info("Updating profile additional: profile has been changed");
			profile.setAdditionalInfo(editedProfile.getAdditionalInfo());
			profileRepository.save(profile);
		}
		else {
			LOGGER.info("Profile {}: nothing to update");
		}
		LOGGER.info("Profile {}: additional info has been updated", idProfile);
	}

	@Override
	public void updateContact(String idProfile, Contact editedContact) {
		LOGGER.info("Profile {}: updating contact info", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		Contact contact = profile.getContact();
		if (UpdateLogicUtil.profileContactChanged(contact, editedContact)) {
			LOGGER.info("Profile {}: contact info has been changed");
			profile.setContact(editedContact);
			profileRepository.save(profile);
		}
		else {
			LOGGER.info("Profile {}: nothing to update");
		}
		LOGGER.info("Profile {}: contact info has been updated", idProfile);
	}

	@Override
	public void removeProfile(String idProfile) {
		LOGGER.info("Profile {}: removing", idProfile);
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
		LOGGER.info("Profile {}: images have been removed", profile.getId());
	}

	private void updateIndexAfterRemoveProfile(final String idProfile) {
		LOGGER.info("Profile {}: has been removed", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profileSearchRepository.delete(profile);
		LOGGER.info("Profile {}: index has been removed", idProfile);
	}

	@Override
	public void addRestoreToken(String idProfile, String token) {
		LOGGER.info("Profile {}: creating restore token", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		ProfileRestore restore = new ProfileRestore();
		restore.setId(profile.getId());
		restore.setProfile(profile);
		restore.setCreated(DataUtil.getCurrentDate());
		restore.setToken(token);
		profileRestoreRepository.save(restore);
		LOGGER.info("Profile {}: restore token " + token + " has been created", profile.getId(), token);
		sendRestoreLinkNotification(profile, token);
	}

	private void sendRestoreLinkNotification(final Profile profile, final String token) {
		notificationManagerService.sendRestoreAccessLink(profile, token);
		LOGGER.info("Profile {}: restore token " + token + " has been send", profile.getId(), token);
	}

	@Override
	public void removeRestoreToken(String idProfile) {
		LOGGER.info("Profile {}: removing restore token", idProfile);
		ProfileRestore restore = profileRestoreRepository.findByProfileId(idProfile);
		profileRestoreRepository.delete(restore);
		LOGGER.info("Profile {}: restore token removed", idProfile);
	}

	@Override
	public void updatePassword(String idProfile, PasswordForm form) {
		LOGGER.info("Profile {}: updating password", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profile.setPassword(passwordEncoder.encode(form.getPassword()));
		profileRepository.save(profile);
		LOGGER.info("Profile {}: password has been changed", profile.getId());
		sendPasswordChangedNotification(profile);
	}

	private void sendPasswordChangedNotification(final Profile profile) {
		notificationManagerService.sendPasswordChanged(profile);
		LOGGER.info("Profile {}: password changed notification has been send", profile.getId());
	}

	@Override
	public void addConfirmEmailToken(String idProfile, String token, EmailForm form) {
		LOGGER.info("Profile {}: creating restore token", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		ProfileConfirmEmail confirm = new ProfileConfirmEmail();
		confirm.setId(profile.getId());
		confirm.setProfile(profile);
		confirm.setCreated(DataUtil.getCurrentDate());
		confirm.setToken(token);
		confirm.setEmail(form.getEmail());
		profileConfirmEmailRepository.save(confirm);
		LOGGER.info("Profile {}: confirm email token " + token + " has been created", profile.getId(), token);
		sendEmailConfirmNotification(profile, token, form.getEmail());
	}

	private void sendEmailConfirmNotification(final Profile profile, String token, String email) {
		notificationManagerService.sendConfirmEmailLink(profile, token, email);
		LOGGER.info("Profile {}: confirm email token " + token + " has been send", profile.getId(), token);
	}

	@Override
	public String removeConfirmEmailToken(String idProfile) {
		LOGGER.info("Profile {}: removing confirm email token", idProfile);
		ProfileConfirmEmail confirm = profileConfirmEmailRepository.findByProfileId(idProfile);
		String confirmedEmail = confirm.getEmail();
		profileConfirmEmailRepository.delete(confirm);
		LOGGER.info("Profile {}: confirm email token removed", idProfile);
		return confirmedEmail;
	}

	@Override
	public void updateEmail(String idProfile, String email) {
		LOGGER.info("Profile {}: updating email", idProfile);
		synchronized (this) {
			checkEmailIsUnique(idProfile, email);
			Profile profile = profileRepository.findById(idProfile);
			profile.setEmail(email);
			profileRepository.save(profile);
			LOGGER.info("Profile {}: email has been changed", profile.getId());
		}
	}

	@Override
	public void updateUid(String idProfile, UidForm form) {
		LOGGER.info("Profile {}: updating uid", idProfile);
		synchronized (this) {
			checkUidIsUnique(idProfile, form.getUid());
			Profile profile = profileRepository.findById(idProfile);
			profile.setUid(form.getUid());
			profileRepository.save(profile);
			LOGGER.info("Profile {}: uid has been changed", profile.getId());
		}
	}
	
	private void checkEmailIsUnique(String email) {
		if (profileRepository.countByEmail(email) != 0) {
			throw new CantCompleteClientRequestException("Profile with email " + email + " already exist. Can't create profile");
		}
	}

	private void checkEmailIsUnique(String idProfile, String email) {
		Profile profile = profileRepository.findByEmail(email);
		if (profile != null) {
			if (!idProfile.equals(profile.getId())) {
				LOGGER.error("Profile with email " + email + " already exist. Can't update profile");
				throw new CantCompleteClientRequestException("Profile with email " + email + " already exist. Can't update profile");
			}
		}
	}
		
	private void checkPhoneIsUnique(String idProfile, String phone) {
		Profile profile = profileRepository.findByPhone(phone);
		if (profile != null) {
			if (!idProfile.equals(profile.getId())) {
				LOGGER.error("Profile with phone " + phone + " already exist. Can't update profile");
				throw new CantCompleteClientRequestException("Profile with phone " + phone + " already exist. Can't update profile");
			}
		}
	}
	
	private void checkUidIsUnique(String idProfile, String uid) {
		Profile profile = profileRepository.findByUid(uid);
		if (profile != null) {
			if (!idProfile.equals(profile.getId())) {
				LOGGER.error("Profile with uid " + uid + " already exist. Can't update profile");
				throw new CantCompleteClientRequestException("Profile with uid " + uid + " already exist. Can't update profile");
			}
		}
	}
}