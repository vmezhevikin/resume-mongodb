package net.devstudy.resume.testenv;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.joda.time.LocalDate;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import net.coobird.thumbnailator.Thumbnails;

public class TestDataMongoGenerator
{
	private static final String DB_URL = "localhost";
	private static final int DB_PORT = 27017;
	private static final String DB_NAME = "resume";

	private static final String PHOTO_DIR = "external/test-data/photos/";
	private static final String CERTIFICATES_DIR = "external/test-data/certificates/";
	private static final String MEDIA_DIR = "src/main/webapp/media";

	private static final String PASSWORD_HASH = "$2a$10$q7732w6Rj3kZGhfDYSIXI.wFp.uwTSi2inB2rYHvm1iDIAf1J1eVq";
	private static final String COUNTRY = "Ukraine";
	private static final String[] CITIES = { "Kharkiv", "Kyiv", "Odessa", "Poltava" };
	private static final String[] LANGUAGES = { "English", "German", "French" };
	private static final String[] LANGUAGE_TYPE = { "All", "Writing", "Speaking" };
	private static final String[] LANGUAGE_LEVEL = { "Beginner", "Elementary", "Pre-intrmediate", "Intermediate", "Upper-Intermediate", "Advanced",
			"Proficiency" };
	private static final String[] HOBBIES = { "Cycling", "Handball", "Football", "Basketball", "Bowling", "Boxing", "Volleyball", "Baseball",
			"Skating", "Skiing", "Table tennis", "Tennis", "Weightlifting", "Automobiles", "Book reading", "Cricket", "Photo", "Shopping", "Cooking",
			"Codding", "Animals", "Traveling", "Movie", "Painting", "Darts", "Fishing", "Kayak slalom", "Games of chance", "Ice hockey",
			"Roller skating", "Swimming", "Diving", "Golf", "Shooting", "Rowing", "Camping", "Archery", "Pubs", "Music", "Computer games",
			"Authorship", "Singing", "Foreign lang", "Billiards", "Skateboarding", "Collecting", "Badminton", "Disco" };
	private static final String[] SKILLS_CATEGORIES = { "Languages", "DBMS", "Web", "Java", "IDE", "CVS", "Web Servers", "Build system", "Cloud" };
	private static final String[] SKILLS_DESC = { "Java, SQL, PLSQL", "Mysql, Postgresql", "HTML, CSS, JS, Foundation, JQuery, Bootstrap",
			"Threads, IO, JAXB, GSON, Servlets, Logback, JSP, JSTL, JDBC, Apache Commons, Google+ Social API, Spring MVC, Spring Data JPA, Spring Security, Hibernate JPA, Facebook Social API",
			"Eclipse for JEE Developer", "Github", "Tomcat, Nginx", "Maven", "OpenShift, AWS" };
	private static final String DUMMY_TEXT = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et "
			+ "magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede "
			+ "justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. "
			+ "Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. "
			+ "Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi "
			+ "vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet "
			+ "adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero "
			+ "venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. "
			+ "Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, "
			+ "mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices "
			+ "posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed "
			+ "aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy "
			+ "metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut.";
	private static final String[] OBJECTIVES = { "Junior java trainee position", "Junior java developer position" };
	private static final String[] SUMMARIES = { "Java core course with developing one simple console application",
			"One Java professional course with developing web application blog",
			"Two Java professional courses with developing two web applications: blog and resume",
			"Three Java professional courses with developing one console application and two web applications: blog and resume" };
	private static final String[] COMPANIES = { "Lorem ipsum", "Aenean commodo", "Etiam rhoncus", "Duis arcu tortor", "Praesent adipiscing", "Aenean vulputate" };
	private static final String[] POSITIONS = { "Junior java developer", "Java trainee" };
	private static final String[] UNIVERSITIES = { "National Technical University", "Karazin Kharkiv National University",
			"Kharkiv National University of Radioelectronics" };
	private static final String[] DEPARTMENTS = { "Computer-driven system and network", "Programming systems",
			"Intelligence system of problem-solving" };
	private static final String[] HOBBY_ICONS = { "<i class='fa fa-paw' aria-hidden='true'></i>", "<i class='fa fa-bullseye' aria-hidden='true'></i>",
			"<i class='fa fa-pencil-square-o' aria-hidden='true'></i>", "<i class='fa fa-car' aria-hidden='true'></i>",
			"<i class='fa fa-futbol-o' aria-hidden='true'></i>", "<i class='fa fa-futbol-o' aria-hidden='true'></i>",
			"<i class='fa fa-futbol-o' aria-hidden='true'></i>", "<i class='fa fa-futbol-o' aria-hidden='true'></i>",
			"<i class='fa fa-book' aria-hidden='true'></i>", "<i class='fa fa-futbol-o' aria-hidden='true'></i>",
			"<i class='fa fa-futbol-o' aria-hidden='true'></i>", "<i class='fa fa-leaf' aria-hidden='true'></i>",
			"<i class='fa fa-code' aria-hidden='true'></i>", "<i class='fa fa-star' aria-hidden='true'></i>",
			"<i class='fa fa-gamepad' aria-hidden='true'></i>", "<i class='fa fa-cutlery' aria-hidden='true'></i>",
			"<i class='fa fa-futbol-o' aria-hidden='true'></i>", "<i class='fa fa-bicycle' aria-hidden='true'></i>",
			"<i class='fa fa-bullseye' aria-hidden='true'></i>", "<i class='fa fa-users' aria-hidden='true'></i>",
			"<i class='fa fa-tint' aria-hidden='true'></i>", "<i class='fa fa-life-ring' aria-hidden='true'></i>",
			"<i class='fa fa-futbol-o' aria-hidden='true'></i>", "<i class='fa fa-language' aria-hidden='true'></i>",
			"<i class='fa fa-list-alt' aria-hidden='true'></i>", "<i class='fa fa-futbol-o' aria-hidden='true'></i>",
			"<i class='fa fa-futbol-o' aria-hidden='true'></i>", "<i class='fa fa-futbol-o' aria-hidden='true'></i>",
			"<i class='fa fa-tint' aria-hidden='true'></i>", "<i class='fa fa-film' aria-hidden='true'></i>",
			"<i class='fa fa-music' aria-hidden='true'></i>", "<i class='fa fa-paint-brush' aria-hidden='true'></i>",
			"<i class='fa fa-camera' aria-hidden='true'></i>", "<i class='fa fa-beer' aria-hidden='true'></i>",
			"<i class='fa fa-star' aria-hidden='true'></i>", "<i class='fa fa-futbol-o' aria-hidden='true'></i>",
			"<i class='fa fa-crosshairs' aria-hidden='true'></i>", "<i class='fa fa-shopping-basket' aria-hidden='true'></i>",
			"<i class='fa fa-microphone' aria-hidden='true'></i>", "<i class='fa fa-star' aria-hidden='true'></i>",
			"<i class='fa fa-star' aria-hidden='true'></i>", "<i class='fa fa-star' aria-hidden='true'></i>",
			"<i class='fa fa-tint' aria-hidden='true'></i>", "<i class='fa fa-futbol-o' aria-hidden='true'></i>",
			"<i class='fa fa-futbol-o' aria-hidden='true'></i>", "<i class='fa fa-map' aria-hidden='true'></i>",
			"<i class='fa fa-futbol-o' aria-hidden='true'></i>", "<i class='fa fa-star' aria-hidden='true'></i>" };
	private static final String[] HOBBY_NAMES = { "Animals", "Archery", "Authorship", "Automobiles", "Badminton", "Baseball", "Basketball",
			"Billiards", "Book reading", "Bowling", "Boxing", "Camping", "Coding", "Collecting", "Computer games", "Cooking", "Cricket", "Cycling",
			"Darts", "Disco", "Diving", "Fishing", "Football", "Foreign language", "Games of chance", "Golf", "Handball", "Ice hockey",
			"Kayak slalom", "Movie", "Music", "Painting", "Photo", "Pubs", "Roller skating", "Rowing", "Shooting", "Shopping", "Singing",
			"Skateboarding", "Skating", "Skiing", "Swimming", "Table tennis", "Tennis", "Traveling", "Volleyball", "Weightlifting" };

