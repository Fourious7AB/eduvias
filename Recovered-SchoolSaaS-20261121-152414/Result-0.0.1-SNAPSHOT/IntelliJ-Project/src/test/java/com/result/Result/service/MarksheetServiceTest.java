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
import java.time.Year;
import java.util.ArrayList;
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

    private List<GradeSystem> standardSevenSchoolGrades() {
        return Arrays.asList(
                grade(90, 100, "AA", "Outstanding"),
                grade(80, 89, "A+", "Excellent"),
                grade(60, 79, "A", "Very Good"),
                grade(45, 59, "B+", "Good"),
                grade(35, 44, "B", "Satisfactory"),
                grade(25, 34, "C", "Marginal"),
                grade(0, 24, "D", "Disqualified")
        );
    }

    @Test
    void annualRejectsBlankInputsWithBadRequest() {
        assertEquals(HttpStatus.BAD_REQUEST, marksheetService.generateAnnualReportCard(" ", "A", "school1").getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, marksheetService.generateAnnualReportCard("LKG", "", "school1").getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, marksheetService.generateAnnualReportCard("LKG", "A", null).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, marksheetService.generateStudentAnnualReportCard("LKG", "A", 1, " ").getStatusCode());
    }

    @Test
    @SuppressWarnings("unchecked")
    void annualExcludesEmptyAndInvalidRegistersAndReturns404WhenNoneRemain() {
        MarksRegister emptyMarks = register("English", "1st Terminal Exam", "80", "20", 100.0);
        MarksRegister zeroFull = register("Math", "2nd Terminal Exam", "0", "0", 0.0, studentMark(1, "Aarav", 10.0, 0.0));
        MarksRegister negativeFull = register("Science", "3rd Terminal Exam", null, null, -50.0, studentMark(1, "Aarav", 10.0, null));
        MarksRegister blankExam = register("Hindi", "   ", "80", "20", 100.0, studentMark(1, "Aarav", 50.0, 10.0));

        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId("LKG", "A", "school1"))
                .thenReturn(Arrays.asList(emptyMarks, zeroFull, negativeFull, blankExam));

        ResponseEntity<ResponseMessage> notFoundRes = marksheetService.generateAnnualReportCard("LKG", "A", "school1");
        assertEquals(HttpStatus.NOT_FOUND, notFoundRes.getStatusCode());

        // Now add 1 valid exam alongside the empty/zero registers; only the populated exam must be included
        MarksRegister validReg = register("English", "1st Terminal Exam - 2026", "80", "20", 100.0, studentMark(1, "Aarav", 75.0, 15.0));
        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId("LKG", "A", "school1"))
                .thenReturn(Arrays.asList(emptyMarks, zeroFull, negativeFull, validReg));
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(standardSevenSchoolGrades());
        when(criteriaService.getCriteriaForClassAndExam("school1", "LKG", "1st Terminal Exam - 2026")).thenReturn(Optional.empty());

        ResponseEntity<ResponseMessage> okRes = marksheetService.generateAnnualReportCard("LKG", "A", "school1");
        assertEquals(HttpStatus.OK, okRes.getStatusCode());
        Map<String, Object> data = (Map<String, Object>) okRes.getBody().getData();
        List<Map<String, Object>> activeExams = (List<Map<String, Object>>) data.get("activeExams");
        assertEquals(1, activeExams.size());
        assertEquals("1st Terminal Exam - 2026", activeExams.get(0).get("examType"));
        assertEquals(100.0, data.get("totalAnnualFullMarks"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void annualScalesDynamicallyAcross1To2To3To4Exams() {
        List<GradeSystem> grades = standardSevenSchoolGrades();
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(grades);
        Criteria criteria = new Criteria();
        criteria.setPassingMark(25.0);
        when(criteriaService.getCriteriaForClassAndExam(eq("school1"), eq("LKG"), anyString())).thenReturn(Optional.of(criteria));

        String[] examNames = {
                "1st Terminal Exam- 2026",
                "2nd Terminal Exam- 2026",
                "3rd Terminal Exam- 2026",
                "4th Terminal Exam- 2026"
        };
        String[] subjects = {"English", "Math", "Science", "Bengali"};

        List<MarksRegister> progressive = new ArrayList<>();
        for (int examCount = 1; examCount <= 4; examCount++) {
            String examName = examNames[examCount - 1];
            for (String subj : subjects) {
                // Roll 1 scores 90/100 in every exam/subject -> 360, 720, 1080, 1440 (exact 90% AA threshold)
                // Roll 2 scores 20/100 in every exam/subject -> 80, 160, 240, 320 (D / Fail)
                progressive.add(register(subj, examName, "80", "20", 100.0,
                        studentMark(1, "Aarav", 72.0, 18.0),
                        studentMark(2, "Riya", 15.0, 5.0)));
            }
            when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId("LKG", "A", "school1"))
                    .thenReturn(new ArrayList<>(progressive));

            ResponseEntity<ResponseMessage> res = marksheetService.generateAnnualReportCard("LKG", "A", "school1");
            assertEquals(HttpStatus.OK, res.getStatusCode());

            Map<String, Object> data = (Map<String, Object>) res.getBody().getData();
            List<Map<String, Object>> activeExams = (List<Map<String, Object>>) data.get("activeExams");
            assertEquals(examCount, activeExams.size());

            double expectedFull = examCount * 400.0;
            assertEquals(expectedFull, data.get("totalAnnualFullMarks"));

            List<Map<String, Object>> dynamicScale = (List<Map<String, Object>>) data.get("dynamicGradeScale");
            assertEquals(7, dynamicScale.size());
            assertEquals((long) (examCount * 360), dynamicScale.get(0).get("minMarks"));
            assertEquals((long) (examCount * 400), dynamicScale.get(0).get("maxMarks"));
            assertEquals(0L, dynamicScale.get(6).get("minMarks"));
            assertEquals((long) (examCount * 100 - 1), dynamicScale.get(6).get("maxMarks"));

            List<Map<String, Object>> students = (List<Map<String, Object>>) data.get("students");
            assertEquals(2, students.size());

            Map<String, Object> aarav = students.get(0);
            assertEquals(examCount * 360.0, aarav.get("grandTotalObtained"));
            assertEquals(expectedFull, aarav.get("grandTotalFullMarks"));
            assertEquals(90.0, aarav.get("overallPercentage"));
            assertEquals("AA", aarav.get("overallGrade"));
            assertEquals("P", aarav.get("result"));
            assertEquals(examCount, ((List<?>) aarav.get("examSummaries")).size());

            Map<String, Object> riya = students.get(1);
            assertEquals(examCount * 80.0, riya.get("grandTotalObtained"));
            assertEquals("D", riya.get("overallGrade"));
            assertEquals("F", riya.get("result"));

            Map<String, Object> stats = (Map<String, Object>) data.get("statistics");
            assertEquals(2, stats.get("totalStudents"));
            assertEquals(1, stats.get("passedCount"));
            assertEquals(1, stats.get("failedCount"));
            assertEquals(50.0, stats.get("passRate"));
            assertEquals(examCount * 360.0, stats.get("highestObtained"));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void annualHandlesAbsentStudentsNonConductedSubjectsAndCachesCriteriaOncePerExam() {
        List<GradeSystem> grades = standardSevenSchoolGrades();
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(grades);
        Criteria c1 = new Criteria();
        c1.setPassingMark(30.0);
        when(criteriaService.getCriteriaForClassAndExam(eq("school1"), eq("LKG"), anyString())).thenReturn(Optional.of(c1));

        // 4 exams: English conducted in exams 1, 2, 3 (NOT in exam 4); Roll 1 absent in exam 3 English
        // Also duplicate "Math " whitespace register in exam 1 must be deduplicated
        MarksRegister e1Eng = register("English", "1st Terminal", "80", "20", 100.0,
                studentMark(1, "Aarav", 72.0, 18.0), studentMark(2, "Riya", 60.0, 15.0));
        MarksRegister e1Math = register("Math", "1st Terminal", "80", "20", 100.0,
                studentMark(1, "Aarav", 70.0, 10.0), studentMark(2, "Riya", 50.0, 10.0));
        MarksRegister e1MathDup = register("Math ", "1st Terminal", "80", "20", 100.0,
                studentMark(1, "Aarav", 70.0, 10.0));
        MarksRegister e2Eng = register("English", "2nd Terminal", "80", "20", 100.0,
                studentMark(1, "Aarav", 74.0, 19.0), studentMark(2, "Riya", 65.0, 15.0));
        MarksRegister e3Eng = register("English", "3rd Terminal", "80", "20", 100.0,
                studentMark(2, "Riya", 70.0, 10.0)); // Roll 1 absent!
        MarksRegister e4Math = register("Math", "4th Terminal", "80", "20", 100.0,
                studentMark(1, "Aarav", 80.0, 20.0), studentMark(2, "Riya", 60.0, 15.0));

        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId("LKG", "A", "school1"))
                .thenReturn(Arrays.asList(e4Math, e2Eng, e1Eng, e1Math, e1MathDup, e3Eng));

        ResponseEntity<ResponseMessage> res = marksheetService.generateAnnualReportCard("LKG", "A", "school1");
        assertEquals(HttpStatus.OK, res.getStatusCode());

        // Criteria lookup called ONCE per unique active exam (4 exams total), NOT 4 exams x 2 students
        verify(criteriaService, times(1)).getCriteriaForClassAndExam("school1", "LKG", "1st Terminal");
        verify(criteriaService, times(1)).getCriteriaForClassAndExam("school1", "LKG", "2nd Terminal");
        verify(criteriaService, times(1)).getCriteriaForClassAndExam("school1", "LKG", "3rd Terminal");
        verify(criteriaService, times(1)).getCriteriaForClassAndExam("school1", "LKG", "4th Terminal");

        Map<String, Object> data = (Map<String, Object>) res.getBody().getData();
        // Total annual full = (100+100) + 100 + 100 + 100 = 500 (duplicate "Math " NOT counted)
        assertEquals(500.0, data.get("totalAnnualFullMarks"));

        List<Map<String, Object>> students = (List<Map<String, Object>>) data.get("students");
        Map<String, Object> aarav = students.get(0);
        // Aarav obtained: e1(90+80=170) + e2(93) + e3(0 absent) + e4(100) = 363 / 500
        assertEquals(363.0, aarav.get("grandTotalObtained"));
        assertEquals(500.0, aarav.get("grandTotalFullMarks"));

        List<Map<String, Object>> examSummaries = (List<Map<String, Object>>) aarav.get("examSummaries");
        assertEquals(false, examSummaries.get(2).get("present")); // absent in 3rd Terminal
        assertEquals(0.0, examSummaries.get(2).get("totalObtained"));
        assertEquals(100.0, examSummaries.get(2).get("totalFullMarks"));

        List<Map<String, Object>> subjects = (List<Map<String, Object>>) aarav.get("subjects");
        Map<String, Object> englishRow = subjects.stream()
                .filter(s -> "English".equals(s.get("subject")))
                .findFirst()
                .orElseThrow();
        List<Map<String, Object>> engExamMarks = (List<Map<String, Object>>) englishRow.get("examMarks");
        assertEquals(4, engExamMarks.size());
        assertEquals(true, engExamMarks.get(0).get("conducted"));
        assertEquals(true, engExamMarks.get(0).get("present"));
        assertEquals(true, engExamMarks.get(2).get("conducted"));
        assertEquals(false, engExamMarks.get(2).get("present")); // absent
        assertEquals(100.0, engExamMarks.get(2).get("fullMarks"));
        assertEquals(false, engExamMarks.get(3).get("conducted")); // not conducted in 4th Terminal
        assertEquals(0.0, engExamMarks.get(3).get("fullMarks"));
        assertEquals("-", engExamMarks.get(3).get("grade"));
        assertEquals(183.0, englishRow.get("annualObtained"));
        assertEquals(300.0, englishRow.get("annualFullMarks"));
    }

    @Test
    void annualOrdersExamsChronologicallyAcross1stTo5thMidPreFinalAndFinal() {
        List<String> shuffled = new ArrayList<>(Arrays.asList(
                "Final Examination - 2026",
                "4th Terminal Exam- 2026",
                "1st Terminal Exam- 2026",
                "Pre-Final Exam",
                "3rd Terminal Exam- 2026",
                "Mid Term",
                "2nd Terminal Exam- 2026",
                "5th Terminal Exam- 2026"
        ));
        shuffled.sort(marksheetService::compareExamsChronologically);

        assertEquals(Arrays.asList(
                "1st Terminal Exam- 2026",
                "2nd Terminal Exam- 2026",
                "Mid Term",
                "3rd Terminal Exam- 2026",
                "4th Terminal Exam- 2026",
                "5th Terminal Exam- 2026",
                "Pre-Final Exam",
                "Final Examination - 2026"
        ), shuffled);

        // Half-Yearly must rank with Mid-Term (25), NOT with Yearly/Final (1000)
        assertTrue(marksheetService.compareExamsChronologically("Half-Yearly Exam", "3rd Terminal") < 0);
        assertTrue(marksheetService.compareExamsChronologically("Half-Yearly Exam", "Annual Exam") < 0);
        // Unit Test 2 must precede Unit Test 10
        assertTrue(marksheetService.compareExamsChronologically("Unit Test 2", "Unit Test 10") < 0);
    }

    @Test
    void dynamicSevenGradeScaleHandlesCeilCutoffsFractionsUnsortedFallbacksAndTinyTotals() {
        List<GradeSystem> grades = standardSevenSchoolGrades();

        // 435 marks -> 35% ceil(152.25) = 153 .. 195
        List<Map<String, Object>> scale435 = marksheetService.buildDynamicSevenGradeScale(grades, 435.0);
        assertEquals(7, scale435.size());
        assertEquals(153L, scale435.get(4).get("minMarks"));
        assertEquals(195L, scale435.get(4).get("maxMarks"));

        // 625 marks -> 25% ceil(156.25) = 157 (prevents passing 156/625 = 24.96%)
        List<Map<String, Object>> scale625 = marksheetService.buildDynamicSevenGradeScale(grades, 625.0);
        assertEquals(157L, scale625.get(5).get("minMarks"));
        assertEquals(156L, scale625.get(6).get("maxMarks"));
        assertEquals("D", marksheetService.resolveDynamicGradeTier(156.9, 625.0, scale625).get("letterGrade"));
        assertEquals("C", marksheetService.resolveDynamicGradeTier(157.0, 625.0, scale625).get("letterGrade"));

        // 1835 marks (400 + 435 + 475 + 525) exact cutoffs from plan table
        List<Map<String, Object>> scale1835 = marksheetService.buildDynamicSevenGradeScale(grades, 1835.0);
        assertEquals(1652L, scale1835.get(0).get("minMarks"));
        assertEquals(1835L, scale1835.get(0).get("maxMarks"));
        assertEquals(1468L, scale1835.get(1).get("minMarks"));
        assertEquals(1651L, scale1835.get(1).get("maxMarks"));
        assertEquals(1101L, scale1835.get(2).get("minMarks"));
        assertEquals(1467L, scale1835.get(2).get("maxMarks"));
        assertEquals(826L, scale1835.get(3).get("minMarks"));
        assertEquals(1100L, scale1835.get(3).get("maxMarks"));
        assertEquals(643L, scale1835.get(4).get("minMarks"));
        assertEquals(825L, scale1835.get(4).get("maxMarks"));
        assertEquals(459L, scale1835.get(5).get("minMarks"));
        assertEquals(642L, scale1835.get(5).get("maxMarks"));
        assertEquals(0L, scale1835.get(6).get("minMarks"));
        assertEquals(458L, scale1835.get(6).get("maxMarks"));

        // Fractional score 359.5 / 400 floors to 359 -> A+ (no gap between 359 and 360)
        List<Map<String, Object>> scale400 = marksheetService.buildDynamicSevenGradeScale(grades, 400.0);
        assertEquals("A+", marksheetService.resolveDynamicGradeTier(359.5, 400.0, scale400).get("letterGrade"));
        assertEquals("AA", marksheetService.resolveDynamicGradeTier(360.0, 400.0, scale400).get("letterGrade"));

        // Unsorted + overlapping 7 school grades -> sorted, gap-free, preserving custom labels
        List<GradeSystem> unsortedSeven = Arrays.asList(
                grade(0, 30, "E", "Fail"),
                grade(90, 100, "O", "Top"),
                grade(45, 65, "B+", "Good"),
                grade(80, 95, "A+", "Great"),
                grade(35, 50, "B", "Ok"),
                grade(60, 82, "A", "Very Good"),
                grade(25, 38, "C", "Pass")
        );
        List<Map<String, Object>> customScale = marksheetService.buildDynamicSevenGradeScale(unsortedSeven, 400.0);
        assertEquals(7, customScale.size());
        assertEquals("O", customScale.get(0).get("letterGrade"));
        assertEquals("E", customScale.get(6).get("letterGrade"));
        for (int i = 1; i < 7; i++) {
            long prevMin = ((Number) customScale.get(i - 1).get("minMarks")).longValue();
            long currMax = ((Number) customScale.get(i).get("maxMarks")).longValue();
            assertEquals(prevMin - 1, currMax);
        }

        // Non-7 grades or duplicate starts -> canonical 7-tier fallback
        List<GradeSystem> threeGrades = Arrays.asList(
                grade(80, 100, "A", "Good"),
                grade(40, 79, "B", "Average"),
                grade(0, 39, "F", "Fail")
        );
        List<Map<String, Object>> fallbackScale = marksheetService.buildDynamicSevenGradeScale(threeGrades, 800.0);
        assertEquals(7, fallbackScale.size());
        assertEquals("AA", fallbackScale.get(0).get("letterGrade"));
        assertEquals("D", fallbackScale.get(6).get("letterGrade"));

        List<GradeSystem> duplicateStarts = Arrays.asList(
                grade(90, 100, "AA", "Outstanding"),
                grade(90, 95, "A+", "Duplicate"),
                grade(60, 79, "A", "Very Good"),
                grade(45, 59, "B+", "Good"),
                grade(35, 44, "B", "Satisfactory"),
                grade(25, 34, "C", "Marginal"),
                grade(0, 24, "D", "Disqualified")
        );
        List<Map<String, Object>> dupFallback = marksheetService.buildDynamicSevenGradeScale(duplicateStarts, 800.0);
        assertEquals("A+", dupFallback.get(1).get("letterGrade"));
        assertEquals(640L, dupFallback.get(1).get("minMarks"));

        // Tiny totalMax < 7 (e.g., totalMax = 4): 7 non-negative tiers, 0 -> D, 4 -> AA
        List<Map<String, Object>> tinyScale = marksheetService.buildDynamicSevenGradeScale(grades, 4.0);
        assertEquals(7, tinyScale.size());
        for (Map<String, Object> tier : tinyScale) {
            long min = ((Number) tier.get("minMarks")).longValue();
            long max = ((Number) tier.get("maxMarks")).longValue();
            assertTrue(min >= 0 && max >= min && max <= 4);
        }
        assertEquals("D", marksheetService.resolveDynamicGradeTier(0.0, 4.0, tinyScale).get("letterGrade"));
        assertEquals("AA", marksheetService.resolveDynamicGradeTier(4.0, 4.0, tinyScale).get("letterGrade"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void annualAcademicYearRangeAndSingleStudentEndpointWorkCorrectly() {
        int currentYear = Year.now().getValue();
        String expectedYearRange = (currentYear - 1) + " to " + currentYear;

        MarksRegister reg = register("English", "1st Terminal", "80", "20", 100.0,
                studentMark(1, "Aarav", 75.0, 15.0));
        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId("LKG", "A", "school1"))
                .thenReturn(Collections.singletonList(reg));
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(standardSevenSchoolGrades());
        when(criteriaService.getCriteriaForClassAndExam("school1", "LKG", "1st Terminal")).thenReturn(Optional.empty());

        ResponseEntity<ResponseMessage> found = marksheetService.generateStudentAnnualReportCard("LKG", "A", 1, "school1");
        assertEquals(HttpStatus.OK, found.getStatusCode());
        Map<String, Object> data = (Map<String, Object>) found.getBody().getData();
        assertEquals(expectedYearRange, data.get("academicYearRange"));
        Map<String, Object> student = (Map<String, Object>) data.get("student");
        assertNotNull(student);
        assertEquals(1, student.get("rollNumber"));
        assertEquals("Aarav", student.get("studentName"));
        assertEquals("AA", student.get("overallGrade"));
        assertEquals("P", student.get("result"));
        List<Map<String, Object>> studentsList = (List<Map<String, Object>>) data.get("students");
        assertEquals(1, studentsList.size());

        ResponseEntity<ResponseMessage> missing = marksheetService.generateStudentAnnualReportCard("LKG", "A", 99, "school1");
        assertEquals(HttpStatus.NOT_FOUND, missing.getStatusCode());
    }

    @Test
    @SuppressWarnings("unchecked")
    void annualUsesValidatedSevenTierGradesForIncompleteSchoolScalesAndNeverAwardsAPlusForZero() {
        // Mirrors real live MongoDB school 67dbece98ed5da3f56394707 (3 incomplete grades missing 0-39)
        List<GradeSystem> incompleteGrades = Arrays.asList(
                grade(80, 90, "A+", "very good"),
                grade(70, 80, "A", "good"),
                grade(40, 50, "A+", "GOOD")
        );

        MarksRegister e1Math = register("Math", "1st Assessment Exams", "80", "20", 100.0,
                studentMark(1, "John Doe", 70.0, 0.0));
        MarksRegister e2Math = register("Math", "2nd Assessment Exams", "50", "0", 50.0,
                studentMark(13, "Roushan Kumar", 41.0, 0.0));

        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId("CLASS-1", "A", "school1"))
                .thenReturn(Arrays.asList(e1Math, e2Math));
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(incompleteGrades);
        when(criteriaService.getCriteriaForClassAndExam(eq("school1"), eq("CLASS-1"), anyString())).thenReturn(Optional.empty());

        ResponseEntity<ResponseMessage> res = marksheetService.generateAnnualReportCard("CLASS-1", "A", "school1");
        assertEquals(HttpStatus.OK, res.getStatusCode());

        Map<String, Object> data = (Map<String, Object>) res.getBody().getData();
        List<Map<String, Object>> students = (List<Map<String, Object>>) data.get("students");
        Map<String, Object> roushan = students.stream()
                .filter(s -> Integer.valueOf(13).equals(s.get("rollNumber")))
                .findFirst()
                .orElseThrow();

        // Roushan was absent (0/100 = 0%) in Exam 1 -> must get failing grade "D", NEVER "A+"
        List<Map<String, Object>> roushanExams = (List<Map<String, Object>>) roushan.get("examSummaries");
        assertEquals("D", roushanExams.get(0).get("grade"));
        assertEquals(false, roushanExams.get(0).get("present"));
        assertEquals(false, roushanExams.get(0).get("passed"));
        // Exam 2: 41/50 = 82% -> A+
        assertEquals("A+", roushanExams.get(1).get("grade"));
        assertEquals(true, roushanExams.get(1).get("passed"));

        // Subject cell for Exam 1 absent must also be "D", not "A+"
        List<Map<String, Object>> roushanSubjects = (List<Map<String, Object>>) roushan.get("subjects");
        List<Map<String, Object>> mathCells = (List<Map<String, Object>>) roushanSubjects.get(0).get("examMarks");
        assertEquals("D", mathCells.get(0).get("grade"));
        // Annual subject: 41/150 = 27.33% -> C
        assertEquals("C", roushanSubjects.get(0).get("annualGrade"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void annualMergesDistinctRollsAcrossSplitRegistersWithoutDoubleCountingFullMarks() {
        MarksRegister splitBatch1 = register("Math", "1st Terminal", "80", "20", 100.0,
                studentMark(1, "Aarav", 70.0, 20.0));
        MarksRegister splitBatch2 = register("Math ", "1st Terminal", "80", "20", 100.0,
                studentMark(1, "Aarav", 10.0, 0.0), // duplicate roll -> ignored (first wins)
                studentMark(2, "Riya", 65.0, 15.0)); // distinct roll -> merged!

        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId("LKG", "A", "school1"))
                .thenReturn(Arrays.asList(splitBatch1, splitBatch2));
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(standardSevenSchoolGrades());
        when(criteriaService.getCriteriaForClassAndExam("school1", "LKG", "1st Terminal")).thenReturn(Optional.empty());

        ResponseEntity<ResponseMessage> res = marksheetService.generateAnnualReportCard("LKG", "A", "school1");
        assertEquals(HttpStatus.OK, res.getStatusCode());

        Map<String, Object> data = (Map<String, Object>) res.getBody().getData();
        // Full marks counted once (100), not doubled (200)
        assertEquals(100.0, data.get("totalAnnualFullMarks"));

        List<Map<String, Object>> students = (List<Map<String, Object>>) data.get("students");
        assertEquals(2, students.size());
        assertEquals(90.0, students.get(0).get("grandTotalObtained")); // Aarav from batch 1
        assertEquals(80.0, students.get(1).get("grandTotalObtained")); // Riya merged from batch 2
        assertEquals("A+", students.get(1).get("overallGrade"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void annualNeverMarksZeroScoreStudentAsPassedEvenWithCustomBottomIndicator() {
        List<GradeSystem> customSeven = Arrays.asList(
                grade(90, 100, "O", "Top"),
                grade(80, 89, "A+", "Great"),
                grade(60, 79, "A", "Good"),
                grade(45, 59, "B+", "Above Avg"),
                grade(35, 44, "B", "Average"),
                grade(25, 34, "C", "Below Avg"),
                grade(0, 24, "E", "Needs Improvement")
        );

        MarksRegister reg = register("English", "1st Terminal", "80", "20", 100.0,
                studentMark(1, "ZeroScore", 0.0, 0.0));
        when(marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId("LKG", "A", "school1"))
                .thenReturn(Collections.singletonList(reg));
        when(gradeSystemRepository.findBySchoolId("school1")).thenReturn(customSeven);
        when(criteriaService.getCriteriaForClassAndExam("school1", "LKG", "1st Terminal")).thenReturn(Optional.empty());

        ResponseEntity<ResponseMessage> res = marksheetService.generateAnnualReportCard("LKG", "A", "school1");
        assertEquals(HttpStatus.OK, res.getStatusCode());

        Map<String, Object> data = (Map<String, Object>) res.getBody().getData();
        Map<String, Object> student = ((List<Map<String, Object>>) data.get("students")).get(0);
        assertEquals("E", student.get("overallGrade"));
        assertEquals("F", student.get("result"));
    }
}
