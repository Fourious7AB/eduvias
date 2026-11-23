/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.Book
 *  com.neel.NewLibrary.model.BookIssue
 *  com.neel.NewLibrary.repo.BookIssueRepository
 *  com.neel.NewLibrary.repo.BookRepository
 *  com.neel.NewLibrary.service.BookIssueService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.web.client.RestTemplate
 */
package com.neel.NewLibrary.service;

import com.neel.NewLibrary.model.Book;
import com.neel.NewLibrary.model.BookIssue;
import com.neel.NewLibrary.repo.BookIssueRepository;
import com.neel.NewLibrary.repo.BookRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookIssueService {
    @Autowired
    private BookIssueRepository bookIssueRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${admission.validate.url:http://localhost:8085/admissions/validate/}")
    private String admissionValidateUrl;

    public BookIssue issueBook(BookIssue bookIssue) {
        bookIssue.setSchoolId("67ca9167d56ec91fc9539234");
        return this.processBookIssue(bookIssue);
    }

    public BookIssue issueBookWithSchool(BookIssue bookIssue, String schoolId) {
        bookIssue.setSchoolId(schoolId);
        return this.processBookIssue(bookIssue);
    }

    public long totalIssueBookBySchool(String schoolId) {
        List issues = this.bookIssueRepository.findBySchoolId(schoolId);
        return issues.size();
    }

    private BookIssue processBookIssue(BookIssue bookIssue) {
        RestTemplate restTemplate = new RestTemplate();
        String admissionServiceUrl = this.admissionValidateUrl + bookIssue.getStudentId();
        ResponseEntity response = restTemplate.getForEntity(admissionServiceUrl, Boolean.class, new Object[0]);
        if (Boolean.FALSE.equals(response.getBody())) {
            throw new RuntimeException("Invalid studentId. Admission record not found.");
        }
        List<BookIssue> existingIssues = this.bookIssueRepository.findByBookNumberAndStudentIdAndSchoolId(bookIssue.getBookNumber(), bookIssue.getStudentId(), bookIssue.getSchoolId());
        boolean hasIssuedSameBook = existingIssues.stream().anyMatch(issue -> "Pending".equals(issue.getStatus()));
        if (hasIssuedSameBook) {
            throw new RuntimeException("Student has already issued this book. Cannot issue multiple copies of the same book.");
        }
        List<BookIssue> studentBookIssues = this.bookIssueRepository.findByStudentIdAndSchoolId(bookIssue.getStudentId(), bookIssue.getSchoolId());
        long pendingIssuesCount = studentBookIssues.stream().filter(issue -> "Pending".equals(issue.getStatus())).count();
        if (pendingIssuesCount >= 2L) {
            throw new RuntimeException("Student can only issue up to two books.");
        }
        Optional optionalBook = this.bookRepository.findByBookNumberAndSchoolId(bookIssue.getBookNumber(), bookIssue.getSchoolId());
        if (optionalBook.isEmpty()) {
            throw new RuntimeException("Book not found");
        }
        Book book = (Book)optionalBook.get();
        if (book.getBookQuantity() < bookIssue.getQuantity()) {
            throw new RuntimeException("Insufficient quantity available");
        }
        book.setBookQuantity(book.getBookQuantity() - bookIssue.getQuantity());
        book.updateStatus();
        this.bookRepository.save(book);
        bookIssue.setStatus("Pending");
        return (BookIssue)this.bookIssueRepository.save(bookIssue);
    }

    public List<BookIssue> getAllBookIssuesBySchool(String schoolId) {
        return this.bookIssueRepository.findBySchoolId(schoolId);
    }

    public Optional<BookIssue> getBookIssueByIdAndSchool(String id, String schoolId) {
        return this.bookIssueRepository.findByIdAndSchoolId(id, schoolId);
    }

    public BookIssue updateBookIssueWithSchool(String id, BookIssue bookIssue, String schoolId) {
        Optional existingIssue = this.bookIssueRepository.findByIdAndSchoolId(id, schoolId);
        if (existingIssue.isPresent()) {
            bookIssue.setId(id);
            bookIssue.setSchoolId(schoolId);
            return (BookIssue)this.bookIssueRepository.save(bookIssue);
        }
        throw new RuntimeException("Book issue not found for this school");
    }

    public void deleteBookIssueWithSchool(String id, String schoolId) {
        Optional bookIssue = this.bookIssueRepository.findByIdAndSchoolId(id, schoolId);
        if (!bookIssue.isPresent()) {
            throw new RuntimeException("Book issue not found for this school");
        }
        this.bookIssueRepository.delete(((BookIssue)bookIssue.get()));
    }

    public List<BookIssue> getPendingBooksByStudentAndSchool(String studentId, String schoolId) {
        List<BookIssue> allBooks = this.bookIssueRepository.findByStudentIdAndSchoolId(studentId, schoolId);
        return allBooks.stream().filter(issue -> "Pending".equals(issue.getStatus())).collect(Collectors.toList());
    }

    public int getPendingBookCountByStudentAndSchool(String studentId, String schoolId) {
        return this.getPendingBooksByStudentAndSchool(studentId, schoolId).size();
    }

    public long getTotalIssuedBooksCount() {
        return this.bookIssueRepository.count();
    }

    public long getTotalIssuedBooksCountBySchool(String schoolId) {
        List issuedBooks = this.bookIssueRepository.findBySchoolId(schoolId);
        return issuedBooks.size();
    }

    public long getTotalPendingIssuedBooksCount() {
        List<BookIssue> allIssues = this.bookIssueRepository.findAll();
        return allIssues.stream().filter(issue -> "Pending".equals(issue.getStatus())).count();
    }

    public long getTotalPendingIssuedBooksCountBySchool(String schoolId) {
        List<BookIssue> schoolIssues = this.bookIssueRepository.findBySchoolId(schoolId);
        return schoolIssues.stream().filter(issue -> "Pending".equals(issue.getStatus())).count();
    }

    public long getTotalReturnIssueBookCountBySchool(String schoolId) {
        List<BookIssue> schoolIssues = this.bookIssueRepository.findBySchoolId(schoolId);
        return schoolIssues.stream().filter(returns -> "Returned".equals(returns.getStatus())).count();
    }
}

