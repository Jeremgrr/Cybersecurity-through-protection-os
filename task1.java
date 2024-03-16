import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/*
 * TODO's
 * 1. randomly fill the 2D-Array
 * 2. 
 */

public class task1 extends Thread{
    public static void main(String[] args) {
        //domain and object from AM class 
        int domainC = AM.domainC;
        int objectC = AM.objectC;
        
        //number of columns (N+M)
        int columns = domainC + objectC;
        //Files portion of y-axis
        int files = objectC + 1;


        //Access Matrix Array
        String[][] myArr = AM.myArr;

        System.out.println("Domain Count: " + domainC);
        System.out.println("Object Count: " + objectC);

        
        System.out.println("Domain/Object  ");
        System.out.println();


        for (int i = 0; i < files;i++){
            myArr[0][i] = "F" + i + "  ";
        }

        for (int i = files,  j = 1; i < columns + 1; i++){
            myArr[0][i] = "D" + j + "  ";
            j++;
        }

        AM.fillFiles();
        AM.fillDomains();
        AM.sameDomain();


        for (int i = 0; i < domainC + 1; i++) {
            myArr[i][0] = "D" + i + "  ";
            myArr[0][0] = "   ";

            for (int j = 0; j < columns + 1; j++) {
                System.out.printf("%10s", myArr[i][j]);
            }
            System.out.printf("\n");
        }


        //Thread creation
        for (int i = 0; i < domainC; i++){
            AM t = new AM(i);
            t.start();
        }
        /* 
        System.out.println("Domain: " + domainC);
        System.out.println("Object: " + objectC);
        System.out.println("Files: " + files);
        */

        

        


        
        

    }
}


class AM extends Thread {
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

    int count = 0;


    public AM(int id) {
        //TODO Auto-generated constructor stub
        tID = id;
    }
    /* 
    public static int domainInput(){
        //User Inputs
        Scanner di = new Scanner(System.in);
        System.out.print("Domain Count: ");
        int domain = di.nextInt();
        int num = domain;

        return num;

    }

    public static int objectCount(){
        //User inputs
        Scanner ob = new Scanner(System.in);
        System.out.print("Object Count:  ");
        int object = ob.nextInt();
        int num =  object;
       
       
    

        return num;

    }
    */

    //fills access matrix with random values
    public static void fillFiles(){
        Random r = new Random();
        for (int i = 1; i < domainC + 1; i++){
            for (int j = 1; j < files ; j++){
                int r1 = r.nextInt(0,4);
                if (r1 == 0){
                    myArr[i][j] =  "   ";
                }else if(r1 == 1){
                    myArr[i][j] = "R"+ "  ";
                }else if(r1 == 2){
                    myArr[i][j] = "W"+ "  ";
                }else{
                    myArr[i][j] = "R/W"+ "  ";

                }

            }
          
        }
    }

    public static void fillDomains(){
        Random r = new Random();
        for (int i = 1; i < domainC + 1; i++){
            for (int j = files; j < columns + 1 ; j++){
                int r1 = r.nextInt(0,2);
                if (r1 == 0){
                    myArr[i][j] =  "   ";
                }else{
                    myArr[i][j] = "allow"+ "  ";
                }

            }
          
        }
    }

    public static void sameDomain(){
        for (int i = 1, j = files; i < domainC + 1; i++){
            myArr[i][j] = "N/A";
            j++;
            }
          
        }


    public String readObject (int a, int b){
        int domainNum = tID + 1;
        System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Attempting to read resource: " + "F" + b);
        if ( myArr[a][b] == "R"+ "  " || myArr[a][b] == "R/W"+ "  "){
            System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Access Granted, Reading object ");
            System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Thread read " + myArr[a][b]);
        }
        return "";
    }

    public String writeObject (int a, int b){
        int domainNum = tID + 1;
        System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Attempting to write resource: " + "F" + b);
        if(myArr[a][b] == "W"+ "  " || myArr[a][b] == "R/W"+ "  "){
            System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Access Granted, Writing object at " + "F" + b);
            myArr[a][b] = "TestWrite";
            System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Thread wrote " + myArr[a][b]);
        }
        return "";
    }

    
/* 
    //create function to read
    public String readWriteObject (int a, int b){
        if ( myArr[a][b] == "R"+ "  "){
            System.out.println("Access Granted, Reading object ");
            System.out.println("Thread read" + myArr[a][b]);


        } else if(myArr[a][b] == "W"+ "  "){
            System.out.println("Access Granted, Writing object at " + myArr[a][b]);
            myArr[a][b] = "TestWrite";
            System.out.println("Thread wrote " + myArr[a][b]);
        } else if (myArr[a][b] == "R/W"+ "  "){
            System.out.println("Access Granted, Reading & Writing at " + myArr[a][b]);
            myArr[a][b] = "TestReadWrite";
            System.out.println("Thread wrote & read " + myArr[a][b]);
        } else {
            //object either empty or equal to null
            System.out.println("Access Denied");

        }
        
        return "";
    }
*/





    //create function to switch domains
    public void switchDomain(int a, int b) {
        int domainNum = tID + 1;
        int c = b % domainC;
        System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Attempting to switch domain: " + "D" + c);
        if( myArr[a][b] == "allow"+ "  ") {
            //System.out.println("Allow switch");

            //now actually switch to the domain using semaphore
           try {
            domainSem[a].release();
            //System.out.println("RELEASED");
            int d = b % domainC;
            //System.out.println("DOMAIN D IS " + d);
            domainSem[d].acquire();
            domainNum = d;
            System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Switched to D" + c);
                
            } catch (Exception e) {
                // TODO: handle exception
                Thread.currentThread().interrupt();
            }
            
            
            
        }

    }





    //create arbitrator function -> checks if your allowed to R/W/Switch

    @Override
    public void run(){
        //Update the count of threads that have started
        countMutex.acquireUninterruptibly();
        count++;
        int domainNum = tID + 1;
        System.out.println("Domain " + tID + " is available." + "(D" + domainNum +")");
        Random r = new Random();
        int r1 = r.nextInt(1,domainC + 1);
        int randDomain = r.nextInt(domainC + 1, columns + 1);


        //System.out.println("RANDOM NUMBER IS " + randDomain);

        readObject(tID + 1, r1);
        writeObject(tID + 1, r1);
        switchDomain(tID + 1, randDomain);

        countMutex.release();
        

    }

}