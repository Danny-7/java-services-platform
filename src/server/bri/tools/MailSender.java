package server.bri.tools;

import server.bri.managers.ServiceManager;
import utils.NetworkData;
import utils.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class MailSender extends Service
{
    private final Socket client;
    private String mail;
    private String messageSend;

    public MailSender(Socket s, NetworkData net)
    {
        super(net);
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
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(copyDest));
            message.setSubject("testEnvoiMail");
            message.setText(messageSend);


        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public synchronized void run()
    {
        NetworkData net = super.getNet();
        net.send("Type the email address ");
        mail = net.read().toString();
        net.send("Type the text to be sent");
        messageSend = net.read().toString();
        sendMail(mail, messageSend);
        net.send("Message sent to -> " + mail);
    }

    public static String toStringue() {
        return "ServiceMailSender";
    }
}
