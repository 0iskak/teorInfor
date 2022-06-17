import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Hamming {
    public static void main(String[] args) {
        var bits = new int[]{
                1, 0, 1, 1, 0, 1, 0, 1
//                1, 0, 1, 0, 1, 1, 1
        };

        var table1 = check(bits);

        bits[1] = 1;
        var table2 = check(bits);

        var pos = compare(table1, table2);
        System.out.printf("Error at position: %d (index: %d)", pos, pos - 1);
    }

    private static int compare(Map<Integer, Integer> table1, Map<Integer, Integer> table2) {
        var index = new AtomicInteger();
        table1.keySet().forEach(i -> {
            if (Objects.equals(table1.get(i), table2.get(i)))
                return;

            index.set(index.get() + i);
        });

        return index.get();
    }

    private static Map<Integer, Integer> check(int[] bits) {
        var length = bits.length;
        var powOf2s = new LinkedList<Integer>();
        while (true) {
            var pow = (int) Math.pow(2, powOf2s.size());
            if (pow > bits.length)
                break;
            powOf2s.add(pow);
        }

        var field = new char[length + powOf2s.size()];
        for (int i = 0, j = 0; i < field.length; i++) {
            if (powOf2s.contains(i + 1)) {
                field[i] = '.';
                continue;
            }

            field[i] = String.valueOf(bits[j++]).charAt(0);
        }

        System.out.println(Arrays.toString(field));

        var table = new HashMap<Integer, Integer>();
        for (int i : powOf2s) {
            var s = new StringBuilder();
            var count = 0;
            for (int j = i - 1; j < field.length; j += i * 2) {
                for (int k = 0; k < i; k++) {
                    var l = j + k;
                    if (l >= field.length)
                        break;

                    var c = field[l];
                    if (c == '.')
                        continue;

                    if (c == '1')
                        count++;

                    s.append(c);
                }
            }

            var value = count % 2 == 0 ? 0 : 1;
            table.put(i, value);
            System.out.printf("%d: %s | %d\n", i, s, value);
        }
        System.out.println();

        return table;
    }
}
