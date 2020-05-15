package midi;

public class TimeUtil {
    public static void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {

        }
    }
}
