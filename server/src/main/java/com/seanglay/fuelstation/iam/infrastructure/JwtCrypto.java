package com.seanglay.fuelstation.iam.infrastructure;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.seanglay.fuelstation.config.JwtProperties;

@Component
class JwtCrypto {

	private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

	private final RSAPublicKey encryptionPublicKey;

	private final RSAPrivateKey encryptionPrivateKey;

	private final JwtEncoder jwtEncoder;

	private final JwtDecoder signatureDecoder;

	JwtCrypto(JwtProperties jwtProperties) {
		RSAPrivateKey signingPrivateKey = parsePrivateKey(jwtProperties.signingPrivateKeyPath());
		RSAPublicKey signingPublicKey = parsePublicKey(jwtProperties.signingPublicKeyPath());
		this.encryptionPrivateKey = parsePrivateKey(jwtProperties.encryptionPrivateKeyPath());
		this.encryptionPublicKey = parsePublicKey(jwtProperties.encryptionPublicKeyPath());

		RSAKey rsaKey = new RSAKey.Builder(signingPublicKey).privateKey(signingPrivateKey).build();
		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
		this.jwtEncoder = new NimbusJwtEncoder(jwkSource);
		this.signatureDecoder = NimbusJwtDecoder.withPublicKey(signingPublicKey)
			.signatureAlgorithm(SignatureAlgorithm.RS256)
			.build();
	}

	String signAndEncrypt(JwtClaimsSet claims) {
		String signedJws = jwtEncoder
			.encode(JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.RS256).build(), claims))
			.getTokenValue();

		try {
			JWEObject jweObject = new JWEObject(
					new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM).contentType("JWT")
						.build(),
					new Payload(signedJws));
			jweObject.encrypt(new RSAEncrypter(encryptionPublicKey));
			return jweObject.serialize();
		}
		catch (JOSEException ex) {
			throw new IllegalStateException("Failed to encrypt JWT", ex);
		}
	}

	Jwt decryptAndVerify(String token) throws JOSEException, ParseException, JwtException {
		JWEObject jweObject = JWEObject.parse(token);
		jweObject.decrypt(new RSADecrypter(encryptionPrivateKey));
		String signedJws = jweObject.getPayload().toString();
		return signatureDecoder.decode(signedJws);
	}

	private static RSAPrivateKey parsePrivateKey(String resourceLocation) {
		try {
			byte[] der = Base64.getDecoder().decode(stripPem(readResource(resourceLocation)));
			return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(der));
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException ex) {
			throw new IllegalStateException("Invalid RSA private key at " + resourceLocation, ex);
		}
	}

	private static RSAPublicKey parsePublicKey(String resourceLocation) {
		try {
			byte[] der = Base64.getDecoder().decode(stripPem(readResource(resourceLocation)));
			return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(der));
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException ex) {
			throw new IllegalStateException("Invalid RSA public key at " + resourceLocation, ex);
		}
	}

	private static String readResource(String resourceLocation) {
		try {
			return StreamUtils.copyToString(RESOURCE_LOADER.getResource(resourceLocation).getInputStream(),
					StandardCharsets.UTF_8);
		}
		catch (IOException ex) {
			throw new UncheckedIOException("Failed to read key file " + resourceLocation, ex);
		}
	}

	private static String stripPem(String pem) {
		return pem.replaceAll("-----(BEGIN|END) [A-Z ]+-----", "").replaceAll("\\s", "");
	}

}
