package net.devstudy.resume.service;

public interface ScheduledJobsService {
	
	void removeNotAciveProfiles();
	
	void removeOldProfilesData();
	
	void sendReturnRemindersNotifications();
	
	void removeNotRelevantProfiles();
	
	void removeOldTokens();
}