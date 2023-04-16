package utilities;
import java.util.Scanner;

public class Scan {
    
    private Scanner reader;
    private static Scan singleton = null;
    
    private Scan() {
        reader = new Scanner(System.in);
    }
    
    public static Scan getInstance() {
        if(singleton == null) {
            singleton = new Scan();
        }
        return singleton;
    }
    public String next(){
        return reader.next();
    }
    
    public int nextInt(){
        int input;
        try {
            input = reader.nextInt(); 
            return input;
        } catch (Exception e) {
            return 100;
        }    
    }
    
    public double nextDouble(){
            return reader.nextDouble();
    }
    
    public String nextLine() {
            return reader.nextLine();
    }
    
    public void close() {
        reader.close();
    }   
}
