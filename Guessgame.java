import java.util.Scanner;
import java.util.Random;

public class Guessgame {
    public static void main(String[] args) {
        Random random = new Random();
        int numberToGuess = random.nextInt(100)+1;
        Scanner scanner = new Scanner(System.in);
        int guess = 0;
        int attempts = 0;

        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("I have chosen a number between 1 to 100.Try to guess it!");

        while(guess != numberToGuess){
            System.out.println("Enter the number:");
            guess = scanner.nextInt();
            attempts++;

            if(guess<numberToGuess){
                System.out.println("Too Low! Try again.");
            }
            else if(guess>numberToGuess){
                System.out.println("Too high! Try again.");
            }
            else{
                System.out.println("Congratulation! You guess the correct number.");
            }

        }
        scanner.close();
    }
}
