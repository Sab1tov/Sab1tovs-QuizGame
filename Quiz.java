package com.example.demo;

import java.io.*;
import java.util.*;


public class Quiz extends Question{
    private String name;
    private static ArrayList<Question> questions = new ArrayList<>();

    Quiz(){
    }
    public static void addQuestion(Question question){
        questions.add(question);
    }
    public static List<Question> loadFromFile(String fileName, boolean check) throws FileNotFoundException {
        Quiz quiz = new Quiz();
        quiz.setName(fileName);
        File file = new File("src/" + fileName);
        Scanner in = new Scanner(file);
        while(in.hasNextLine()){
            String description = in.nextLine();
            if (description.contains("{blank}")) {
                FillIn fillIn = new FillIn();
                fillIn.setDescription(description.replace("{blank}","_____"));
                fillIn.setAnswer(in.nextLine());
                addQuestion(fillIn);
            }

            else {
                Test test = new Test();
                test.setDescription(description);
                String[] options = new String[test.numOfOptions];
                for (int i = 0; i < test.numOfOptions; i++) {
                    options[i] = in.nextLine();
                }
                test.setAnswer(options[0]);
                test.setOptions(options);
                addQuestion(test);
            }
            in.nextLine();
        }
        if(check) {
            Collections.shuffle(questions);
        }
        return questions;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//    public void start(){
//        Scanner input = new Scanner(System.in);
//        int score = 0;
//        int num = 1;
//        for(Question q : questions){
//            System.out.println(num + "." + q);
//            if(q instanceof Test){
//                System.out.println("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
//                System.out.print("Enter the correct choice: ");
//                while (true) {
//                    try {
//                        String ins = input.nextLine();
//                        if(ins.length() > 1) {
//                            System.out.print("Invalid choice! Try again (Ex: A, B, ...):");
//                        }
//                        else {
//                            char usersAns = ins.charAt(0);
//                            int ind = (usersAns - 'A');
//                            String choice = ((Test) q).getOptionAt(ind);
//                            if (choice.equals(q.getAnswer())) {
//                                System.out.println("Correct!");
//                                System.out.println("_____________________________________________________");
//                                score++;
//                            } else {
//                                System.out.println("Incorrect!");
//                                System.out.println("_____________________________________________________");
//                            }
//                            break;
//                        }
//                    }catch (InputMismatchException | ArrayIndexOutOfBoundsException e){
//                        System.out.print("Invalid choice! Try again (Ex: A, B, ...):");
//                    }
//                }
//            }
//            else{
//                System.out.println("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
//                System.out.print("Type your answer: ");
//                String usersAns = input.nextLine();
//                if (usersAns.equals(q.getAnswer())) {
//                    System.out.println("Correct!");
//                    System.out.println("_____________________________________________________");
//                    score++;
//                } else {
//                    System.out.println("Incorrect!");
//                    System.out.println("_____________________________________________________");
//                }
//
//            }
//            num++;
//        }
//        System.out.println();
//        System.out.printf( "Correct Answers: %s/%s (%.1f%c)", score, questions.size(), ((float)(score*100)/(float)(questions.size())), '%');
//    }

    public String toString() {
        return "WELCOME TO \"" + getName() + "\" QUIZ!!!" + "\n" + "_____________________________________________________" ;
    }
}