	private static final Random rand = new Random();
	private static List<String> dummySentences = Arrays.asList(DUMMY_TEXT.split("[.][^,]"));

	private static void clearDestinationFolder() throws IOException {
		System.out.println("Clearing destination folder");

		Path path = Paths.get(MEDIA_DIR);
		if (Files.exists(path)) {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		}
		System.out.println("Destination folder has been cleared");
	}
	
	private static DB getDB(MongoClient mongoClient) {
		return mongoClient.getDB(DB_NAME);
	}

	private static void clearDB(MongoClient mongoClient) throws SQLException {
		
		System.out.println("Clearing DB");
		getDB(mongoClient).getCollection("skillCategory").drop();
		getDB(mongoClient).getCollection("hobbyName").drop();
		getDB(mongoClient).getCollection("profile").drop();
		getDB(mongoClient).getCollection("profileRestore").drop();
		getDB(mongoClient).getCollection("rememberMeToken").drop();
		System.out.println("DB cleared");
	}

	private static void insertData(MongoClient mongoClient) throws SQLException, IOException {
		System.out.println("Inserting data");
		insertSkillCategory(mongoClient);
		insertHobbyName(mongoClient);
		insertProfile(mongoClient);
		insertProfileRestore(mongoClient);
		insertRememberMeToken(mongoClient);
		insertIndexes(mongoClient);
		System.out.println("Data have been inserted");
	}

