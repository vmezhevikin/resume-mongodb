package net.devstudy.resume.repository.storage;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.devstudy.resume.domain.SkillCategory;

public interface SkillCategoryRepository2 extends PagingAndSortingRepository<SkillCategory, String> {
	
	List<SkillCategory> findAll(Sort sort);
}