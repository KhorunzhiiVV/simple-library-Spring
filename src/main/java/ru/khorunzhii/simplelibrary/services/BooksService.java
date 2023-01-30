package ru.khorunzhii.simplelibrary.services;

import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khorunzhii.simplelibrary.models.Book;
import ru.khorunzhii.simplelibrary.models.Person;
import ru.khorunzhii.simplelibrary.repositories.BooksRepository;

import javax.persistence.Transient;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear){
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public List<Book> findAll(String page, String booksPerPage, boolean sortByYear){
        try{
            int pageToshow = Integer.parseInt(page);
            int booksPerPageToShow = Integer.parseInt(booksPerPage);
            if (sortByYear)
                return booksRepository.findAll(PageRequest.of(pageToshow,booksPerPageToShow, Sort.by("year"))).getContent();
            else
                return booksRepository.findAll(PageRequest.of(pageToshow,booksPerPageToShow)).getContent();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return findAll(sortByYear);
    }

    public Book findOne(int id){
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    public Book findByTitle(String title){
        return booksRepository.findByTitle(title);
    }

    public List<Book> findByTitleLike(String searchQuery){
        List<Book> books = booksRepository.findByTitleLike("%" + searchQuery + "%");
        for (Book book : books)
            Hibernate.initialize(book);
        return books;
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void updateOwner(int id, Person newOwner){
        Book book = booksRepository.getOne(id);
        book.setOwner(newOwner);
        book.setDateOfAssign(new Date());
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(int id){
        Book book = booksRepository.getOne(id);
        book.setOwner(null);
        book.setDateOfAssign(null);
        booksRepository.save(book);
    }

    @Transactional
    public Person getOwnerByBookId(int id){
        Optional<Book> book = booksRepository.findById(id);

        if (book.isPresent()){
            Hibernate.initialize(book.get().getOwner());
            return book.get().getOwner();
        } else{
            return null;
        }
    }

    public boolean isOwnedMoreThan10Days(Book book){
        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        c.add(Calendar.DATE, -10); //same with c.add(Calendar.DAY_OF_MONTH, 1);

        Date currentDateMinusTenDays = c.getTime();
        System.out.println(currentDateMinusTenDays);
        System.out.println(book.getDateOfAssign());
        System.out.println(book.getDateOfAssign() != null && book.getDateOfAssign().after(currentDateMinusTenDays));
        return book.getDateOfAssign() != null && book.getDateOfAssign().after(currentDateMinusTenDays);

    }
}
