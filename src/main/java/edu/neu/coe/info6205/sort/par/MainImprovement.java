package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class MainImprovement {

    public static void main(String[] args) {
        processArgs(args);
        int thread = 2;
        while(thread < 128) {
        	ForkJoinPool pool = new ForkJoinPool(thread);
            System.out.println("Degree of parallelism: " + pool.getParallelism());
            //System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
            Random random = new Random();            
            ArrayList<Long> timeList = new ArrayList<>();
            for(int arraysize = 30000; arraysize < 2100000; arraysize *= 2)
            {
            	System.out.println("Size of Array: " + arraysize);
            	int[] array = new int[arraysize];
            	for (int j = 50; j < 100; j++) {         
                    ParSort.cutoff = 10000 * (j + 1);
                // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                    long time;
                    long startTime = System.currentTimeMillis();
                    for (int t = 0; t < 10; t++) {
                        for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                        ParSort.sort(array, 0, array.length);
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    timeList.add(time);
                    System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10 times Time: " + time + "ms");
                    //System.out.println(time);
                } try {
                    FileOutputStream fis = new FileOutputStream("./src/result.csv");
                    OutputStreamWriter isr = new OutputStreamWriter(fis);
                    BufferedWriter bw = new BufferedWriter(isr);
                    int j = 0;
                    for (long i : timeList) {
                        String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
                        j++;
                        bw.write(content);
                        bw.flush();
                    } bw.close();} 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            thread *= 2;
        }
    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
