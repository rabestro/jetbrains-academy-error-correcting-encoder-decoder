package correcter;

import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public final class Application implements Runnable {
    private static final Logger log = Logger.getLogger(Application.class.getName());
    private static final Random random = new Random();
    private static final Pattern SPLIT_BY_THREE = Pattern.compile("(?<=\\G...)");
    private static final String ERRORS = "ABCDEFGHIJKLMNOPQRSTUVXYZ abcdefghijklmnopqrstuvxyz";

    @Override
    public void run() {
        log.fine("Application started");
        final var scanner = new Scanner(System.in);

        final var message = scanner.nextLine();
        final var encoded = encode(message);
        final var received = send(encoded);
        final var decoded = decode(received);

        System.out.println(message);
        System.out.println(encoded);
        System.out.println(received);
        System.out.println(decoded);
        scanner.close();
    }

    private String encode(final String message) {
        return message.replaceAll("(.)", "$1$1$1");
    }

    private String send(final String message) {
        return SPLIT_BY_THREE.splitAsStream(message).map(this::injectError).collect(joining());
    }

    private String decode(final String message) {
        return SPLIT_BY_THREE.splitAsStream(message).map(this::fixError).collect(joining());
    }

    private String injectError(final String subString) {
        final var chars = subString.toCharArray();
        chars[random.nextInt(chars.length)] = ERRORS.charAt(random.nextInt(ERRORS.length()));
        return new String(chars);
    }

    private String fixError(final String subString) {
        final var a = subString.charAt(0);
        final var b = subString.charAt(1);
        final var c = subString.charAt(2);
        return String.valueOf(a == b || a == c ? a : b);
    }
}
