package net.devstudy.resume.scheduler;

import java.util.Iterator;
import java.util.List;

import org.joda.time.LocalDate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.devstudy.resume.domain.Course;
import net.devstudy.resume.domain.Education;
import net.devstudy.resume.domain.Experience;
import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.service.EditProfileService;
import net.devstudy.resume.service.FindProfileService;

public class RemoveOldDataServiceJob implements Job {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveOldDataServiceJob.class);

	@Autowired
	private EditProfileService editProfileService;
	
	@Autowired
	private FindProfileService findProfileService;

	@Value("${course.years.ago}")
	private int courseYearsAgo;

	@Value("${education.years.ago}")
	private int educationYearsAgo;

	@Value("${practic.years.ago}")
	private int practicYearsAgo;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("Scheduled: RemoveOldDataServiceJob");	
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
}
