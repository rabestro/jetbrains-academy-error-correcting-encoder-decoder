package correcter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
            final var buffer = ByteBuffer.allocate(message.length());

            message.chars()
                    .map(i -> i ^ (1 << random.nextInt(8)))
                    .forEach(i -> buffer.put((byte) i));

            FileChannel fc = new FileOutputStream("received.txt").getChannel();
            fc.write(buffer.flip());
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
