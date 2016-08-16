package net.devstudy.resume.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.devstudy.resume.domain.Profile;

public interface FindProfileService {
	
	@Nullable Profile findByUid(@Nonnull String uid);
	
	@Nullable Profile findById(String id);
	
	@Nullable Profile findByEmail(@Nonnull String email);

	@Nonnull Page<Profile> findAll(@Nonnull Pageable pageable);

	@Nonnull Page<Profile> findAllActive(@Nonnull Pageable pageable);
	
	@Nonnull Iterable<Profile> findAllForIndexing();
	
	@Nonnull Page<Profile> findBySearchQuery(@Nonnull String query, @Nonnull Pageable pageable);
	
	@Nullable Profile findByUniqueId(@Nonnull String anyUniqueId);
	
	@Nullable Profile findByRestoreToken(@Nonnull String token);
	
	@Nullable Profile findByConfirmRegistrationToken(@Nonnull String token);
	
	@Nullable Profile findByConfirmEmailToken(@Nonnull String token);

	@Nonnull List<Profile> findNotActiveProfilesCreatedBefore(@Nonnull Date date);
	
	@Nonnull List<Profile> findProfilesVisitedBefore(@Nonnull Date date);
	
	@Nonnull List<Profile> findProfilesWithCompletedCourseBefore(@Nonnull Date date);
	
	@Nonnull List<Profile> findProfilesWithCompletedEducationBefore(int year);
	
	@Nonnull List<Profile> findProfilesWithCompletedExperienceBefore(@Nonnull Date date);
}