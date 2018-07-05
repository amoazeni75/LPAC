import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws Exception {
        String truePath = "./Test/community.txt";
        String path = "./Test/network.txt";
        Tester test = new Tester(path,truePath,1000);
        test.TesterALG(4);
    }
}
