package net.devstudy.resume.service.impl;

import java.util.Date;
import java.util.List;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.domain.ProfileConfirmEmail;
import net.devstudy.resume.domain.ProfileConfirmRegistration;
import net.devstudy.resume.domain.ProfileRestore;
import net.devstudy.resume.model.CurrentProfile;
import net.devstudy.resume.repository.search.ProfileSearchRepository;
import net.devstudy.resume.repository.storage.ProfileConfirmEmailRepository;
import net.devstudy.resume.repository.storage.ProfileConfirmRegistrationRepository;
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
	
	@Autowired
	private ProfileConfirmRegistrationRepository profileRegistrationRepository;
	
	@Autowired
	private ProfileConfirmEmailRepository profileConfirmEmailRepository;

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
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
			.withQuery(QueryBuilders.multiMatchQuery(query)
			.type(MultiMatchQueryBuilder.Type.PHRASE)
			.slop(2)
			.field("summary")
			.field("objective")
			.field("additionalInfo")
			.field("language.name")
			.field("certificate.description")
			.field("course.description")
			.field("experience.company")
			.field("experience.position")
			.field("experience.responsibility")
			.field("skill.category")
			.field("skill.description")
			.fuzziness(Fuzziness.AUTO)
			.operator(MatchQueryBuilder.Operator.AND))
			.withSort(SortBuilders.fieldSort("uid").order(SortOrder.DESC))
			.build();
		searchQuery.setPageable(pageable);
		return profileSearchRepository.search(searchQuery);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Profile profile = findByUniqueId(username);
		if (profile == null) {
			LOGGER.error("Profile not found by uid/email/phone {}", username);
			throw new UsernameNotFoundException("Profile not found by uid/email/phone " + username);
		}
		if (!profile.getActive()) {
			LOGGER.warn("Profile with uid/email/phone {} is not active", username);
			throw new UsernameNotFoundException("Profile with uid/email/phone " + username + " is not active");
		}
		return new CurrentProfile(profile);
	}

	@Override
	public Profile findByUniqueId(String anyUniqueId) {
		return profileRepository.findByUidOrEmailOrPhone(anyUniqueId, anyUniqueId, anyUniqueId);
	}

	@Override
	public Profile findByRestoreToken(String token) {
		ProfileRestore restore = profileRestoreRepository.findByToken(token);
		return restore.getProfile();
	}

	@Override
	public Profile findByConfirmRegistrationToken(String token) {
		ProfileConfirmRegistration registration = profileRegistrationRepository.findByToken(token);
		return registration.getProfile();
	}

	@Override
	public Profile findByConfirmEmailToken(String token) {
		ProfileConfirmEmail confirmEmail = profileConfirmEmailRepository.findByToken(token);
		return confirmEmail.getProfile();
	}

	@Override
	public List<Profile> findNotActiveProfilesCreatedBefore(Date date) {
		return profileRepository.findByActiveFalseAndCreatedBefore(date);
	}

	@Override
	public List<Profile> findProfilesVisitedBefore(Date date) {
		return profileRepository.findByLastVisitBefore(date);
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
