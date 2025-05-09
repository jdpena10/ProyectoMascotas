package co.edu.unipiloto.mascotas;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    private static final String EMAIL = "correodeprueba3478@gmail.com";  // Tu correo
    private static final String PASSWORD = "ycwyluizpynslbdl"; // La clave de aplicación generada

    public static void sendEmail(String toEmail, String subject, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);
            System.out.println("Correo enviado a " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

