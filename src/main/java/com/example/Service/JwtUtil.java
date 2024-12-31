package com.example.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
	
	private static final SecureRandom random = new SecureRandom();
	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public static String genarateToken(String username, boolean rememberMe) {
		long expirationTime = rememberMe ? 864000000L : 3600000L;
		
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(key)
				.compact();
	}
	
	 public static String generatePasswordResetToken(String username) {
	        long expirationTime = 60000L; // 1 phút tính bằng milliseconds

	        return Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
	                .signWith(key)
	                .compact();
	    }
	
	public static String validateToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
				return claims.getSubject();	
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}
