import org.omg.PortableInterceptor.INACTIVE;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LPA {
    private Vector<Vector> graph;
    private Vector<Integer> prediction;
    private int nodesCount;
    private int maxIteration = 10;

    /**
     * get the location of community and number of nodes </br>
     * then create graph from file
     *
     * @param path       is the location of network file
     * @param nodesCount is the number of nodes in the community
     */
    public LPA(String path, int nodesCount) throws IOException {
        this.graph = ReaderWriter.reader(path, nodesCount);
        this.nodesCount = nodesCount;
        prediction = new Vector<>();
        for (int i = 0; i <= nodesCount; i++)
            prediction.add(0);
    }

    /**
     * we get result from this method</br>
     * this method returns a vector of integers that content of every cell</br>
     * shows community number of that node.
     *
     * @return
     */
    public Vector<Integer> getPrediction() {
        int t = 1;
        boolean endProcess = false;

        while (t < maxIteration && !endProcess) {
            //shuffle all nodes
            Collections.shuffle(graph);
            for (Vector node : graph) {

            }


            t++;
        }
        return prediction;
    }

    /**
     * this method detect that there is a cycle between two vertex or not
     *
     * @param start
     * @param target
     * @return
     */
    public boolean detectCycleBetween2Vertex(int start, int target) {
        for (int i = 0; i < graph.get(start).size(); i++)
            if (((int[]) (graph.get(start).get(i)))[0] == target) {
                ((int[]) (graph.get(start).get(i)))[0] = -1;
                break;
            }

        for (int i = 0; i < graph.get(target).size(); i++)
            if (((int[]) (graph.get(target).get(i)))[0] == start) {
                ((int[]) (graph.get(target).get(i)))[0] = -1;
                break;
            }


        boolean res = isReachable(start, target);

        for (int i = 0; i < graph.get(start).size(); i++)
            if (((int[]) (graph.get(start).get(i)))[0] == -1) {
                ((int[]) (graph.get(start).get(i)))[0] = target;
                break;
            }

        for (int i = 0; i < graph.get(target).size(); i++)
            if (((int[]) (graph.get(target).get(i)))[0] == -1) {
                ((int[]) (graph.get(target).get(i)))[0] = start;
                break;
            }

        return res;
    }

    Boolean isReachable(int s, int d) {
        boolean visited[] = new boolean[nodesCount + 1];
        Arrays.fill(visited, false);

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark the current node as visited and enqueue it
        visited[s] = true;
        queue.add(s);


        // 'i' will be used to get all adjacent vertices of a vertex
        //Iterator<Integer> i;
        while (queue.size() != 0) {
            // Dequeue a vertex from queue and print it
            s = queue.poll();
            for (int i = 1; i < graph.get(s).size(); i++) {
                int n = ((int[]) (graph.get(s).get(i)))[0];
                if (n == -1)
                    continue;
                if (n == d)
                    return true;

                // Else, continue to do BFS
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }

        // If BFS is complete without visited d
        return false;
    }

    /**
     * this method return label of most frequently in all neighbour
     *
     * @param inputList
     * @return -1 means all of neighbour have same frequently, else this is the label of most frequently
     */
    public int getMostFrequentlyLabel(Vector<int[]> inputList) {
        //calculate frequently of each label
        Map<Integer, Integer> frequently = calculateFrequentlyLabel(inputList);

        //sort frequently hashMap based on it's values(Label in this problem)
        frequently = sortByComparator(frequently);

        if(frequently.size() > 1)
        {
            Integer lastElement = (Integer) frequently.values().toArray()[frequently.size()-1];
            Integer onBeforeLastElement = (Integer) frequently.values().toArray()[frequently.size()-2];
            if(lastElement.equals(onBeforeLastElement))
                return -1;
            else
                return (Integer) frequently.keySet().toArray()[frequently.size()-1];
        }
        else
            return (Integer) frequently.keySet().toArray()[frequently.size()-1];

    }

    public void showGraph() {
        for (int i = 1; i <= nodesCount; i++) {
            String out = "";
            out += ((int[]) ((graph.get(i)).get(0)))[0] + " : ";
            for (int j = 1; j < graph.get(i).size(); j++) {
                out += ((int[]) ((graph.get(i)).get(j)))[0] + " ,";
            }
            System.out.println(out);
        }
    }



    private class MyComparator implements Comparator<Entry<Integer, Integer>> {
        public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    }

    private Map<Integer, Integer> sortByComparator(Map<Integer, Integer> unsortMap) {

        List<Entry<Integer, Integer>> list = new LinkedList<Entry<Integer, Integer>>(
                unsortMap.entrySet());

        Collections.sort(list, new MyComparator());

        Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        for (Entry<Integer, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private Map<Integer, Integer> calculateFrequentlyLabel(Vector<int[]> inputList){
        Map<Integer, Integer> frequently = new HashMap<>(); // key = label of each neighbour node and value = frequently count

        //calculate frequently count
        for (int i = 1; i < inputList.size(); i++) {
            if (frequently.containsKey(inputList.elementAt(i)[1]))
                frequently.replace(inputList.elementAt(i)[1], frequently.get(inputList.elementAt(i)[1]) + 1);
            else
                frequently.put(inputList.elementAt(i)[1], 1);
        }
        return frequently;
    }

}
