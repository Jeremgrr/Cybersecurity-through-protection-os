
import java.util.Random;
import java.util.concurrent.Semaphore;



//Randomly fill the 2D-Array and display access list



public class task2 extends Thread {
    public static void main(String[] args) {
        int domainC = AM.domainC;
        int objectC = AM.objectC;

        // Access Matrix Array
        String[][] myArr = AM.myArr;

        System.out.println("Access control scheme: Access List");
        System.out.println("Domain Count: " + domainC);
        System.out.println("Object Count: " + objectC);

        // Fill access matrix
        AM.fillFiles();
        AM.fillDomains();
        AM.sameDomain();

        // Print access list for objects
        for (int i = 1; i <= objectC; i++) {
            System.out.print("F" + i + " --> ");
            boolean firstDomain = true;
            for (int j = 1; j <= domainC; j++) {
                if (!AM.myArr[j][i].equals("   ")) {
                    if (!firstDomain) {
                        System.out.print(", ");
                    }
                    System.out.print("D" + j + ":" + AM.myArr[j][i]);
                    firstDomain = false;
                }
            }
            System.out.println();
        }

        // Print access list for domains
        for (int i = 1; i <= domainC; i++) {
            boolean firstObject = true;
            for (int j = 1; j <= objectC; j++) {
                if (!AM.myArr[i][j].equals("   ")) {
                    if (!firstObject) {
                        System.out.print(", ");
                    }
                    System.out.print("D" + j + ":" + AM.myArr[i][j]);
                    firstObject = false;
                }
            }
            if (!firstObject) {
                System.out.println();
            }
        }

        // Thread creation
        for (int i = 0; i < domainC; i++) {
            AM t = new AM(i);
            t.start();
        }
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
    int domainNum;

    int count = 0;


    public AM(int id) {
        //Auto-generated constructor stub
        tID = id;

    }

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

    public static void sameDomain() {
        for (int i = 1; i < domainC + 1; i++) {
            myArr[i][0] = "   "; // Initialize the first column
            i++;
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



    //create function to switch domains
    public void switchDomain(int a, int b) {
        int domainNum = tID + 1;
        int c = (b - objectC);
        System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Attempting to switch domain: " + "D" + c);
        if( myArr[a][b] == "allow"+ "  ") {
            //System.out.println("Allow switch");

            //now actually switch to the domain using semaphore
           try {
            domainSem[a].release();
            //System.out.println("RELEASED");
            int d = (b - objectC) ;
            //System.out.println("DOMAIN D IS " + d);
            domainSem[d].acquire();
            domainNum = d;
            System.out.println("[Thread: " + tID + "(D" + domainNum + ")] " + "Switched to D" + c);
            //System.out.println(myArr[c][0]);

            //need correct domain location to switch permissions
            

            readObject(c, 1);
            writeObject(c, 1);

                
            } catch (Exception e) {
                //Handle's exception
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
        int randDomain = r.nextInt(objectC + 1, columns + 1);


        //System.out.println("RANDOM NUMBER IS " + randDomain);

        readObject(tID + 1, r1);
        writeObject(tID + 1, r1);
        switchDomain(domainNum, randDomain);
        //System.out.println("SWITCH AT " + domainNum + "," + randDomain);
        

        countMutex.release();
        

    }

}

