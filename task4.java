import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class task4 {
    public static void main(String[] args) {
        //domain and object from AM class 
        int domainC = CL.domainC;
        int objectC = CL.objectC;
        
        //number of columns (N+M)
        int columns = domainC + objectC;
        //Files portion of y-axis
        int files = objectC + 1;


        //Access Matrix Array
        String[][] myArr = CL.myArr;

        System.out.println("Domain Count: " + domainC);
        System.out.println("Object Count: " + objectC);
        
        
        for (int i = files,  j = 1; i < columns + 1; i++){
            myArr[0][i] = "D" + j + "  ";
            j++;
        }


        for (int i = 1; i < domainC + 1; i++) {
            myArr[i][0] = "D" + i + "  ";
            myArr[i][1] = "-->";

            for (int j = 0; j < columns + 1; j++) {
                System.out.printf("%10s", myArr[i][j]);
            }
            System.out.printf("\n");
        }

        //Thread creation
        for (int i = 0; i < domainC; i++){
            CL t = new CL(i);
            t.start();
        }


        
        
    }
    
}


class CL extends Thread {
    static Random r = new Random();
    static int dom = r.nextInt(3,8);
    static int obj = r.nextInt(3,8);
    static int domainC = dom;
    static int objectC = obj;
    
    //number of columns (N+M)
    static int columns = domainC + objectC;
    //Files portion of y-axis
    static int files = objectC + 1;
    

    static String[][] myArr = new String [domainC + 1][columns + 1];


  

    

    static Semaphore countMutex = new Semaphore(1);
    static Semaphore barrierSem = new Semaphore(1);
    static Semaphore[] domainSem = new Semaphore[domainC + 1];



    static {
        for (int i = 0; i < domainC + 1; i++){
            domainSem[i] = new Semaphore(1);
        }
    }

    int tID;
    int domainNum;

    int count = 0;


    public CL(int id) {
        //TODO Auto-generated constructor stub
        tID = id;

    }

}