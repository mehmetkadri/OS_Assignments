import java.io.*;  
import java.util.*;  

public class Allocator{     
    public static void main(String args[])throws IOException{

        List<String> memoryList = new ArrayList<String>();
        List<String> memoryListCopy = new ArrayList<String>();

        File file = new File(args[0]);

        Scanner sc = new Scanner(file);

        String[] memory = (sc.nextLine().split(","));
        for(String memo:memory){
            memoryList.add(memo);
        }

        String[] sizeOfProcesses = (sc.nextLine().split(","));

        File myObj = new File("output.txt");
        if (!myObj.createNewFile()) {
            myObj.delete();
            myObj.createNewFile();
        }
        System.out.println("\nFile created: " + myObj.getName());
        System.out.println("Have a good day sir, take care...");

        FileWriter myWriter = new FileWriter(myObj.getName());

        backup(memoryList,memoryListCopy);
        firstFit(memoryList, sizeOfProcesses, myWriter);
        restore(memoryList,memoryListCopy);
        bestFit(memoryList, sizeOfProcesses, myWriter);
        restore(memoryList,memoryListCopy);
        worstFit(memoryList, sizeOfProcesses, myWriter);
        myWriter.close();
    }


    public static void firstFit(List<String> memoryList, String[] sizeOfProcesses, FileWriter myWriter)throws IOException {
        myWriter.write("First-Fit Memory Allocation\n-----------------------------------------------------------------------------------------------\n");
        myWriter.write("start\t=> ");
        for(String m:memoryList){
            myWriter.write(m + " ");
        }
        myWriter.write("\n");
        boolean added;
        for(String s:sizeOfProcesses){
            int indexCounter = 0;
            myWriter.write(s + "\t=> ");
            added = false;

            for(String m:memoryList){
                if((!m.contains("*"))&&Integer.parseInt(s)<Integer.parseInt(m)){
                    int diff = ((Integer.parseInt(m))-(Integer.parseInt(s)));
                    memoryList.add(indexCounter,(s + "*"));
                    memoryList.remove(indexCounter+1);
                    memoryList.add(indexCounter+1,Integer.toString(diff));
                    added = true;
                    break;
                }else if((!m.contains("*"))&&Integer.parseInt(s)==Integer.parseInt(m)){
                    memoryList.add(indexCounter,(s + "*"));
                    memoryList.remove(indexCounter+1);
                    added = true;
                    break;
                }
                indexCounter++;
            }
            if(added){
                for(String stringMemo:memoryList){
                    myWriter.write(stringMemo + " ");
                }
            }else{
                myWriter.write("not allocated, must wait");
            }

            myWriter.write("\n");
        }
    }

    public static void bestFit(List<String> memoryList, String[] sizeOfProcesses, FileWriter myWriter)throws IOException {
        myWriter.write("\n\nBest-Fit Memory Allocation\n-----------------------------------------------------------------------------------------------\n");
        myWriter.write("start  => ");
        for(String m:memoryList){
            myWriter.write(m + " ");
        }
        myWriter.write("\n");

        int smallestFitIndex;
        int smallestFit = 0;


        for(String s:sizeOfProcesses){                                                  //115,500,358,200,375
            myWriter.write(s + "\t=> ");
            smallestFitIndex = -1;

            for(String m:memoryList) {
                if ((!m.contains("*")) && Integer.parseInt(s) < Integer.parseInt(m)) {
                    smallestFit = Integer.parseInt(m);
                }
            }

            boolean equals = false;
            for(String m:memoryList){                                                   //300,600,350,200,750,125
                if((!m.contains("*"))&&Integer.parseInt(s)<Integer.parseInt(m)) {
                    if(Integer.parseInt(m)<=smallestFit){
                        smallestFitIndex = memoryList.indexOf(m);
                        smallestFit = Integer.parseInt(m);
                    }
                }else if((!m.contains("*"))&&Integer.parseInt(s)==Integer.parseInt(m)) {
                    if(Integer.parseInt(m)<=smallestFit){
                        smallestFitIndex = memoryList.indexOf(m);
                        smallestFit = Integer.parseInt(m);
                        equals = true;
                    }
                }
            }
            if(smallestFitIndex!=-1&&(!equals)){
                int diff = ((Integer.parseInt(memoryList.get(smallestFitIndex)))-(Integer.parseInt(s)));
                memoryList.add(smallestFitIndex,(s + "*"));
                memoryList.remove(smallestFitIndex+1);
                memoryList.add(smallestFitIndex+1,Integer.toString(diff));
                for(String stringMemo:memoryList){
                    myWriter.write(stringMemo + " ");
                }
            }else if(smallestFitIndex!=-1&&equals){
                memoryList.add(smallestFitIndex,(s + "*"));
                memoryList.remove(smallestFitIndex+1);
                for(String stringMemo:memoryList){
                    myWriter.write(stringMemo + " ");
                }
            }else{
                myWriter.write("not allocated, must wait");
            }

            myWriter.write("\n");
        }
    }

    public static void worstFit(List<String> memoryList, String[] sizeOfProcesses, FileWriter myWriter)throws IOException {
        myWriter.write("\n\nWorst-Fit Memory Allocation\n-----------------------------------------------------------------------------------------------\n");
        myWriter.write("start  => ");
        for(String m:memoryList){
            myWriter.write(m + " ");
        }
        myWriter.write("\n");
        int biggestFitIndex;
        int biggestFit = 0;


        for(String s:sizeOfProcesses){                                                  //115,500,358,200,375
            myWriter.write(s + "\t=> ");
            biggestFitIndex = -1;

            for(String m:memoryList) {
                if ((!m.contains("*")) && Integer.parseInt(s) < Integer.parseInt(m)) {
                    biggestFit = Integer.parseInt(m);
                }
            }

            boolean equals = false;
            for(String m:memoryList){                                                   //300,600,350,200,750,125
                if((!m.contains("*"))&&Integer.parseInt(s)<Integer.parseInt(m)) {
                    if(Integer.parseInt(m)>=biggestFit){
                        biggestFitIndex = memoryList.indexOf(m);
                        biggestFit = Integer.parseInt(m);
                    }
                }else if((!m.contains("*"))&&Integer.parseInt(s)==Integer.parseInt(m)) {
                    if(Integer.parseInt(m)>=biggestFit){
                        biggestFitIndex = memoryList.indexOf(m);
                        biggestFit = Integer.parseInt(m);
                        equals = true;
                    }
                }
            }
            if(biggestFitIndex!=-1&&(!equals)){
                int diff = ((Integer.parseInt(memoryList.get(biggestFitIndex)))-(Integer.parseInt(s)));
                memoryList.add(biggestFitIndex,(s + "*"));
                memoryList.remove(biggestFitIndex+1);
                memoryList.add(biggestFitIndex+1,Integer.toString(diff));
                for(String stringMemo:memoryList){
                    myWriter.write(stringMemo + " ");
                }
            }else if(biggestFitIndex!=-1&&equals){
                memoryList.add(biggestFitIndex,(s + "*"));
                memoryList.remove(biggestFitIndex+1);
                for(String stringMemo:memoryList){
                    myWriter.write(stringMemo + " ");
                }
            }else{
                myWriter.write("not allocated, must wait");
            }

            myWriter.write("\n");
        }
    }

    public static void backup(List<String> memoryList,List<String> memoryListCopy){
        memoryListCopy.clear();
        for(String s:memoryList){
            memoryListCopy.add(s);
        }
    }

    public static void restore(List<String> memoryList,List<String> memoryListCopy){
        memoryList.clear();
        for(String s:memoryListCopy){
            memoryList.add(s);
        }
    }
}