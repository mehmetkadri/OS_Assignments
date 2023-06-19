import java.io.*;
import java.util.*;

public class Schedule{
    public static void main(String args[])throws IOException{
        String algorithmName=args[0];					                //taking algorithm name as input
        File file = new File(args[1]);                                  //file to be scanned

        Scanner countElements = new Scanner(file);

        int counter=0;
        while (countElements.hasNextLine()) {                            //takes the task count
            counter++;
            countElements.nextLine();
        }
        countElements.close();

        String[] tasks = new String[counter];

        Scanner sc = new Scanner(file);
        for (int i = 0; i < tasks.length; i++) {                        //fills the array with tasks
            tasks[i] = sc.nextLine();
        }
        sc.close();

        String[][] finalTaskArray= new String[counter][4];
        for(int i=0; i<counter; i++){					                //creates a 2d array where every element is a task and every element inside them are:
            int j=0;							                        //[task name] [arrival time] [priority] [ CPU burst] respectively.
            for(String next:tasks[i].split(", ")){
                finalTaskArray[i][j]=next;
                j++;
            }
        }


        File myObj = new File("output.txt");                        //creates output file
        if (!myObj.createNewFile()) {                                        //if output file already exists, deletes and recreates output file
            myObj.delete();
            myObj.createNewFile();
        }
        System.out.println("\nFile created: " + myObj.getName());
        System.out.println("Have a good day sir, take care...");


        if(algorithmName.equals("fcfs")){
            fcfs(finalTaskArray,myObj);
        }else if(algorithmName.equals("sjf")){
            sjf(finalTaskArray,myObj);
        }else if(algorithmName.equals("pri")){
            pri(finalTaskArray,myObj);
        }else if(algorithmName.equals("rr")){
            rr(finalTaskArray,myObj);
        }else if(algorithmName.equals("pri-rr")){
            priRr(finalTaskArray,myObj);
        }
    }

    public static void fcfs(String[][] finalTaskArray, File myObj)throws IOException{
        FileWriter myWriter = new FileWriter(myObj.getName());
        myWriter.write("First Come First Served Scheduling\n\n");
        String[][] copy = finalTaskArray;

        float turnaroundTime= (float) 0.0;
        float waitingTime= (float) 0.0;

        int timeCounter = 0;
        int timeIncreased = 0;
        int exitCounter = 0;

        while(true){                                                                                //continue until all array is emptied
            if (exitCounter == copy.length){                                                        //if all array is emptied, break
                break;
            }
            for(int a=0; a< copy.length; a++) {                                                     //iterate array
                if(copy[a]!=null) {

                    int i = a;
                    int smallestArrTime = Integer.parseInt(copy[a][1]);

                    for(int b = 0; b< copy.length ; b++){
                        if (copy[b]!=null){
                            if ((Integer.parseInt(copy[b][1]))<smallestArrTime){
                                smallestArrTime=(Integer.parseInt(copy[b][1]));
                                i=b;
                            }
                        }
                    }

                    String taskName = copy[i][0];
                    int arrTime = Integer.parseInt(copy[i][1]);
                    int priority = Integer.parseInt(copy[i][2]);
                    int burst = Integer.parseInt(copy[i][3]);

                    if (arrTime <= timeCounter) {
                        timeCounter += burst;
                        turnaroundTime += timeCounter-arrTime;
                        waitingTime += timeCounter-arrTime-burst;
                        myWriter.write("Will run Name: " + taskName + "\n");
                        myWriter.write("Priority: " + priority + "\n");
                        myWriter.write("Burst: " + burst + "\n");
                        myWriter.write("Task " + taskName + " finished\n" + "\n");
                        copy[i] = null;

                        exitCounter++;
                    }
                }
            }
            if(timeCounter==timeIncreased){                                                     //if there is no process in the cpu and waiting queue, increase time
                timeCounter++;
                timeIncreased=timeCounter;
            }
        }
        turnaroundTime=turnaroundTime/copy.length;
        waitingTime=waitingTime/copy.length;
        myWriter.write("Average turnaround time is: " + turnaroundTime + "\n");
        myWriter.write("Average waiting time is: " + waitingTime + "\n");
        myWriter.close();
    }

