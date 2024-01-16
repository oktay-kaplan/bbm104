import java.util.HashMap;

public class MoveRight {
    static int first_location;

//If the next move is to the right, this function runs and performs the necessary operations.
    public static String move_right(int first_location1, int column, HashMap<Integer, String> map_board, HashMap<String, Integer> map_board2){
        String note1 = "Game over",note2 = "Go On";
        first_location = first_location1;
        int location = first_location1;

        if ((first_location +1 ) % (column) == 1) {
            first_location = first_location - (column - 1);
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
                first_location = location - 1;
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
                    return note1;
                } else {
                    map_board.put(location, map_board.get(first_location));map_board.put(first_location, "*");map_board2.put("*", first_location);
                    return note2;
                }
            } else if (map_board.get(first_location).equals("H")) {
                map_board.put(location, " ");
                return note1;
            } else {
                map_board.put(location, (map_board.get(first_location)));map_board.put(first_location, "*");map_board2.put("*", first_location);
                return note2;
            }
        }else if ((first_location +1 ) % (column) == 2){
            first_location = location + 1;
            if (map_board.get(first_location).equals("R")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("R");
                return note2;
            } else if (map_board.get(first_location).equals("Y")){
                map_board.put(first_location,"*");map_board2.put("*",first_location);map_board.put(location,"X");
                ScoreCalculate.calculate_score("Y");
                return note2;
            } else if (map_board.get(first_location).equals("B")){
                map_board.put(first_location,"*");map_board2.put("*",first_location);map_board.put(location,"X");
                ScoreCalculate.calculate_score("B");
                return note2;
            } else if (map_board.get(first_location).equals("W")) {
                first_location = location +column -1;
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
            }else if (map_board.get(first_location).equals("H")){
                map_board.put(location," ");
                return note1;
            } else {
                map_board.put(location, (map_board.get(first_location)));map_board.put(first_location, "*");map_board2.put("*", first_location);
                return note2;
            }
        } else {
            first_location = location + 1;
            if (map_board.get(first_location).equals("R")) {
                map_board.put(first_location, "*");map_board2.put("*", first_location);map_board.put(location, "X");
                ScoreCalculate.calculate_score("R");
                return note2;
            } else if (map_board.get(first_location).equals("Y")){
                map_board.put(first_location,"*");map_board2.put("*",first_location);map_board.put(location,"X");
                ScoreCalculate.calculate_score("Y");
                return note2;
            } else if (map_board.get(first_location).equals("B")){
                map_board.put(first_location,"*");map_board2.put("*",first_location);map_board.put(location,"X");
                ScoreCalculate.calculate_score("B");
                return note2;
            } else if (map_board.get(first_location).equals("W")) {
                first_location = location - 1;
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
                    return note1;
                } else {
                    map_board.put(location,map_board.get(first_location));map_board.put(first_location, "*");map_board2.put("*", first_location);
                    return note2;
                }
            }else if (map_board.get(first_location).equals("H")){
                map_board.put(location," ");
                return note1;
            } else {
                map_board.put(location, (map_board.get(first_location)));map_board.put(first_location, "*");map_board2.put("*", first_location);
                return note2;
            }
        }
    }
}
