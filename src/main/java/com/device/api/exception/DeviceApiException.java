package com.device.api.exception;

import javax.ws.rs.core.Response.Status;

@SuppressWarnings("serial")
public class DeviceApiException extends Exception {
	private Status status;
	
	public DeviceApiException(String message, Status status) {
		super(message);
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	
}
