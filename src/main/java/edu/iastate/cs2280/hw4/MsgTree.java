package edu.iastate.cs2280.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author Ayr Nasser Neto
 */

public class MsgTree {

    public char payloadChar;
    public MsgTree left;
    public MsgTree right;

    // Used to determine where tree is traversing
    // 0 is left, 1 is right
    public int LeftOrRight = 0;

    // Strings for scheme (2nd one only used if there are 2 lines)
    public String SchemeString1 = "";
    public String SchemeString2 = "";

    // String used to store char's binary
    static String charBinary = "";

    public StringBuilder stringBuilder = new StringBuilder();

    /**
     * Constructor for building tree from input String
     * @param encodingString Input encoding String
     */
    public MsgTree(String encodingString){
        Stack<MsgTree> stack = new Stack<>();
        int index = 0;
        payloadChar = encodingString.charAt(index++);
        MsgTree currentInstance = this;

        stack.push(this);

        while(index < encodingString.length()){
            MsgTree node = new MsgTree(encodingString.charAt(index++));

            if(LeftOrRight == 0){
                currentInstance.left = node;
                if(node.payloadChar == '^'){
                    currentInstance = stack.push(node);
                    LeftOrRight = 0;
                }

                else if(!stack.isEmpty()){
                    currentInstance = stack.pop();
                    LeftOrRight = 1;
                }

                else{
                    LeftOrRight = 1;
                }

            }

            else{
                currentInstance.right = node;

                if(node.payloadChar == '^'){
                    currentInstance = stack.push(node);
                    LeftOrRight = 0;
                }

                else if(!stack.isEmpty()){
                    currentInstance = stack.pop();
                    LeftOrRight = 1;
                }

                else{
                    LeftOrRight = 1;
                }
            }
        }
    }

    /**
     * Second constructor for single node
     * @param payloadChar single node with null children
     */
    public MsgTree(char payloadChar){
        this.payloadChar = payloadChar;
        this.left = null;
        this.right = null;
    }


    // Method to get scheme string (PROB USE ON MAIN)
    public void getSchemeString(File file) throws FileNotFoundException {

        Scanner scanner = new Scanner(file);

        SchemeString1 = scanner.nextLine();

        if(scanner.nextLine().charAt(0) == '^'){
            scanner.close();

            scanner = new Scanner(file);
            scanner.nextLine();
            SchemeString2 = scanner.nextLine();

            scanner.close();
        }
    }

    public static boolean getBinaryString(MsgTree root, char ch, String BSTpath){
        boolean leftIteration;
        boolean rightIteration;

        if(root.payloadChar == ch){
            charBinary = BSTpath;
            return true;
        }

        else{
            leftIteration = getBinaryString(root.left, ch, BSTpath + "0");
            rightIteration = getBinaryString(root.right, ch, BSTpath + "1");

            return leftIteration || rightIteration;
        }
    }

    // Method to print characters and their binary codes
    public static void printCodes(MsgTree root, String code){
        System.out.println("character code");
        System.out.println("-------------------------");

        for(int i = 0; i < code.length(); i++){
            if(root.payloadChar == code.charAt(i)){
                getBinaryString(root, code.charAt(i), charBinary + "");
                System.out.println((code.charAt(i) == '\n' ? "\\n" : code.charAt(i) + " ") + "      " + charBinary);
            }
        }
    }

    // Method to print decoded message
    public void decode(MsgTree codes, String msg){
        MsgTree currentInstance = codes;
        //StringBuilder stringBuilder = new StringBuilder();
        char ch;

        System.out.println("MESSAGE:");

        for(int i = 0; i < msg.length(); i++){
            ch = msg.charAt(i);

            if(ch == '0'){
                currentInstance = currentInstance.left;
            }
            else{
                currentInstance = currentInstance.right;
            }

            if(currentInstance.payloadChar != '^'){
                getBinaryString(codes, currentInstance.payloadChar, charBinary = "");
                stringBuilder.append(currentInstance.payloadChar);
                currentInstance = codes;
            }
        }

        System.out.println(stringBuilder);

    }

    public void statistics(String encoded){
        double bitsPerChar = encoded.length() / ((double)stringBuilder.toString().length());
        double spaceSaved = (1.0 - stringBuilder.toString().length() / (double)encoded.length()) * 100;

        System.out.println("STATISTICS:");
        System.out.printf ("Avg bits/char:        %.1f", bitsPerChar);
        System.out.println("Total characters:     " + stringBuilder.toString().length());
        System.out.printf("Space savings:      %.1f", spaceSaved);
    }

}
