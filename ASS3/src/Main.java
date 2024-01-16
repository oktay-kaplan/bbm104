import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<Book> books = new ArrayList<Book>();
    public static List<Member> members = new ArrayList<Member>();
    public static List<Member> students = new ArrayList<Member>();
    public static List<Member> academics = new ArrayList<Member>();

    public static List<Book> borrow_books = new ArrayList<Book>();  //all books currently borrowed from the library
    public static List<Book> reading_atlibrary = new ArrayList<Book>();   //all books currently being read in the library

    public static List<Book> printeds = new ArrayList<Book>();
    public static List<Book> handwrittens = new ArrayList<Book>();



    public static void main(String[] args) throws IOException {

        Reader reader = new Reader(args[0],args[1]);
        reader.readfile();
    }
}