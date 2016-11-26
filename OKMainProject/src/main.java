/**
 * Created by Jarek on 2016-11-26.
 */
public class main {
    public static void main(String[] args) {
        System.out.println(Integer.toString(addition(1,2)));
        System.out.println("Hello");
        for(i:args){
            System.out.println(i);
        }
    }


    public static int addition(int x, int y){
        return x+y;
    }
}
