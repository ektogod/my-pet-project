package bot.utils;

import java.util.Random;

public class EmailUtils {
    private static final int LENGTH = 12;
    public static String generateValidCode(){
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            char ch = (char) (r.nextInt(26) + 65);
            builder.append(ch);
        }

        return builder.toString();
    }
}
