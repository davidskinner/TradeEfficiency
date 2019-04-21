package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


//class Activity implements Comparable<Activity>
//{
//    int ID;
//    int Start;
//    int Finish;
//    int Value;
//
//    Activity(String id, String start, String finish, String value)
//    {
//        ID = Integer.valueOf(id);
//        Start = Integer.valueOf(start);
//        Finish = Integer.valueOf(finish);
//        Value = Integer.valueOf(value);
//    }
//
//    @Override
//    public int compareTo(Activity activity)
//    {
//        int compareFinish = activity.getFinish();
//        return this.Finish - compareFinish;
//    }
//
//    public int getFinish()
//    {
//        return this.Finish;
//    }
//
//    @Override
//    public String toString()
//    {
//        StringBuilder builder = new StringBuilder();
//
//        builder.append(this.ID).append(" ").append(this.Start).append(" ").append(this.Finish).append(" ").append(this.Value);
//        return builder.toString();
//    }
//}

public class Main
{
    public static void log(String str)
    {
        System.out.println(str);
    }
    

    public static String buildStringOutput(int maxValue, ArrayList<Integer> finalActivityArray, String uniqueness)
    {
        String max = String.valueOf(maxValue);
        String activityString;

        StringBuilder builder = new StringBuilder();

        Collections.sort(finalActivityArray);

        for (int i = 0; i < finalActivityArray.size(); i++)
        {
            builder.append(finalActivityArray.get(i)).append(" ");
        }

        activityString = builder.toString();

        return max + "\n" + activityString + "\n" + uniqueness;
    }

    public static void main(String[] args)
    {

        String inputFile = "";
        String outputFile = "";

        try
        {
            if (args[0] == "" || args[1] == "")
            {
                throw new RuntimeException("Make sure to pass valid strings for file names as arguments to the program. args[0] is the input. args[1] is output.");
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Make sure to pass valid strings for file names as arguments");
        }

        inputFile = args[0];
        outputFile = args[1];

        File file = new File("/Users/davidskinner/Documents/Repositories/LocalTradeEfficiency/" + inputFile);

        int numberOfActivities = 0;
        int interval;
        ArrayList<Activity> activities = new ArrayList<>();

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
                    numberOfActivities = Integer.valueOf(splitter[0]);
                    log("The number of activites is: " + String.valueOf(numberOfActivities));

                    interval = Integer.valueOf(splitter[1]);
                    log("The interval is : 0 to " + String.valueOf(interval));

                } else
                {
                    //import the activities into a list
                    activities.add(new Activity(splitter[0], splitter[1], splitter[2], splitter[3]));
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