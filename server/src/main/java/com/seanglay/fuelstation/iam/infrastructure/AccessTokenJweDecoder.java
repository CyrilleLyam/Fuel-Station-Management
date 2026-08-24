package com.seanglay.fuelstation.iam.infrastructure;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import static com.seanglay.fuelstation.iam.infrastructure.JwtTokenProvider.ACCESS_TOKEN_TYPE;
import static com.seanglay.fuelstation.iam.infrastructure.JwtTokenProvider.TOKEN_TYPE_CLAIM;

@Component
class AccessTokenJweDecoder implements JwtDecoder {

	private final JwtCrypto jwtCrypto;

	AccessTokenJweDecoder(JwtCrypto jwtCrypto) {
		this.jwtCrypto = jwtCrypto;
	}

	@Override
	public Jwt decode(String token) throws JwtException {
		try {
			Jwt jwt = jwtCrypto.decryptAndVerify(token);

			if (!ACCESS_TOKEN_TYPE.equals(jwt.getClaimAsString(TOKEN_TYPE_CLAIM))) {
				throw new BadJwtException("Not an access token");
			}

			return jwt;
		}
		catch (JOSEException | ParseException ex) {
			throw new BadJwtException("Invalid or expired token", ex);
		}
	}

}
