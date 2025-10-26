import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.JFrame;

/**
 * Simple navigation stack for Swing frames.
 * Use Navigation.push(currentFrame) before opening a new frame.
 * Use Navigation.goBack(currentFrame) to close the current frame and show the previous one.
 */
public class Navigation {
    private static final Deque<JFrame> stack = new ArrayDeque<>();

    public static void push(JFrame frame) {
        if (frame != null) {
            stack.push(frame);
        }
    }

    public static void goBack(JFrame current) {
        try {
            if (current != null) {
                current.dispose();
            }
            if (!stack.isEmpty()) {
                JFrame prev = stack.pop();
                prev.setVisible(true);
            }
        } catch (Exception ex) {
            // swallow exceptions to avoid breaking UI navigation
            ex.printStackTrace();
        }
    }

    public static void clear() {
        stack.clear();
    }
}