    public static void sjf(String[][] finalTaskArray, File myObj)throws IOException{
        FileWriter myWriter = new FileWriter(myObj.getName());
        myWriter.write("Shortest Job First Scheduling\n\n");
        String[][] tempCopy = finalTaskArray;
        String[][] copy=sortbyBurst(tempCopy);

        float turnaroundTime= (float) 0.0;
        float waitingTime= (float) 0.0;

        int timeCounter = 0;
        int timeIncreased = 0;
        int exitCounter = 0;
        int i=0;


        while(true){                                                                            //continue until all array is emptied
            if (exitCounter == copy.length){                                                    //if all array is emptied, break
                break;
            }
            for(int a=0; a< copy.length; a++) {                                                 //iterate array
                if(copy[a]!=null&&copy[i]!=null) {
                    String taskName = copy[i][0];
                    int arrTime = Integer.parseInt(copy[i][1]);
                    int priority = Integer.parseInt(copy[i][2]);
                    int burst = Integer.parseInt(copy[i][3]);

                    if (arrTime <= timeCounter) {
                        timeCounter += burst;
                        turnaroundTime += timeCounter-arrTime;
                        waitingTime += timeCounter-arrTime-burst;
                        myWriter.write("Will run Name: " + taskName + "\n");
                        myWriter.write("Priority: " + priority + "\n");
                        myWriter.write("Burst: " + burst + "\n");
                        myWriter.write("Task " + taskName + " finished" + "\n\n");

                        copy[i] = null;
                        i=-1;
                        exitCounter++;
                        break;
                    }
                }
                i++;
            }
            i=0;
            if(timeCounter==timeIncreased){                                                     //if there is no process in the cpu and waiting queue, increase time
                timeCounter++;
                timeIncreased=timeCounter;
            }
        }
        turnaroundTime=turnaroundTime/copy.length;
        waitingTime=waitingTime/copy.length;
        myWriter.write("Average turnaround time is: " + turnaroundTime + "\n");
        myWriter.write("Average waiting time is: " + waitingTime + "\n");
        myWriter.close();
    }

    public static void pri(String[][] finalTaskArray, File myObj)throws IOException{
        FileWriter myWriter = new FileWriter(myObj.getName());
        myWriter.write("Priority Scheduling\n\n");
        String[][] tempCopy = finalTaskArray;
        String[][] copy=sortbyPrio(tempCopy);
        float turnaroundTime= (float) 0.0;
        float waitingTime= (float) 0.0;

        int timeCounter = 0;
        int timeIncreased = 0;
        int exitCounter = 0;
        int i=0;

        while(true){                                                                        //continue until all array is emptied
            if (exitCounter == copy.length){                                                //if all array is emptied, break
                break;
            }
            for(int a=0; a< copy.length; a++) {                                             //iterate array
                if(copy[a]!=null&&copy[i]!=null) {

                    String taskName = copy[i][0];
                    int arrTime = Integer.parseInt(copy[i][1]);
                    int priority = Integer.parseInt(copy[i][2]);
                    int burst = Integer.parseInt(copy[i][3]);

                    if (arrTime <= timeCounter) {
                        timeCounter += burst;
                        turnaroundTime += timeCounter-arrTime;
                        waitingTime += timeCounter-arrTime-burst;

                        myWriter.write("Will run Name: " + taskName + "\n");
                        myWriter.write("Priority: " + priority + "\n");
                        myWriter.write("Burst: " + burst + "\n");
                        myWriter.write("Task " + taskName + " finished" + "\n\n");

                        copy[i] = null;
                        i=-1;
                        exitCounter++;
                        break;
                    }
                }
                i++;
            }
            i=0;
            if(timeCounter==timeIncreased){                                                     //if there is no process in the cpu and waiting queue, increase time
                timeCounter++;
                timeIncreased=timeCounter;
            }
        }
        turnaroundTime=turnaroundTime/copy.length;
        waitingTime=waitingTime/copy.length;
        myWriter.write("Average turnaround time is: " + turnaroundTime + "\n");
        myWriter.write("Average waiting time is: " + waitingTime + "\n");
        myWriter.close();
    }


