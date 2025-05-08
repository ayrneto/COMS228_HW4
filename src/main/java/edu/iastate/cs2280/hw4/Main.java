package edu.iastate.cs2280.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** @author Ayr Nasser Neto */

public class Main {
    public static void main(String[] args) throws IOException {

        String filepath;
        String SchemeString1;
        String SchemeString2 = "";
        String SchemeStringFinal;
        String binaryCode;
        String singleChars;

        int lastNewLineIndex;

        System.out.println("File path:");
        Scanner scanner = new Scanner(System.in);
        filepath = scanner.next();
        scanner.close();

        File file = new File(filepath);


        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        SchemeString1 = scanner.nextLine();

        if(scanner.nextLine().charAt(0) == '^'){
            scanner.close();

            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            scanner.nextLine();
            SchemeString2 = scanner.nextLine();

            scanner.close();
        }

        // Right here, we have SchemeString1 with the first line of file
        // If there are two for the scheme, then SchemeString2 is also used

        // Now add them
        SchemeStringFinal = SchemeString1 + "\n";
        if(!SchemeString2.isEmpty()){
            SchemeStringFinal += SchemeString2 + "\n";
        }


        // Get index of where binary code starts
        String wholeFile = new String(Files.readAllBytes(Paths.get(filepath))).trim();
        lastNewLineIndex = wholeFile.lastIndexOf('\n');

        // Get all binary code
        binaryCode = wholeFile.substring(lastNewLineIndex).trim();

        Set<Character> chars = new HashSet<>();
        for (char ch : SchemeStringFinal.toCharArray()) {
            if (ch != '^') {
                chars.add(ch);
            }
        }

        singleChars = chars.stream().map(String::valueOf).collect(Collectors.joining());
        MsgTree root = new MsgTree(SchemeStringFinal);
        MsgTree.printCodes(root, singleChars);
        root.decode(root, binaryCode);
        root.statistics(binaryCode);
    }
}
