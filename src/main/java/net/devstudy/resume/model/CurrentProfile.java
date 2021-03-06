package net.devstudy.resume.model;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import net.devstudy.resume.Constants;
import net.devstudy.resume.domain.Profile;

public class CurrentProfile extends User {
	
	private static final long serialVersionUID = 5493244383969862376L;

	public CurrentProfile(Profile profile) {
		super(profile.getId(), profile.getPassword(), true, true, true, true, Collections.singleton(new SimpleGrantedAuthority(Constants.USER)));
	}
}