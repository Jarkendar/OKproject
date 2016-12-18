package ProgramPackage;

import ProgramPackage.TaskPackage.Task;

import java.util.LinkedList;

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
     * @param współcznynnik - współczynnik wyliczany na podstawie porównania najlepszych roziązań
     */
    public void addFeromonWay(LinkedList<Task> tasks, double współcznynnik){
        for (int i = 0; i<tasks.size()-1 ; i++){
            if (!(tasks.get(i).getTask_name().equals("maintanance")) && !(tasks.get(i+1).getTask_name().equals("maintanance"))){
                addOneValueFeromonToWay(tasks.get(i).getNumber_task(), tasks.get(i+1).getNumber_task(), współcznynnik);
            }
        }
    }

    /**
     * Dodaje trochę feromonu na ścieżce pomiędzy zadaniami from->to
     * @param from - od nr zadania
     * @param to - do nr zdania
     */
    private void addOneValueFeromonToWay(int from, int to, double współczynnik){
        this.feromonMatrix[to-1][from-1] += 1*współczynnik; //wartość do ustalenie
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

    private int ruleteAlgorithm(int from){
        int[] array_of_section = new int[this.feromonMatrix[from].length];



        //wylosowanie docelowej ścieżki na podstawie algorytmu ruletki
        return 0;
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
