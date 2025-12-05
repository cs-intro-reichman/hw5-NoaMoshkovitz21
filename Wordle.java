import java.util.Random;
import java.util.NoSuchElementException;

public class Wordle {

    public static final int WORD_LENGTH = 5;
    public static final int MAX_ATTEMPTS = 6;
 
    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
    In in = new In(filename);    
    String[] dict = in.readAllStrings();
    in.close();
    return dict;

} 
    public static String chooseSecretWord(String[] dict) {

        if (dict == null || dict.length == 0) {
            return null;
    }
        Random rand = new Random();
        int randomIndex = rand.nextInt(dict.length);
        return dict[randomIndex];
}
    public static boolean containsChar(String secret, char c) {
		return secret.indexOf(c) != -1;
    }

    public static void computeFeedback(String secret, String guess, char[] resultRow) {
		
        for (int i = 0; i< WORD_LENGTH; i++) {
            char guessChar = guess.charAt(i);
            char seceretChar = secret.charAt(i);

            if (guessChar == seceretChar) {
                resultRow[i] = 'G';
            }   else if (containsChar(secret, seceretChar)) {
                resultRow[i] = 'Y';
            }   else    {
                resultRow[i] = '_';
            }
        }
		// you may want to use containsChar in your implementation
    }

    // Store guess string (chars) into the given row of guesses 2D array.
    // For example, of guess is HELLO, and row is 2, then after this function 
    // guesses should look like:
    // guesses[2][0] // 'H'
	// guesses[2][1] // 'E'
	// guesses[2][2] // 'L'
	// guesses[2][3] // 'L'
	// guesses[2][4] // 'O'
    public static void storeGuess(String guess, char[][] guesses, int row) {
		for (int col = 0; col < WORD_LENGTH; col++) {
            guesses[row][col] = guess.charAt(col);
        }
        System.out.println();
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
		for (char c : resultRow) {
            if (c != 'G') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String[] dict = readDictionary("dictionary.txt");

        String secret = chooseSecretWord(dict);

        if (secret == null) {
            System.err.println("Error: Dictionary is empty or could not be read.");
            return;
        }

        char[][] guesses = new char[MAX_ATTEMPTS][WORD_LENGTH];
        char[][] results = new char[MAX_ATTEMPTS][WORD_LENGTH];

        In inp = new In();
        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");

                try { 
                guess = inp.readLine();
                } catch (NoSuchElementException e) {

                    break;
                }
                if (guess == null || guess.length() != WORD_LENGTH) {
                    System.out.println("Invalid word. Please try asgain.");
                } else {
                    if (guess.matches("^[A-Z]*$")){
                        valid = true;

                    } else {
                        System.out.println("invalid word. Please try again.");
                    }
                }

            }
            if (!valid) break;

            storeGuess(guess, guesses, attempt);
            
            computeFeedback(secret, guess, results[attempt]);
            
            printBoard(guesses, results, attempt);

                if (isAllGreen(results[attempt])) {
                    System.out.println("Congratulations! You guesses the word in " + (attempt + 1) + " attempts.");
                    won=true;
                }
                attempt++;
        }


            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }
            attempt++;

            if (!won) {
            System.err.println("Sorry, you did not guess the word.");
            System.out.println("The seceret word was: " + secret);

        }

        inp.close();
    }
}
