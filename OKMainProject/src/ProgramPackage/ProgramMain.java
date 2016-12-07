package ProgramPackage;

import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Jarek on 2016-12-06.
 */
public class ProgramMain {
    public static void main(String[] args) {
        int size = 0, inscance_number = 0;
        int count_object = 0, count_maintanance = 0;
        String path_to_instance = "";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj rozmiar instancji do rozwiązania.");
        size = scanner.nextInt();
        System.out.println("Podaj numer instancji do rozwiązania.");
        inscance_number = scanner.nextInt();

        count_object = size;
        count_maintanance = 1 + size/8;

        path_to_instance = createPathToInstance(size, inscance_number);

        Task[] tasks = readInstanceFromFile(path_to_instance, count_object);
        Maintanance[] maintanances = readMaintananceFromFile(path_to_instance, count_object);

        DisplayTest(maintanances,tasks);
    }

    private static void DisplayTest(Maintanance[] maintanances, Task[] tasks){
        for (Task x: tasks) {
            if (x.getPart_number() == 1) {
                System.out.println("Numer zadania " + x.getNumber_task() + "; numer operacji " + x.getPart_number()
                        + "; czas operacji : " + x.getDuration() + "; numer maszyny : "+ x.getMachine_number()
                        + "; czas opóźnienia : "+ x.getTime_start());
            }else{
                System.out.println("Numer zadania " + x.getNumber_task() + "; numer operacji " + x.getPart_number()
                        + "; czas operacji : " + x.getDuration() + "; numer maszyny : "+ x.getMachine_number());
            }
        }
        System.out.println("**********************");
        for (Maintanance x: maintanances){
            System.out.println("Numer przerwy : " + x.getBreak_number() + "; numer maszyny : " + x.getMachine_number()
                        + "; czas trwania przerwy : " + x.getTime_duration() + "; czas rozpoczęcia przerwy : "
                        + x.getTime_start());
        }
    }

    /**
     * Generowanie ścieżki do pliku z zadaną instancją, brak zabezpieczeń przed brakiem konkretnej instancji.
     * @param size - rozmiar = folder zawierający
     * @param number - numer = konkretny numer instancji wejściowej
     * @return - zwraca ścieżkę do konkretnej instancji
     */
    private static String createPathToInstance(int size, int number){
        return ("Instancje\\rozmiar "+size+ "\\nr "+ number +".txt");
    }

    /**
     * Wczytuje tablice zadań z pliku.
     * @param path - ścieżka dostępu do pliku
     * @param count_objects - rozmiar instancji, ilość zadań
     * @return - zwraca tablicę zadań o rozmiarze = rozmiar instancji *2, wypełnioną obiektami części poszczególnych zadań
     */
    private static Task[] readInstanceFromFile(String path, int count_objects) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));
        }catch (FileNotFoundException e){
            System.out.println(e);
        }

        System.out.println(path);
        Task[] tasks = new Task[count_objects*2];
        scanner.nextLine();
        scanner.nextLine();
        for (int i = 0; i<count_objects; i++){
            String tmp = scanner.nextLine();
            String[] strings_array = null;
            strings_array = tmp.split(";");
            System.out.println(strings_array[0] + " " +strings_array[1] + " "
                    +strings_array[2] + " " +strings_array[3] + " "+ strings_array[4]);
            PartFirst partFirst = new PartFirst(i, Integer.parseInt(strings_array[0]),
                    Byte.parseByte(strings_array[2]), (byte) 1, Integer.parseInt(strings_array[4]));
            PartSecond partSecond = new PartSecond(i, Integer.parseInt(strings_array[1]),
                    Byte.parseByte(strings_array[3]), (byte) 2);
            tasks[i] = partFirst;
            tasks[i+count_objects] = partSecond;
        }
        return tasks;
    }

    /**
     * Wczytanie Maintanance'ów z pliku
     * @param path - ścieżka do pliku instancji
     * @param task_count - liczba zadań, tu służy do przesunięcia głowicy
     * @return - zwraca tablicę przerw
     */
    private static Maintanance[] readMaintananceFromFile(String path, int task_count){
        Scanner scanner = null;
        try{
            scanner = new Scanner(new File(path));
        }catch (FileNotFoundException e){
            System.out.println(e);
        }
        Maintanance[] maintanances = new Maintanance[1+task_count/8];
        for (int i = 0; i<(task_count+2); i++){
            scanner.nextLine();
        }
        for (int i = 0; i<(1+task_count/8); i++){
            String tmp = scanner.nextLine();
            tmp = tmp.substring(11);
            String[] strings_array = tmp.split(";");
            Maintanance maintanance = new Maintanance(Integer.parseInt(strings_array[0]),
                    Byte.parseByte(strings_array[1]), Integer.parseInt(strings_array[2]),
                    Integer.parseInt(strings_array[3]));
            maintanances[i] = maintanance;
        }
        return maintanances;
    }


}
