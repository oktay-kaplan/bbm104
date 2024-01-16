public class ScoreCalculate {
    static int score = 0;

//This method takes the color to go as a parameter and stores the score that should be written at the end of the game.
    public static int calculate_score(String colour) {
        switch (colour) {
            case "R":
                score = score + 10;
                return score;
            case "Y":
                score = score + 5;
                return score;
            case "B":
                score = score - 5;
                return score;
            default:
                return score;
        }
    }
}
