package ru.khorunzhii.simplelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khorunzhii.simplelibrary.models.Book;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    public Book findByTitle(String title);

    public List<Book> findByTitleLike(String searchQuery);
}
