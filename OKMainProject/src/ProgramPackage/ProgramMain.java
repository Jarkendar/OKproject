package ProgramPackage;

import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;

import java.util.Scanner;

/**
 * Created by Jarek on 2016-12-06.
 */
public class ProgramMain {
    public static void main(String[] args) {
        int size = 0, inscance_number = 0;
        String path_to_instance = "";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj rozmiar instancji do rozwiązania.");
        size = scanner.nextInt();
        System.out.println("Podaj numer instancji do rozwiązania.");
        inscance_number = scanner.nextInt();

        path_to_instance = createPathToInstance(size, inscance_number);


    }

    private static String createPathToInstance(int size, int number){
        return ("Instancje\\rozmiar "+size+ "\\nr "+ number +".txt");
    }




}
