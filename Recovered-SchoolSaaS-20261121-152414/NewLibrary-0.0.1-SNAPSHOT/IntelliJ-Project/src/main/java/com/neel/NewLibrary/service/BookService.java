/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.Book
 *  com.neel.NewLibrary.repo.BookRepository
 *  com.neel.NewLibrary.service.BookService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.NewLibrary.service;

import com.neel.NewLibrary.model.Book;
import com.neel.NewLibrary.repo.BookRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book saveBook(Book book) {
        book.setSchoolId("67ca9167d56ec91fc9539234");
        this.validateBookQuantity(book);
        book.updateStatus();
        return (Book)this.bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return this.bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return this.bookRepository.findById(id);
    }

    public Book updateBook(String id, Book updatedBook) {
        Optional existingBook = this.bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = (Book)existingBook.get();
            book.setBookName(updatedBook.getBookName());
            book.setAuthor(updatedBook.getAuthor());
            book.setBookNumber(updatedBook.getBookNumber());
            book.setBookQuantity(updatedBook.getBookQuantity());
            book.setBookCategory(updatedBook.getBookCategory());
            book.setClassAssigned(updatedBook.getClassAssigned());
            book.updateStatus();
            return (Book)this.bookRepository.save(book);
        }
        throw new RuntimeException("Book not found");
    }

    public void deleteBook(String id) {
        this.bookRepository.deleteById(id);
    }

    public Book saveBookWithSchool(Book book, String schoolId) {
        book.setSchoolId(schoolId);
        this.validateBookQuantity(book);
        book.updateStatus();
        return (Book)this.bookRepository.save(book);
    }

    public List<Book> getAllBooksBySchool(String schoolId) {
        return this.bookRepository.findBySchoolId(schoolId);
    }

    public Optional<Book> getBookByIdAndSchool(String id, String schoolId) {
        return this.bookRepository.findByIdAndSchoolId(id, schoolId);
    }

    public Book updateBookWithSchool(String id, Book updatedBook, String schoolId) {
        Optional existingBook = this.bookRepository.findByIdAndSchoolId(id, schoolId);
        if (existingBook.isPresent()) {
            Book book = (Book)existingBook.get();
            book.setBookName(updatedBook.getBookName());
            book.setAuthor(updatedBook.getAuthor());
            book.setBookNumber(updatedBook.getBookNumber());
            book.setBookQuantity(updatedBook.getBookQuantity());
            book.setBookCategory(updatedBook.getBookCategory());
            book.setClassAssigned(updatedBook.getClassAssigned());
            book.updateStatus();
            book.setSchoolId(schoolId);
            return (Book)this.bookRepository.save(book);
        }
        throw new RuntimeException("Book not found for this school");
    }

    public void deleteBookWithSchool(String id, String schoolId) {
        Optional book = this.bookRepository.findByIdAndSchoolId(id, schoolId);
        if (!book.isPresent()) {
            throw new RuntimeException("Book not found for this school");
        }
        this.bookRepository.delete(((Book)book.get()));
    }

    private void validateBookQuantity(Book book) {
        if (book.getBookQuantity() < 0) {
            throw new IllegalArgumentException("Book quantity cannot be negative");
        }
    }

    public long getTotalBookCount() {
        return this.bookRepository.count();
    }

    public long getTotalBookCountBySchool(String schoolId) {
        List books = this.bookRepository.findBySchoolId(schoolId);
        return books.size();
    }

    public int getTotalBookQuantity() {
        List<Book> allBooks = this.bookRepository.findAll();
        return allBooks.stream().mapToInt(Book::getBookQuantity).sum();
    }

    public long getTotalBookQuantityBySchool(String schoolId) {
        List<Book> schoolBooks = this.bookRepository.findBySchoolId(schoolId);
        return schoolBooks.stream().mapToInt(Book::getBookQuantity).sum();
    }
}

