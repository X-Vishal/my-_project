import java.util.Scanner;

public class studentgradecalc {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of subjects:");
        int numOfSubj = scanner.nextInt();
        int totalMarks = 0;

        for (int i = 0; i<=numOfSubj; i++){
            System.out.println("Enter marks for subject" + i + ":");
            int marks = scanner.nextInt();
            totalMarks += marks;
        }

        double percentage = (double) totalMarks/ numOfSubj;

        char grade;
        if (percentage>=90){
            grade = 'A';
        } else if (percentage>=80){
            grade = 'B';
        }  else if (percentage>=70){
            grade = 'C';
        } else if (percentage>=60){
            grade = 'D';
        } else if (percentage>=50){
            grade = 'E';
        } else{
            grade = 'F';
        }

        System.out.println("Total marks :"+ totalMarks);
        System.out.println("Percenntage :"+ percentage + "%");
        System.out.println("Grade : " + grade);


    }
}
