package cribbage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
// Reference for File Class
// https://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html#write(java.nio.file.Path,%20java.lang.Iterable,%20java.nio.charset.Charset,%20java.nio.file.OpenOption...)

public class Logger {

    private static final Logger logger = new Logger();

    private final Path filePath = Path.of("cribbage.log");

    private Logger() {

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Logger getInstance() {
        return logger;
    }


    public void log(String string) {
        string = string + "\n";
        byte[] logByte = string.getBytes();
        try {
            Files.write(filePath, logByte, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
