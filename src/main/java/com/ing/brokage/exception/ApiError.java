package com.ing.brokage.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
public class ApiError {

	private HttpStatus status;
	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
	private Date timestamp;
	private String message;
	private String debugMessage;
	private int errorCode;

	private ApiError() {
		timestamp = new Date();
	}
	
	ApiError(HttpStatus status, String message, Throwable ex, int errorCode) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
		this.errorCode = errorCode;
	}

	ApiError(HttpStatus status, String message, Throwable ex) {
		this(status, message, ex, 0);
	}
	
	ApiError(HttpStatus status, String message) {
		this();
		this.status = status;
		this.message = message;
	}

}
