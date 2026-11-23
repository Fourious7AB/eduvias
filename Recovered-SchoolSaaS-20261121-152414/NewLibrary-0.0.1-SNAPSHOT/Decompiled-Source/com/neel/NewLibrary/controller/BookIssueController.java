/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.controller.BookIssueController
 *  com.neel.NewLibrary.model.BookIssue
 *  com.neel.NewLibrary.model.ResponseMessage
 *  com.neel.NewLibrary.service.BookIssueService
 *  com.neel.NewLibrary.service.BookService
 *  com.neel.NewLibrary.service.CategoryService
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

import com.neel.NewLibrary.model.BookIssue;
import com.neel.NewLibrary.model.ResponseMessage;
import com.neel.NewLibrary.service.BookIssueService;
import com.neel.NewLibrary.service.BookService;
import com.neel.NewLibrary.service.CategoryService;
import java.util.HashMap;
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
public class BookIssueController {
    @Autowired
    private BookIssueService bookIssueService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping(value={"/book-issues"})
    public ResponseEntity<ResponseMessage> issueBook(@RequestBody BookIssue bookIssue) {
        try {
            BookIssue issuedBook = this.bookIssueService.issueBook(bookIssue);
            return new ResponseEntity((Object)new ResponseMessage("Book issued successfully", (Object)issuedBook), (HttpStatusCode)HttpStatus.CREATED);
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value={"/book-issues/id/{id}"})
    public ResponseEntity<ResponseMessage> getBookIssue(@PathVariable String id) {
        Optional bookIssue = this.bookIssueService.getBookIssueByIdAndSchool(id, "67ca9167d56ec91fc9539234");
        if (bookIssue.isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Book issue found", bookIssue.get()), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Book issue not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @GetMapping(value={"/book-issues"})
    public ResponseEntity<ResponseMessage> getAllBookIssues() {
        List bookIssues = this.bookIssueService.getAllBookIssuesBySchool("67ca9167d56ec91fc9539234");
        return new ResponseEntity((Object)new ResponseMessage("List of book issues", (Object)bookIssues), (HttpStatusCode)HttpStatus.OK);
    }

    @PutMapping(value={"/book-issues/{id}"})
    public ResponseEntity<ResponseMessage> updateBookIssue(@PathVariable String id, @RequestBody BookIssue bookIssue) {
        try {
            BookIssue updatedBookIssue = this.bookIssueService.updateBookIssueWithSchool(id, bookIssue, "67ca9167d56ec91fc9539234");
            return new ResponseEntity((Object)new ResponseMessage("Book issue updated successfully", (Object)updatedBookIssue), (HttpStatusCode)HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value={"/book-issues/{id}"})
    public ResponseEntity<ResponseMessage> deleteBookIssue(@PathVariable String id) {
        try {
            this.bookIssueService.deleteBookIssueWithSchool(id, "67ca9167d56ec91fc9539234");
            return new ResponseEntity((Object)new ResponseMessage("Book issue deleted successfully", null), (HttpStatusCode)HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value={"/book-issues/{schoolId}"})
    public ResponseEntity<ResponseMessage> issueBookWithSchool(@PathVariable String schoolId, @RequestBody BookIssue bookIssue) {
        try {
            BookIssue issuedBook = this.bookIssueService.issueBookWithSchool(bookIssue, schoolId);
            return new ResponseEntity((Object)new ResponseMessage("Book issued successfully", (Object)issuedBook), (HttpStatusCode)HttpStatus.CREATED);
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value={"/book-issues/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllBookIssuesBySchool(@PathVariable String schoolId) {
        List bookIssues = this.bookIssueService.getAllBookIssuesBySchool(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("List of book issues for school", (Object)bookIssues), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/book-issues/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getBookIssueBySchool(@PathVariable String id, @PathVariable String schoolId) {
        Optional bookIssue = this.bookIssueService.getBookIssueByIdAndSchool(id, schoolId);
        if (bookIssue.isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Book issue found", bookIssue.get()), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Book issue not found for this school", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @PutMapping(value={"/book-issues/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateBookIssueWithSchool(@PathVariable String id, @PathVariable String schoolId, @RequestBody BookIssue bookIssue) {
        try {
            BookIssue updatedBookIssue = this.bookIssueService.updateBookIssueWithSchool(id, bookIssue, schoolId);
            return new ResponseEntity((Object)new ResponseMessage("Book issue updated successfully", (Object)updatedBookIssue), (HttpStatusCode)HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value={"/book-issues/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteBookIssueWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        try {
            this.bookIssueService.deleteBookIssueWithSchool(id, schoolId);
            return new ResponseEntity((Object)new ResponseMessage("Book issue deleted successfully", null), (HttpStatusCode)HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return new ResponseEntity((Object)new ResponseMessage(e.getMessage(), null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value={"/book-issues/pending-count/{studentId}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getPendingBookCount(@PathVariable String studentId, @PathVariable String schoolId) {
        List pendingBooks = this.bookIssueService.getPendingBooksByStudentAndSchool(studentId, schoolId);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("count", pendingBooks.size());
        response.put("books", pendingBooks);
        return new ResponseEntity((Object)new ResponseMessage("Pending book information retrieved successfully", response), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/book-issues/pending-count/{studentId}"})
    public ResponseEntity<ResponseMessage> getPendingBookCountLegacy(@PathVariable String studentId) {
        List pendingBooks = this.bookIssueService.getPendingBooksByStudentAndSchool(studentId, "67ca9167d56ec91fc9539234");
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("count", pendingBooks.size());
        response.put("books", pendingBooks);
        return new ResponseEntity((Object)new ResponseMessage("Pending book information retrieved successfully", response), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/books/issued/count"})
    public ResponseEntity<ResponseMessage> getTotalIssuedBooksCount() {
        long count = this.bookIssueService.getTotalIssuedBooksCount();
        return new ResponseEntity((Object)new ResponseMessage("Total issued books count retrieved successfully", (Object)count), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/books/issued/pending/count"})
    public ResponseEntity<ResponseMessage> getTotalPendingIssuedBooksCount() {
        long count = this.bookIssueService.getTotalPendingIssuedBooksCount();
        return new ResponseEntity((Object)new ResponseMessage("Total pending issued books count retrieved successfully", (Object)count), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/books/issued/count/{schoolId}"})
    public ResponseEntity<ResponseMessage> getTotalIssuedBooksCountBySchool(@PathVariable String schoolId) {
        long count = this.bookIssueService.getTotalIssuedBooksCountBySchool(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Total issued books count retrieved successfully", (Object)count), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/books/issued/pending/count/{schoolId}"})
    public ResponseEntity<ResponseMessage> getTotalPendingIssuedBooksCountBySchool(@PathVariable String schoolId) {
        long count = this.bookIssueService.getTotalPendingIssuedBooksCountBySchool(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Total pending issued books count retrieved successfully", (Object)count), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/fetch-librarian-summary/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllCountBySchool(@PathVariable String schoolId) {
        HashMap<String, Long> counts = new HashMap<String, Long>();
        counts.put("totalBook", this.bookService.getTotalBookCountBySchool(schoolId));
        counts.put("totalPendingBook", this.bookIssueService.getTotalPendingIssuedBooksCountBySchool(schoolId));
        counts.put("totalIssueBook", this.bookIssueService.getTotalIssuedBooksCountBySchool(schoolId));
        counts.put("totalReturnBook", this.bookIssueService.getTotalReturnIssueBookCountBySchool(schoolId));
        counts.put("totalLibraryQuantity", this.bookService.getTotalBookQuantityBySchool(schoolId));
        counts.put("totalCategories", this.categoryService.totalCategoryBySchool(schoolId));
        return new ResponseEntity((Object)new ResponseMessage("fetch-librarian-summary by school ", counts), (HttpStatusCode)HttpStatus.OK);
    }
}

