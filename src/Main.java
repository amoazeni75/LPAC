import java.io.IOException;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();
        LPA sampleDetection = new LPA("./network1.txt", 6);

        sampleDetection.showGraph();

        Vector<int []> testHash = new Vector<>();
        testHash.add(new int[]{1,100});
        testHash.add(new int[]{2,2});
        testHash.add(new int[]{3,9});
        testHash.add(new int[]{4,2});
        testHash.add(new int[]{5,1});
        testHash.add(new int[]{6,2});
        testHash.add(new int[]{7,1});
        testHash.add(new int[]{8,1});

        int x =sampleDetection.getMostFrequentlyLabel(testHash);
        /* test cycle
        boolean res = sampleDetection.detectCycleBetween2Vertex(1, 2);
        res = sampleDetection.detectCycleBetween2Vertex(1, 3);
        res = sampleDetection.detectCycleBetween2Vertex(2, 1);
        res = sampleDetection.detectCycleBetween2Vertex(2, 4);
        res = sampleDetection.detectCycleBetween2Vertex(3, 1);
        res = sampleDetection.detectCycleBetween2Vertex(3, 4);
        res = sampleDetection.detectCycleBetween2Vertex(4, 3);
        res = sampleDetection.detectCycleBetween2Vertex(4, 5);
        res = sampleDetection.detectCycleBetween2Vertex(5, 4);
        res = sampleDetection.detectCycleBetween2Vertex(4, 2);
        res = sampleDetection.detectCycleBetween2Vertex(4, 3);
        res = sampleDetection.detectCycleBetween2Vertex(6, 5);
        */

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.err.println("Execution time : " + duration + " ms");
    }
}
