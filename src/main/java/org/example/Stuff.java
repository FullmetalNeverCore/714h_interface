package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.net.InetAddress;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Stuff {

    private static final Logger logger = LoggerFactory.getLogger(Stuff.class);

    private Config config = Config.getInstance();

    public static boolean containsAny(String mainString, String searchString) {
        if (searchString.isEmpty()){
            return true;
        }
        if (mainString.contains(searchString)){
            return true;
        }
        return false;
    }


    //Kill Bi--Threads
    public void StopThreads(ArrayList<ExecutorService> arr){
        logger.warn("Shutting down...");
        for (ExecutorService x : arr){
            if (x != null) {
                x.shutdown();
                try {
                    if (!x.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                        x.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    x.shutdownNow();
                }
            }
        }
    }

    public StringBuilder Ping(){

            StringBuilder output = new StringBuilder();

            try {
                String commandOutput = "";
                String line;


                for(String x : new ArrayList<String>(Arrays.asList("google.com",config.getIP()))){
                    logger.debug(String.format("%s",x));
                    Process process = Runtime.getRuntime().exec(String.format("ping -c 1 %s",x));
                    BufferedReader inputStream = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    while ((line = inputStream.readLine()) != null) {
                        commandOutput += line + "\n";

                    }
                    output.append(commandOutput+'\n');
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        return output;
    }
}