    public static void rr(String[][] finalTaskArray, File myObj)throws IOException {
        FileWriter myWriter = new FileWriter(myObj.getName());
        myWriter.write("Round Robin Scheduling\n\n");
        String[][] copy = finalTaskArray;
        copy = sortbyArrTime(copy);
        int quantum = 10;

        float turnaroundTime= (float) 0.0;
        float waitingTime= (float) 0.0;

        int timeCounter = 0;
        int timeIncreased = 0;
        int exitCounter = 0;
        int arrTime = Integer.parseInt(copy[0][1]);
        timeCounter = arrTime;

        int[] burstLeftArray= new int[copy.length];

        for (int burstLeftArrayInitializer=0 ; burstLeftArrayInitializer< copy.length ; burstLeftArrayInitializer++){
            burstLeftArray[burstLeftArrayInitializer]=Integer.parseInt(copy[burstLeftArrayInitializer][3]);
        }

        Queue<String[]> taskLine = new LinkedList<>();

        while (true) {                                                                              //continue until all array is emptied
            if (exitCounter == copy.length) {                                                       //if all array is emptied, break
                break;
            }
            for (int a = 0; a < copy.length; a++) {                                                 //iterate array
                if (copy[a] != null) {

                    for (int taskAdder = 0 ; taskAdder<copy.length ; taskAdder++) {
                        if (copy[taskAdder]!=null && Integer.parseInt(copy[taskAdder][1]) <= timeCounter && !taskLine.contains(copy[taskAdder])) {
                            taskLine.add(copy[taskAdder]);
                        }
                    }

                    String taskName = taskLine.peek()[0];
                    arrTime = Integer.parseInt(taskLine.peek()[1]);
                    int priority = Integer.parseInt(taskLine.peek()[2]);
                    int burstLeft = Integer.parseInt(taskLine.peek()[3]);

                    int taskNumber = 0;

                    for(int counter=0; counter< copy.length; counter++){
                        if(copy[counter]!=null&&copy[counter][0].equals(taskName)){
                            taskNumber = counter;
                        }
                    }

                    if (arrTime <= timeCounter) {
                        myWriter.write("Will run Name: " + taskName + "\n");
                        myWriter.write("Priority: " + priority + "\n");
                        if (burstLeftArray[taskNumber] - quantum <= 0) {
                            timeCounter += burstLeftArray[taskNumber];
                            turnaroundTime += timeCounter-arrTime;
                            waitingTime += timeCounter-arrTime-Integer.parseInt(copy[taskNumber][3]);

                            for (int taskAdder = 0 ; taskAdder<copy.length ; taskAdder++) {
                                if (copy[taskAdder]!=null && Integer.parseInt(copy[taskAdder][1]) <= timeCounter && !taskLine.contains(copy[taskAdder])) {
                                    taskLine.add(copy[taskAdder]);
                                }
                            }

                            burstLeftArray[taskNumber] = 0;
                            myWriter.write("Task " + taskName + " finished" + "\n\n");
                            copy[taskNumber] = null;
                            exitCounter++;
                            taskLine.poll();
                        } else if (burstLeft - quantum > 0) {
                            timeCounter += quantum;
                            for (int taskAdder = 0 ; taskAdder<copy.length ; taskAdder++) {
                                if (copy[taskAdder]!=null && Integer.parseInt(copy[taskAdder][1]) <= timeCounter && !taskLine.contains(copy[taskAdder])) {
                                    taskLine.add(copy[taskAdder]);
                                }
                            }
                            burstLeftArray[taskNumber] =  burstLeftArray[taskNumber] - quantum;
                            taskLine.poll();
                            myWriter.write("Burst left: " + burstLeftArray[taskNumber] + "\n\n");
                        }
                    }
                }
            }
            if (timeCounter == timeIncreased) {                                                     //if there is no process in the cpu and waiting queue, increase time
                timeCounter++;
                timeIncreased = timeCounter;
            }
        }
        turnaroundTime=turnaroundTime/copy.length;
        waitingTime=waitingTime/copy.length;
        myWriter.write("Average turnaround time is: " + turnaroundTime + "\n");
        myWriter.write("Average waiting time is: " + waitingTime + "\n");
        myWriter.close();
    }


