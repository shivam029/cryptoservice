package com.xm.cryptoservice.model;

import org.springframework.http.HttpStatus;

public class LoadResponseDTO {

	int size;

	String status_msg;

	HttpStatus statuscode;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getStatus_msg() {
		return status_msg;
	}

	public void setStatus_msg(String status_msg) {
		this.status_msg = status_msg;
	}

	public HttpStatus getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(HttpStatus statuscode) {
		this.statuscode = statuscode;
	}

}
