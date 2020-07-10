package correcter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.logging.Logger;

public final class Application implements Runnable {
    private static final Logger log = Logger.getLogger(Application.class.getName());
    private static final Random random = new Random();

    @Override
    public void run() {
        log.fine("Application started");

        try {
            final var message = Files.readString(Path.of("send.txt"));
            final var brokenMessage = message
                    .chars()
                    .map(i -> i ^ (1 << random.nextInt(8)))
                    .toArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
