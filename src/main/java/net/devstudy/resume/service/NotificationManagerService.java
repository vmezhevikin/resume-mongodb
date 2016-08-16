package net.devstudy.resume.service;

import javax.annotation.Nonnull;

import net.devstudy.resume.domain.Profile;

public interface NotificationManagerService {
	
	void sendConfirmRegistrationLink(@Nonnull Profile profile, @Nonnull String token);
	
	void sendRestoreAccessLink(@Nonnull Profile profile, @Nonnull String token);
	
	void sendPasswordChanged(@Nonnull Profile profile);
	
	void sendReturnReminder(@Nonnull Profile profile);
	
	void sendConfirmEmailLink(@Nonnull Profile profile, @Nonnull String token, @Nonnull String destinationAddress);
}