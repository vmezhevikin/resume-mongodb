package net.devstudy.resume.repository.storage;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.RepositoryDefinition;

import net.devstudy.resume.domain.StaticSkillData;

@RepositoryDefinition(domainClass = StaticSkillData.class, idClass = String.class)
public interface StaticSkillDataRepository {
	
	List<StaticSkillData> findAll(Sort sort);
}