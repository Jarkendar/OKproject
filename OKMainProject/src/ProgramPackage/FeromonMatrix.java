package ProgramPackage;

/**
 * Created by Jarek on 2016-12-05.
 */
public class FeromonMatrix {
    private double[][] feromonMatrix;
    //pionowo nr zadania od którego idziemy - from
    //poziomo nr zadania do którego idziemy - to
    //            to
    //    from |

    public double[][] getFeromonMatrix() {
        return feromonMatrix;
    }

    public void setFeromonMatrix(double[][] feromonMatrix) {
        this.feromonMatrix = feromonMatrix;
    }


    /**
     * Konstruktor macierzy
     * @param size - ilość zadań
     */
    public FeromonMatrix(int size) {
        this.feromonMatrix = new double[size][size];
    }

    public void alignValueInMatrix(){
        //zmniejsza różnice między najbardziej i najmniej wyczuwalnym śladem feromonowym
    }

    /**
     * Dodaje trochę feromonu na ścieżce pomiędzy zadaniami from->to
     * @param from - od nr zadania
     * @param to - do nr zdania
     */
    public void addFeromonToWay(int from, int to){
        feromonMatrix[to][from] += 1; //wartość do ustalenie
    }

    public int[] useFeromonMatrix(){
        //użycie macierzy feromonowej
        int[] ints = new int[2];
        return ints;
    }

    private int ruleteAlgorithm(int from){
        //wylosowanie docelowej ścieżki na podstawie algorytmu ruletki
        return 0;
    }



}