	private static void insertSkillCategory(MongoClient mongoClient) {
		DBCollection collection = getDB(mongoClient).getCollection("skillCategory");
		for (int i = 1; i <= SKILLS_CATEGORIES.length; i++) {
			BasicDBObject obj = new BasicDBObject("idCategory", i)
					.append("name", SKILLS_CATEGORIES[i - 1]);
			collection.insert(obj);
		}
	}

	private static void insertHobbyName(MongoClient mongoClient) {
		DBCollection collection = getDB(mongoClient).getCollection("hobbyName");
		for (int i = 1; i <= HOBBY_ICONS.length; i++) {
			BasicDBObject obj = new BasicDBObject("idHobby", i)
					.append("icon", HOBBY_ICONS[i - 1])
					.append("name", HOBBY_NAMES[i - 1]);
			collection.insert(obj);
		}
	}

	private static void insertProfile(MongoClient mongoClient) throws SQLException, IOException {
		DBCollection collection = getDB(mongoClient).getCollection("profile");
		File[] photos = new File(PHOTO_DIR).listFiles();
		for (File file : photos) {
			
			String[] names = file.getName().replace(".jpg", "").split(" ");
			String firstName = names[0];
			String lastName = names[1];
			
			BasicDBObject profile = generateProfile(firstName, lastName, file);
			profile.append("contact", generateContact(firstName, lastName))
					.append("certificate", generateCertificate())
					.append("course", generateCourse())
					.append("education", generateEducation())
					.append("experience", generateExperience())
					.append("hobby", generateHobby())
					.append("language", generateLanguage())
					.append("skill", generateSkill());
			
			collection.insert(profile);
		}
	}

	private static void insertProfileRestore(MongoClient mongoClient) {
		// empty
	}

	private static void insertRememberMeToken(MongoClient mongoClient) {
		// empty
	}
	
	private static void insertIndexes(MongoClient mongoClient) {
		System.out.println("Inserting indexes");
		getDB(mongoClient).getCollection("profile").createIndex(new BasicDBObject("active", 1), "profile_idx_active", false);
		getDB(mongoClient).getCollection("profile").createIndex(new BasicDBObject("created", 1), "profile_idx_created", false);
		getDB(mongoClient).getCollection("profile").createIndex(new BasicDBObject("email", 1), "profile_idx_email", true);
		getDB(mongoClient).getCollection("profile").createIndex(new BasicDBObject("phone", 1), "profile_idx_phone", true);
		getDB(mongoClient).getCollection("profile").createIndex(new BasicDBObject("uid", 1), "profile_idx_uid", true);
		getDB(mongoClient).getCollection("profileRestore").createIndex(new BasicDBObject("token", 1), "profile_restore_idx_token", true);
		getDB(mongoClient).getCollection("rememberMeToken").createIndex(new BasicDBObject("series", 1), "remember_me_idx_series", true);
		getDB(mongoClient).getCollection("rememberMeToken").createIndex(new BasicDBObject("username", 1), "remember_me_idx_username", false);
		System.out.println("Indexes have been inserted");
	}
	
