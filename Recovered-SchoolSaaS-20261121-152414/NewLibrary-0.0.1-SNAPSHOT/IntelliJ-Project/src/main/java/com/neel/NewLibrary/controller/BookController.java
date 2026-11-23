/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.controller.BookController
 *  com.neel.NewLibrary.model.Book
 *  com.neel.NewLibrary.model.ResponseMessage
 *  com.neel.NewLibrary.service.BookService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.NewLibrary.controller;

import com.neel.NewLibrary.model.Book;
import com.neel.NewLibrary.model.ResponseMessage;
import com.neel.NewLibrary.service.BookService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/v1/library"})
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping(value={"/books"})
    public ResponseEntity<ResponseMessage> createBook(@RequestBody Book book) {
        try {
            Book savedBook = this.bookService.saveBook(book);
            return new ResponseEntity((Object)new ResponseMessage("Book created successfully", savedBook), (HttpStatusCode)HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity((Object)new ResponseMessage("Error creating book: " + e.getMessage(), null), (HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value={"/books"})
    public ResponseEntity<ResponseMessage> getAllBooks() {
        List books = this.bookService.getAllBooks();
        return ResponseEntity.ok(new ResponseMessage("Books retrieved successfully", books));
    }

    @GetMapping(value={"/books/id/{id}"})
    public ResponseEntity<ResponseMessage> getBookById(@PathVariable String id) {
        Optional book = this.bookService.getBookById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(new ResponseMessage("Book retrieved successfully", book.get()));
        }
        return new ResponseEntity((Object)new ResponseMessage("Book not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @PutMapping(value={"/books/{id}"})
    public ResponseEntity<ResponseMessage> updateBook(@PathVariable String id, @RequestBody Book book) {
        try {
            Book updatedBook = this.bookService.updateBook(id, book);
            return ResponseEntity.ok(new ResponseMessage("Book updated successfully", updatedBook));
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage("Book not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value={"/books/{id}"})
    public ResponseEntity<ResponseMessage> deleteBook(@PathVariable String id) {
        try {
            this.bookService.deleteBook(id);
            return ResponseEntity.ok(new ResponseMessage("Book deleted successfully", null));
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage("Book not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value={"/books/{schoolId}"})
    public ResponseEntity<ResponseMessage> createBookWithSchool(@PathVariable String schoolId, @RequestBody Book book) {
        try {
            Book savedBook = this.bookService.saveBookWithSchool(book, schoolId);
            return new ResponseEntity((Object)new ResponseMessage("Book created successfully", savedBook), (HttpStatusCode)HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity((Object)new ResponseMessage("Error creating book: " + e.getMessage(), null), (HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value={"/books/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllBooksBySchool(@PathVariable String schoolId) {
        List books = this.bookService.getAllBooksBySchool(schoolId);
        return ResponseEntity.ok(new ResponseMessage("Books retrieved successfully", books));
    }

    @GetMapping(value={"/books/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getBookByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        Optional book = this.bookService.getBookByIdAndSchool(id, schoolId);
        if (book.isPresent()) {
            return ResponseEntity.ok(new ResponseMessage("Book retrieved successfully", book.get()));
        }
        return new ResponseEntity((Object)new ResponseMessage("Book not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @PutMapping(value={"/books/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateBookWithSchool(@PathVariable String id, @PathVariable String schoolId, @RequestBody Book book) {
        try {
            Book updatedBook = this.bookService.updateBookWithSchool(id, book, schoolId);
            return ResponseEntity.ok(new ResponseMessage("Book updated successfully", updatedBook));
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage("Book not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value={"/books/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteBookWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        try {
            this.bookService.deleteBookWithSchool(id, schoolId);
            return ResponseEntity.ok(new ResponseMessage("Book deleted successfully", null));
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage("Book not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value={"/books/count"})
    public ResponseEntity<ResponseMessage> getTotalBookCount() {
        long count = this.bookService.getTotalBookCount();
        return new ResponseEntity((Object)new ResponseMessage("Total book count retrieved successfully", count), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/books/quantity"})
    public ResponseEntity<ResponseMessage> getTotalBookQuantity() {
        int quantity = this.bookService.getTotalBookQuantity();
        return new ResponseEntity((Object)new ResponseMessage("Total book quantity retrieved successfully", quantity), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/books/count/{schoolId}"})
    public ResponseEntity<ResponseMessage> getTotalBookCountBySchool(@PathVariable String schoolId) {
        long count = this.bookService.getTotalBookCountBySchool(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Total book count retrieved successfully", count), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/books/quantity/{schoolId}"})
    public ResponseEntity<ResponseMessage> getTotalBookQuantityBySchool(@PathVariable String schoolId) {
        long quantity = this.bookService.getTotalBookQuantityBySchool(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Total book quantity retrieved successfully", quantity), (HttpStatusCode)HttpStatus.OK);
    }
}

