package correcter;

import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public class Application implements Runnable {
    private static final Logger log = Logger.getLogger(Application.class.getName());
    private static final Random random = new Random();
    private static final Pattern SPLIT_BY_THREE = Pattern.compile("(?<=\\G...)");
    private static final String ERRORS = "ABCDEFGHIJKLMNOPQRSTUVXYZ abcdefghijklmnopqrstuvxyz";

    @Override
    public void run() {
        log.fine("Application started");
        final var scanner = new Scanner(System.in);
        final var message = scanner.nextLine();
        scanner.close();

        final var brokenMessage = SPLIT_BY_THREE
                .splitAsStream(message)
                .map(this::injectError)
                .collect(joining());

        System.out.println(brokenMessage);
    }

    private String injectError(final String subString) {
        if (subString.length() < 3) {
            log.log(Level.FINE, "Short substring received: '{}'", subString);
            return subString;
        }
        final var chars = subString.toCharArray();
        chars[random.nextInt(chars.length)] = ERRORS.charAt(random.nextInt(ERRORS.length()));
        return new String(chars);
    }

}
