package kliving.util;

/**
 * Utility class for encoding/decoding data stored in text files.
 * Ensures that pipe characters and newlines in user content
 * do not break the |-delimited file format.
 */
public class DataUtil {

    public static String encode(String s) {
        if (s == null) return "";
        return s.replace("[PIPE]", "[PIPE_ESC]")
                .replace("|", "[PIPE]")
                .replace("\r\n", "[NL]")
                .replace("\n", "[NL]")
                .replace("\r", "[NL]");
    }

    public static String decode(String s) {
        if (s == null) return "";
        return s.replace("[NL]", "\n")
                .replace("[PIPE]", "|")
                .replace("[PIPE_ESC]", "[PIPE]");
    }
}
