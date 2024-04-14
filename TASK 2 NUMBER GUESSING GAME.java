import java.util.Random;
import java.util.Scanner;

public class GuessTheNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int min = 1;
        int max = 100;
        int totalRounds = 3; // Let's play 3 rounds
        int currentRound = 0;
        int totalAttempts = 0; // Total attempts across all rounds
        int totalScore = 0; // Total score across all rounds
        boolean playAgain = true;
        int initialAttempts = 5; // Initial number of attempts

        // Welcome message and game instructions
        System.out.println("Welcome to Guess the Number Game!");
        System.out.println("How many attempts do you want for each round?");
        int userAttempts = scanner.nextInt();

        while (playAgain && currentRound < totalRounds) {
            currentRound++;
            System.out.println("\nRound " + currentRound + ":");
            int attempts = 0;
            totalAttempts += userAttempts; // Increment total attempts
            System.out.println("You have " + userAttempts + " attempts to guess the correct number.");
            int randomNumber = random.nextInt(max - min + 1) + min;
            System.out.println("Guess the number between " + min + " and " + max + ":");

            boolean guessedCorrectly = false;

            while (!guessedCorrectly && attempts < userAttempts) {
                attempts++;
                int guessedNumber = scanner.nextInt();

                if (guessedNumber == randomNumber) {
                    System.out.println("Congratulations! You guessed the correct number in " + attempts + " attempts.");
                    guessedCorrectly = true;
                    int roundScore = calculateScore(attempts);
                    totalScore += roundScore; // Increment total score
                    System.out.println("Round " + currentRound + " Score: " + roundScore);
                } else if (guessedNumber < randomNumber) {
                    System.out.println("Try again! The number is higher.");
                } else {
                    System.out.println("Try again! The number is lower.");
                }
            }

            if (!guessedCorrectly) {
                System.out.println("Out of attempts! The correct number was: " + randomNumber);
            }

            System.out.println("Total Attempts so far: " + totalAttempts);
            System.out.println("Total Score so far: " + totalScore);
            System.out.println("Do you want to play the next round? (yes/no)");
            String playNextRoundResponse = scanner.next();

            if (!playNextRoundResponse.equalsIgnoreCase("yes")) {
                playAgain = false;
            }
        }

        // Display final score and thank you message
        System.out.println("\nFinal Score: " + totalScore);
        System.out.println("Thanks for playing!");
        scanner.close();
    }

    // Calculate score based on number of attempts
    private static int calculateScore(int attempts) {
        int baseScore = 100; // Base score for correct guess
        int scoreDeduction = 10 * (5 - attempts); // Score deduction for each wrong attempt
        return Math.max(0, baseScore - scoreDeduction); // Ensure score is non-negative
    }
}
