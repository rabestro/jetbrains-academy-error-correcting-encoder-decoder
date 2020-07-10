package correcter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public final class Main {
    static {
        try {
            LogManager.getLogManager().readConfiguration(
                    new FileInputStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
    }

    private static final Random random = new Random();
    private static final Pattern SPLIT_BY_THREE = Pattern.compile("(?<=\\G...)");
    private static final String ERRORS = "ABCDEFGHIJKLMNOPQRSTUVXYZ abcdefghijklmnopqrstuvxyz";

    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);
        final var message = scanner.nextLine();
        scanner.close();

        final var brokenMessage = SPLIT_BY_THREE
                .splitAsStream(message)
                .map(Main::injectError)
                .collect(joining());

        System.out.println(brokenMessage);
    }

    private static String injectError(final String subString) {
        if (subString.length() < 3) {
            return subString;
        }
        final var chars = subString.toCharArray();
        chars[random.nextInt(chars.length)] = ERRORS.charAt(random.nextInt(ERRORS.length()));
        return new String(chars);
    }
}
