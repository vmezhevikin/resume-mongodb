package net.devstudy.resume.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.domain.ProfileRestore;
import net.devstudy.resume.model.CurrentProfile;
import net.devstudy.resume.repository.search.ProfileSearchRepository;
import net.devstudy.resume.repository.storage.ProfileRepository;
import net.devstudy.resume.repository.storage.ProfileRestoreRepository;
import net.devstudy.resume.service.FindProfileService;

@Service
public class FindProfileServiceImpl implements FindProfileService, UserDetailsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FindProfileServiceImpl.class);

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private ProfileSearchRepository profileSearchRepository;
	
	@Autowired
	private ProfileRestoreRepository profileRestoreRepository;

	@Override
	public Profile findByUid(String uid) {
		return profileRepository.findByUid(uid);
	}

	@Override
	public Profile findById(String id) {
		return profileRepository.findById(id);
	}

	@Override
	public Profile findByEmail(String email) {
		return profileRepository.findByEmail(email);
	}

	@Override
	public Page<Profile> findAll(Pageable pageable) {
		return profileRepository.findAll(pageable);
	}

	@Override
	public Page<Profile> findAllActive(Pageable pageable) {
		return profileRepository.findAllByActiveTrue(pageable);
	}

	@Override
	public Iterable<Profile> findAllForIndexing() {
		Iterable<Profile> allProfiles = profileRepository.findAll();
		return allProfiles;
	}

	@Override
	public Page<Profile> findBySearchQuery(String query, Pageable pageable) {
		return profileSearchRepository.findByAllSubstantialFields(query, pageable);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Profile profile = findByUniqueId(username);
		if (profile != null) {
			return new CurrentProfile(profile);
		}
		else {
			LOGGER.error("Profile not found by " + username);
			throw new UsernameNotFoundException("Profile not found by " + username);
		}
	}

	@Override
	public Profile findByUniqueId(String anyUniqueId) {
		return profileRepository.findByUidOrEmailOrPhone(anyUniqueId, anyUniqueId, anyUniqueId);
	}

	@Override
	public Profile findByToken(String token) {
		ProfileRestore restore = profileRestoreRepository.findByToken(token);
		return restore.getProfile();
	}

	@Override
	public List<Profile> findNotCompletedProfilesCreatedBefore(Date date) {
		return profileRepository.findByActiveFalseAndCreatedBefore(date);
	}

	@Override
	public List<Profile> findProfilesWithCompletedCourseBefore(Date date) {
		return profileRepository.findByCourseCompletionDateBefore(date);
	}

	@Override
	public List<Profile> findProfilesWithCompletedEducationBefore(int year) {
		return profileRepository.findByEducationCompletionYearBefore(year);
	}

	@Override
	public List<Profile> findProfilesWithCompletedExperienceBefore(Date date) {
		return profileRepository.findByExperienceCompletionDateBefore(date);
	}
}
