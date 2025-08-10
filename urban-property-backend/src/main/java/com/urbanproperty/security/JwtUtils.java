package com.urbanproperty.security;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.urbanproperty.dao.UserDao;
import com.urbanproperty.entities.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {
	
	@Value("${SECRET_KEY}")
	private String jwtSecret;
	
	@Value("${EXP_TIMEOUT}")
	private int jwtExpirationMs;
	
	@Autowired
    private UserDao userDao;
	private SecretKey key;
	
	@PostConstruct
	public void init() {
		log.info("Initializing SecretKey for JWT. Expiration Time: {} ms", jwtExpirationMs);
		// Create a SecretKey instance from the Base64 encoded secret
		key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}
	
	// Generates a JWT token upon successful authentication
		public String generateJwtToken(Authentication authentication) {
			log.info("Generating JWT token for: {}", authentication.getName());
			UserEntity userPrincipal = (UserEntity) authentication.getPrincipal();

			return Jwts.builder()
					.setSubject(userPrincipal.getUsername()) // Set user's email as the subject
					.setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
					.claim("authorities", getAuthoritiesInString(userPrincipal.getAuthorities())) // Custom claim for roles
					.signWith(key, Jwts.SIG.HS256) // Sign the token
					.compact();
		}
		
		// Validates the JWT and returns the claims (payload)
		public Claims validateJwtToken(String jwtToken) {
			// Throws various exceptions if validation fails (e.g., ExpiredJwtException)
			return Jwts.parser()
					.verifyWith(key) // Sets the secret key for signature verification
					.build()
					.parseSignedClaims(jwtToken)
					.getPayload();
		}
		// Extracts the username from the token's claims
		public String getUserNameFromJwtToken(Claims claims) {
			return claims.getSubject();
		}
		// Creates a fully populated Authentication object from a valid JWT
		public Authentication populateAuthenticationTokenFromJWT(String jwt) {
			Claims claims = validateJwtToken(jwt);
			String email = getUserNameFromJwtToken(claims);
			List<GrantedAuthority> authorities = getAuthoritiesFromClaims(claims);
			
			  UserEntity user = userDao.findByEmail(email)
		                .orElseThrow(() -> new UsernameNotFoundException("User not found from token."));
			// Create and return the Authentication object, which Spring Security will use
			return new UsernamePasswordAuthenticationToken(email, null, authorities);
		}
		
		private List<String> getAuthoritiesInString(Collection<? extends GrantedAuthority> authorities) {
			return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		}
		// Extracts user roles from the token's claims
		public List<GrantedAuthority> getAuthoritiesFromClaims(Claims claims) {
			@SuppressWarnings("unchecked")
			List<String> authorityNames = (List<String>) claims.get("authorities");
			return authorityNames.stream()
								 .map(SimpleGrantedAuthority::new)
								 .collect(Collectors.toList());
		}
}
