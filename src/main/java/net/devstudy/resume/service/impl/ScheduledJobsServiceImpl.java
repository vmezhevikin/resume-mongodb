package net.devstudy.resume.service.impl;

import java.util.Iterator;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.devstudy.resume.domain.Course;
import net.devstudy.resume.domain.Education;
import net.devstudy.resume.domain.Experience;
import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.repository.storage.ProfileConfirmRegistrationRepository;
import net.devstudy.resume.repository.storage.ProfileRestoreRepository;
import net.devstudy.resume.service.EditProfileService;
import net.devstudy.resume.service.FindProfileService;
import net.devstudy.resume.service.NotificationManagerService;
import net.devstudy.resume.service.ScheduledJobsService;

@Service
public class ScheduledJobsServiceImpl implements ScheduledJobsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledJobsServiceImpl.class);
	
	@Autowired
	private FindProfileService findProfileService;

	@Autowired
	private EditProfileService editProfileService;
	
	@Autowired
	private NotificationManagerService notificationManagerService;
	
	@Autowired
	private ProfileRestoreRepository profileRestoreRepository;
	
	@Autowired
	private ProfileConfirmRegistrationRepository profileRegistrationRepository;

	@Value("${remove.not.active.profiles.interval}")
	private int removeNotActiveProfilesInterval;
	
	@Value("${send.notifications.interval}")
	private int sendNotificationsInterval;

	@Value("${course.years.ago}")
	private int courseYearsAgo;

	@Value("${education.years.ago}")
	private int educationYearsAgo;

	@Value("${practic.years.ago}")
	private int practicYearsAgo;

	@Value("${profile.lastvisit.year}")
	private int profileLastvisitYear;

	@Value("${remove.tokens.interval}")
	private int removeTtokensInterval;
	
	@Override
	@Scheduled(cron = "${remove.not.active.profiles.cron}")
	public void removeNotAciveProfiles() {
		LOGGER.info("Scheduled : removing not active profiles");
		LocalDate boundDate = new LocalDate().minusDays(removeNotActiveProfilesInterval);
		List<Profile> profilesToRemove = findProfileService.findNotActiveProfilesCreatedBefore(boundDate.toDate());
		for (Profile profile : profilesToRemove) {
			editProfileService.removeProfile(profile.getId());
		}
	}

	@Override
	@Scheduled(cron = "${remove.old.data.cron}")
	public void removeOldProfilesData() {
		LOGGER.info("Scheduled: removing old profiles data");	
		removeOldCourses();
		removeOldEducations();
		removeOldExperiences();
	}
	
	private void removeOldCourses() {
		LOGGER.info("Scheduled : removing old courses");
		LocalDate boundDate = new LocalDate().minusYears(courseYearsAgo);
		List<Profile> profilesWithOldCourses = findProfileService.findProfilesWithCompletedCourseBefore(boundDate.toDate());
		for (Profile profile : profilesWithOldCourses) {
			List<Course> courseList = profile.getCourse();
			Iterator<Course> iterator = courseList.iterator();
			while (iterator.hasNext()) {
				Course course = iterator.next();
				if (course.getCompletionDate() == null) {
					continue;
				}
				LocalDate completionDate = new LocalDate(course.getCompletionDate());
				if (completionDate.isBefore(boundDate)) {
					iterator.remove();
				}
			}
			editProfileService.updateCourse(profile.getId(), courseList);
		}
	}

	private void removeOldEducations() {
		LOGGER.info("Scheduled : removing old educations");
		int boundYear = new LocalDate().getYear() - educationYearsAgo;
		List<Profile> profilesWithOldEducations = findProfileService.findProfilesWithCompletedEducationBefore(boundYear);
		for (Profile profile : profilesWithOldEducations) {
			List<Education> educationList = profile.getEducation();
			Iterator<Education> iterator = educationList.iterator();
			while (iterator.hasNext()) {
				Education education = iterator.next();
				if (education.getCompletionYear() == null) {
					continue;
				}
				if (education.getCompletionYear() < boundYear) {
					iterator.remove();
				}
			}
			editProfileService.updateEducation(profile.getId(), educationList);
		}
	}

	private void removeOldExperiences() {
		LOGGER.info("Scheduled : removing old experiences");
		LocalDate boundDate = new LocalDate().minusYears(practicYearsAgo);
		List<Profile> profilesWithOldExperiences = findProfileService.findProfilesWithCompletedExperienceBefore(boundDate.toDate());
		for (Profile profile : profilesWithOldExperiences) {
			List<Experience> experienceList = profile.getExperience();
			Iterator<Experience> iterator = experienceList.iterator();
			while (iterator.hasNext()) {
				Experience experience = iterator.next();
				if (experience.getCompletionDate() == null) {
					continue;
				}
				LocalDate completionDate = new LocalDate(experience.getCompletionDate());
				if (completionDate.isBefore(boundDate)) {
					iterator.remove();
				}
			}
			editProfileService.updateExperience(profile.getId(), experienceList);
		}
	}

	@Override
	@Scheduled(cron = "${send.return.notification.cron}")
	public void sendReturnRemindersNotifications() {
		LOGGER.info("Scheduled : sending return notifications");
		LocalDate boundDate = new LocalDate().minusMonths(sendNotificationsInterval);
		List<Profile> profiles = findProfileService.findProfilesVisitedBefore(boundDate.toDate());
		for (Profile profile : profiles) {
			notificationManagerService.sendReturnReminder(profile);
		}
	}

	@Override
	@Scheduled(cron = "${remove.old.profiles.cron}")
	public void removeNotRelevantProfiles() {
		LOGGER.info("Scheduled : removing old profiles");
		LocalDate boundDate = new LocalDate().minusYears(profileLastvisitYear);
		List<Profile> profiles = findProfileService.findProfilesVisitedBefore(boundDate.toDate());
		for (Profile profile : profiles) {
			editProfileService.removeProfile(profile.getId());
		}
	}

	@Override
	@Scheduled(cron = "${remove.old.tokens.cron}")
	public void removeOldTokens() {
		LOGGER.info("Scheduled : removing old tokens");
		removeRestoreTokens();
		removeConfirmRegistrationTokens();
	}
	
	private void removeRestoreTokens() {
		LOGGER.info("Scheduled : removing old restore tokens");
		LocalDate boundDate = new LocalDate().minusDays(removeTtokensInterval);
		profileRestoreRepository.deleteByCreatedBefore(boundDate.toDate());
	}
	
	private void removeConfirmRegistrationTokens() {
		LOGGER.info("Scheduled : removing old confirm registration tokens");
		LocalDate boundDate = new LocalDate().minusDays(removeTtokensInterval);
		profileRegistrationRepository.deleteByCreatedBefore(boundDate.toDate());
	}
}