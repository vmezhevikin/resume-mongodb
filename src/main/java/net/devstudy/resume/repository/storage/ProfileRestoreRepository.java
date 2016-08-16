package net.devstudy.resume.repository.storage;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import net.devstudy.resume.domain.ProfileRestore;

public interface ProfileRestoreRepository extends CrudRepository<ProfileRestore, String> {
	
	ProfileRestore findByToken(String token);
	
	void deleteByCreatedBefore(Date date);
	
	ProfileRestore findByProfileId(String id);
}