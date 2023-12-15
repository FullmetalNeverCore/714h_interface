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

public class Stuff {

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
        System.out.println("Shutting down...");
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
                    System.out.println(x);
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
