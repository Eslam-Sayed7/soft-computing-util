package softcomputing.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class AppLogger {

    private final Logger logger;

    private AppLogger(Class<?> clazz) {
        this.logger = Logger.getLogger(clazz.getName());
    }

    public static AppLogger getLogger(Class<?> clazz) {
        return new AppLogger(clazz);
    }

    public static void configure(Level level) {
        Logger root = Logger.getLogger("");

        for (var h : root.getHandlers()) {
            root.removeHandler(h);
        }
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(level);

        Formatter formatter = new Formatter() {
        private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss")
            .withZone(ZoneId.systemDefault());

            @Override
            public String format(LogRecord record) {
                String time = dtf.format(Instant.ofEpochMilli(record.getMillis()));
                String lvl = record.getLevel().getName();
                String loggerName = record.getLoggerName();
                String simpleName = loggerName == null ? "" : loggerName.substring(loggerName.lastIndexOf('.') + 1);
                String msg = formatMessage(record);
                String thrown = "";
                if (record.getThrown() != null) {
                    var sw = new java.io.StringWriter();
                    record.getThrown().printStackTrace(new java.io.PrintWriter(sw));
                    thrown = System.lineSeparator() + sw.toString();
                }
                return String.format("%s %-7s [%s] %s%s%n", time, lvl, simpleName, msg, thrown);
            }
        };
        ch.setFormatter(formatter);

        try {
            FileHandler fh = new FileHandler("app.log", true); // append
            fh.setLevel(level);
            fh.setFormatter(formatter);
            root.addHandler(fh);
        } catch (java.io.IOException | SecurityException e) {
            root.addHandler(ch);
            root.setLevel(level);
            root.log(Level.WARNING, "Could not create FileHandler app.log; logging to console only", e);
            return;
        }

        root.addHandler(ch);
        root.setLevel(level);
    }

    public void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public void debug(String msg) {
        logger.log(Level.FINE, msg);
    }

    public void warn(String msg) {
        logger.log(Level.WARNING, msg);
    }

    public void error(String msg) {
        logger.log(Level.SEVERE, msg);
    }

}
