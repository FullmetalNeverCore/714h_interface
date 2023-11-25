package org.example;

public class CurrentSetup {
    private String curEng;

    private String engine;

    private float rnd;

    private float fpen;

    private float ppen;

    private static CurrentSetup instance = new CurrentSetup();

    private CurrentSetup() {}

    public void setEng(String data){
        System.out.println(data);
        this.curEng = data;
    }

    public String getEng(){
        return curEng;
    }

    public void setEngine(String data){
        System.out.println(data);
        this.engine = data;
    }

    public String getEngine(){
        return engine;
    }

    public void setRnd(Float data){
        System.out.println(data);
        this.rnd = data;
    }

    public float getRnd(){
        return rnd;
    }

    public void setFpen(Float data){
        System.out.println(data);
        this.fpen = data;
    }

    public float getFpen(){
        return fpen;
    }

    public void setPpen(Float data){
        System.out.println(data);
        this.ppen = data;
    }

    public float getPpen(){
        return ppen;
    }

    public static CurrentSetup getInstance() {
        return instance;
    }
}
