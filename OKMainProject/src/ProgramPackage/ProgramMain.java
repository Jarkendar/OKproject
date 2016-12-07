package ProgramPackage;

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
        for (int i = 0; i<tasks.length; i++){
            System.out.println("Numer zadania " + tasks[i].getNumber_task() + "; nazwa operacji : " + tasks[i].getTask_name()
                    + "; czas operacji : " + tasks[i].getDuration() + "; numer maszyny : "+ tasks[i].getMachine_number()
                    + "; czas opóźnienia : "+ tasks[i].getTime_delay());
        }
        for (int i = 0; i<maintanances.length; i++){
            System.out.println("Numer przerwy " + maintanances[i].getNumber_task() + "; nazwa operacji : " + maintanances[i].getTask_name()
                    + "; czas przerwy : " + maintanances[i].getDuration() + "; numer maszyny : "+ maintanances[i].getMachine_number()
                    + "; czas opóźnienia : "+ maintanances[i].getTime_delay());
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
                    +strings_array[2] + " " +strings_array[3] + " " + strings_array[4]);
            System.out.println(Integer.parseInt(strings_array[0]) + " " + Integer.parseInt(strings_array[1])+ " "
                    +Byte.parseByte(strings_array[2]) + " " + Byte.parseByte(strings_array[3]) + " "+ Integer.parseInt(strings_array[4]));

            PartFirst partFirst = new PartFirst((i+1), Integer.parseInt(strings_array[0]), Byte.parseByte(strings_array[2])
                    , "part1", Integer.parseInt(strings_array[4]));
            PartSecond partSecond = new PartSecond((i+1), Integer.parseInt(strings_array[1]), Byte.parseByte(strings_array[3])
                    , "part2");
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
            Maintanance maintanance = new Maintanance(Integer.parseInt(strings_array[0]), Integer.parseInt(strings_array[2])
                    , Byte.parseByte(strings_array[1]), "maintanance", Integer.parseInt(strings_array[3]));
            maintanances[i] = maintanance;
        }
        return maintanances;
    }


}
