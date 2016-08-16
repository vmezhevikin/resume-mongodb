package net.devstudy.resume.repository.storage;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import net.devstudy.resume.domain.ProfileConfirmEmail;

public interface ProfileConfirmEmailRepository extends CrudRepository<ProfileConfirmEmail, String> {
	
	ProfileConfirmEmail findByToken(String token);
	
	void deleteByCreatedBefore(Date date);
	
	ProfileConfirmEmail findByProfileId(String id);
}