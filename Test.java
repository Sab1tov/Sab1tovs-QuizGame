package com.example.demo;



import java.util.ArrayList;
import java.util.Collections;

public class Test extends Question {
    public int numOfOptions = 4;
    private final String[] options = new String[numOfOptions];
    public ArrayList<String> labels = new ArrayList<>();
    public Test(){
        labels.add(Character.toString((char)(65)));
        labels.add(Character.toString((char)(66)));
        labels.add(Character.toString((char)(67)));
        labels.add(Character.toString((char)(68)));
    }
    //A B C D

    public void setOptions(String[] options) {
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < numOfOptions; i++) {
            list.add(options[i]);
        }
        Collections.shuffle(list);
        for (int i = 0; i < numOfOptions; i++) {
            this.options[i] = list.get(i);
        }
    }
    public String getOptionAt(int ind){
        return options[ind];
    }
    public String toString() {
        String temp = getDescription() + "\n";
        for (int i = 0; i < numOfOptions; i++) {
            temp += (labels.get(i) + ") " + getOptionAt(i) + "\n");
        }
        return temp ;
    }
}
