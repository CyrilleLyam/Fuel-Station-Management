package com.seanglay.fuelstation.iam.infrastructure;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;

import com.nimbusds.jose.JOSEException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.seanglay.fuelstation.config.JwtProperties;
import com.seanglay.fuelstation.iam.domain.TokenIssuer;
import com.seanglay.fuelstation.iam.domain.User;
import com.seanglay.fuelstation.shared.domain.UnauthorizedException;

@Component
class JwtTokenProvider implements TokenIssuer {

	static final String TOKEN_TYPE_CLAIM = "type";

	static final String ACCESS_TOKEN_TYPE = "access";

	static final String REFRESH_TOKEN_TYPE = "refresh";

	private final JwtCrypto jwtCrypto;

	private final JwtProperties jwtProperties;

	JwtTokenProvider(JwtCrypto jwtCrypto, JwtProperties jwtProperties) {
		this.jwtCrypto = jwtCrypto;
		this.jwtProperties = jwtProperties;
	}

	@Override
	public String issueAccessToken(User user) {
		return issue(user, ACCESS_TOKEN_TYPE, jwtProperties.accessTokenTtl());
	}

	@Override
	public String issueRefreshToken(User user) {
		return issue(user, REFRESH_TOKEN_TYPE, jwtProperties.refreshTokenTtl());
	}

	@Override
	public String verifyRefreshToken(String token) {
		try {
			Jwt jwt = jwtCrypto.decryptAndVerify(token);

			if (!REFRESH_TOKEN_TYPE.equals(jwt.getClaimAsString(TOKEN_TYPE_CLAIM))) {
				throw new UnauthorizedException("Unexpected token type");
			}

			return jwt.getSubject();
		}
		catch (JOSEException | ParseException | JwtException ex) {
			throw new UnauthorizedException("Invalid or expired token", ex);
		}
	}

	private String issue(User user, String tokenType, Duration ttl) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
			.subject(user.getUsername())
			.claim(TOKEN_TYPE_CLAIM, tokenType)
			.issuedAt(now)
			.expiresAt(now.plus(ttl))
			.build();

		return jwtCrypto.signAndEncrypt(claims);
	}

}
