package de.lmu.bio.ifi;

import java.util.Scanner;

public class Utilities {

    public static final Scanner scanner = new Scanner(System.in);

    public static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();  // clear invalid input
        }
        int value = scanner.nextInt();
        scanner.nextLine();  // consume the newline
        return value;
    }
}
