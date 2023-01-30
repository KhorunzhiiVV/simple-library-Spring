package ru.khorunzhii.simplelibrary.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Title should not be empty")
    @Size(min = 1, max = 100, message = "Title should be between 1 and 100 characters")
    @Column(name = "title")
    private String title;
    @Size(min = 1, max = 100, message = "Author name should be between 1 and 100 characters")
    @Column(name = "author")
    private String author;
    @Min(value = 1, message = "Year should be greater then 0")
    @Column(name = "year")
    private int year;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "date_of_assign")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfAssign;

    public Book(){
    }


    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getDateOfAssign() {
        return dateOfAssign;
    }

    public void setDateOfAssign(Date dateOfAssign) {
        this.dateOfAssign = dateOfAssign;
    }

    public boolean isOutdated() {
        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        c.add(Calendar.DATE, -10); //same with c.add(Calendar.DAY_OF_MONTH, 1);

        Date currentDateMinusTenDays = c.getTime();
        return this.getDateOfAssign() != null && this.getDateOfAssign().before(currentDateMinusTenDays);
    }



    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", owner=" + owner +
                '}';
    }
}
