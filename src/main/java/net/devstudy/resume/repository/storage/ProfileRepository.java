package net.devstudy.resume.repository.storage;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.devstudy.resume.domain.Profile;

public interface ProfileRepository extends PagingAndSortingRepository<Profile, String> {
	
	List<Profile> findAll(Sort sort);

	Profile findById(String id);

	Profile findByUid(String uid);

	Profile findByEmail(String email);

	Profile findByPhone(String phone);

	Profile findByUidOrEmailOrPhone(String uid, String email, String phone);

	int countByUid(String uid);

	int countByEmail(String email);

	int countByPhone(String phone);

	List<Profile> findByActiveFalseAndCreatedBefore(Date date);
	
	List<Profile> findByEducationCompletionYearBefore(int year);
	
	List<Profile> findByCourseCompletionDateBefore(Date date);
	
	List<Profile> findByExperienceCompletionDateBefore(Date date);
	
	Page<Profile> findAllByActiveTrue(Pageable pageable);
}
