import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

    static String end_message ="", real_moves ="";

    public static void main(String[] args) throws IOException {

        FileReader fileReader1 = new FileReader(args[0]);
        BufferedReader br1 = new BufferedReader(fileReader1);

        FileReader fileReader2 = new FileReader(args[1]);
        BufferedReader br2 = new BufferedReader(fileReader2);

        String line1,line2;

        ArrayList<String> board = new ArrayList<>();
        ArrayList<String> moves = new ArrayList<>();

        HashMap<Integer, String> map_board = new HashMap<>();
        HashMap<String, Integer> map_board2 = new HashMap<>();

        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));

        writer.write("Game board:\n");

        int row = 0,column = 0;

        while ((line2 = br1.readLine()) != null) {
            row++;
            writer.write(line2 + "\n");
            String[] liner;
            liner = line2.split(" ");
            column = liner.length;
            board.addAll(Arrays.asList(liner));
        }
        while ((line1 = br2.readLine()) != null) {
            String[] liner;
            liner = line1.split(" ");
            moves.addAll(Arrays.asList(liner));
        }
        int count1 = 1;
        for (String cell : board) {
            map_board.put(count1, cell);
            map_board2.put(cell, count1);
            count1++;
        }
        int first_location = map_board2.get("*");

        run(first_location, column, row,moves, map_board, map_board2);

        writer.write("\nYour movement is:");
        writer.write("\n" + real_moves);
        writer.write("\n\nYour output is:\n");

        int count2 =0;
        for (String value : map_board.values()){
            count2 ++;
            if (count2 % column ==0 ) {
                writer.write(value+" \n");
            }else{
                writer.write(value+" ");
            }
        }
        if (end_message.equals("Game over")){
            writer.write("\nGame Over!");
        }
        writer.write("\nScore: "+ScoreCalculate.score);

        writer.close();
        br1.close();
        br2.close();
    }
    public static void run(int first_location, int column, int row,ArrayList<String> moves , HashMap<Integer, String> map_board, HashMap<String, Integer> map_board2){
            for (String move : moves) {
                real_moves = real_moves + move+" ";
                if (move.equals("R")) {
                    if (MoveRight.move_right(first_location, column, map_board, map_board2).equals("Game over")) {
                        end_message = "Game over";
                        break;
                    } else {
                        first_location = MoveRight.first_location;
                    }
                } else if (move.equals("L")) {
                    if (MoveLeft.move_left(first_location, column, map_board, map_board2).equals("Game over")) {
                        end_message = "Game over";
                        break;
                    }else {
                        first_location =MoveLeft.first_location;
                    }
                } else if (move.equals("U")) {
                    if (MoveUp.move_up(first_location, column, row, map_board, map_board2).equals("Game over")) {
                        end_message = "Game over";
                        break;
                    }else {
                        first_location =MoveUp.first_location;
                    }
                } else if (move.equals("D")) {
                    if (MoveDown.move_down(first_location, column, row, map_board, map_board2).equals("Game over")){
                        end_message = "Game over";
                        break;
                    }else {
                        first_location = MoveDown.first_location;
                    }
                }
            }
    }
}