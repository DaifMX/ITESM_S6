package net.daifo;

import net.daifo.models.ClassroomModel;
import net.daifo.services.MenuService;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<ClassroomModel> cms = new ArrayList<>();

    public static void main(String[] args) {
        String motd = """
                ╔═══════════════════════╗
                ║  STUDENT SYSTEM v1.0  ║
                ╚═══════════════════════╝
                """;
        System.out.println(motd);

        ClassroomModel cm = MenuService.pickClassroom(cms);
        MenuService menu = new MenuService(cm);

        // Main Loop
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\nPick an option:");
            System.out.println("    1) Register students");
            System.out.println("    2) Register grades");
            System.out.println("    3) View grades");
            System.out.println("    4) View statistics");
            System.out.println("    5) Search students");
            System.out.println("    6) Switch classroom");
            System.out.println("    0) Exit\n");

            int in;
            try {
                String optionStr = new Scanner(System.in).nextLine();
                in = Integer.parseInt(optionStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.\n");
                continue;
            }

            switch (in) {
                case 1:
                    menu.registerStudents();
                    break;
                case 2:
                    menu.registerGrades();
                    break;
                case 3:
                    menu.viewGrades();
                    break;
                case 4:
                    menu.viewStatistics();
                    break;
                case 5:
                    menu.searchStudents();
                    break;
                case 6:
                    menu.switchClassroom(cms);
                    break;
                case 0:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid input. Try again.\n");
            }
        }
    }
}