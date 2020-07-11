package correcter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.function.IntConsumer;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public final class Application implements Runnable {
    private static final Logger log = Logger.getLogger(Application.class.getName());
    private static final int THREE_BITES = 6;
    private static final int ONE_BYTE = 42;

    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Runnable> ACTIONS = Map.of(
            "encode", Application::encode,
            "send", Application::send,
            "decode", Application::decode);

    @Override
    public void run() {
        log.fine("Application started");
        System.out.print("Write a mode: ");
        final var mode = scanner.next().toLowerCase();

        ACTIONS.getOrDefault(mode, Application::unknownMode).run();
    }

    private static void encode() {
        log.fine("Encoding the message");
        try (final var writer = new FileOutputStream(new File("encoded.txt"))) {
            final var message = BitSet.valueOf(Files.readAllBytes(Path.of("send.txt")));
            final var encoded = new BitSet(message.length() * 3);

            for (int i = 0, e = 0; i < message.length(); ++i) {
                encoded.set(e++, message.get(i));
                encoded.set(e++, message.get(i));
                if (e % THREE_BITES == 0) {
                    final var parityBit = message.get(i) ^ message.get(i - 1) ^ message.get(i - 2);
                    encoded.set(e++, parityBit);
                    encoded.set(e++, parityBit);
                }
                if (e % ONE_BYTE == 0) {
                    e += 6;
                }
            }
            writer.write(encoded.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void send() {
        log.fine("Sending the message");
        try (final var writer = new FileOutputStream(new File("received.txt"))) {
            var message = Files.readAllBytes(Path.of("encoded.txt"));
            IntConsumer injectError = i -> message[i] ^= (1 << random.nextInt(8));
            IntStream.range(0, message.length).forEach(injectError);
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decode() {
        log.fine("Decoding the message");
        try (final var writer = new FileOutputStream(new File("decoded.txt"))) {
            final var message = BitSet.valueOf(Files.readAllBytes(Path.of("received.txt")));
            final var decoded = new BitSet(message.length() / 3);
//            for (int i = 0, e = 0; i < decoded.length(); ++i) {
//                if (message.get(e) == message.get(e + 1)) {
//                    decoded.set(i, message.get(e));
//                } else {
//
//                }
//            }
//            for (int i = 0, e = 0; i < message.length(); ++i) {
//                encoded.set(e++, message.get(i));
//                encoded.set(e++, message.get(i));
//                if (e % THREE_BITES == 0) {
//                    final var parityBit = message.get(i) ^ message.get(i - 1) ^ message.get(i - 2);
//                    encoded.set(e++, parityBit);
//                    encoded.set(e++, parityBit);
//                }
//                if (e % ONE_BYTE == 0) {
//                    e += 6;
//                }
//            }
            writer.write(decoded.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void unknownMode() {
        System.out.println("Unknown mode");
    }

}
