/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.controller.ReturnBookController
 *  com.neel.NewLibrary.model.ResponseMessage
 *  com.neel.NewLibrary.model.ReturnBook
 *  com.neel.NewLibrary.service.ReturnBookService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.NewLibrary.controller;

import com.neel.NewLibrary.model.ResponseMessage;
import com.neel.NewLibrary.model.ReturnBook;
import com.neel.NewLibrary.service.ReturnBookService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/v1/library"})
public class ReturnBookController {
    @Autowired
    private ReturnBookService returnBookService;

    @PostMapping(value={"/return-books"})
    public ResponseEntity<ResponseMessage> returnBook(@RequestBody ReturnBook returnBook) {
        try {
            ReturnBook returnedBook = this.returnBookService.returnBook(returnBook);
            return new ResponseEntity((Object)new ResponseMessage("Book returned successfully", (Object)returnedBook), (HttpStatusCode)HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value={"/return-books"})
    public ResponseEntity<ResponseMessage> getAllReturnBooks() {
        List returnBooks = this.returnBookService.getAllReturnBooksBySchool("67ca9167d56ec91fc9539234");
        return new ResponseEntity((Object)new ResponseMessage("Return books retrieved successfully", (Object)returnBooks), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/return-books/{id}"})
    public ResponseEntity<ResponseMessage> getReturnBookById(@PathVariable String id) {
        Optional returnBook = this.returnBookService.getReturnBookByIdAndSchool(id, "67ca9167d56ec91fc9539234");
        if (returnBook.isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Return book retrieved successfully", returnBook.get()), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Return book not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @PostMapping(value={"/return-books/{schoolId}"})
    public ResponseEntity<ResponseMessage> returnBookWithSchool(@PathVariable String schoolId, @RequestBody ReturnBook returnBook) {
        try {
            ReturnBook returnedBook = this.returnBookService.returnBookWithSchool(returnBook, schoolId);
            return new ResponseEntity((Object)new ResponseMessage("Book returned successfully", (Object)returnedBook), (HttpStatusCode)HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value={"/return-books/school/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllReturnBooksBySchool(@PathVariable String schoolId) {
        List returnBooks = this.returnBookService.getAllReturnBooksBySchool(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Return books retrieved successfully", (Object)returnBooks), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/return-books/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getReturnBookByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        Optional returnBook = this.returnBookService.getReturnBookByIdAndSchool(id, schoolId);
        if (returnBook.isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Return book retrieved successfully", returnBook.get()), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Return book not found for this school", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @GetMapping(value={"/return-books/student/{studentId}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getReturnBooksByStudentAndSchool(@PathVariable String studentId, @PathVariable String schoolId) {
        List returnBooks = this.returnBookService.getReturnBooksByStudentAndSchool(studentId, schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Return books retrieved successfully", (Object)returnBooks), (HttpStatusCode)HttpStatus.OK);
    }
}

