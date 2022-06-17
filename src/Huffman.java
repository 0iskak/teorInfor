import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Huffman {
    public static void main(String[] args) {
        var letters = List.of(
                new Node('B', 1),
                new Node('D', 3),
                new Node('A', 5),
                new Node('C', 6)
        );

        var root = letters.stream()
                .reduce((a, b) -> {
                    if (a.i > b.i) {
                        var tmp = a;
                        a = b;
                        b = tmp;
                    }

                    return new Node('.', a.i + b.i, a, b);
                })
                .orElseThrow();

        var map = new LinkedHashMap<Node, String>();
        loop(map, root, "");

        map.forEach((key, value) -> System.out.println(key.c + ": " + value));
    }

    private static void loop(Map<Node, String> map, Node node, String code) {
        if (node.children.length == 0)
            map.put(node, code);

        for (int i = 0; i < node.children.length; i++) {
            loop(map, node.children[i], code += i);
            code = code.substring(0, code.length() - 1);
        }
    }

    static class Node {
        char c;
        int i;
        Node[] children;

        public Node(char c, int i, Node... children) {
            this.c = c;
            this.i = i;
            this.children = children;
        }
    }
}
