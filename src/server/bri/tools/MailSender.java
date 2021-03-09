package server.bri.tools;

import server.bri.Service;
import server.bri.managers.ServiceManager;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class MailSender implements Service
{
    private final Socket client;
    private String mail;
    private String messageSend;


    public MailSender ()
    {
        this.client = null;
    }

    public MailSender(Socket s)
    {
        this.client = s;
    }


    public void sendMail(String messageSend, String copyDest)
    {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        String myAccountMail ="remyinformatique007@gmail.com";
        String password ="my_K6dcP,]DM3?UG";

        // création d'une session
        Session session = Session.getInstance(properties, new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(myAccountMail, password);
            }
        });


        Message message = prepareMessage(session, myAccountMail, copyDest, messageSend);
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    // création du message
    private static Message prepareMessage(Session s, String account, String copyDest,String messageSend)
    {
        Message message = new MimeMessage(s);
        try {
            message.setFrom(new InternetAddress(account));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(copyDest));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            message.setSubject("testEnvoiMail");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            message.setText(messageSend);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;

    }


    @Override
    public synchronized void run()
    {
        try {
            BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
            PrintWriter out = new PrintWriter(client.getOutputStream ( ), true);
            out.println("Type the email address ");
            mail = in.readLine();
            out.println("type the text to be sent");
            messageSend = in.readLine();
            sendMail(mail, messageSend);
            out.println("Message envoyé #n"+ ServiceManager.serviceListing() + " # Tapez le numéro de service désiré :# 1 pour lancer un service # 2 pour lister les services # 3 pour se déconnecter # suivi du nom du service");
        }
        catch (IOException e) {
            System.err.println("End of the mail sending service");
        }



    }
    protected void finalize() throws Throwable {
        client.close();
    }
    public static String toStringue() {
        return "ServiceEnvoiMail";
    }
}
