import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShannonFano {
    public static void main(String[] args) {
        var word = "гигабайт";
       var letters = sort(word);

        var lettersList = split(letters);
        System.out.println(lettersList);

        var lettersMap = new LinkedHashMap<Letter, String>();
        loop(lettersMap, lettersList, "");

        lettersMap.forEach((key, value) -> System.out.println(key.key + ": " + value));
    }

    static List<Letter> sort(String word) {
        var map = word.chars().mapToObj(i -> (char) i)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        var letters = new ArrayList<Letter>();
        map.forEach((key, value) -> letters.add(new Letter(key, value)));

        letters.sort((o1, o2) -> {
            var value = Float.compare(o2.value, o1.value);
            if (value == 0)
                return Integer.compare(word.indexOf(o1.key), word.indexOf(o2.key));
            else
                return value;
        });

        letters.forEach(letter -> letter.value /= word.length());

        return letters;
    }

    private static void loop(Map<Letter, String> map, List<?> lettersList, String code) {
        var i = 0;
        for (Object o : lettersList) {
            code += i++;
            if (o instanceof Letter l)
                map.put(l, code);
            else
                loop(map, (List<?>) o, code);

            code = code.substring(0, code.length() - 1);
        }
    }

    static List<?> split(List<Letter> letters) {
        var half = letters.stream().map(l -> l.value).reduce(Float::sum).orElseThrow() / 2;
        var value = 0f;
        var index = 0;
        while (Float.compare(value, half) < 0)
            value += letters.get(index++).value;

        var letters1 = IntStream.range(0, index).mapToObj(letters::get).toList();
        var letters2 = IntStream.range(index, letters.size()).mapToObj(letters::get).toList();

        return List.of(
                letters1.size() > 2 ? split(letters1) : letters1,
                letters2.size() > 2 ? split(letters2) : letters2
        );
    }

    static class Letter {
        char key;
        float value;

        public Letter(char key, float value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + ": " + value;
        }
    }
}
