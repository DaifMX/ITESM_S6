package net.daifo.memento;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Memento Pattern Example
 *
 * Scenario: A graphic editor tracks the state of a {@link Canvas} (position +
 * color of a single shape) and supports multi-level undo. Each time the user
 * commits a change, the editor saves a {@link CanvasMemento} snapshot. Undo
 * restores the most recent snapshot without exposing the canvas internals to
 * the history manager.
 *
 * Roles:
 *   - Originator  : {@link Canvas}         — creates and restores mementos.
 *   - Memento     : {@link CanvasMemento}  — opaque value object; only Canvas
 *                                            can read its fields directly.
 *   - Caretaker   : {@link EditorHistory}  — stores/pops mementos, never
 *                                            inspects their contents.
 */
public class MementoExample {

    // ---------- Memento (immutable snapshot) ----------
    public static final class CanvasMemento {
        private final int x;
        private final int y;
        private final String color;

        private CanvasMemento(int x, int y, String color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    // ---------- Originator ----------
    public static class Canvas {
        private int x;
        private int y;
        private String color;

        public Canvas(int x, int y, String color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public void move(int dx, int dy) {
            x += dx;
            y += dy;
            System.out.println("[Canvas] Moved to (" + x + ", " + y + ")");
        }

        public void recolor(String newColor) {
            color = newColor;
            System.out.println("[Canvas] Recolored to " + color);
        }

        public CanvasMemento save() {
            return new CanvasMemento(x, y, color);
        }

        public void restore(CanvasMemento m) {
            x = m.x;
            y = m.y;
            color = m.color;
            System.out.println("[Canvas] Restored to (" + x + ", " + y + ") color=" + color);
        }

        @Override
        public String toString() {
            return "Canvas{x=" + x + ", y=" + y + ", color='" + color + "'}";
        }
    }

    // ---------- Caretaker ----------
    public static class EditorHistory {
        private final Deque<CanvasMemento> stack = new ArrayDeque<>();

        public void push(CanvasMemento m) {
            stack.push(m);
        }

        public CanvasMemento pop() {
            if (stack.isEmpty()) {
                System.out.println("[History] Nothing to undo.");
                return null;
            }
            return stack.pop();
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        Canvas canvas = new Canvas(0, 0, "red");
        EditorHistory history = new EditorHistory();

        // save initial state
        history.push(canvas.save());

        canvas.move(10, 5);
        history.push(canvas.save());

        canvas.recolor("blue");
        history.push(canvas.save());

        canvas.move(-3, 7);
        history.push(canvas.save());

        System.out.println("\nCurrent: " + canvas);

        System.out.println("\n-- Undo x3 --");
        CanvasMemento m;
        m = history.pop(); // current (move -3,7)
        m = history.pop(); canvas.restore(m); // recolor blue
        m = history.pop(); canvas.restore(m); // move 10,5
        m = history.pop(); canvas.restore(m); // initial

        System.out.println("After undos: " + canvas);
    }
}
