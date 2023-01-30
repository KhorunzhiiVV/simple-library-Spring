package ru.khorunzhii.simplelibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.khorunzhii.simplelibrary.models.Book;
import ru.khorunzhii.simplelibrary.models.Person;
import ru.khorunzhii.simplelibrary.services.BooksService;
import ru.khorunzhii.simplelibrary.services.PeopleService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, HttpServletRequest request){
        String page = request.getParameter("page");
        String booksPerPage = request.getParameter("books_per_page");
        boolean sortByYear = Objects.equals(request.getParameter("sort_by_year"), "true");
        System.out.println(request.getParameter("sort_by_year"));
        System.out.println(sortByYear);
        if (page != null && booksPerPage != null)
            model.addAttribute("books", booksService.findAll(page,booksPerPage, sortByYear));
        else
            model.addAttribute("books", booksService.findAll(sortByYear));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        Book book = booksService.findOne(id);

        model.addAttribute("book", book);
        if (booksService.getOwnerByBookId(id) == null)
            model.addAttribute("people", peopleService.findAll());
        else
            model.addAttribute("owner", booksService.getOwnerByBookId(id));
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @GetMapping("/search")
    public String bookSearch(Model model, HttpServletRequest request) {
        String searchQuery = request.getParameter("userInput");
        if (searchQuery != null && !searchQuery.equals(""))
            model.addAttribute("books", booksService.findByTitleLike(searchQuery));
        model.addAttribute("userInput", searchQuery);
        System.out.println(searchQuery);
        return "books/search";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){

        //bookValidator.validate(book, bindingResult);

        //if (bindingResult.hasErrors())
        //    return "books/new";

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id){
        //bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String updateOwner(@ModelAttribute("person")Person selectedPerson, BindingResult bindingResult,
                              @PathVariable("id") int id){
        System.out.println(selectedPerson);
        booksService.updateOwner(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String updateOwner(@PathVariable("id") int id){
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }

}
