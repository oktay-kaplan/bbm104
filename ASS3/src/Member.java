import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private int id;
    private String type;
    private List<Book> borrowedBooks; //Books this person is currently borrowing from the library
    private List<Book> reading_atLibrary; //Books this person is currently reading in the library

    public Member(int id, String type) {
        this.id = id;
        this.borrowedBooks = new ArrayList<>();
        this.reading_atLibrary = new ArrayList<>();
        if (type.equals("S")){
            this.type = "Student";
            Main.students.add(this);
        }
        else if (type.equals("A")){
            this.type = "Academic";
            Main.academics.add(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
    public void borrowBook(Book book, LocalDate deadline, LocalDate taking_date, BufferedWriter writer) throws IOException {
        if ((getType().equals("Academic") && borrowedBooks.size() < 4) || (getType().equals("Student") && borrowedBooks.size() < 2)){
            this.borrowedBooks.add(book);
            book.setTaking_date(taking_date);
            book.setAvailable(false);
            book.setTaking_member(getId());
            book.setDeadline(deadline);
            Main.books.set(book.getId()-1,book);
            Main.borrow_books.add(book);
            writer.write("The book ["+book.getId()+"] was borrowed by member ["+getId()+"] at "+book.getTaking_date()+"\n");
        }else{
            writer.write("You have exceeded the borrowing limit!\n");
        }
    }
    public void readingAtLibrary(Book book,LocalDate taking_date,BufferedWriter writer) throws IOException {
        if (book.getType().equals("Printed")){
            this.reading_atLibrary.add(book);
            book.setAvailable(false);
            book.setTaking_member(getId());
            book.setTaking_date(taking_date);
            Main.books.set(book.getId()-1,book);
            Main.reading_atlibrary.add(book);
            writer.write("The book [" + book.getId() + "] was read in library by member [" + getId() + "] at " + book.getTaking_date()+"\n");
        }else if (book.getType().equals("Handwritten")){
            if (getType().equals("Academic")){
                this.reading_atLibrary.add(book);
                book.setAvailable(false);
                book.setTaking_date(taking_date);
                book.setTaking_member(getId());
                Main.books.set(book.getId()-1,book);
                Main.reading_atlibrary.add(book);
                writer.write("The book [" + book.getId() + "] was read in library by member [" + getId() + "] at " + book.getTaking_date()+"\n");
            }else{
                writer.write("Students can not read handwritten books!\n");
            }
        }
    }
    public void returnBook(Book book, LocalDate returnDate,BufferedWriter writer) throws IOException {
        int fee =0;

        if (book.getDeadline() != null) {
            // Calculate penalty if return date is after deadline
            if (returnDate.isAfter(book.getDeadline())) {
                long daysLate = (returnDate.getDayOfMonth() - book.getDeadline().getDayOfMonth());
                fee = (int) daysLate;
                writer.write(" Fee: " + fee+"\n");
            } else {
                writer.write(" Fee: 0\n");
            }
            this.borrowedBooks.remove(book);
            book.setAvailable(true);
            book.setDeadline(null);
            Main.borrow_books.remove(book);
        }else{
            writer.write(" Fee: 0\n");
            this.borrowedBooks.remove(book);
            book.setAvailable(true);
            book.setDeadline(null);
            Main.reading_atlibrary.remove(book);
        }
    }
    public void extendBook(Book book, LocalDate currentDate,BufferedWriter writer) throws IOException {
        if (book.getExtend_number() != 1){
            if (book.getDeadline() == null) {
                writer.write("Book is not borrowed.\n");
            }else if (book.getDeadline().isBefore(currentDate)) {
                writer.write("Book is already overdue, cannot extend deadline.\n");
            }else {
                if (getType().equals("Student")) {
                    book.setDeadline(book.getDeadline().plusDays(7)); // Extend by 7 days
                    writer.write("The deadline of book ["+book.getId()+"] was extended by member ["+getId()+"] at "+currentDate+"\n");
                    writer.write("New deadline of book ["+book.getId()+"] is "+book.getDeadline()+"\n");
                    book.setExtend_number(1);
                    Main.books.set(book.getId()-1,book);
                }else if (getType().equals("Academic")) {
                    book.setDeadline(book.getDeadline().plusDays(14)); // Extend by 14 days
                    writer.write("The deadline of book ["+book.getId()+"] was extended by member ["+getId()+"] at "+currentDate+"\n");
                    writer.write("New deadline of book ["+book.getId()+"] is "+book.getDeadline()+"\n");
                    book.setExtend_number(1);
                    Main.books.set(book.getId()-1,book);
                }
            }
        }else {
            writer.write("You cannot extend the deadline!\n");
        }
    }
}

