package net.devstudy.resume.exception;

public class NotificationServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 4230534124578400747L;

	public NotificationServiceException(String message) {
		super(message);
	}

	public NotificationServiceException(Throwable cause) {
		super(cause);
	}

	public NotificationServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}