package com.hk.sso.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * https://developer.okta.com/blog/2018/10/31/jwts-with-java
 * @author hk128b
 *
 */
@Component
//@PropertySource("classpath:application.properties")
public class GenerateAuthToken {
	
	//@Value("${opus.publickey.password}")
	private String SECRET_KEY="Opus#Pswd09";
	
	public String createJWT(String id, String subject) {
		System.out.println("createJWT :: Enter");
		System.out.println("Secret Key :: " + SECRET_KEY);
		/*System.out.println("Secret Key :: " + env.getProperty("opus.publickey.password"));*/
		
		//SECRET_KEY = env.getProperty("opus.publickey.password");
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	    System.out.println("Expiry time :: " + now.getTime() +  " : " + now.getMinutes());
	    
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(id)
	            .setIssuedAt(now)
	            .setSubject(subject)
	            .setIssuer("TECHM_AUTH")
	            .signWith(signatureAlgorithm, signingKey);
	  
	    //if it has been specified, let's add the expiration
	    
	        long expMillis = nowMillis + 300000;  // token valid for five minutes
	        Date exp = new Date(expMillis);
	        System.out.println("Expiry time :: " + exp.getTime() +  " : " + exp.getMinutes());
	        builder.setExpiration(exp);
	  
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
	
	public Claims decodeJWT(String jwt) {
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()
	            .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
	            .parseClaimsJws(jwt).getBody();
	    return claims;
	}
	public boolean isTokenValid(Claims claims) {
		
		System.out.println("Id :: " + claims.getId() + " Subject :: " + claims.getSubject() + " Issuer :: " + claims.getIssuer());
		if(claims.getExpiration().after(new Date(System.currentTimeMillis())))
		{
			return true;
		}else {
			return false;
		}
		
		
	}

}
