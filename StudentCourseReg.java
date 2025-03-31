import java.util.Scanner;
import java.util.ArrayList;

class Student {
    String name;
    int id;
    ArrayList<String> courses;

    public Student(String name, int id) {
        this.id = id;
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public void courseReg(String course) {
        if (!courses.contains(course)) {
            courses.add(course);
            System.out.println(course + " has been successfully registered.");
        } else {
            System.out.println("You are already registered for " + course);
        }
    }

    public void displayCourses() {
        System.out.println("Registered courses for " + name + ": " + courses);
    }
}

public class StudentCourseReg {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Student Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Student ID:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Student student = new Student(name, id);

        boolean running = true;
        while (running) {
            System.out.println("\n1. Register Course");
            System.out.println("2. Display Registered Courses");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {  // Handle invalid input (e.g., letters instead of numbers)
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();  // Clear the invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter course name to register:");
                    String course = scanner.nextLine();
                    student.courseReg(course);
                    break;

                case 2:
                    student.displayCourses();
                    break;

                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    running = false;  // End the loop
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close(); // Close scanner safely after the loop ends
    }
}
