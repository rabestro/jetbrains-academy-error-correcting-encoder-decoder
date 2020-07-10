package correcter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public final class Application implements Runnable {
    private static final Logger log = Logger.getLogger(Application.class.getName());
    private static final Random random = new Random();

    @Override
    public void run() {
        log.fine("Application started");

        try (FileOutputStream writer = new FileOutputStream(new File("received.txt"))) {
            final var message = Files.readAllBytes(Path.of("send.txt"));
            IntStream.range(0, message.length)
                    .forEach(i -> message[i] ^= (1 << random.nextInt(8)));
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
