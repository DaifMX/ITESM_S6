package net.daifo.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Command Pattern Example
 *
 * Scenario: A simple text editor supports typed operations — insert text,
 * delete text — through a command queue that also enables unlimited undo/redo.
 *
 * Each editing action is encapsulated as a {@link EditorCommand} object that
 * knows both how to execute itself and how to reverse the operation. The
 * {@link Editor} (receiver) owns the document buffer; the
 * {@link CommandHistory} (invoker) manages the undo/redo stacks; client code
 * creates concrete command objects and submits them.
 *
 * This decoupling lets the invoker support undo/redo, macro recording, and
 * logging without knowing anything about the concrete editing operations.
 */
public class CommandExample {

    // ---------- Receiver ----------
    public static class Editor {
        private final StringBuilder buffer = new StringBuilder();

        public void insert(int pos, String text) {
            buffer.insert(pos, text);
        }

        public void delete(int pos, int length) {
            buffer.delete(pos, pos + length);
        }

        public String getContent() {
            return buffer.toString();
        }
    }

    // ---------- Command interface ----------
    public interface EditorCommand {
        void execute();
        void undo();
    }

    // ---------- Concrete commands ----------
    public static class InsertCommand implements EditorCommand {
        private final Editor editor;
        private final int pos;
        private final String text;

        public InsertCommand(Editor editor, int pos, String text) {
            this.editor = editor;
            this.pos = pos;
            this.text = text;
        }

        @Override
        public void execute() {
            editor.insert(pos, text);
            System.out.println("[Insert] \"" + text + "\" at " + pos
                    + " → \"" + editor.getContent() + "\"");
        }

        @Override
        public void undo() {
            editor.delete(pos, text.length());
            System.out.println("[Undo Insert] removed \"" + text + "\""
                    + " → \"" + editor.getContent() + "\"");
        }
    }

    public static class DeleteCommand implements EditorCommand {
        private final Editor editor;
        private final int pos;
        private final int length;
        private String deleted;

        public DeleteCommand(Editor editor, int pos, int length) {
            this.editor = editor;
            this.pos = pos;
            this.length = length;
        }

        @Override
        public void execute() {
            deleted = editor.getContent().substring(pos, pos + length);
            editor.delete(pos, length);
            System.out.println("[Delete] removed \"" + deleted + "\""
                    + " → \"" + editor.getContent() + "\"");
        }

        @Override
        public void undo() {
            editor.insert(pos, deleted);
            System.out.println("[Undo Delete] restored \"" + deleted + "\""
                    + " → \"" + editor.getContent() + "\"");
        }
    }

    // ---------- Invoker ----------
    public static class CommandHistory {
        private final Deque<EditorCommand> history = new ArrayDeque<>();
        private final Deque<EditorCommand> redoStack = new ArrayDeque<>();

        public void execute(EditorCommand cmd) {
            cmd.execute();
            history.push(cmd);
            redoStack.clear();
        }

        public void undo() {
            if (history.isEmpty()) {
                System.out.println("[History] Nothing to undo.");
                return;
            }
            EditorCommand cmd = history.pop();
            cmd.undo();
            redoStack.push(cmd);
        }

        public void redo() {
            if (redoStack.isEmpty()) {
                System.out.println("[History] Nothing to redo.");
                return;
            }
            EditorCommand cmd = redoStack.pop();
            cmd.execute();
            history.push(cmd);
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        Editor editor = new Editor();
        CommandHistory history = new CommandHistory();

        history.execute(new InsertCommand(editor, 0, "Hello"));
        history.execute(new InsertCommand(editor, 5, " World"));
        history.execute(new DeleteCommand(editor, 5, 6)); // remove " World"

        System.out.println("-- Undo x2 --");
        history.undo();
        history.undo();

        System.out.println("-- Redo x1 --");
        history.redo();
    }
}
