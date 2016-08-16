package net.devstudy.resume.repository.storage;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import net.devstudy.resume.domain.ProfileConfirmRegistration;

public interface ProfileConfirmRegistrationRepository extends CrudRepository<ProfileConfirmRegistration, String> {
	
	ProfileConfirmRegistration findByToken(String token);
	
	void deleteByCreatedBefore(Date date);
	
	ProfileConfirmRegistration findByProfileId(String id);
}