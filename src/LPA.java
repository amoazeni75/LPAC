import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class LPA {
    private Vector<Vector> graph;
    private Vector<Integer> prediction;
    private int nodesCount;

    /**
     * get the location of community and number of nodes </br>
     * then create graph from file
     * @param path is the location of network file
     * @param nodesCount is the number of nodes in the community
     */
    public LPA(String path, int nodesCount) throws IOException {
        this.graph = ReaderWriter.reader(path,nodesCount);
        this.nodesCount = nodesCount;
        prediction = new Vector<>();
        for (int i = 0; i <= nodesCount; i++)
            prediction.add(0);
    }

    /**
     * we get result from this method</br>
     * this method returns a vector of integers that content of every cell</br>
     * shows community number of that node.
     * @return
     */
    public Vector<Integer> getPrediction(){
        
        return prediction;
    }

    public void showGraph(){
        for (int i = 1; i <= nodesCount; i++) {
            String out = "";
            out +=  ((int[])((graph.get(i)).get(0)))[0] + " : ";
            for (int j = 2; j < graph.get(i).size(); j++) {
                out += ((int[])((graph.get(i)).get(j)))[0] + " ,";
            }
            System.out.println(out);
        }
    }
}
