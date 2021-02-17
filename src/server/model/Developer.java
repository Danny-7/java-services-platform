package server.model;

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
        this.isCertified = false;
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
    public String toString() {
        return "Developer{" +
                "login='" + login + '\'' +
                ", pwd='" + pwd + '\'' +
                ", ftpUrl='" + ftpUrl + '\'' +
                ", isCertified=" + isCertified +
                '}';
    }
}
