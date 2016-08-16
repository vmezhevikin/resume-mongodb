package net.devstudy.resume.util;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.domain.ProfileCollectionField;

public class ProfileDataUtil {
	
	public static final String[] GENERAL_FIELDS = { "firstName", "lastName", "birthday", "country", "city", "phone", "objective", "summary" };
	public static final String[] CONTACT_FIELDS = { "skype", "vkontakte", "facebook", "linkedin", "github", "stackoverflow" };
	public static final String[] ADDITIONAL_FIELDS = { "additionalInfo" };
	public static final String[] COLLECTION_FIELDS = { "certificate", "course", "education", "experience", "hobby", "language", "skill" };
	
	@SuppressWarnings("unchecked")
	public static <E extends ProfileCollectionField> List<E> getProfileCollection(Profile profile, Class<E> clazz) {
		String collName = clazz.getSimpleName().toLowerCase();
		return (List<E>) ObjectDataUtil.getObjectField(profile, collName);
	}
	
	public static void setAllProfileCollectionsAsEmty(Profile profile) {
		try {
			for (String field : ProfileDataUtil.COLLECTION_FIELDS) {
				ObjectDataUtil.setObjectField(profile, field, Collections.EMPTY_LIST);
			}
		} catch (Exception e) {
			throw new RuntimeException("Can't write collection fields to " + profile, e);
		}
	}
	
	public static void copyGeneralFields(Profile destProfile, Profile sourceProfile) {
		try {
			for (String field : GENERAL_FIELDS) {
				Object copy = ObjectDataUtil.getObjectField(sourceProfile, field);
				ObjectDataUtil.setObjectField(destProfile, field, copy);
			}
		} catch (Exception e) {
			throw new RuntimeException("Can't copy fields from " + sourceProfile + " to " + destProfile, e);
		}
	}
	
	public static void setEmptyProfileFieldsAsNulls(Object object, String[] fileds) {
		try {
			for (String field : fileds) {
				String value = (String) ObjectDataUtil.getObjectField(object, field);
				if (StringUtils.isBlank(value)) {
					ObjectDataUtil.setObjectField(object, field, null);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Can't write collection fields to " + object, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> int  compareFields(Comparable<T> first, Comparable<T> second) {
		if (first == null) {
			if (second == null) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (second == null) {
				return -1;
			} else {
				return first.compareTo((T) second);
			}
		}
	}
}