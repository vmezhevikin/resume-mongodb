package net.devstudy.resume.util;

import java.util.LinkedList;
import java.util.List;

import net.devstudy.resume.domain.Contact;
import net.devstudy.resume.domain.Hobby;
import net.devstudy.resume.domain.Profile;

public class FormUtil {

	public static List<Hobby> getCheckedItems(List<Hobby> items) {
		List<Hobby> checkedItems = new LinkedList<>();
		for (Hobby hobby : items) {
			if ("checked".equals(hobby.getChecked())) {
				checkedItems.add(hobby);
			}
		}
		return checkedItems;
	}
	
	public static List<Hobby> setCheckedItems(List<Hobby> allItems, List<Hobby> currentItems) {
		for (Hobby hobby : allItems) {
			hobby.setChecked("unchecked");
			String desc = hobby.getDescription();
			for (Hobby current : currentItems) {
				if (desc.equals(current.getDescription())) {
					hobby.setChecked("checked");
					break;
				}
			}
		}
		return allItems;
	}

	public static Contact setBlankContatcFieldsAsNulls(Contact form) {
		ProfileDataUtil.setEmptyProfileFieldsAsNulls(form, ProfileDataUtil.CONTACT_FIELDS);
		return form;
	}

	public static Profile setBlankAdditionalFieldsAsNulls(Profile form) {
		ProfileDataUtil.setEmptyProfileFieldsAsNulls(form, ProfileDataUtil.ADDITIONAL_FIELDS);
		return form;
	}

	public static Profile setBlankGeneralFieldsAsNulls(Profile form) {
		ProfileDataUtil.setEmptyProfileFieldsAsNulls(form, ProfileDataUtil.GENERAL_FIELDS);
		return form;
	}
}