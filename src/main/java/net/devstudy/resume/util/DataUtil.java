package net.devstudy.resume.util;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.joda.time.LocalDate;

public class DataUtil {
	
	public static String normailizeName(String name) {
		return name.trim().toLowerCase();
	}

	public static String capitailizeName(String name) {
		return WordUtils.capitalize(normailizeName(name));
	}

	public static Date generateDateFromString(String dateStr) {
		String[] part = dateStr.split("-");
		int year = Integer.parseInt(part[0]);
		int month = Integer.parseInt(part[1]);
		int day = Integer.parseInt(part[2]);
		return new LocalDate(year, month, day).toDate();
	}
	
	public static String generateStringFromDate(Date date) {
		return new LocalDate(date).toString("yyyy-MM-dd");
	}

	public static int getCurrentYear() {
		return LocalDate.now().getYear();
	}

	public static Date getCurrentDate() {
		return LocalDate.now().toDate();
	}

	public static String generateImageUid() {
		return UUID.randomUUID().toString();
	}
}