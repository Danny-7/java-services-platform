package server.model;

import java.util.Objects;

public class Developer {
    private String login;
    private String pwd;
    private String ftpUrl;
    private boolean isCertified;

    public Developer() {}

    public Developer(String login, String pwd, String ftpUrl) {
        this.login = login;
        this.pwd = pwd;
        this.ftpUrl = ftpUrl;
        this.isCertified = true;
    }

    public String getLogin() {
        return login;
    }

    public String getPwd() {
        return pwd;
    }

    public String getFtpUrl() {
        return ftpUrl;
    }

    public boolean isCertified() {
        return isCertified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Developer)) return false;
        Developer developer = (Developer) o;
        return login.equals(developer.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "Developer{" +
                "login='" + login + '\'' +
                ", pwd='" + pwd + '\'' +
                ", ftpUrl='" + ftpUrl + '\'' +
                ", isCertified=" + isCertified +
                '}';
    }
}
