package com.wieik.amberbronze.logic;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The abstract class representing a logged operation.
 * Subclasses of this class can define their own log message and perform logging actions.
 */
public interface LoggedOperation {

    /**
     * Gets the log message for the operation.
     *
     * @return The log message.
     */
    String getLogMessage();

    /**
     * Logs an action with an optional additional message.
     *
     * @param message The additional message to include in the log.
     */
    default void logAction(String message) {
        String path = "operations.log";
        try (FileWriter fw = new FileWriter(path, true)) {
            String logMessage = getLogMessage();

            if (message != null) {
                logMessage += " | " + message;
            }

            String log = time() + " " + logMessage;
            fw.write(log + "\n");
            System.out.println(log);
        } catch (IOException e) {
            System.out.println("Error while writing log file: " + e.getMessage());
        }
    }

    /**
     * Helper function for formatting the log message with the current timestamp.
     *
     * @return The formatted log message.
     */
    private String time() {
        SimpleDateFormat sdf = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
        return sdf.format(new Date());
    }
}
