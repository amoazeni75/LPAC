import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * authors : S.Alireza Moazeni and Ali Arjomand Bigdeli</br>
 * in this class we try to read graph from specific file</br>
 */
public class ReaderWriter {
    /**
     * in this method we start reading from file<br>
     * @param path is the location of network
     * @param nodesCount is the number of nodes in given network
     * @return vector of vectors that in the every cell of vectors we have list of</br>
     *     neighbours
     * @throws IOException
     */
    public static Vector<Vector> reader(String path, int nodesCount) throws IOException {
        Vector graph = new Vector<Vector>();
        for (int i = 0; i <= nodesCount; i++) {
            graph.add(new Vector<int[]>());
            ((Vector) (graph.get(i))).add(new int[]{i,i});
        }

        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine();
        while (line != null) {
            String[] parts = line.split(" ");
            int source = Integer.parseInt(parts[0]);
            int destination = Integer.parseInt(parts[1]);
            ((Vector) (graph.get(source))).add(new int[]{destination,destination});
            ((Vector) (graph.get(destination))).add(new int[]{source,source});
            line = br.readLine();
        }
        br.close();
        return graph;
    }

}
