package com.xm.cryptoservice.model;

import org.springframework.http.HttpStatus;

public record LoadResponseDTO(int size,String status_msg,HttpStatus statuscode) {}

	