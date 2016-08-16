package net.devstudy.resume.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.devstudy.resume.domain.Profile;
import net.devstudy.resume.model.NotificationMessage;
import net.devstudy.resume.service.NotificationManagerService;
import net.devstudy.resume.service.NotificationSenderService;
import net.devstudy.resume.service.NotificationTemplateService;
import net.devstudy.resume.util.DataUtil;

@Service
public class NotificationManagerServiceImpl implements NotificationManagerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationManagerServiceImpl.class);

	@Autowired
	private NotificationSenderService notificationSenderService;

	@Autowired
	private NotificationTemplateService notificationTemplateService;

	@Value("${email.restorelink.address}")
	private String emailRestoreAddress;

	@Value("${email.confirm.registration.address}")
	private String emailConfirmRegistrationAddress;
	
	@Value("${email.profile.address}")
	private String emailProfileAddress;
	
	@Value("${email.confirm.email.address}")
	private String emailConfirmEmailAddress;

	@Override
	public void sendConfirmRegistrationLink(Profile profile, String token) {
		String link = emailConfirmRegistrationAddress + token;
		LOGGER.debug("Sending confirm registration link: {} for account {}", link, profile.getUid());
		Map<String, Object> model = new HashMap<>();
		model.put("confirmRegistrationLink", link);
		proccesNotification(profile, "confirmRegistrationNotification", model);
	}

	@Override
	public void sendRestoreAccessLink(Profile profile, String token) {
		String link = emailRestoreAddress + token;
		LOGGER.debug("Sending restore link: {} for account {}", link, profile.getUid());
		Map<String, Object> model = new HashMap<>();
		model.put("restoreLink", link);
		proccesNotification(profile, "restoreAccessNotification", model);
	}

	@Override
	public void sendPasswordChanged(Profile profile) {
		String link = emailProfileAddress + profile.getUid();
		LOGGER.debug("Sending password changed for account {}", profile.getUid());
		Map<String, Object> model = new HashMap<>();
		model.put("profileLink", link);
		proccesNotification(profile, "passwordChangedNotification", model);
	}

	@Override
	public void sendReturnReminder(Profile profile) {
		String link = emailProfileAddress + profile.getUid();
		LOGGER.debug("Sending password changed for account {}", profile.getUid());
		Map<String, Object> model = new HashMap<>();
		model.put("profileLink", link);
		model.put("lastVisit", DataUtil.generateStringFromDate(profile.getLastVisit()));
		proccesNotification(profile, "returnReminderNotification", model);
	}

	@Override
	public void sendConfirmEmailLink(Profile profile, String token, String destinationAddress) {
		String link = emailConfirmEmailAddress + token;
		LOGGER.debug("Sending confirm email link: {} for account {}", link, profile.getUid());
		Map<String, Object> model = new HashMap<>();
		model.put("confirmEmailLink", link);
		proccesNotification(destinationAddress, profile.getUid(), "confirmEmailNotification", model);
	}

	private void proccesNotification(Profile profile, String templateName, Object model) {
		String destinationAddress = notificationSenderService.getDestinationAddress(profile);
		proccesNotification(destinationAddress, profile.getUid(), templateName, model);
	}
	
	private void proccesNotification(String destinationAddress, String uid, String templateName, Object model) {
		if (StringUtils.isNotBlank(destinationAddress)) {
			NotificationMessage notificationMessage = notificationTemplateService.createNotificationMessage(templateName, model);
			notificationMessage.setDestinationAddress(destinationAddress);
			notificationMessage.setDestinationName(uid);
			notificationSenderService.sendNotification(notificationMessage);
		} else {
			LOGGER.error("Notification ignored: destinationAddress is empty for profile " + uid);
		}
	}
}