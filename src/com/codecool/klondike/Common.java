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
            br.close();
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

    public static int loadGameMode() {
        int mode = 1;
        try {
            BufferedReader br = new BufferedReader(new FileReader("resources/gameMode.txt"));
            String line = br.readLine();
            mode = Integer.parseInt(line);
            br.close();
        }
        catch (IOException e) {
            System.out.println("Settings file damaged");
            }
        return mode;
        }

    
    public static void saveGameMode(int cardsMode){
        try{
            FileWriter fw = new FileWriter("resources/gameMode.txt");
            String setting = String.valueOf(cardsMode);
            fw.write(setting);
            fw.close();
        }
        catch (IOException e){
            System.out.println("Cannot find settings.");
        }
    }

    }
