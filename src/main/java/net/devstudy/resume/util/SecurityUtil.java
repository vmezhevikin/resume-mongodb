package net.devstudy.resume.util;

import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.exception.CantCompleteClientRequestException;
import net.devstudy.resume.model.CurrentProfile;

public final class SecurityUtil {
	
	public static CurrentProfile getCurrentProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof CurrentProfile) {
			return (CurrentProfile) principal;
		}
		else {
			return null;
		}
	}

	public static String getCurrentProfileId() {
		CurrentProfile currentProfile = getCurrentProfile();
		if (currentProfile != null) {
			return currentProfile.getUsername();
		}
		else {
			return null;
		}
	}

	public static void authentificate(Profile profile) {
		CurrentProfile currentProfile = new CurrentProfile(profile);
		Authentication authentication = new UsernamePasswordAuthenticationToken(currentProfile, currentProfile.getPassword(), currentProfile.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public static void logoutCurrentUser(HttpServletRequest request) {
		try {
			request.logout();
		} catch (ServletException e) {
			throw new CantCompleteClientRequestException("Can't logout current user", e);
		}
	}

	public static String generateNewActionUid() {
		return generateUuid(true);
	}

	public static String generateNewRestoreAccessToken() {
		return generateUuid(false);
	}

	public static String generateNewConfirmRegistrationToken() {
		return generateUuid(false);
	}

	public static String generateNewConfirmEmailToken() {
		return generateUuid(false);
	}

	public static String generatePassword() {
		return generateUuid(false).substring(0, 15);
	}
	
	public static String generateProfileUid() {
		return generateUuid(false).substring(0, 8);
	}
	
	private static String generateUuid(boolean withHyphens) {
		if (withHyphens) {
			return UUID.randomUUID().toString();
		} else {
			return UUID.randomUUID().toString().replace("-", "");
		}
	}
}
