package org.example;

public class Config {
    private String ip;

    private static Config instance = new Config();


    private Config() {}
    public void setIP(String ip){
        this.ip = ip;
    }

    public String getIP(){
        return ip;
    }

    public static Config getInstance() {
        return instance;
    }
}
