package br.com.app.service.security;

import br.com.app.model.Credentials;
import br.com.app.model.JWT;
import org.json.JSONObject;

import javax.crypto.Mac;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DefaultTokenFactory extends TokenFactory {

    static final Logger LOG = Logger.getLogger(DefaultTokenFactory.class.getName());

    private static PrivateKey SECRET_KEY;

    public DefaultTokenFactory() throws Exception {

        loadSecretKey();
    }

    private void loadSecretKey() throws URISyntaxException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        //try (InputStream is = Files.newInputStream(Path.of(ClassLoader.getSystemResource("security.properties").toURI()))) {
        //try (InputStream is = this.getClass().getResourceAsStream("security.properties")) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("security.properties")) {

            Properties properties = new Properties();
            properties.load(is);

            String secretPath = properties.getProperty("access.security.api.privatekey.path");
            String algoritmKey = properties.getProperty("access.security.api.privatekey.algorithm");

            String encodePK = Files.readAllLines(Path.of(secretPath)).get(1);
            byte[] decodedBytes = Base64.getDecoder().decode(encodePK.getBytes(UTF_8));

            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(algoritmKey);
            SECRET_KEY = keyFactory.generatePrivate(keySpec);
        }
    }

    @Override
    public JWT issueToken(Credentials credentials) {

        JWT jwt = new JWT();
        fillHeader(jwt);
        fillPayload(jwt, credentials);
        fillSignature(jwt);
        return jwt;
    }

    private void fillSignature(JWT jwt) {

        byte[] encryptedSignature = signWithHs256(jwt.getB64Header() + "." + jwt.getB64Payload());
        jwt.setSignature(encode(encryptedSignature));
    }

    private byte[] signWithHs256(String data) {

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(SECRET_KEY);
            byte[] encryptedData = mac.doFinal(data.getBytes(UTF_8));
            return encryptedData;
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fillPayload(JWT jwt, Credentials credentials) {

        JSONObject payload = new JSONObject();
        payload.put("iss", ISSUER);
        payload.put("scope", credentials.getScope());
        payload.put("iat", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        payload.put("exp", LocalDateTime.now().plusMinutes(1).toEpochSecond(ZoneOffset.UTC));
        payload.put("jti", UUID.randomUUID().toString());

        jwt.setPayload(payload);
        jwt.setB64Payload(encode(payload.toString()));
        jwt.setExpires_in(String.valueOf(payload.getLong("exp")));
    }

    private void fillHeader(JWT jwt) {
        jwt.setB64Header(encode(HEADER));
    }

    private String encode(String data) {
        return encode(data.getBytes(UTF_8));
    }

    private String encode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }
}
