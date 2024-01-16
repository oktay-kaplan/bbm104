import java.io.*;
import java.time.LocalDate;

public class Reader {

    String input_file;
    String output_file;

    public Reader(String input_file,String output_file) {
        this.input_file = input_file;
        this.output_file = output_file;
    }
    public void readfile() throws IOException {

        FileReader fileReader1 = new FileReader(input_file);
        BufferedReader br1 = new BufferedReader(fileReader1);
        BufferedWriter writer = new BufferedWriter(new FileWriter(output_file));

        String line;
        int book_id = 0;  // total book number
        int member_id = 0; // total member number

        while ((line = br1.readLine()) != null) {
            String[] split = line.split("\t");
            if (split[0].equals("addBook")) {
                book_id ++;
                Main.books.add(new Book(book_id,split[1]));
                writer.write("Created new book: "+Main.books.get(book_id-1).getType()+" [id: "+ book_id +"]\n");
            }
            else if (split[0].equals("addMember")) {
                member_id ++;
                Main.members.add(new Member(member_id,split[1]));
                writer.write("Created new member: "+Main.members.get(member_id-1).getType()+" [id: "+member_id+"]\n");
            }
            else if (split[0].equals("borrowBook")) {
                if (Main.books.get(Integer.parseInt(split[1]) - 1).isAvailable()) {
                    if (Main.members.get(Integer.parseInt(split[2])).getType().equals("Student")){
                        LocalDate current_date = LocalDate.parse(split[3]);
                        LocalDate deadline = current_date.plusWeeks(1);
                        Main.members.get(Integer.parseInt(split[2])-1).borrowBook(Main.books.get(Integer.parseInt(split[1])-1),deadline,current_date,writer);
                    }
                    else if (Main.members.get(Integer.parseInt(split[2])).getType().equals("Academic")) {
                        LocalDate current_date = LocalDate.parse(split[3]);
                        LocalDate deadline = current_date.plusWeeks(2);
                        Main.members.get(Integer.parseInt(split[2])-1).borrowBook(Main.books.get(Integer.parseInt(split[1])-1),deadline,current_date,writer);
                    }
                } else {
                    writer.write("You can not borrow this book!\n");
                }
            }
            else if (split[0].equals("returnBook")) {
                LocalDate return_date = LocalDate.parse(split[3]);
                writer.write("The book ["+split[1]+"] was returned by member ["+split[2]+"] at "+split[3]);
                Main.members.get(Integer.parseInt(split[2])-1).returnBook(Main.books.get(Integer.parseInt(split[1])-1),return_date,writer);

            }
            else if (split[0].equals("extendBook")) {
                LocalDate current_date = LocalDate.parse(split[3]);
                Main.members.get(Integer.parseInt(split[2])-1).extendBook(Main.books.get(Integer.parseInt(split[1])-1),current_date,writer);
            }
            else if (split[0].equals("readInLibrary")) {
                if (Main.books.get(Integer.parseInt(split[1]) - 1).isAvailable()) {
                    LocalDate current_date = LocalDate.parse(split[3]);
                    Main.members.get(Integer.parseInt(split[2]) - 1).readingAtLibrary(Main.books.get(Integer.parseInt(split[1]) - 1), current_date,writer);
                } else {
                    writer.write("You can not read this book!\n");
                }
            }else if (split[0].equals("getTheHistory")) {

                writer.write("History of library:\n\n");

                writer.write("Number of students: "+Main.students.size()+"\n");
                for (int i =0;i<Main.students.size();i++) {
                    writer.write("Student [id: " + Main.students.get(i).getId() + "]\n");
                }
                writer.write("\nNumber of academics: "+Main.academics.size()+"\n");
                for (int i =0;i<Main.academics.size();i++)
                    writer.write("Academic [id: "+Main.academics.get(i).getId()+"]\n");

                writer.write("\nNumber of printed books: "+Main.printeds.size()+"\n");
                for (int i =0;i<Main.printeds.size();i++)
                    writer.write("Printed [id: "+Main.printeds.get(i).getId()+"]\n");

                writer.write("\nNumber of handwritten books: "+Main.handwrittens.size()+"\n");
                for (int i =0;i<Main.handwrittens.size();i++)
                    writer.write("Handwritten [id: "+Main.handwrittens.get(i).getId()+"]\n");

                writer.write("\nNumber of borrowed books: "+Main.borrow_books.size()+"\n");
                for (int i =0;i<Main.borrow_books.size();i++) {
                    writer.write("The book [" + Main.borrow_books.get(i).getId() + "] was borrowed by member " +
                            "["+Main.borrow_books.get(i).getTaking_member()+"] at "+Main.borrow_books.get(i).getTaking_date()+"\n");
                }

                writer.write("\nNumber of books read in library: "+Main.reading_atlibrary.size()+"\n");
                for (int i =0;i<Main.reading_atlibrary.size();i++) {
                    writer.write("The book [" + Main.reading_atlibrary.get(i).getId() + "] was read in library by member " +
                            "[" + Main.reading_atlibrary.get(i).getTaking_member() + "] at " + Main.reading_atlibrary.get(i).getTaking_date()+"\n");
                }

            }
            else{
                writer.write("ERROR: Erroneous command!\n");
            }

        }
        writer.close();
    }
}

