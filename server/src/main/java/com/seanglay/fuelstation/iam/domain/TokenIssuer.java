package com.seanglay.fuelstation.iam.domain;

public interface TokenIssuer {

	String issueAccessToken(User user);

	String issueRefreshToken(User user);

	String verifyRefreshToken(String token);

}
