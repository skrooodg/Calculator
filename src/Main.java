import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static Map<String, Value> values;

    public static void main(String[] args) {
        generateValuesMap();
        String inputResult = input();
        String result = calculate(inputResult);
        System.out.println(result);
    }

    private static String input() {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        String inputResult = input.replaceAll("\\s", "");
        return inputResult;
    }

    private static void generateValuesMap() {
        String filePath = "RIM.txt";

        String rimCharacters = null;
        try {
            rimCharacters = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        values = new HashMap<>();
        String[] rimNumbers = rimCharacters.split(" ");

        for (int i = -10; i < 101; i++) {
            if (i > 0) {
                values.put(Integer.toString(i), new Value(rimNumbers[i - 1], i, false));
                values.put(rimNumbers[i - 1], new Value(rimNumbers[i - 1], i, true));
            } else if (i < 0) {
                values.put(Integer.toString(i), new Value("-" + rimNumbers[-i - 1], i, false));
            } else {
                values.put("0", new Value("0", 0, false));
            }
        }
    }

    public static String calculate(String input)
    {
        if (input.contains("+")) {
            return sum(input);
        } else if (input.contains("-")) {
            return sub(input);
        } else if (input.contains("/")) {
            return devision(input);
        } else if (input.contains("*")) {
            return mult(input);
        } else
            throw new RuntimeException("Строка не является математической операцией");
    }

    private static String mult(String input) {
        String[] inputValues = input.split("\\*");
        checkInputValues(inputValues);
        Value a = values.get(inputValues[0]);
        Value b = values.get(inputValues[1]);
        checkCriterion(a, b);
        int result = a.value * b.value;
        return result(result, values.get(inputValues[0]).rim);
    }

    private static String devision(String input) {
        String[] inputValues = input.split("\\/");
        checkInputValues(inputValues);
        Value a = values.get(inputValues[0]);
        Value b = values.get(inputValues[1]);
        checkCriterion(a, b);
        int result = a.value / b.value;

        return result(result, values.get(inputValues[0]).rim);
    }

    private static String sub(String input) {
        String[] inputValues = input.split("\\-");
        checkInputValues(inputValues);
        Value a = values.get(inputValues[0]);
        Value b = values.get(inputValues[1]);
        checkCriterion(a, b);
        int result = a.value - b.value;
        if (result < 1 && a.rim) {
            throw new RuntimeException("Получено отрицательное или нулевое римское число");
        }
        return result(result, values.get(inputValues[0]).rim);
    }

    private static String sum(String input) {
        String[] inputValues = input.split("\\+");
        checkInputValues(inputValues);
        Value a = values.get(inputValues[0]);
        Value b = values.get(inputValues[1]);
        checkCriterion(a, b);
        int result = a.value + b.value;
        return result(result, values.get(inputValues[0]).rim);
    }

    private static void checkInputValues(String[] line) {
        if (line.length != 2 || !values.containsKey(line[0]) || !values.containsKey(line[1])) {
            throw new RuntimeException("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
    }

    public static String result(int result, boolean rim) {
        if (rim) {
            return values.get(Integer.toString(result)).valueRim;
        } else {
            return Integer.toString(result);
        }

    }

    public static void checkCriterion(Value a, Value b) {
        if (a.value < 1 || a.value > 10 || b.value < 1 || b.value > 10) {
            throw new RuntimeException("Введено некорректное значение");
        }
        if (a.rim != b.rim) {
            throw new RuntimeException("Указаны несовместимые значения");
        }
    }
}
