package net.devstudy.resume.repository.storage;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.RepositoryDefinition;

import net.devstudy.resume.domain.HobbyName;

@RepositoryDefinition(domainClass = HobbyName.class, idClass = String.class)
public interface HobbyNameRepository {
	
	List<HobbyName> findAll(Sort sort);
}