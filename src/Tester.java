import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Tester {

    private long startTime;
    private long endTime;
    private long duringTime;
    private int nodeCount;
    private String netPath;
    private String resPath;
    private Double NMIsum;

    public Tester(String networkPath, String resultPath, int nodeCount) {
        this.netPath = networkPath;
        this.resPath = resultPath;
        this.NMIsum = 0.0;
        this.nodeCount = nodeCount;
    }

    public void TesterALG(int index) throws Exception {

        if (index == 1) {
            //LPALC Test With Multi Thread
            int threadCount = 4;
            int testCount = 100;
            ArrayList<Vector<Integer>> tests = new ArrayList<>();
            ArrayList<LPALC> workers = new ArrayList<>();
            startTime = System.currentTimeMillis();
            for (int i = 0; i < (testCount / threadCount); i++) {
                for (int j = 0; j < threadCount; j++) {
                    LPALC t = new LPALC(netPath, nodeCount);
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
            endTime = System.currentTimeMillis();
            duringTime = (endTime - startTime);
            System.err.println("Execution time : " + duringTime + " ms");
            for (int i = 0; i < tests.size(); i++) {
                NMIsum += TestResult.NMI(tests.get(i), resPath);
            }
            NMIsum /= testCount;
            System.out.println("NMI LPALC: " + NMIsum);

        } else if (index == 2) {

            //LPA Test With Multi Thread
            int threadCount = 4;
            int testCount = 12;
            ArrayList<Vector<Integer>> tests = new ArrayList<>();
            ArrayList<LPA> workers = new ArrayList<>();
            startTime = System.currentTimeMillis();
            for (int i = 0; i < (testCount / threadCount); i++) {
                for (int j = 0; j < threadCount; j++) {
                    LPA t = new LPA(netPath, nodeCount);
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
            endTime = System.currentTimeMillis();
            duringTime = (endTime - startTime);
            System.err.println("Execution time : " + duringTime + " ms");

            for (int i = 0; i < tests.size(); i++) {
                NMIsum += TestResult.NMI(tests.get(i), resPath);
            }
            NMIsum /= testCount;
            System.out.println("NMI LPA : " + NMIsum);

        } else if (index == 3) {
            //LPALC Single Core
            LPALC sampleDetection = new LPALC(netPath, nodeCount);
            sampleDetection.getPrediction();
            Vector<Integer> res = sampleDetection.prediction;
            System.out.println("NMI LPALC 1 Time : " + TestResult.NMI(res, resPath));
            //sampleDetection.showGraph();

        } else if (index == 4) {
            //LPA Single Core
            LPA sampleDetection = new LPA(netPath, nodeCount);
            sampleDetection.getPrediction();
            Vector<Integer> res = sampleDetection.prediction;
            System.out.println("NMI LPA 1 Time : " + TestResult.NMI(res, resPath));
           // sampleDetection.showGraph();
        }
    }
}
