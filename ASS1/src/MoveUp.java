import java.util.HashMap;

public class MoveUp {
    static int first_location;

    //If the next move is to the up, this function runs and performs the necessary operations.
    public static String move_up(int first_location1, int column, int row, HashMap<Integer, String> map_board, HashMap<String, Integer> map_board2) {
        String note1 = "Game over";
        String note2 = "Go On";
        first_location = first_location1;
        int location = first_location1;
        if (first_location <= column) {
            first_location = first_location + (row - 1) * column;
            if (map_board.get(first_location).equals("R")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("R");
                return note2;
            } else if (map_board.get(first_location).equals("Y")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("Y");
                return note2;
            } else if (map_board.get(first_location).equals("B")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("B");
                return note2;
            } else if (map_board.get(first_location).equals("W")) {
                first_location = location + column;
                if (map_board.get(first_location).equals("R")) {
                    map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                    ScoreCalculate.calculate_score("R");
                    return note2;
                } else if (map_board.get(first_location).equals("Y")) {
                    map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                    ScoreCalculate.calculate_score("Y");
                    return note2;
                } else if (map_board.get(first_location).equals("B")) {
                    map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                    ScoreCalculate.calculate_score("B");
                    return note2;
                } else if (map_board.get(first_location).equals("H")) {
                    map_board.put(location, " ");
                    return note1;}
                else {
                    map_board.put(location,map_board.get(first_location));map_board.put(first_location, "*");map_board2.put("*", first_location);
                    return note2;
                }
            } else if (map_board.get(first_location).equals("H")) {
                map_board.put(location, " ");
                return note1;
            } else {
                map_board.put(location, (map_board.get(first_location)));map_board.put(first_location, "*");map_board2.put("*", first_location);
                return note2;
            }
        }else if (column*(row-1) < first_location ) {
            first_location = first_location - column;
            if (map_board.get(first_location).equals("R")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("R");
                return note2;
            } else if (map_board.get(first_location).equals("Y")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("Y");
                return note2;
            } else if (map_board.get(first_location).equals("B")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("B");
                return note2;
            } else if (map_board.get(first_location).equals("W")) {
                first_location = location % column;
                if (map_board.get(first_location).equals("R")) {
                    map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                    ScoreCalculate.calculate_score("R");
                    return note2;
                } else if (map_board.get(first_location).equals("Y")) {
                    map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                    ScoreCalculate.calculate_score("Y");
                    return note2;
                } else if (map_board.get(first_location).equals("B")) {
                    map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                    ScoreCalculate.calculate_score("B");
                    return note2;
                } else if (map_board.get(first_location).equals("H")) {
                    map_board.put(location, " ");
                    return note1;}
                else {
                    map_board.put(location,map_board.get(first_location));map_board.put(first_location, "*");map_board2.put("*", first_location);
                    return note2;
                }
            } else if (map_board.get(first_location).equals("H")) {
                map_board.put(location, " ");
                return note1;
            } else {
                map_board.put(location, (map_board.get(first_location)));map_board.put(first_location, "*");map_board2.put("*", first_location);
                return note2;
            }
        } else {
            first_location = first_location - column;
            if (map_board.get(first_location).equals("R")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("R");
                return note2;
            } else if (map_board.get(first_location).equals("Y")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("Y");
                return note2;
            } else if (map_board.get(first_location).equals("B")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("B");
                return note2;
            } else if (map_board.get(first_location).equals("W")) {
                first_location = location + column;
                if (map_board.get(first_location).equals("R")) {
                    map_board.put(location, "X");map_board.put(first_location, "*");map_board2.put("*", first_location);
                    ScoreCalculate.calculate_score("R");
                    return note2;
                } else if (map_board.get(first_location).equals("Y")) {
                    map_board.put(location, "X");map_board.put(first_location, "*");map_board2.put("*", first_location);
                    ScoreCalculate.calculate_score("Y");
                    return note2;
                } else if (map_board.get(first_location).equals("B")) {
                    map_board.put(location, "X");map_board.put(first_location, "*");map_board2.put("*", first_location);
                    ScoreCalculate.calculate_score("B");
                    return note2;
                } else if (map_board.get(first_location).equals("H")) {
                    map_board.put(location, " ");
                    return note1;}
                else {
                    map_board.put(location,map_board.get(first_location));map_board.put(first_location, "*");map_board2.put("*", first_location);
                    return note2;
                }
            } else if (map_board.get(first_location).equals("H")) {
                map_board.put(location, " ");
                return note1;
            } else {
                map_board.put(location, (map_board.get(first_location)));map_board.put(first_location, "*");map_board2.put("*", first_location);
                return note2;
            }
        }
    }
}