	private static BasicDBObject generateContact(String firstName, String lastName) {
		BasicDBObject contact = new BasicDBObject("skype", (firstName + "." + lastName).toLowerCase());
		if (rand.nextBoolean()) {
			contact.append("vkontakte", "http://vk.com/" + lastName.toLowerCase());
		}
		if (rand.nextBoolean()) {
			contact.append("facebook", "http://facebook.com/" + lastName.toLowerCase());
		}
		if (rand.nextBoolean()) {
			contact.append("linkedin", "http://linkedin.com/" + lastName.toLowerCase());
		}
		if (rand.nextBoolean()) {
			contact.append("github", "http://github.com/" + lastName.toLowerCase());
		}
		if (rand.nextBoolean()) {
			contact.append("stackoverflow", "http://stackoverflow.com/" + lastName.toLowerCase());
		}
		return contact;
	}
	
	private static BasicDBList generateCourse() {
		BasicDBList course = new BasicDBList();
		for (int i = 0; i < rand.nextInt(3); i++) {
			BasicDBObject item = new BasicDBObject("description", "Java basic")
					.append("school", "Vivamus");
			if (rand.nextBoolean()) {
				item.append("completionDate", generateDate());
			}
			course.add(item);
		}
		return course;
	}
	
	private static BasicDBList generateCertificate() throws IOException {
		BasicDBList certificate = new BasicDBList();
		File[] images = new File(CERTIFICATES_DIR).listFiles();
		for (File file : images) {
			ImageLinks links = generatCertificateImage(file);
			BasicDBObject item = new BasicDBObject("description", links.name)
					.append("img", links.largeImage)
					.append("imgSmall", links.smallImage);
			certificate.add(item);
		}
		return certificate;
	}
	
	private static ImageLinks generatCertificateImage(File source) throws IOException {
		String uuid = UUID.randomUUID().toString() + ".jpg";
		String smallUuid = uuid.replace(".jpg", "-sm.jpg");
		
		File largeImage = new File(MEDIA_DIR + "/certificate/" + uuid);
		File smallImage = new File(MEDIA_DIR + "/certificate/" + smallUuid);
		if (!largeImage.getParentFile().exists()) {
			largeImage.getParentFile().mkdirs();
		}
		Thumbnails.of(source).size(800, 800).toFile(largeImage);
		Thumbnails.of(source).size(100, 100).toFile(smallImage);

		ImageLinks links = new ImageLinks();
		links.name = source.getName().replace(".jpg", "");
		links.largeImage = "/media/certificate/" + uuid;
		links.smallImage = "/media/certificate/" + smallUuid;
		return links;
	}

	private static BasicDBList generateEducation() {
		BasicDBList education = new BasicDBList();
		int count = rand.nextInt(3);
		for (int i = 0; i < count; i++) {
			int year = generateYear();
			BasicDBObject item = new BasicDBObject("speciality", "Specialist of computer systems")
					.append("university", UNIVERSITIES[rand.nextInt(UNIVERSITIES.length)])
					.append("department", DEPARTMENTS[rand.nextInt(DEPARTMENTS.length)])
					.append("startingYear", year);
			if (rand.nextBoolean()) {
				item.append("completionYear", year + rand.nextInt(3) + 1);
			}
			education.add(item);
		}
		return education;
	}
	
	private static BasicDBList generateExperience() {
		BasicDBList experience = new BasicDBList();
		int count = rand.nextInt(3);
		for (int i = 0; i < count; i++) {
			Date completionDate = generateDate();
			BasicDBObject item = new BasicDBObject("company", COMPANIES[rand.nextInt(COMPANIES.length)])
					.append("position", POSITIONS[rand.nextInt(POSITIONS.length)])
					.append("startingDate", generateDateBefore(completionDate));
			if (rand.nextBoolean()) {
				item.append("completionDate", completionDate);
			}
			item.append("responsibility", dummySentences.get(rand.nextInt(dummySentences.size())))
					.append("demo", "http://LINK_TO_DEMO_SITE")
					.append("code", "http://github.com/TODO");
			experience.add(item);
		}
		return experience;
	}

	private static BasicDBList generateHobby() {
		BasicDBList hobby = new BasicDBList();
		int count = rand.nextInt(6);
		for (int i = 0; i < count; i++) {
			BasicDBObject item = new BasicDBObject("description", HOBBIES[rand.nextInt(HOBBIES.length)]);
			hobby.add(item);
		}
		return hobby;
	}

	private static BasicDBList generateLanguage() {
		BasicDBList language = new BasicDBList();
		int count = rand.nextInt(3);
		for (int i = 0; i < count; i++) {
			BasicDBObject item = new BasicDBObject("name", LANGUAGES[rand.nextInt(LANGUAGES.length)])
					.append("type", LANGUAGE_TYPE[rand.nextInt(LANGUAGE_TYPE.length)])
					.append("level", LANGUAGE_LEVEL[rand.nextInt(LANGUAGE_LEVEL.length)]);
			language.add(item);
		}
		return language;
	}

