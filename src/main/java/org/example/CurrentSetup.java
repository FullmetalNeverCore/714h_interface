package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CurrentSetup {
    private String curEng;

    private String engine;

    private float rnd;

    private float fpen;

    private float ppen;

    private static final Logger logger = LoggerFactory.getLogger(CurrentSetup.class);

    private static CurrentSetup instance = new CurrentSetup();

    private CurrentSetup() {}

    public void setEng(String data){
        logger.debug(data.toString());
        this.curEng = data;
    }

    public String getEng(){
        return curEng;
    }

    public void setEngine(String data){
        logger.debug(data.toString());
        this.engine = data;
    }

    public String getEngine(){
        return engine;
    }

    public void setRnd(Float data){
        logger.debug(data.toString());
        this.rnd = data;
    }

    public float getRnd(){
        return rnd;
    }

    public void setFpen(Float data){
        logger.debug(data.toString());
        this.fpen = data;
    }

    public float getFpen(){
        return fpen;
    }

    public void setPpen(Float data){
        logger.debug(data.toString());
        this.ppen = data;
    }

    public float getPpen(){
        return ppen;
    }

    public static CurrentSetup getInstance() {
        return instance;
    }
}
