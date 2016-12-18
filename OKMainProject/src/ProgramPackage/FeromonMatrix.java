package ProgramPackage;

import ProgramPackage.TaskPackage.Task;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Jarek on 2016-12-05.
 */
public class FeromonMatrix {
    private double[][] feromonMatrix;
    //pionowo nr zadania od którego idziemy - from
    //poziomo nr zadania do którego idziemy - to
    //            to
    //    from |

    /**
     * Konstruktor macierzy, z zerowaniem macierzy
     * @param size - ilość zadań
     */
    public FeromonMatrix(int size) {
        this.feromonMatrix = new double[size][size];
        for (int i =0; i<size; i++){
            for (int k=0; k<size; k++){
                this.feromonMatrix[i][k] = 0;
            }
        }
    }

    public void alignValueInMatrix(){
        //zmniejsza różnice między najbardziej i najmniej wyczuwalnym śladem feromonowym
    }

    /**
     * Dodanie śladów feromonowych na ścieżce z rozwiązania
     * @param tasks - ścieżka rozwiązania
     * @param wspolcznynnik - współczynnik wyliczany na podstawie porównania najlepszych roziązań
     */
    public void addFeromonWay(LinkedList<Task> tasks, double wspolcznynnik){
        for (int i = 0; i<tasks.size()-1 ; i++){
            if (!(tasks.get(i).getTask_name().equals("maintanance")) && !(tasks.get(i+1).getTask_name().equals("maintanance"))){
                addOneValueFeromonToWay(tasks.get(i).getNumber_task(), tasks.get(i+1).getNumber_task(), wspolcznynnik);
            }
        }
    }

    /**
     * Dodaje trochę feromonu na ścieżce pomiędzy zadaniami from->to
     * @param from - od nr zadania
     * @param to - do nr zdania
     */
    private void addOneValueFeromonToWay(int from, int to, double wspolczynnik){
        this.feromonMatrix[from-1][to-1] += 1*wspolczynnik; //wartość do ustalenie
    }

    /**
     * Wybranie użycia macierzy feromonowej, wylosowanie drogi za pomocą algorytmu ruletki
     * @param from - skąd chcę iść
     * @return zwraca krok skąd dokąd rozwiązanie ma przejść
     */
    public int[] useFeromonMatrix(int from){
        //użycie macierzy feromonowej
        int[] step = new int[2];
        step[0] = from; // skąd
        step[1] = ruleteAlgorithm(from); // dokąd
        return step;
    }

    /**
     * Ruletka, tworzy tablicę przedziałów oparta na wartościach rzędu z
     * macierzy następnie losuję wartość od 0 do sumy liczb z rzędu i na podstawie tej liczby wybiera wierzchołek docelowy
     * w wypadku rzędu wypełnionego samymi zerami zwraca -1
     * @param from -rząd z macierzy, komórka skąd
     * @return - zwraca komórkę dokąd lub -1 w wypadku rzędu z zerami
     */
    private int ruleteAlgorithm(int from){
        double[] array_of_section = new double[this.feromonMatrix[from].length];
        for (double x: array_of_section) x=0;
        double value_of_section = 0;

        for (int i=0; i<this.feromonMatrix[from].length; i++){
            if (value_of_section<this.feromonMatrix[from][i]){
                value_of_section+=this.feromonMatrix[from][i];
                array_of_section[i] = value_of_section;
            }else {
                array_of_section[i] = value_of_section;
            }
        }

        Random random = new Random(System.currentTimeMillis());
        //random.nextDouble() zwraca liczbę <0,1> pomnożona przez value_of_section
        // daje wartość którą już możemy sprawdzić w przedziałach wcześniej utworzonych
        double choose_value = random.nextDouble() * value_of_section;

        for (int i =0; i<array_of_section.length; i++){
            if (i == 0){
                if (array_of_section[0] != 0 && array_of_section[0]>= choose_value){
                    System.out.println(feromonMatrix[from][i]);
                    return i;
                }
            }
            else if (array_of_section[i-1] != array_of_section[i]
                    && array_of_section[i-1]<choose_value
                    && array_of_section[i] >= choose_value){
                System.out.println(feromonMatrix[from][i]);
                return i;
            }
        }
        return -1;
    }

    /**
     * Wyświetlenie macierzy feromonowej
     */
    public void displayFeromonMatrix(){
        for (int i=0; i<this.feromonMatrix.length; i++ ){
            for (int k = 0; k<this.feromonMatrix[i].length ; k++){
                System.out.print(this.feromonMatrix[i][k] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Parowanie śladu feromonowego co turę
     */
    public void evaporationFeromonWay(){
        for (int i=0; i<this.feromonMatrix.length; i++){
            for (int k=0; k<this.feromonMatrix[i].length; k++){
                /**
                 * Współcznynnik parowania do ustalenia
                 */
                this.feromonMatrix[i][k] *= 0.9;
            }
        }
    }

}
