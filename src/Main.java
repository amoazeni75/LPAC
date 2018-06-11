import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();
        LPA sampleDetection = new LPA("./network.txt",1000);

        //sampleDetection.showGraph();
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.err.println("Execution time : " + duration + " ms");
    }
}
