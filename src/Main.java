import java.util.Scanner;

public class Main {
    private static final String[] ROMAN_SYMBOLS = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C"};
    private static final int[] ARABIC_VALUES = {1, 4, 5, 9, 10, 40, 50, 90, 100};

    public static void main(String[] args) {
        System.out.println("Добро пожаловать!");
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.print("Введите арифметическую операцию (например, 5 + 3 или V - III): ");
            String expression = scanner.nextLine().toUpperCase();

            String[] parts = expression.split(" ");

            try {
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Неверный формат ввода, ожидалось 3 элемента (два операнда и один оператор).");
                }
                if ((isRimNum(parts[0]) && !isRimNum(parts[2])) || (!isRimNum(parts[0]) && isRimNum(parts[2]))) {
                    throw new IllegalArgumentException("Используются одновременно разные системы счисления.");
                }
                if (!isValidExpression(expression)) {
                    System.out.println("Ошибка: Недопустимые символы в выражении.");
                    exit = true;
                }

                int num1;
                int num2;
                boolean isRim = false;

                if (isRimNum(parts[0]) && isRimNum(parts[2])) {
                    isRim = true;
                    num1 = getArabNum(parts[0]);
                    num2 = getArabNum(parts[2]);
                } else {
                    num1 = Integer.parseInt(parts[0]);
                    num2 = Integer.parseInt(parts[2]);
                }

                if (num1 > 10 || num2 > 10) {
                    System.out.println("Ошибка: Используйте числа не больше 10(X).");
                    exit = true;
                }

                char operator = parts[1].charAt(0);

                int result = 0;

                switch (operator) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            System.out.println("Ошибка: Делить на ноль нельзя.");
                            exit = true;
                        }
                        break;
                }

                if (isRim) {
                    System.out.println("Результат: " + getRimNum(result));
                } else {
                    System.out.println("Результат: " + result);
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static boolean isValidExpression(String expression) {
        return expression.matches("^[IVXLCDM0-9+\\-*/\\s]+$");
    }

    private static int getArabNum(String rimNum) {
        int result = 0;
        int prevValue = 0;

        for (int i = rimNum.length() - 1; i >= 0; i--) {
            int value = getFoRimNum(String.valueOf(rimNum.charAt(i)));

            if (value < prevValue) {
                result -= value;
            } else {
                result += value;
            }

            prevValue = value;
        }

        return result;
    }

    private static int getFoRimNum(String symbol) {
        for (int i = 0; i < ROMAN_SYMBOLS.length; i++) {
            if (ROMAN_SYMBOLS[i].equals(symbol)) {
                return ARABIC_VALUES[i];
            }
        }
        return 0;
    }

    private static boolean isRimNum(String input) {
        return input.matches("^[IVXLCDM]+$");
    }

    private static String getRimNum(int arabic) {
        if (arabic <= 0) {
            return "Римские числа не могут представлять ноль или отрицательные значения.";
        }

        StringBuilder rim = new StringBuilder();

        for (int i = ROMAN_SYMBOLS.length - 1; i >= 0; i--) {
            while (arabic >= ARABIC_VALUES[i]) {
                rim.append(ROMAN_SYMBOLS[i]);
                arabic -= ARABIC_VALUES[i];
            }
        }

        return rim.toString();
    }
}