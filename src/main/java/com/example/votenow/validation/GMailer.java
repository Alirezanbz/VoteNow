package com.example.votenow.validation;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import static com.google.api.services.gmail.GmailScopes.*;
import static javax.mail.Message.*;

@Component
public class GMailer {
    private static final String TEST_EMAIL = "nabavizadehseyedalireza@gmail.com";
    private String verificationCode;

    private final Gmail service;



    public GMailer() throws Exception {

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory= GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("Test Mailer")
                .build();
    }


    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {

        InputStream in = GMailer.class.getResourceAsStream("/client_secret_654417122024-di1tcbr5v461plq8etlcq58tg4nen7vk.apps.googleusercontent.com.json");

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return credential;
    }

    public void sendMail(String recipientEmail,String subject, String htmlContent) throws Exception{




        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(TEST_EMAIL));
        email.addRecipient(RecipientType.TO, new InternetAddress(recipientEmail));
        email.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart();

        MimeBodyPart MessageBodyPart = new MimeBodyPart();
        MessageBodyPart.setContent(htmlContent, "text/html");


/*
        MimeBodyPart attachment = new MimeBodyPart();

        String filePath = "C:\\Users\\SANabavizadeh\\MailSender\\PDFBox_auto.pdf";
        File file1 = Paths.get(filePath).toFile();
        attachment.attachFile(file1);
*/



        multipart.addBodyPart(MessageBodyPart);
    //    multipart.addBodyPart(attachment);

        email.setContent(multipart);


        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {

            msg = service.users().messages().send("me", msg).execute();
            System.out.println("Message id: " + msg.getId());
            System.out.println(msg.toPrettyString());

        } catch (GoogleJsonResponseException e) {

            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }
    public void sendVerificationEmail(String recipientEmail) throws Exception {
        generateVerificationCode();
        String subject = "Email Verification Test";
        String htmlContent = "<p>Your verification code: " + verificationCode + "</p>";
        sendMail(recipientEmail, subject, htmlContent);
    }

    public void generateVerificationCode() {
        int codeLength = 6;
        String characters = "0123456789";
        StringBuilder code = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        verificationCode = code.toString();
    }
    private String loadHtmlFile(String filePath) throws IOException {
        InputStream inputStream = GMailer.class.getResourceAsStream(filePath);
        try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    public void sendResetPasswordEmail(String recipientEmail, String token) throws Exception {
        String subject = "Reset Password";
        String htmlContent = "<p>Please click the link below to reset your password:</p>"
                + "<a href=\"http://localhost:8080/reset-password?token=" + token + "\">Reset Password</a>";
        sendMail(recipientEmail, subject, htmlContent);
    }

    public String generateToken(){
        return UUID.randomUUID().toString();
    }
    public String getVerificationCode() {
        return verificationCode;
    }



}
