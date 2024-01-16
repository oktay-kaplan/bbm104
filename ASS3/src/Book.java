import java.time.LocalDate;

public class Book {
    private int id;
    private String type;
    private boolean available;
    private LocalDate deadline;
    private LocalDate taking_date;
    private int taking_member; //id of the member who bought the book
    private int extend_number;


    public Book(int id, String type) {
        this.id = id;
        this.available = true;
        this.deadline = null;
        this.taking_date = null;
        if (type.equals("P")){
            this.type = "Printed";
            Main.printeds.add(this);
        }
        else if (type.equals("H")){
            this.type = "Handwritten";
            Main.handwrittens.add(this);
        }
    }
    public Book(int id, String type,Boolean available,LocalDate deadline) {
        this.id = id;
        this.type = type;
        this.available = available;
        this.deadline = deadline;
    }


    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDate getTaking_date() {
        return taking_date;
    }

    public void setTaking_date(LocalDate taking_date) {
        this.taking_date = taking_date;
    }

    public int getTaking_member() {
        return taking_member;
    }

    public void setTaking_member(int taking_member) {
        this.taking_member = taking_member;
    }

    public int getExtend_number() {
        return extend_number;
    }

    public void setExtend_number(int extend_number) {
        this.extend_number = extend_number;
    }
}

