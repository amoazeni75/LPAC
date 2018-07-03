import java.io.IOException;
import java.util.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * this class first store data in structure of this :</br>
 * vector of vector that when you get a vector it contain a node with all neighbour of it's</br>
 * in first position, stored data of that node in this structure  : imagine an array that first is the number of that node</br>
 * in the second cell is the label of that node in the main network and in third cell is the percentage that </br>
 * show how many of the node's neighbour has same label as node in the network.
 */
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
        prediction = null;
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
        ArrayList<Integer> numberHelp = new ArrayList<>();
        for (int i = 0; i < graph.size() - 1; i++)
            numberHelp.add(i + 1);
//        numberHelp.add(3);
//        numberHelp.add(4);
//        numberHelp.add(6);
//        numberHelp.add(5);
//        numberHelp.add(1);
//        numberHelp.add(2);
        while (t < maxIteration && !endProcess) {
            //shuffle all nodes
            Collections.shuffle(numberHelp);
            //set New Label for each node
            for (int i = 0; i < numberHelp.size(); i++)
                setNewLabelOfNode(graph.get(numberHelp.get(i)));

            if (checkTermination())
                endProcess = true;
            t++;
        }
        prediction = new Vector<>();
        for (int i = 0; i < graph.size(); i++)
            prediction.add(((int[]) (graph.get(i).get(0)))[1]);

        return prediction;
    }

    /**
     * find and set appropriate label for the given node
     *
     * @param node is a vector that first element of that is target node that should set it's value
     */
    private void setNewLabelOfNode(Vector<int[]> node) {
        Map<Integer, MyNode> frequently = getMostFrequentlyLabel(node);
        int result = -1;
        if (frequently.size() == 0) //this node dose not have any neighbour
            result = ((int[]) (node.get(0)))[0];
        else if (frequently.size() == 1) // this node has just one neighbour that we should set it's label same as it's
            // neighbour's label
            result = (Integer) frequently.keySet().toArray()[frequently.size() - 1];
        else if (frequently.size() > 1) {
            Integer lastElement = ((MyNode) frequently.values().toArray()[frequently.size() - 1]).counter;
            Integer onBeforeLastElement = ((MyNode) frequently.values().toArray()[frequently.size() - 2]).counter;
            if (!lastElement.equals(onBeforeLastElement))
                result = (Integer) frequently.keySet().toArray()[frequently.size() - 1];
        }

        if (result != -1)
            ((int[]) (node.get(0)))[1] = result;

        else {//same frequently for all neighbour
            ArrayList<MyNode> sameFrequentlyLabel = new ArrayList<MyNode>();
            sameFrequentlyLabel.add((MyNode) frequently.values().toArray()[frequently.size() - 1]);
            Integer lastFreq = ((MyNode) frequently.values().toArray()[frequently.size() - 1]).counter;
            for (int i = frequently.size() - 2; i >= 0; i--) {
                Integer tmp = ((MyNode) frequently.values().toArray()[i]).counter;
                if (tmp.equals(lastFreq)) {
                    sameFrequentlyLabel.add((MyNode) frequently.values().toArray()[i]);
                } else
                    break;
            }
            //try to test cycle
            for (int i = 0; i < sameFrequentlyLabel.size(); i++) {
                boolean flag = false;
                for (int j = 0; j < sameFrequentlyLabel.get(i).counter; j++) {
                    if (detectCycleBetween2Vertex(((int[]) (node.get(0)))[0], ((int[]) (sameFrequentlyLabel.get(i).nodes.get(j)))[0])) {
                        ((int[]) (node.get(0)))[1] = sameFrequentlyLabel.get(i).label;
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    break;
            }
        }

        //update value of node in list of it's neighbour
        updateValueOfNodeInNeighbourList(node);
    }

    /**
     * this method detect that there is a cycle between two vertex or not
     *
     * @param start
     * @param target
     * @return
     */
    private boolean detectCycleBetween2Vertex(int start, int target) {
        setLabelForDetectCycle(start, target, -1);
        setLabelForDetectCycle(target, start, -1);
        boolean res = isReachable(start, target);
        setLabelForDetectCycle(start, -1, target);
        setLabelForDetectCycle(target, -1, start);
        return res;
    }

    Boolean isReachable(int s, int d) {
        boolean visited[] = new boolean[nodesCount + 1];
        Arrays.fill(visited, false);
        LinkedList<Integer> queue = new LinkedList<Integer>();
        visited[s] = true;
        queue.add(s);
        while (queue.size() != 0) {
            s = queue.poll();
            for (int i = 1; i < graph.get(s).size(); i++) {
                int n = ((int[]) (graph.get(s).get(i)))[0];
                if (n == -1)
                    continue;
                if (n == d)
                    return true;
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
        return false;
    }

    /**
     * this method search in the vector of start index and find element that </br>
     * it's value be same as condition, then change it's value to target value.
     *
     * @param start    index of vector that we should search in that
     * @param target   is the value that we try to find it
     * @param newValue is the value that after finding target we should set for it
     */
    private void setLabelForDetectCycle(int start, int target, int newValue) {
        for (int i = 1; i < graph.get(start).size(); i++) {
            if (((int[]) (graph.get(start).get(i)))[0] == target) {
                ((int[]) (graph.get(start).get(i)))[0] = newValue;
                return;
            }
        }
    }

    /**
     * this method return label of most frequently in all neighbour
     *
     * @param inputList
     * @return -1 means all of neighbour have same frequently, else this is the label of most frequently
     */
    private Map<Integer, MyNode> getMostFrequentlyLabel(Vector<int[]> inputList) {
        //calculate frequently of each label
        Map<Integer, MyNode> frequently = calculateFrequentlyLabel(inputList);

        //sort frequently hashMap based on it's values(Label in this problem)
        frequently = sortByComparator(frequently);

        return frequently;
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

    private class MyComparator implements Comparator<Entry<Integer, MyNode>> {
        public int compare(Entry<Integer, MyNode> o1, Entry<Integer, MyNode> o2) {
            return o1.getValue().counter.compareTo(o2.getValue().counter);
        }
    }

    private Map<Integer, MyNode> sortByComparator(Map<Integer, MyNode> unsortMap) {

        List<Entry<Integer, MyNode>> list = new LinkedList<Entry<Integer, MyNode>>(
                unsortMap.entrySet());

        Collections.sort(list, new MyComparator());

        Map<Integer, MyNode> sortedMap = new LinkedHashMap<Integer, MyNode>();
        for (Entry<Integer, MyNode> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private Map<Integer, MyNode> calculateFrequentlyLabel(Vector<int[]> inputList) {
        Map<Integer, MyNode> frequently = new HashMap<>(); // key = label of each neighbour node and value = frequently count

        //calculate frequently count
        for (int i = 1; i < inputList.size(); i++) {
            if (frequently.containsKey(inputList.elementAt(i)[1])) {
                MyNode tmp = frequently.get(inputList.elementAt(i)[1]);
                tmp.counter = tmp.counter + 1;
                tmp.nodes.add(inputList.elementAt(i));
                tmp.label = inputList.elementAt(i)[1];
                frequently.replace(inputList.elementAt(i)[1], tmp);

            } else {
                MyNode tmp = new MyNode();
                tmp.counter = 1;
                tmp.nodes.add(inputList.elementAt(i));
                tmp.label = inputList.elementAt(i)[1];
                frequently.put(inputList.elementAt(i)[1], tmp);
            }
        }
        return frequently;
    }

    private boolean checkTermination() {
        boolean res = true;
        for (int i = 1; i < graph.size(); i++) {
            int counter = 0;
            int a = ((int[]) graph.get(i).get(0))[1];
            for (int j = 1; j < graph.get(i).size(); j++) {
                int b = ((int[]) graph.get(i).get(j))[1];
                if (a == b)
                    counter++;
            }
            if (graph.get(i).size() != 1) { //if has more than one neighbour
                double ratio = counter * 100.0 / (graph.get(i).size() - 1);
                if (((int[])(graph.get(i).get(0)))[2] != (int) (ratio)) {
                    ((int[])(graph.get(i).get(0)))[2] = (int) (ratio);
                    res = false;
                }
            } else
                ((int[])(graph.get(i).get(0)))[2] = 100;
        }
        return res;
    }

    /**
     * we should update label of node in the list of it's neighbour
     *
     * @param nodeID list of node's neighbour
     */
    private void updateValueOfNodeInNeighbourList(Vector<int[]> nodeID) {
        int node = nodeID.get(0)[0];
        int label = nodeID.get(0)[1];
        for (int i = 1; i < nodeID.size(); ++i) {
            Vector<int[]> item = graph.get(((int[]) nodeID.get(i))[0]);
            for (int j = 1; j < item.size(); j++) {
                if (((int[]) (item.get(j)))[0] == node) {
                    ((int[]) (item.get(j)))[1] = label;
                    break;
                }
            }
        }
    }

    private class MyNode {
        Integer counter = new Integer(0);
        Integer label = 0;
        ArrayList<int[]> nodes = new ArrayList<>();
    }

    public int getShortestPath(int u, int v){
        Vector<Boolean> visited = new Vector<Boolean>(nodesCount + 1);
        for (int i = 0; i < nodesCount + 1; i++)
            visited.addElement(false);

        // Initialize distances as 0
        Vector<Integer> distance = new Vector<Integer>(nodesCount + 1);
        for (int i = 0; i < nodesCount + 1; i++)
            distance.addElement(0);


        // queue to do BFS.
        Queue<Integer> Q = new LinkedList<>();
        distance.setElementAt(0, u);

        Q.add(u);
        visited.setElementAt(true, u);
        while (!Q.isEmpty())
        {
            int x = Q.peek();
            Q.poll();

            for (int i=1; i< graph.get(x).size(); i++)
            {
                if (visited.elementAt(((int[])(graph.get(x).get(i)))[0]))
                    continue;

                // update distance for i
                distance.setElementAt(distance.get(x) + 1,((int[])(graph.get(x).get(i)))[0]);
                Q.add(((int[])(graph.get(x).get(i)))[0]);
                visited.setElementAt(true,((int[])(graph.get(x).get(i)))[0]);
            }
        }
        return distance.get(v);
    }
}
