import java.io.IOException;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();
        LPA sampleDetection = new LPA("./network_2squer.txt", 9);
        Vector<Integer> res = sampleDetection.getPrediction();
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.err.println("Execution time : " + duration + " ms");
    }
}
