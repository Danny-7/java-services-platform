package server.bri.utils;

import org.w3c.dom.Document;
import server.bri.Service;

import java.io.*;
import java.net.Socket;

public class XMLAnalysis implements Service
{
    private final Socket client;
    private String rapport;
    private Document file;

    public XMLAnalysis()
    {
        this.client = null;
    }

    public XMLAnalysis(Socket s)
    {
        this.client = s;
    }

    @Override
    public synchronized void run()
    {
        try {
            BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
            PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
            out.println("type the name of the file you want to analyze");
            String[] information = in.readLine().split(" ");
            String nameFile = information[0];
            String mail = information[1];
            File xmlDoc = new File(nameFile);

            // voir SAXReader :
        }catch (IOException e)
        {
            System.out.println("Fin du service d'analyse de fichier");

        }

    }
    protected void finalize() throws Throwable
    {
        client.close();
    }

    @Override
    public String toString() {
        return "XMLAnalysis{}";
    }
}
