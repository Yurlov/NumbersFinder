
import java.util.*;
public class Runner {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        Utils.getNumbers(Utils.getAllFiles(sc));
        Utils.repeat(sc);
    }

}
