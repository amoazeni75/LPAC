import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws Exception {

        ArrayList<Vector<Integer>> tests = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        String truePath = "./Test/community.txt";
        String path = "./Test/network.txt";
        
        int threadCount = 4;
        int testCount = 12;
        double NMIsum = 0.0;
        
        ArrayList<LPALC> workers = new ArrayList<>();

        for (int i = 0; i < (testCount / threadCount); i++) {
            for (int j = 0; j < threadCount; j++) {
                LPALC t = new LPALC(path,1000);
                workers.add(t);
                t.start();
            }
            for (int j = 0; j < threadCount; j++) {
                workers.get(j).join();
            }
            for (int j = 0; j < workers.size(); j++) {
                tests.add(workers.get(j).prediction);
            }
            workers.clear();
        }
//        for (int i = 0; i < tests.size(); i++) {
//            for (int j = 0; j < tests.get(i).size(); j++) {
//                System.out.println(tests.get(i).get(j));
//            }
//        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.err.println("Execution time : " + duration + " ms");

        for (int i = 0; i < tests.size(); i++) {
            NMIsum += TestResult.NMI(tests.get(i),truePath);
        }
        NMIsum /= testCount;


        //LPA sampleDetection = new LPA(path, 1000);

        //Vector<Integer> res = sampleDetection.getPrediction();
//        System.out.println("----------------");
//        for (int i = 0; i < res.size(); i++) {
//            System.out.println(res.get(i));
//        }
        System.out.println("-----------------");
        System.out.println("NMI : " + NMIsum);


    }
}
