package com.example.Service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

	public static String generateSixDigitToken() {
	    Random random = new Random();
	    int token = 100000 + random.nextInt(900000);
	    return String.valueOf(token);
	}

}