	private static BasicDBList generateSkill() {
		BasicDBList skill = new BasicDBList();
		for (int i = 0; i < SKILLS_CATEGORIES.length; i++) {
			BasicDBObject item = new BasicDBObject("category", SKILLS_CATEGORIES[i])
					.append("description", SKILLS_DESC[i]);
			skill.add(item);
		}
		return skill;
	}
	
	private static BasicDBObject generateProfile(String firstName, String lastName, File file) throws IOException {
		ImageLinks photoLinks = generatePhoto(file);

		BasicDBObject profileObj = new BasicDBObject("uid", (firstName + "-" + lastName).toLowerCase())
				.append("password", PASSWORD_HASH)
				.append("active", true)
				.append("firstName", firstName)
				.append("lastName", lastName)
				.append("country", COUNTRY)
				.append("city", CITIES[rand.nextInt(CITIES.length)])
				.append("birthday", generateBirthday())
				.append("email", (firstName + "." + lastName).toLowerCase() + "@gmail.com")
				.append("phone", generatePhone())
				.append("additionalInfo", dummySentences.get(rand.nextInt(dummySentences.size())))
				.append("objective", OBJECTIVES[rand.nextInt(OBJECTIVES.length)])
				.append("summary", SUMMARIES[rand.nextInt(SUMMARIES.length)])
				.append("photo", photoLinks.largeImage)
				.append("photoSmall", photoLinks.smallImage)
				.append("created", LocalDate.now().toDate());
		return profileObj;
	}
	
	private static ImageLinks generatePhoto(File source) throws IOException {
		String uuid = UUID.randomUUID().toString() + ".jpg";
		String smallUuid = uuid.replace(".jpg", "-sm.jpg");
		
		File largeImage = new File(MEDIA_DIR + "/avatar/" + uuid);
		File smallImage = new File(MEDIA_DIR + "/avatar/" + smallUuid);
		if (!largeImage.getParentFile().exists()) {
			largeImage.getParentFile().mkdirs();
		}
		Thumbnails.of(source).size(400, 400).toFile(largeImage);
		Thumbnails.of(source).size(100, 100).toFile(smallImage);

		ImageLinks links = new ImageLinks();
		links.largeImage = "/media/avatar/" + uuid;
		links.smallImage = "/media/avatar/" + smallUuid;
		return links;
	}

	private static Date generateBirthday() {
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.DAY_OF_MONTH, rand.nextInt(30));
		cl.set(Calendar.MONTH, rand.nextInt(12));
		int year = cl.get(Calendar.YEAR) - 30;
		cl.set(Calendar.YEAR, year + rand.nextInt(10));
		return new Date(cl.getTimeInMillis());
	}

	private static String generatePhone() {
		StringBuilder phone = new StringBuilder("+38");
		for (int i = 0; i < 10; i++) {
			int code = '0' + rand.nextInt(10);
			phone.append(((char) code));
		}
		return phone.toString();
	}

	private static Date generateDate() {
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.DAY_OF_MONTH, rand.nextInt(30));
		cl.set(Calendar.MONTH, rand.nextInt(12));
		int year = cl.get(Calendar.YEAR);
		cl.set(Calendar.YEAR, year - rand.nextInt(5));
		return new Date(cl.getTimeInMillis());
	}

	private static Date generateDateBefore(Date laterDate) {
		LocalDate date = new LocalDate(laterDate.getTime());
		date.minusMonths(rand.nextInt(48) + 12);
		return new Date(date.toDate().getTime());
	}

	private static int generateYear() {
		Calendar cl = Calendar.getInstance();
		int year = cl.get(Calendar.YEAR);
		return year - rand.nextInt(10) - 3;
	}
	
	private static class ImageLinks {
		private String name;
		private String largeImage;
		private String smallImage;
	}

	public static void main(String[] args) {
		System.out.println("Start generating");
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(DB_URL, DB_PORT);
			clearDestinationFolder();
			clearDB(mongoClient);
			insertData(mongoClient);
			System.out.println("Data have been generated");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mongoClient != null){
				mongoClient.close();
			}
		}
	}
}
