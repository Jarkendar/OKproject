/**
 * Created by Jarek on 2016-11-26.
 */
public class main {
    public static void main(String[] args) {
        System.out.println(Integer.toString(addition(1,2)));
        System.out.println("Hello");
        for (int i= 0; i<args.length; i++){
            System.out.println(args[i]);
        }
    }

    /**
     * Dodawnie x+y
     * @param x
     * @param y
     * @return Sum
     */
    public static int addition(int x, int y){
        return x+y;
    }
}