    public static void priRr(String[][] finalTaskArray, File myObj)throws IOException{

        FileWriter myWriter = new FileWriter(myObj.getName());
        myWriter.write("Priority with Round Robin Scheduling\n\n");
        String[][] copy = finalTaskArray.clone();
        String[][] copy2 = finalTaskArray.clone();
        copy2 = sortbyArrTime(copy2);
        copy = sortbyPrio(copy);
        int quantum=10;

        float turnaroundTime= (float) 0.0;
        float waitingTime= (float) 0.0;

        int timeCounter = 0;
        int timeIncreased = 0;
        int exitCounter = 0;
        int arrTime = Integer.parseInt(copy2[0][1]);
        timeCounter = arrTime;

        int[] burstLeftArray= new int[copy.length];

        for (int burstLeftArrayInitializer=0 ; burstLeftArrayInitializer< copy.length ; burstLeftArrayInitializer++){
            burstLeftArray[burstLeftArrayInitializer]=Integer.parseInt(copy[burstLeftArrayInitializer][3]);
        }

        Queue<String[]> taskLine = new LinkedList<>();
        int priority = 0;

        String[] lastPrinted = new String[4];

        while (true) {                                                                          //continue until all array is emptied
            if (exitCounter == copy.length) {                                                   //if all array is emptied, break
                break;
            }
            for (int a = 0; a < copy.length; a++) {                                             //iterate array
                if (copy[a] != null) {

                    for (int taskAdder = 0 ; taskAdder<copy.length ; taskAdder++) {
                        if (copy[taskAdder]!=null && Integer.parseInt(copy[taskAdder][1]) <= timeCounter && !taskLine.contains(copy[taskAdder])) {

                            if(!taskLine.isEmpty()){
                                if ((Integer.parseInt(taskLine.peek()[2])<Integer.parseInt(copy[taskAdder][2]))){
                                    taskLine.add(copy[taskAdder]);
                                }else if((Integer.parseInt(taskLine.peek()[2])==Integer.parseInt(copy[taskAdder][2]))){
                                    taskLine.add(copy[taskAdder]);
                                }
                            }else if(taskLine.isEmpty()){
                                taskLine.add(copy[taskAdder]);
                            }

                        }
                    }
                    String taskName = taskLine.peek()[0];
                    arrTime = Integer.parseInt(taskLine.peek()[1]);
                    priority = Integer.parseInt(taskLine.peek()[2]);
                    int burstLeft = Integer.parseInt(taskLine.peek()[3]);

                    int taskNumber = 0;

                    for(int counter=0; counter< copy.length; counter++){
                        if(copy[counter]!=null&&copy[counter][0].equals(taskName)){
                            taskNumber = counter;
                        }
                    }
                    if (arrTime <= timeCounter) {
                        myWriter.write("Will run Name: " + taskName + "\n");
                        myWriter.write("Priority: " + priority + "\n");

                        if (burstLeftArray[taskNumber] - quantum <= 0) {
                            timeCounter += burstLeftArray[taskNumber];
                            turnaroundTime += timeCounter-arrTime;
                            waitingTime += timeCounter-arrTime-Integer.parseInt(copy[taskNumber][3]);

                            for (int taskAdder = 0 ; taskAdder<copy.length ; taskAdder++) {
                                if (copy[taskAdder]!=null && Integer.parseInt(copy[taskAdder][1]) <= timeCounter && !taskLine.contains(copy[taskAdder])) {
                                    if((!taskLine.isEmpty())&&(Integer.parseInt(taskLine.peek()[2])<Integer.parseInt(copy[taskAdder][2]))){
                                        taskLine.add(copy[taskAdder]);

                                    }else if((!taskLine.isEmpty())&&(Integer.parseInt(taskLine.peek()[2])==Integer.parseInt(copy[taskAdder][2]))){
                                        if(lastPrinted[0]==(copy[taskAdder][0])){
                                            continue;

                                        }else if(Integer.parseInt(taskLine.peek()[1])>Integer.parseInt(copy[taskAdder][1])){
                                            taskLine.add(copy[taskAdder]);

                                        }
                                    }
                                }
                            }

                            lastPrinted=taskLine.peek();

                            burstLeftArray[taskNumber] = 0;
                            myWriter.write("Task " + taskName + " finished" + "\n\n");
                            copy[taskNumber] = null;
                            exitCounter++;
                            taskLine.poll();

                        } else if (burstLeft - quantum > 0) {
                            timeCounter += quantum;

                            for (int taskAdder = 0 ; taskAdder<copy.length ; taskAdder++) {
                                if (copy[taskAdder]!=null && Integer.parseInt(copy[taskAdder][1]) <= timeCounter && !taskLine.contains(copy[taskAdder])) {
                                    if((!taskLine.isEmpty())&&(Integer.parseInt(taskLine.peek()[2])<Integer.parseInt(copy[taskAdder][2]))){
                                        taskLine.add(copy[taskAdder]);

                                    }else if((!taskLine.isEmpty())&&(Integer.parseInt(taskLine.peek()[2])==Integer.parseInt(copy[taskAdder][2]))){
                                        if(lastPrinted[0]==(copy[taskAdder][0])){
                                            continue;

                                        }else if(Integer.parseInt(taskLine.peek()[1])>Integer.parseInt(copy[taskAdder][1])){
                                            taskLine.add(copy[taskAdder]);

                                        }
                                    }
                                }
                            }

                            lastPrinted=taskLine.peek();

                            burstLeftArray[taskNumber] =  burstLeftArray[taskNumber] - quantum;
                            taskLine.poll();

                            myWriter.write("Burst left: " + burstLeftArray[taskNumber] + "\n\n");
                        }
                    }
                }
            }
            if (timeCounter == timeIncreased) {                                                     //if there is no process in the cpu and waiting queue, increase time
                timeCounter++;
                timeIncreased = timeCounter;
            }
        }
        turnaroundTime=turnaroundTime/copy.length;
        waitingTime=waitingTime/copy.length;
        myWriter.write("Average turnaround time is: " + turnaroundTime + "\n");
        myWriter.write("Average waiting time is: " + waitingTime + "\n");
        myWriter.close();
    }



