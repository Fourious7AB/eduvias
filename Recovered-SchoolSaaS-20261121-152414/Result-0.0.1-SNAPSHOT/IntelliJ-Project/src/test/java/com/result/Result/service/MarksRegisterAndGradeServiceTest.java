package com.result.Result.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.result.Result.model.GradeSystem;
import com.result.Result.model.MarksRegister;
import com.result.Result.model.ResponseMessage;
import com.result.Result.repo.GradeSystemRepository;
import com.result.Result.repo.MarksRegisterRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MarksRegisterAndGradeServiceTest {
    @Mock
    private MarksRegisterRepository marksRegisterRepository;
    @InjectMocks
    private MarksRegisterService marksRegisterService;

    @Mock
    private GradeSystemRepository gradeSystemRepository;
    @InjectMocks
    private GradeSystemService gradeSystemService;

    @Test
    @SuppressWarnings("unchecked")
    void searchMarksSubjectIsCaseInsensitiveTrimmedAndIncludesRegisterId() {
        MarksRegister.StudentMark m1 = new MarksRegister.StudentMark();
        m1.setRoll(5);
        m1.setName("Bina");
        m1.setMark(70);
        m1.setProjectMark(null);

        MarksRegister.StudentMark m2 = new MarksRegister.StudentMark();
        m2.setRoll(2);
        m2.setName("Anil");
        m2.setMark(80);
        m2.setProjectMark(15.0);

        MarksRegister reg = new MarksRegister();
        reg.setId("reg-123");
        reg.setSchoolId("s1");
        reg.setClassName("LKG");
        reg.setSection("A");
        reg.setSubject("BIO ");
        reg.setExamType("Mid Term");
        reg.setSubjectFullMark("80");
        reg.setProjectFullMark("20");
        reg.setFullMarks(100);
        reg.setMarks(Arrays.asList(m1, m2));

        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId("LKG", "A", "Mid Term", "s1"))
                .thenReturn(Collections.singletonList(reg));

        ResponseEntity<ResponseMessage> res = marksRegisterService.searchMarksSubjectWithSchool("LKG", "A", "bio", "Mid Term", "s1");
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<Map<String, Object>> rows = (List<Map<String, Object>>) res.getBody().getData();
        assertEquals(2, rows.size());
        // Sorted by roll ascending
        assertEquals(2, rows.get(0).get("roll"));
        assertEquals("reg-123", rows.get(0).get("registerId"));
        assertEquals(5, rows.get(1).get("roll"));
        assertEquals(0.0, rows.get(1).get("projectMark"));
    }

    @Test
    void updateMarksRegisterPreservesAndUpdatesSubjectAndProjectFullMarks() {
        MarksRegister existing = new MarksRegister();
        existing.setId("reg-1");
        existing.setSchoolId("s1");
        existing.setClassName("LKG");
        existing.setSection("A");
        existing.setSubject("Math");
        existing.setExamType("Unit 1");
        existing.setSubjectFullMark("60");
        existing.setProjectFullMark("40");
        existing.setFullMarks(100);

        MarksRegister.StudentMark updatedMark = new MarksRegister.StudentMark();
        updatedMark.setRoll(1);
        updatedMark.setName("Sam");
        updatedMark.setMark(75);
        updatedMark.setProjectMark(18.0);

        MarksRegister incoming = new MarksRegister();
        incoming.setSubjectFullMark("80");
        incoming.setProjectFullMark("20");
        incoming.setMarks(Collections.singletonList(updatedMark));

        when(marksRegisterRepository.findByIdAndSchoolId("reg-1", "s1")).thenReturn(Optional.of(existing));
        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId("LKG", "A", "Unit 1", "s1"))
                .thenReturn(Collections.singletonList(existing));
        when(marksRegisterRepository.save(any(MarksRegister.class))).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<ResponseMessage> res = marksRegisterService.updateMarksRegisterWithSchool("reg-1", incoming, "s1");
        assertEquals(HttpStatus.OK, res.getStatusCode());
        MarksRegister saved = (MarksRegister) res.getBody().getData();
        assertEquals("80", saved.getSubjectFullMark());
        assertEquals("20", saved.getProjectFullMark());
        assertEquals(100.0, saved.getFullMarks());
    }

    @Test
    void gradeSystemRejectsOverlappingAndInvalidRanges() {
        GradeSystem existing = new GradeSystem();
        existing.setId("g1");
        existing.setSchoolId("s1");
        existing.setScaleStartMarks(80);
        existing.setScaleEndMarks(89);
        existing.setLetterGrade("A+");
        existing.setPerformanceIndicator("Excellent");

        when(gradeSystemRepository.findBySchoolId("s1")).thenReturn(Collections.singletonList(existing));

        GradeSystem inverted = new GradeSystem();
        inverted.setScaleStartMarks(90);
        inverted.setScaleEndMarks(70);
        inverted.setLetterGrade("AA");
        inverted.setPerformanceIndicator("Outstanding");
        assertEquals(HttpStatus.BAD_REQUEST, gradeSystemService.createGradeWithSchool(inverted, "s1").getStatusCode());

        GradeSystem overlapping = new GradeSystem();
        overlapping.setScaleStartMarks(75);
        overlapping.setScaleEndMarks(85);
        overlapping.setLetterGrade("A");
        overlapping.setPerformanceIndicator("Very Good");
        assertEquals(HttpStatus.BAD_REQUEST, gradeSystemService.createGradeWithSchool(overlapping, "s1").getStatusCode());
    }
}
