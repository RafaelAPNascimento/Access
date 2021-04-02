package br.com.app.integration;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class ConfigCertificate {

    private static final Path TEST_TRUST_STORE;
    private static final String LOCAL_HOST_CERTIFICATE = "localhost";

    static {

        final String JAVA_HOME = System.getenv("JAVA_HOME");
        TEST_TRUST_STORE = Path.of(JAVA_HOME, "lib", "security", "only_test_cert");

        try {
            Path certificate = Paths.get(ClassLoader.getSystemResource(LOCAL_HOST_CERTIFICATE).toURI());
            loadCertificate(certificate);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (CertificateException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }


    private static void loadCertificate(Path certificate) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException {

        String alias = "localhost";
        String password = "changeit";

        if (!Files.exists(TEST_TRUST_STORE)) {

            createTestTrustStoreFile();

            // to load a new truststore other than default cacerts
            KeyStore clienTruststore = KeyStore.getInstance(KeyStore.getDefaultType());

            // pass null to create an empty keystore
            clienTruststore.load(Files.newInputStream(TEST_TRUST_STORE), password.toCharArray());

            // CertficateFactory to create a new reference to the server certificate file
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            // read the server certificate
            InputStream serverCertstream = Files.newInputStream(certificate);

            // certificate instance
            Certificate serverCertificate = cf.generateCertificate(serverCertstream);

            // add the server certificate to our newly truststore
            clienTruststore.setCertificateEntry(alias, serverCertificate);

            // save modifications
            OutputStream out = Files.newOutputStream(TEST_TRUST_STORE);
            clienTruststore.store(out, password.toCharArray());
            out.close();
        }

        // dynamically set default truststore for this application from cacerts to TEST_TRUST_STORE
        System.setProperty("javax.net.ssl.trustStore", TEST_TRUST_STORE.toString());
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }

    private static void createTestTrustStoreFile() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        KeyStore clientTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = "changeit".toCharArray();

        clientTrustStore.load(null, password);

        OutputStream fos = Files.newOutputStream(TEST_TRUST_STORE);
        clientTrustStore.store(fos, password);
        fos.close();
    }

}
