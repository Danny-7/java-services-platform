package server.bri.services;

import server.model.Developer;

public class LoginService {
    private Developer dev;

    public void connect(String login, String pwd, String ftpServer) {
        this.dev = new Developer(login, pwd, ftpServer);
    }

    public Developer getDev() {
        return dev;
    }
}
