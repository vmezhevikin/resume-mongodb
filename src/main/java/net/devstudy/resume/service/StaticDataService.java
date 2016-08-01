package net.devstudy.resume.service;

import java.util.List;

import javax.annotation.Nonnull;

import net.devstudy.resume.domain.Hobby;
import net.devstudy.resume.domain.StaticHobbyData;
import net.devstudy.resume.domain.StaticSkillData;

public interface StaticDataService {
	
	@Nonnull List<StaticHobbyData> getListHobbyData();
	
	@Nonnull List<Hobby> getListHobby();

	@Nonnull List<StaticSkillData> getListSkillData();
}