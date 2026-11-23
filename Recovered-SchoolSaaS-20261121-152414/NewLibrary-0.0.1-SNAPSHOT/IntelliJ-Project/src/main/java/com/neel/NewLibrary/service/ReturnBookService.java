/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.Book
 *  com.neel.NewLibrary.model.BookIssue
 *  com.neel.NewLibrary.model.ReturnBook
 *  com.neel.NewLibrary.repo.BookIssueRepository
 *  com.neel.NewLibrary.repo.BookRepository
 *  com.neel.NewLibrary.repo.ReturnBookRepository
 *  com.neel.NewLibrary.service.ReturnBookService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.NewLibrary.service;

import com.neel.NewLibrary.model.Book;
import com.neel.NewLibrary.model.BookIssue;
import com.neel.NewLibrary.model.ReturnBook;
import com.neel.NewLibrary.repo.BookIssueRepository;
import com.neel.NewLibrary.repo.BookRepository;
import com.neel.NewLibrary.repo.ReturnBookRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnBookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookIssueRepository bookIssueRepository;
    @Autowired
    private ReturnBookRepository returnBookRepository;

    public ReturnBook returnBook(ReturnBook returnBook) {
        returnBook.setSchoolId("67ca9167d56ec91fc9539234");
        return this.processReturnBook(returnBook);
    }

    public ReturnBook returnBookWithSchool(ReturnBook returnBook, String schoolId) {
        returnBook.setSchoolId(schoolId);
        return this.processReturnBook(returnBook);
    }

    private ReturnBook processReturnBook(ReturnBook returnBook) {
        List<BookIssue> bookIssues = this.bookIssueRepository.findByBookNumberAndStudentIdAndSchoolId(returnBook.getBookNumber(), returnBook.getStudentId(), returnBook.getSchoolId());
        if (bookIssues.isEmpty()) {
            throw new RuntimeException("No matching book issue found for the given bookNumber and studentId");
        }
        BookIssue pendingIssue = (BookIssue)bookIssues.stream().filter(issue -> "Pending".equals(issue.getStatus())).findFirst().orElseThrow(() -> new RuntimeException("No pending book issue found for return"));
        Optional optionalBook = this.bookRepository.findByBookNumberAndSchoolId(returnBook.getBookNumber(), returnBook.getSchoolId());
        if (optionalBook.isEmpty()) {
            throw new RuntimeException("Book not found in the system");
        }
        Book book = (Book)optionalBook.get();
        book.setBookQuantity(book.getBookQuantity() + pendingIssue.getQuantity());
        book.updateStatus();
        this.bookRepository.save(book);
        pendingIssue.setStatus("Returned");
        this.bookIssueRepository.save(pendingIssue);
        returnBook.setMessage("Book returned successfully");
        return (ReturnBook)this.returnBookRepository.save(returnBook);
    }

    public List<ReturnBook> getAllReturnBooksBySchool(String schoolId) {
        return this.returnBookRepository.findBySchoolId(schoolId);
    }

    public Optional<ReturnBook> getReturnBookByIdAndSchool(String id, String schoolId) {
        return this.returnBookRepository.findByIdAndSchoolId(id, schoolId);
    }

    public List<ReturnBook> getReturnBooksByStudentAndSchool(String studentId, String schoolId) {
        return this.returnBookRepository.findByStudentIdAndSchoolId(studentId, schoolId);
    }
}

