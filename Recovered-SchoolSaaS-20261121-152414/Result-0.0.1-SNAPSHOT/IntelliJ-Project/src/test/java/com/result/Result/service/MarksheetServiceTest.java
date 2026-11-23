package com.result.Result.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.result.Result.model.Criteria;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MarksheetServiceTest {
    @Mock
    private MarksRegisterRepository marksRegisterRepository;
    @Mock
    private GradeSystemRepository gradeSystemRepository;
    @Mock
    private CriteriaService criteriaService;
    @Spy
    @InjectMocks
    private MarksheetService marksheetService;

    private GradeSystem grade(int start, int end, String letter, String indicator) {
        GradeSystem g = new GradeSystem();
        g.setSchoolId("school1");
        g.setScaleStartMarks(start);
        g.setScaleEndMarks(end);
        g.setLetterGrade(letter);
        g.setPerformanceIndicator(indicator);
        return g;
    }

    private MarksRegister register(String subject, String examType, String subjFull, String projFull, double full, MarksRegister.StudentMark... marks) {
        MarksRegister reg = new MarksRegister();
        reg.setId("reg-" + subject + "-" + examType);
        reg.setSchoolId("school1");
        reg.setClassName("LKG");
        reg.setSection("A");
        reg.setSubject(subject);
        reg.setExamType(examType);
        reg.setSubjectFullMark(subjFull);
        reg.setProjectFullMark(projFull);
        reg.setFullMarks(full);
        reg.setMarks(Arrays.asList(marks));
        return reg;
    }

    private MarksRegister.StudentMark studentMark(int roll, String name, double mark, Double projectMark) {
        MarksRegister.StudentMark sm = new MarksRegister.StudentMark();
        sm.setRoll(roll);
        sm.setName(name);
        sm.setMark(mark);
        sm.setProjectMark(projectMark);
        return sm;
    }

    @BeforeEach
    void stubStudentDetails() {
        lenient().doReturn(null).when(marksheetService).fetchStudentDetails(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @SuppressWarnings("unchecked")
    void gradesByPercentageForNon100FullMarksAndLegacyNullProjectMark() {
        List<GradeSystem> grades = Arrays.asList(
                grade(90, 100, "O+", "Outstanding"),
                grade(80, 89, "O", "Excellent"),
                grade(60, 79, "A", "Very Good"),
                grade(34, 59, "B", "Satisfactory"),
                grade(0, 33, "F", "Fail")
        );

        // Non-100 full mark (10 + 26 = 36), student gets 36/36 = 100%
        MarksRegister english = register("English", "Unit 1", "10", "26", 36.0, studentMark(1, "Aarav", 10.0, 26.0));
        // Legacy record: subjectFullMark & projectFullMark are null, projectMark is null, fullMarks = 100, mark = 89.5
        MarksRegister math = register("Math", "Unit 1", null, null, 100.0, studentMark(1, "Aarav", 89.5, null));

        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId("LKG", "A", "Unit 1", "school1"))
                .thenReturn(Arrays.asList(english, math));
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(grades);
        when(criteriaService.getCriteriaForClassAndExam("school1", "LKG", "Unit 1")).thenReturn(Optional.empty());

        ResponseEntity<ResponseMessage> res = marksheetService.generateStudentMarksheetWithSchool("LKG", "A", 1, "Unit 1", "school1");
        assertEquals(HttpStatus.OK, res.getStatusCode());

        Map<String, Object> data = (Map<String, Object>) res.getBody().getData();
        List<Map<String, Object>> sheets = (List<Map<String, Object>>) data.get("markSheets");
        Map<String, Object> sheet = sheets.get(0);

        List<Map<String, Object>> subjects = (List<Map<String, Object>>) sheet.get("subjects");
        // English: 36 / 36 = 100% -> O+
        assertEquals("O+", subjects.get(0).get("grade"));
        assertEquals(36.0, subjects.get(0).get("fullMarks"));
        assertEquals(100.0, subjects.get(0).get("percentage"));
        // Math: 89.5 / 100 -> floored 89 -> matches school grade "O" (80-89), NOT fallback!
        assertEquals("O", subjects.get(1).get("grade"));
        assertEquals(89.5, subjects.get(1).get("writtenMarks"));
        assertEquals(0.0, subjects.get(1).get("projectMarks"));
        assertEquals(100.0, subjects.get(1).get("fullMarks"));

        // Overall: 125.5 / 136 = 92.28% -> O+, PASS
        assertEquals(125.5, sheet.get("totalMarks"));
        assertEquals(136.0, sheet.get("totalFullMarks"));
        assertEquals("O+", sheet.get("overallGrade"));
        assertEquals("P", sheet.get("result"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void marksDisqualifiedGradeAndPassingMarkThresholdAsFail() {
        // Mirrors real DB school 69118d20c4c6be31096c1ab4 where fail grade is "D" / "Disqualified"
        List<GradeSystem> grades = Arrays.asList(
                grade(90, 100, "AA", "Outstanding"),
                grade(35, 89, "B", "Satisfactory"),
                grade(26, 34, "C", "Marginal"),
                grade(0, 25, "D", "Disqualified")
        );

        MarksRegister science = register("Science", "Mid Term", "80", "20", 100.0, studentMark(2, "Riya", 10.0, 5.0));
        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId("LKG", "A", "Mid Term", "school1"))
                .thenReturn(Collections.singletonList(science));
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(grades);
        Criteria criteria = new Criteria();
        criteria.setPassingMark(35.0);
        when(criteriaService.getCriteriaForClassAndExam("school1", "LKG", "Mid Term")).thenReturn(Optional.of(criteria));

        ResponseEntity<ResponseMessage> res = marksheetService.generateStudentMarksheetWithSchool("LKG", "A", 2, "Mid Term", "school1");
        assertEquals(HttpStatus.OK, res.getStatusCode());

        Map<String, Object> data = (Map<String, Object>) res.getBody().getData();
        Map<String, Object> sheet = ((List<Map<String, Object>>) data.get("markSheets")).get(0);
        assertEquals("D", sheet.get("overallGrade"));
        assertEquals("F", sheet.get("result"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void cumulativeDoesNotDoubleCountProjectFullMarkAndHandlesNoInternalExams() {
        List<GradeSystem> grades = Arrays.asList(
                grade(90, 100, "AA", "Outstanding"),
                grade(60, 89, "A", "Very Good"),
                grade(0, 59, "F", "Fail")
        );

        // fullMarks = 100 already equals 60 + 40! Must NOT become 140!
        MarksRegister annualMath = register("Math", "Annual Exam", "60", "40", 100.0, studentMark(1, "Karan", 60.0, 40.0));
        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId("LKG", "A", "school1"))
                .thenReturn(Collections.singletonList(annualMath));
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(grades);

        ResponseEntity<ResponseMessage> res = marksheetService.calculateCumulativeMarksheet("LKG", "A", "school1");
        assertEquals(HttpStatus.OK, res.getStatusCode());

        Map<String, Object> data = (Map<String, Object>) res.getBody().getData();
        Map<String, Object> student = ((List<Map<String, Object>>) data.get("students")).get(0);

        assertEquals(100.0, student.get("grandTotalObtained"));
        assertEquals(100.0, student.get("grandTotalFullMarks"));
        assertEquals(100.0, student.get("overallPercentage"));
        assertEquals("AA", student.get("overallGrade"));
        assertEquals("P", student.get("result"));
        // No internal exams -> must be "N/A", not "F"
        assertEquals("N/A", student.get("internalResult"));
        assertEquals("N/A", student.get("overallInternalGrade"));
    }
}
