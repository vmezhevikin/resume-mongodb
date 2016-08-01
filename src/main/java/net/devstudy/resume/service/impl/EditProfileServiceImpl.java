package net.devstudy.resume.service.impl;

import java.util.LinkedList;
import java.util.List;

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
import net.devstudy.resume.util.ProfileDataUtil;
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
		LOGGER.debug("Profile: creating new profile");
		Profile profile = new Profile();
		profile.setUid(generateProfileUid(form.getFirstName(), form.getLastName()));
		profile.setFirstName(DataUtil.capitailizeName(form.getFirstName()));
		profile.setLastName(DataUtil.capitailizeName(form.getLastName()));
		profile.setPassword(passwordEncoder.encode(form.getPassword()));
		profile.setActive(false);
		ProfileDataUtil.setAllProfileCollectionsAsEmty(profile);
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

	private void registerIndexAfterCreateProfile(final Profile profile) {
		LOGGER.info("Profile {}: has been created", profile.getUid());
		ProfileDataUtil.setAllProfileCollectionsAsEmty(profile);
		profileSearchRepository.save(profile);
		LOGGER.info("Profile {}: index has been created", profile.getUid());
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
		LOGGER.debug("Profile {}: updating {} collection", idProfile, clazz.getSimpleName());
		Profile profile = profileRepository.findById(idProfile);
		List<E> currentList = ProfileDataUtil.getProfileCollection(profile, clazz);
		List<String> currentImagesList = null, editedImagesList = null;
		if (UpdateLogicUtil.profileCollectionChanged(editedList, currentList)) {
			LOGGER.debug("Profile {}: profile {} collection have been changed", idProfile, clazz.getSimpleName());	
			if ("Certificate".equals(clazz.getSimpleName())) {
				currentImagesList = getImagesList((List<Certificate>) currentList);
				editedImagesList = getImagesList((List<Certificate>) editedList);
			}
			currentList.clear();
			currentList.addAll(editedList);
			profileRepository.save(profile);
			if (!("Education".equals(clazz.getSimpleName()) || "Hobby".equals(clazz.getSimpleName()))) {
				updateIndexAfterEditCollection(idProfile, clazz, editedList);
			}
			if ("Certificate".equals(clazz.getSimpleName())) {
				LOGGER.debug("Profile {}: old certificate images removing", idProfile);
				removeCertificatesImages(currentImagesList, editedImagesList);
			}
		} else {
			LOGGER.debug("Updating profile {}: nothing to update", idProfile);
		}
	}
	
	private <E extends ProfileCollectionField> void updateIndexAfterEditCollection(String idProfile, Class<E> clazz, List<E> editedList) {
		LOGGER.info("Profile {}: profile {} collection have been updated", idProfile, clazz.getSimpleName());
		Profile profile = profileSearchRepository.findOne(idProfile);
		List<E> currentList = ProfileDataUtil.getProfileCollection(profile, clazz);
		currentList.clear();
		currentList.addAll(editedList);
		profileSearchRepository.save(profile);
		LOGGER.info("Profile {}: index has been updated", idProfile);
	}
	
	@Override
	public void addCertificate(String idProfile, Certificate newCertificate) {
		LOGGER.debug("Profile {}: adding new certificate", idProfile);
		UploadImageResult uploadResult = imageProcessorService.processProfileCertificate(newCertificate.getFile());
		Profile profile = profileRepository.findById(idProfile);
		newCertificate.setImg(uploadResult.getLargeImageLink());
		newCertificate.setImgSmall(uploadResult.getSmallImageLink());
		profile.getCertificate().add(newCertificate);
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
		LOGGER.debug("Profile {}; updating general info", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		if (UpdateLogicUtil.profileGeneralInfoChanged(profile, editedProfile)) {
			LOGGER.debug("Profile {}: general info has been changed", idProfile);
			synchronized (this) {
				checkEmailAddressIsUnique(idProfile, editedProfile.getEmail(), editedProfile.getPhone());
				ProfileDataUtil.copyGeneralFields(profile, editedProfile);
				profile.setBirthday(DataUtil.generateDateFromString(editedProfile.getBirthdayString()));
				boolean profileWasActiveBeforeEdit = profile.getActive();
				profile.setActive(true);
				if (!editedProfile.getFile().isEmpty()) {
					UploadImageResult uploadResult = imageProcessorService.processProfilePhoto(editedProfile.getFile());
					imageProcessorService.removeProfilePhoto(profile.getPhoto());
					imageProcessorService.removeProfilePhoto(profile.getPhotoSmall());
					profile.setPhoto(uploadResult.getLargeImageLink());
					profile.setPhotoSmall(uploadResult.getSmallImageLink());
				}
				profileRepository.save(profile);
				updateIndexAfterEditGeneralInfo(idProfile, profile);
				if (!profileWasActiveBeforeEdit) {
					sendProfileActivatedNotification(profile);
				}
			}
		}
		else {
			LOGGER.debug("Profile {}: nothing to update", idProfile);
		}
	}

	private void checkEmailAddressIsUnique(String idProfile, String email, String phone) {
		Profile profile = profileRepository.findByEmail(email);
		if (profile != null) {
			if (!idProfile.equals(profile.getId())) {
				LOGGER.error("Profile with email " + email + " already exist. Can't update profile");
				throw new CantCompleteClientRequestException("Profile with email " + email + " already exist. Can't update profile");
			}
		}
		profile = profileRepository.findByPhone(phone);
		if (profile != null) {
			if (!idProfile.equals(profile.getId())) {
				LOGGER.error("Profile with phone " + phone + " already exist. Can't update profile");
				throw new CantCompleteClientRequestException("Profile with phone " + phone + " already exist. Can't update profile");
			}
		}
	}

	private void updateIndexAfterEditGeneralInfo(final String idProfile, final Profile updatedProfile) {
		LOGGER.info("Profile {}: general info has been updated", idProfile);
		Profile profile = profileSearchRepository.findOne(idProfile);
		profileSearchRepository.delete(profile);
		profileSearchRepository.save(updatedProfile);
		LOGGER.info("Profile {}: index has been updated", idProfile);
	}

	private void sendProfileActivatedNotification(final Profile profile) {
		LOGGER.info("Profile {}: profile has been completed, now it's active", profile.getId());
		notificationManagerService.sendProfileActive(profile);
	}

	@Override
	public void updateAdditionalInfo(String idProfile, Profile editedProfile) {
		LOGGER.debug("Profile {}: updating additional info", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		if (UpdateLogicUtil.profileAdditionalInfoChanged(profile, editedProfile)) {
			LOGGER.debug("Updating profile additional: profile has been changed");
			profile.setAdditionalInfo(editedProfile.getAdditionalInfo());
			profileRepository.save(profile);
		}
		else {
			LOGGER.debug("Profile {}: nothing to update");
		}
		LOGGER.info("Profile {}: additional info has been updated", idProfile);
	}

	@Override
	public void updateContact(String idProfile, Contact editedContact) {
		LOGGER.debug("Profile {}: updating contact info", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		Contact contact = profile.getContact();
		if (UpdateLogicUtil.profileContactChanged(contact, editedContact)) {
			LOGGER.debug("Profile {}: contact info has been changed");
			profile.setContact(editedContact);
			profileRepository.save(profile);
		}
		else {
			LOGGER.debug("Profile {}: nothing to update");
		}
		LOGGER.info("Profile {}: contact info has been updated", idProfile);
	}

	@Override
	public void removeProfile(String idProfile) {
		LOGGER.debug("Profile {}: removing", idProfile);
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
		LOGGER.info("Profile {}: restore link has been created", profile.getId());
		notificationManagerService.sendRestoreAccessLink(profile, restoreLink);
	}

	@Override
	public void removeRestoreToken(String idProfile) {
		LOGGER.debug("Profile {}: removing restore token", idProfile);
		ProfileRestore restore = profileRestoreRepository.findByProfileId(idProfile);
		profileRestoreRepository.delete(restore);
	}

	@Override
	public void updatePassword(String idProfile, ChangePasswordForm form) {
		LOGGER.debug("Profile {}: updating password", idProfile);
		Profile profile = profileRepository.findById(idProfile);
		profile.setPassword(passwordEncoder.encode(form.getPassword()));
		profileRepository.save(profile);
		sendPasswordChangedNotification(profile);
	}

	private void sendPasswordChangedNotification(final Profile profile) {
		LOGGER.info("Profile {}: password has been changed", profile.getId());
		notificationManagerService.sendPasswordChanged(profile);
	}
}