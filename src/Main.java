import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args){

        Constructor constructor = new Constructor();
        constructor.generateCombinations("Node(Node parent = null, int val)");
        ArrayList<String> combinations = constructor.getCombinations();
        for(String s : combinations){
            System.out.println(s);
        }

    }

}
