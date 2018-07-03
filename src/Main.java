import java.io.IOException;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();
        String truePath = "./Test/community.txt";
        String path = "./network1.txt";
        LPA sampleDetection = new LPA(path, 6);
        int x = sampleDetection.getShortestPath(1,6);
        Vector<Integer> res = sampleDetection.getPrediction();
//        System.out.println("----------------");
//        for (int i = 0; i < res.size(); i++) {
//            System.out.println(res.get(i));
//        }
        System.out.println("-----------------");
        System.out.println("NMI : " + TestResult.NMI(res,truePath));
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.err.println("Execution time : " + duration + " ms");

    }
}
