package com.codecool.klondike;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;


public class Common{

    public static ArrayList<String> loadThemeSettings(){
        ArrayList<String> themeSettings = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("resources/theme.txt"));
            String line = br.readLine();
            while (line != null) {
                themeSettings.add(line);
                line = br.readLine();
            }
        }
        catch (IOException e) {
            System.out.println("Settings file damaged");
            }
        return themeSettings;
        }

    public static void saveThemeSettings(ArrayList<String> newSettings){
        try{
            FileWriter fw = new FileWriter("resources/theme.txt");
            for (String setting : newSettings){
                fw.write(setting);
            }
            fw.close();
        }
        catch (IOException e){
            System.out.println("Cannot find settings.");
        }
    }

    }
