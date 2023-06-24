package common;

import java.util.Scanner;

public class Parent {
    protected int startingPort = -1;
    protected Scanner scan;

    public Parent(String startingPort) {
        scan = new Scanner(System.in);
        boolean noError = true;
        while (this.startingPort == -1) {
            try {
                if (!noError) {
                    System.out.println("Enter valid starting port number: ");
                    this.startingPort = scan.nextInt();
                }
                this.startingPort = Integer.parseInt(startingPort);
            } catch (Exception e) {
                System.out.println("Invalid Port number " + startingPort);
                noError = false;
            }
        }
    }
}
