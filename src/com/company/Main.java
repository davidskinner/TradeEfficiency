package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;





public class Main
{
    public static void log(String str)
    {
        System.out.println(str);
    }

    public static void main(String[] args)
    {

        String inputFile = "";
        String outputFile = "";

        try
        {
            if (args[0] == "")
            {
                throw new RuntimeException("args[0] is the input file name");
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Make sure to pass valid strings for file names as arguments");
        }

        inputFile = args[0];

        String str= inputFile;
        String number = str.replaceAll("[^0-9]", "");

        outputFile = "output" + number + ".txt";

        File file = new File("/Users/davidskinner/Documents/Repositories/LocalTradeEfficiency/" + inputFile);

        int numberOfVertices = 0;
//        ArrayList<Activity> activities = new ArrayList<>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            int counter = 0;
            while ((st = br.readLine()) != null)
            {
                String[] splitter = st.split(" ");

                if (counter == 0)
                {
                    // read in first line
                    numberOfVertices = Integer.valueOf(splitter[0]);
                    log("The number of vertices is: " + String.valueOf(numberOfVertices));

                } else
                {
                    //import the activities into a list
//                    activities.add(new Activity(splitter[0], splitter[1], splitter[2], splitter[3]));
                }
                System.out.println(st);

                counter++;
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        log("");
    }
}