    public static String[][] sortbyArrTime(String[][] arr) {
        String[] temp = new String[4];
        for (int i=0 ; i< arr.length ; i++) {
            for (int j=i+1 ; j <arr.length ; j++ ){
                if (Integer.parseInt(arr[i][1]) > Integer.parseInt(arr[j][1])) {
                    temp = arr[i];
                    arr[i]=arr[j];
                    arr[j]=temp;
                }
            }
        }
        return arr;
    }

    public static String[][] sortbyPrio(String[][] arr) {
        String[] temp = new String[4];
        for (int i=0 ; i< arr.length ; i++) {
            for (int j=i+1 ; j <arr.length ; j++ ){
                if (Integer.parseInt(arr[i][2]) < Integer.parseInt(arr[j][2])) {
                    temp = arr[i];
                    arr[i]=arr[j];
                    arr[j]=temp;
                }
            }
        }
        return arr;
    }

    public static String[][] sortbyBurst(String[][] arr) {
        String[] temp = new String[4];
        for (int i=0 ; i< arr.length ; i++) {
            for (int j=i+1 ; j <arr.length ; j++ ){
                if (Integer.parseInt(arr[i][3]) > Integer.parseInt(arr[j][3])) {
                    temp = arr[i];
                    arr[i]=arr[j];
                    arr[j]=temp;
                }
            }
        }
        return arr;
    }
}