package net.devstudy.resume.service;

import java.util.List;

import javax.annotation.Nonnull;

import net.devstudy.resume.domain.HobbyName;
import net.devstudy.resume.domain.SkillCategory;

public interface StaticDataService {
	
	@Nonnull List<HobbyName> getListHobbyName();

	@Nonnull List<SkillCategory> getListSkillCategory();
}