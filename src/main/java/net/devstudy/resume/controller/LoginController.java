package net.devstudy.resume.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.form.SignUpForm;
import net.devstudy.resume.service.EditProfileService;
import net.devstudy.resume.service.FindProfileService;
import net.devstudy.resume.service.FormService;
import net.devstudy.resume.util.SecurityUtil;
import net.devstudy.resume.validator.RecaptchaFormValidator;

@Controller
public class LoginController {
	
	@Autowired
	private FindProfileService findProfileService;

	@Autowired
	private EditProfileService editProfileService;
	
	@Autowired
	private FormService formService;

	@Value("${app.host}")
	private String appHost;
	
	@Value("${recaptcha.site.key}")
	private String recaptchaSiteKey;
	
	@Value("${practic.years.ago}")
	private int practicYearsAgo;
	
	@Value("${course.years.ago}")
	private int courseYearsAgo;
			
	@Value("${education.years.ago}")
	private int educationYearsAgo;
	
	@Value("${profile.lastvisit.year}")
	private int profileLastVisit;

	@Autowired
	private RecaptchaFormValidator recaptchaFormValidator;

	@InitBinder("form")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(recaptchaFormValidator);
	}

	@RequestMapping(value = "/sign-in", method = RequestMethod.GET)
	public String getSignIn() {
		String currentProfileId = SecurityUtil.getCurrentProfileId();
		if (currentProfileId != null) {
			Profile profile = findProfileService.findById(currentProfileId);
			return "redirect:/" + profile.getUid();
		}
		else {
			return "sign-in";
		}
	}

	@RequestMapping(value = "/sign-in-failed", method = RequestMethod.GET)
	public String getSignInFailed(HttpSession session) {
		if (session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") == null) {
			return "redirect:/sign-in";
		}
		else {
			return "sign-in";
		}
	}

	@RequestMapping(value = "/sign-up", method = RequestMethod.GET)
	public String getSignUp(Model model) {
		model.addAttribute("form", formService.produceForm("sign-up"));
		model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
		return "sign-up";
	}

	@RequestMapping(value = "/sign-up", method = RequestMethod.POST)
	public String postSignUp(@Valid @ModelAttribute("form") SignUpForm form, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			return "sign-up";
		}
		Profile profile = editProfileService.createNewNotActiveProfile(form);
		editProfileService.addConfirmRegistrtionToken(profile.getId(), SecurityUtil.generateNewConfirmRegistrationToken());
		return "confirm-registration";
	}
	
	@RequestMapping(value = "/sign-up/confirm/{token}", method = RequestMethod.GET)
	public String postSignUpConfirm(@PathVariable("token") String token, Model model) {
		Profile profile = findProfileService.findByConfirmRegistrationToken(token);
		if (profile == null) {
			return "error";
		}
		Profile activeProfile = editProfileService.activateProfile(profile);
		editProfileService.removeConfirmRegistrtionToken(activeProfile.getId());
		SecurityUtil.authentificate(activeProfile);
		return "redirect:/sign-up/success";
	}

	@RequestMapping(value = "/sign-up/success", method = RequestMethod.GET)
	public String getSignUpSuccess(Model model) {
		Profile profile = findProfileService.findById(SecurityUtil.getCurrentProfileId());
		if (profile == null) {
			return "error";
		}
		addAttributesForSuccessSignUp(model, profile);
		return "sign-up-success";
	}

	@RequestMapping(value = "/sign-up/success-via-facebook", method = RequestMethod.GET)
	public String getSignUpViaFacebbok(Model model) {
		Profile profile = findProfileService.findById(SecurityUtil.getCurrentProfileId());
		if (profile == null) {
			return "error";
		}
		addAttributesForSuccessSignUp(model, profile);
		return "sign-up-facebook-success";
	}
	
	private void addAttributesForSuccessSignUp(Model model, Profile profile) {
		model.addAttribute("profile", profile);
		model.addAttribute("appHost", appHost);
		model.addAttribute("practicYearsAgo", practicYearsAgo);
		model.addAttribute("courseYearsAgo", courseYearsAgo);
		model.addAttribute("educationYearsAgo", educationYearsAgo);
		model.addAttribute("profileLastVisit", profileLastVisit);
	}

	@RequestMapping(value = "/restore", method = RequestMethod.GET)
	public String getRestore() {
		return "restore";
	}

	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public String postRestore(@ModelAttribute("anyUniqueId") String anyUniqueId, Model model) {
		Profile profile = findProfileService.findByUniqueId(anyUniqueId);
		if (profile != null) {
			editProfileService.addRestoreToken(profile.getId(), SecurityUtil.generateNewRestoreAccessToken());
		}
		return "redirect:/restore/success";
	}

	@RequestMapping(value = "/restore/success", method = RequestMethod.GET)
	public String getRestoreSuccess() {
		return "restore-success";
	}

	@RequestMapping(value = "/restore/{token}", method = RequestMethod.GET)
	public String getRestoreToken(@PathVariable("token") String token, Model model) {
		Profile profile = findProfileService.findByRestoreToken(token);
		if (profile == null) {
			return "error";
		}
		SecurityUtil.authentificate(profile);
		editProfileService.removeRestoreToken(profile.getId());
		return "redirect:/edit/settings";
	}
}
