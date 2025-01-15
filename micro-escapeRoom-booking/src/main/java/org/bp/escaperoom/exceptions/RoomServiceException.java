package org.bp.escaperoom.exceptions;

public class RoomServiceException extends Exception {

	public RoomServiceException() {
	}

	public RoomServiceException(String message) {
		super(message);
	}

	public RoomServiceException(Throwable cause) {
		super(cause);
	}

	public RoomServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public RoomServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
