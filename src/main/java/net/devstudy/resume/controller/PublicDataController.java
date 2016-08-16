package net.devstudy.resume.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.devstudy.resume.Constants;
import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.service.FindProfileService;
import net.devstudy.resume.service.StaticDataService;

@Controller
public class PublicDataController {
	
	@Autowired
	private FindProfileService findProfileService;

	@Autowired
	private StaticDataService staticDataService;

	@RequestMapping(value = "/{uid}", method = RequestMethod.GET)
	public String getResume(@PathVariable("uid") String uid, Model model) {
		Profile profile = findProfileService.findByUid(uid);
		if (profile == null) {
			return "profile-not-found";
		}
		model.addAttribute("profile", profile);
		model.addAttribute("hobbies", staticDataService.getListHobbyData());
		return "resume";
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String getWelcome(Model model) {
		PageRequest pageable = new PageRequest(0, Constants.MAX_PROFILES_PER_PAGE, new Sort("id"));
		Page<Profile> profiles = findProfileService.findAllActive(pageable);
		model.addAttribute("profiles", profiles.getContent());
		model.addAttribute("page", profiles);
		return "welcome";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String getSearch(@ModelAttribute("query") String query, Model model) {
		PageRequest pageable = new PageRequest(0, Constants.MAX_PROFILES_PER_PAGE, new Sort("id"));
		Page<Profile> profiles = null;
		if (StringUtils.isEmpty(query)) {
			profiles = findProfileService.findAllActive(pageable);
		} else {
			profiles = findProfileService.findBySearchQuery(query, pageable);
		}
		model.addAttribute("profiles", profiles.getContent());
		model.addAttribute("page", profiles);
		return "welcome";
	}
	
	@RequestMapping(value = "/more-profiles", method = RequestMethod.GET)
	public String getWelcomeMore(@RequestParam(value = "query", required = false) String query, Model model, @PageableDefault(size = Constants.MAX_PROFILES_PER_PAGE) @SortDefault(sort = "id") Pageable pageable) {
		Page<Profile> profiles = null;
		if (StringUtils.isEmpty(query)) {
			profiles = findProfileService.findAllActive(pageable);
		} else {
			profiles = findProfileService.findBySearchQuery(query, pageable);
		}
		model.addAttribute("profiles", profiles.getContent());
		return "more-profiles";
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String getError() {
		return "error";
	}
}
