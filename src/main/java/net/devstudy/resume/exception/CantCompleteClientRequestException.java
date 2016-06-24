package net.devstudy.resume.exception;

public class CantCompleteClientRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 823670011447353032L;

	public CantCompleteClientRequestException(String message) {
		super(message);
	}

	public CantCompleteClientRequestException(Throwable cause) {
		super(cause);
	}

	public CantCompleteClientRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}