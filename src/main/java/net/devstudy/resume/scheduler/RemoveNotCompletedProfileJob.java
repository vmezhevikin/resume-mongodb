package net.devstudy.resume.scheduler;

import java.util.List;

import org.joda.time.LocalDate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.service.EditProfileService;
import net.devstudy.resume.service.FindProfileService;

public class RemoveNotCompletedProfileJob implements Job {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveNotCompletedProfileJob.class);

	@Autowired
	private FindProfileService findProfileService;

	@Autowired
	private EditProfileService editProfileService;

	@Value("${remove.not.completed.profiles.interval}")
	private int removeNotCompletedProfilesInterval;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("Scheduled : removing not completed profiles");
		LocalDate boundDate = new LocalDate().minusDays(removeNotCompletedProfilesInterval);
		List<Profile> profilesToRemove = findProfileService.findNotCompletedProfilesCreatedBefore(boundDate.toDate());
		for (Profile profile : profilesToRemove) {
			editProfileService.removeProfile(profile.getId());
		}
	}
}