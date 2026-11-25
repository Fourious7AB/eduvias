package com.result.Result.service;

import com.result.Result.Dto.StudentDetailsResponse;
import com.result.Result.model.Criteria;
import com.result.Result.model.GradeSystem;
import com.result.Result.model.MarksRegister;
import com.result.Result.model.ResponseMessage;
import com.result.Result.repo.GradeSystemRepository;
import com.result.Result.repo.MarksRegisterRepository;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MarksheetService {
    private static final int ANNUAL_GRADE_TIER_COUNT = 7;

    private record GradeTierTemplate(int percentageStart, String letterGrade, String performanceIndicator) {}

    private static final List<GradeTierTemplate> CANONICAL_SEVEN_GRADES = List.of(
            new GradeTierTemplate(90, "AA", "Outstanding"),
            new GradeTierTemplate(80, "A+", "Excellent"),
            new GradeTierTemplate(60, "A", "Very Good"),
            new GradeTierTemplate(45, "B+", "Good"),
            new GradeTierTemplate(35, "B", "Satisfactory"),
            new GradeTierTemplate(25, "C", "Marginal"),
            new GradeTierTemplate(0, "D", "Disqualified")
    );

    private static final Pattern ORDINAL_NUMBER_PATTERN = Pattern.compile("\\b(\\d{1,2})(?:st|nd|rd|th)\\b");
    private static final Pattern KEYWORD_NUMBER_PATTERN = Pattern.compile(
            "\\b(?:term(?:inal)?|unit|internal|fa|pa|pt|ut|ct|test|exam(?:ination)?|assessment|periodic|quarterly|cycle|phase|eval(?:uation)?|sem(?:ester)?)\\s*[-#:]?\\s*(\\d{1,2})(?!\\d)");
    private static final Pattern SA1_PATTERN = Pattern.compile("\\bsa\\s*[-]?\\s*1(?!\\d)");
    private static final Pattern SA2_PATTERN = Pattern.compile("\\bsa\\s*[-]?\\s*2(?!\\d)");
    private static final Pattern MID_WORD_PATTERN = Pattern.compile("\\bmid\\b");
    private static final Pattern YEARLY_WORD_PATTERN = Pattern.compile("\\byearly\\b");
    private static final Pattern ALPHANUM_CHUNK_PATTERN = Pattern.compile("(\\d+|\\D+)");

    private static final List<Map.Entry<Pattern, Integer>> ORDINAL_WORD_PATTERNS = List.of(
            Map.entry(Pattern.compile("\\bfirst\\b"), 1),
            Map.entry(Pattern.compile("\\bsecond\\b"), 2),
            Map.entry(Pattern.compile("\\bthird\\b"), 3),
            Map.entry(Pattern.compile("\\bfourth\\b"), 4),
            Map.entry(Pattern.compile("\\bfifth\\b"), 5),
            Map.entry(Pattern.compile("\\bsixth\\b"), 6),
            Map.entry(Pattern.compile("\\bseventh\\b"), 7),
            Map.entry(Pattern.compile("\\beighth\\b"), 8),
            Map.entry(Pattern.compile("\\bninth\\b"), 9),
            Map.entry(Pattern.compile("\\btenth\\b"), 10)
    );

    @Autowired
    private MarksRegisterRepository marksRegisterRepository;
    @Autowired
    private GradeSystemRepository gradeSystemRepository;
    @Autowired(required = false)
    private CriteriaService criteriaService;
    @Value(value="${fetch.student.details:http://localhost:8085/admissions/student-details}")
    private String fetchStudentDetailsUrl;
    private final RestTemplate restTemplate = createBoundedRestTemplate();
    @Autowired(required = false)
    private GradeSystemService gradeSystemService;

    private static RestTemplate createBoundedRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        return new RestTemplate(factory);
    }

    public ResponseEntity<ResponseMessage> generateStudentMarksheet(String className, String section, int roll, String examType) {
        String schoolId = "67ca9167d56ec91fc9539234";
        return this.generateMarksheet(className, section, roll, examType, schoolId);
    }

    public ResponseEntity<ResponseMessage> generateStudentMarksheetWithSchool(String className, String section, int roll, String examType, String schoolId) {
        return this.generateMarksheet(className, section, roll, examType, schoolId);
    }

    public ResponseEntity<ResponseMessage> generateClassSectionMarksheets(String className, String section, String examType) {
        String schoolId = "67ca9167d56ec91fc9539234";
        return this.generateClassSectionMarksheets(className, section, examType, schoolId);
    }

    public ResponseEntity<ResponseMessage> generateClassSectionMarksheetsWithSchool(String className, String section, String examType, String schoolId) {
        return this.generateClassSectionMarksheets(className, section, examType, schoolId);
    }

    private ResponseEntity<ResponseMessage> generateClassSectionMarksheets(String className, String section, String examType, String schoolId) {
        List<MarksRegister> registers = this.marksRegisterRepository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(
                        className.trim(), section.trim(), examType.trim(), schoolId.trim());
        if (registers == null || registers.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No records found for the given criteria.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        List<GradeSystem> grades = this.gradeSystemRepository.findBySchoolId(schoolId.trim());
        if (grades == null || grades.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No grade system defined for this school.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        Double passingMark = this.lookupPassingMark(schoolId, className, examType);

        HashSet<Integer> rollNumbers = new HashSet<>();
        for (MarksRegister register : registers) {
            if (register.getMarks() == null) continue;
            for (MarksRegister.StudentMark mark : register.getMarks()) {
                rollNumbers.add(mark.getRoll());
            }
        }
        List<Map<String, Object>> allMarksheets = new ArrayList<>();
        for (Integer roll : rollNumbers) {
            Map<String, Object> marksheetData = this.generateMarksheetData(
                    registers, grades, passingMark, className.trim(), section.trim(), roll, examType.trim(), schoolId.trim());
            if (marksheetData != null) {
                allMarksheets.add(marksheetData);
            }
        }
        if (allMarksheets.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No student data found for the given criteria.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        allMarksheets.sort(Comparator.comparing(m -> (Integer) m.get("roll")));
        Map<String, Object> response = new HashMap<>();
        response.put("className", className.trim());
        response.put("section", section.trim());
        response.put("examType", examType.trim());
        response.put("passingMark", passingMark);
        response.put("markSheets", allMarksheets);
        response.put("gradeScale", grades);
        return new ResponseEntity<>(new ResponseMessage("Mark-Sheets generated successfully for all students.", response), (HttpStatusCode) HttpStatus.OK);
    }

    private ResponseEntity<ResponseMessage> generateMarksheet(String className, String section, int roll, String examType, String schoolId) {
        List<MarksRegister> registers = this.marksRegisterRepository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(
                        className.trim(), section.trim(), examType.trim(), schoolId.trim());
        if (registers == null || registers.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No records found for the given criteria.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        List<GradeSystem> grades = this.gradeSystemRepository.findBySchoolId(schoolId.trim());
        if (grades == null || grades.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No grade system defined for this school.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        Double passingMark = this.lookupPassingMark(schoolId, className, examType);

        Map<String, Object> marksheetData = this.generateMarksheetData(
                registers, grades, passingMark, className.trim(), section.trim(), roll, examType.trim(), schoolId.trim());
        if (marksheetData == null) {
            return new ResponseEntity<>(new ResponseMessage("Student with roll number " + roll + " not found.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("passingMark", passingMark);
        response.put("gradeScale", grades);
        response.put("markSheets", Collections.singletonList(marksheetData));
        return new ResponseEntity<>(new ResponseMessage("Marksheet generated successfully.", response), (HttpStatusCode) HttpStatus.OK);
    }

    private Double lookupPassingMark(String schoolId, String className, String examType) {
        if (this.criteriaService == null) {
            return null;
        }
        try {
            return this.criteriaService.getCriteriaForClassAndExam(schoolId, className, examType)
                    .map(Criteria::getPassingMark)
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> generateMarksheetData(
            List<MarksRegister> registers,
            List<GradeSystem> grades,
            Double passingMark,
            String className,
            String section,
            int roll,
            String examType,
            String schoolId) {

        String studentName = null;
        for (MarksRegister register : registers) {
            if (register.getMarks() == null) continue;
            Optional<MarksRegister.StudentMark> studentMark = register.getMarks().stream()
                    .filter(mark -> mark.getRoll() == roll)
                    .findFirst();
            if (studentMark.isPresent()) {
                studentName = studentMark.get().getName();
                break;
            }
        }
        if (studentName == null) {
            return null;
        }

        Map<String, Object> marksheetData = new HashMap<>();
        marksheetData.put("name", studentName);
        marksheetData.put("class", className);
        marksheetData.put("section", section);
        marksheetData.put("roll", roll);
        marksheetData.put("examType", examType);

        StudentDetailsResponse.StudentData studentDetails = this.fetchStudentDetails(String.valueOf(roll), className, section, schoolId);
        marksheetData.put("studentDetails", studentDetails);

        List<Map<String, Object>> subjectResults = new ArrayList<>();
        double grandTotal = 0.0;
        double grandTotalFull = 0.0;
        int subjectCount = 0;

        for (MarksRegister register : registers) {
            if (register.getMarks() == null) continue;
            Optional<MarksRegister.StudentMark> studentMark = register.getMarks().stream()
                    .filter(mark -> mark.getRoll() == roll)
                    .findFirst();
            if (studentMark.isEmpty()) continue;

            double writtenMarks = round2(studentMark.get().getMark());
            double projectMarks = round2(studentMark.get().getProjectMark() != null ? studentMark.get().getProjectMark() : 0.0);
            double totalMarks = round2(writtenMarks + projectMarks);

            double totalFull = resolveTotalFullMarks(register);
            double writtenFull = resolveWrittenFullMark(register);
            double projectFull = resolveProjectFullMark(register);
            if (writtenFull <= 0.0 && projectFull <= 0.0) {
                writtenFull = totalFull;
                projectFull = 0.0;
            }

            double percentage = totalFull > 0.0 ? round2((totalMarks / totalFull) * 100.0) : 0.0;
            String grade = this.determineGrade(percentage, grades);
            boolean passed = this.isSubjectPassed(percentage, grade, passingMark, grades);

            Map<String, Object> subjectResult = new HashMap<>();
            subjectResult.put("subject", register.getSubject() != null ? register.getSubject().trim() : "");
            subjectResult.put("subjectFullMark", register.getSubjectFullMark() != null ? register.getSubjectFullMark() : formatFullMark(writtenFull));
            subjectResult.put("projectFullMark", register.getProjectFullMark() != null ? register.getProjectFullMark() : formatFullMark(projectFull));
            subjectResult.put("fullMarks", round2(totalFull));
            subjectResult.put("writtenMarks", writtenMarks);
            subjectResult.put("projectMarks", projectMarks);
            subjectResult.put("totalMarks", totalMarks);
            subjectResult.put("percentage", percentage);
            subjectResult.put("grade", grade);
            subjectResult.put("passed", passed);
            subjectResults.add(subjectResult);

            grandTotal += totalMarks;
            grandTotalFull += totalFull;
            subjectCount++;
        }

        double overallPercentage = grandTotalFull > 0.0 ? round2((grandTotal / grandTotalFull) * 100.0) : 0.0;
        String overallGrade = subjectCount > 0 ? this.determineGrade(overallPercentage, grades) : "F";
        boolean allPassed = subjectCount > 0 && subjectResults.stream().allMatch(s -> Boolean.TRUE.equals(s.get("passed")));

        marksheetData.put("subjects", subjectResults);
        marksheetData.put("totalMarks", round2(grandTotal));
        marksheetData.put("totalFullMarks", round2(grandTotalFull));
        marksheetData.put("overallPercentage", overallPercentage);
        marksheetData.put("overallGrade", overallGrade);
        marksheetData.put("passingMark", passingMark);
        marksheetData.put("result", allPassed ? "P" : "F");
        return marksheetData;
    }

    double resolveWrittenFullMark(MarksRegister register) {
        if (register != null && register.getSubjectFullMark() != null && !register.getSubjectFullMark().trim().isEmpty()) {
            try {
                return Math.max(0.0, Double.parseDouble(register.getSubjectFullMark().trim()));
            } catch (NumberFormatException ignored) {}
        }
        return 0.0;
    }

    double resolveProjectFullMark(MarksRegister register) {
        if (register != null && register.getProjectFullMark() != null && !register.getProjectFullMark().trim().isEmpty()) {
            try {
                return Math.max(0.0, Double.parseDouble(register.getProjectFullMark().trim()));
            } catch (NumberFormatException ignored) {}
        }
        return 0.0;
    }

    /**
     * Resolves total full marks for a MarksRegister without double-counting projectFullMark.
     * Prefers (subjectFullMark + projectFullMark) when defined; falls back to legacy fullMarks.
     */
    double resolveTotalFullMarks(MarksRegister register) {
        if (register == null) {
            return 100.0;
        }
        double writtenFull = resolveWrittenFullMark(register);
        double projFull = resolveProjectFullMark(register);
        if (writtenFull > 0.0 || projFull > 0.0) {
            return writtenFull + projFull;
        }
        if (register.getFullMarks() > 0.0) {
            return register.getFullMarks();
        }
        return 100.0;
    }

    String determineGrade(double percentage, List<GradeSystem> grades) {
        double clamped = Math.max(0.0, Math.min(100.0, percentage));
        int flooredScore = (int) Math.floor(clamped);

        if (grades != null && !grades.isEmpty()) {
            List<GradeSystem> sorted = new ArrayList<>(grades);
            sorted.sort(Comparator.comparingInt(GradeSystem::getScaleStartMarks).reversed());

            for (GradeSystem grade : sorted) {
                if (flooredScore >= grade.getScaleStartMarks() && flooredScore <= grade.getScaleEndMarks()) {
                    return grade.getLetterGrade();
                }
            }
            // Handle gaps in user-configured grade scales gracefully without jumping to an unrelated fallback
            for (GradeSystem grade : sorted) {
                if (flooredScore >= grade.getScaleStartMarks()) {
                    return grade.getLetterGrade();
                }
            }
            return sorted.get(sorted.size() - 1).getLetterGrade();
        }

        if (flooredScore >= 90) return "AA";
        if (flooredScore >= 80) return "A+";
        if (flooredScore >= 70) return "A";
        if (flooredScore >= 60) return "B+";
        if (flooredScore >= 50) return "B";
        if (flooredScore >= 40) return "C";
        if (flooredScore >= 33) return "D";
        return "F";
    }

    boolean isFailingGrade(String letterGrade, List<GradeSystem> grades) {
        if (letterGrade == null || letterGrade.trim().isEmpty()) {
            return true;
        }
        String cleanGrade = letterGrade.trim();
        if (cleanGrade.equalsIgnoreCase("F") || cleanGrade.equalsIgnoreCase("FAIL")) {
            return true;
        }
        if (grades != null) {
            for (GradeSystem g : grades) {
                if (g.getLetterGrade() != null && g.getLetterGrade().trim().equalsIgnoreCase(cleanGrade)) {
                    String indicator = g.getPerformanceIndicator() != null
                            ? g.getPerformanceIndicator().trim().toLowerCase()
                            : "";
                    if (indicator.contains("fail") || indicator.contains("disqualified")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean isSubjectPassed(double percentage, String grade, Double passingMark, List<GradeSystem> grades) {
        if (isFailingGrade(grade, grades)) {
            return false;
        }
        if (passingMark != null && passingMark > 0.0 && percentage < passingMark) {
            return false;
        }
        return true;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private String formatFullMark(double val) {
        if (val == Math.floor(val)) {
            return String.valueOf((long) val);
        }
        return String.valueOf(round2(val));
    }

    public StudentDetailsResponse.StudentData fetchStudentDetails(String rollNo, String className, String section, String schoolId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.fetchStudentDetailsUrl)
                    .queryParam("rollNo", rollNo)
                    .queryParam("className", className)
                    .queryParam("section", section)
                    .queryParam("schoolId", schoolId)
                    .toUriString();
            StudentDetailsResponse response = this.restTemplate.getForObject(url, StudentDetailsResponse.class);
            return response != null ? response.getData() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public ResponseEntity<ResponseMessage> calculateCumulativeMarksheet(String className, String section, String schoolId) {
        List<MarksRegister> registers = this.marksRegisterRepository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(className.trim(), section.trim(), schoolId.trim());
        if (registers == null || registers.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No marks records found for class " + className + " section " + section + ".", null), HttpStatus.NOT_FOUND);
        }

        List<GradeSystem> grades = this.gradeSystemRepository.findBySchoolId(schoolId.trim());
        if (grades == null) {
            grades = Collections.emptyList();
        }

        LinkedHashMap<String, String> uniqueExamTypes = new LinkedHashMap<>();
        for (MarksRegister reg : registers) {
            if (reg.getExamType() != null && !reg.getExamType().trim().isEmpty()) {
                String trimmed = reg.getExamType().trim();
                uniqueExamTypes.putIfAbsent(trimmed.toLowerCase(), trimmed);
            }
        }
        List<String> allExamTypes = new ArrayList<>(uniqueExamTypes.values());
        List<String> internalExamTypes = new ArrayList<>();
        List<String> annualExamTypes = new ArrayList<>();

        for (String exam : allExamTypes) {
            String lower = exam.toLowerCase();
            if (lower.contains("annual") || lower.contains("final")) {
                annualExamTypes.add(exam);
            } else {
                internalExamTypes.add(exam);
            }
        }

        TreeSet<Integer> rollNumbers = new TreeSet<>();
        for (MarksRegister reg : registers) {
            if (reg.getMarks() != null) {
                for (MarksRegister.StudentMark sm : reg.getMarks()) {
                    rollNumbers.add(sm.getRoll());
                }
            }
        }

        if (rollNumbers.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No student marks found for class " + className + " section " + section + ".", null), HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> studentResultsList = new ArrayList<>();
        double classTotalPercentage = 0.0;
        double highestPercentage = 0.0;
        int passCount = 0;
        int failCount = 0;

        for (Integer roll : rollNumbers) {
            Map<String, Object> studentData = this.buildStudentCumulativeData(
                    roll, registers, grades, className.trim(), section.trim(), schoolId.trim(), internalExamTypes, annualExamTypes);
            if (studentData != null) {
                studentResultsList.add(studentData);
                double pct = ((Number) studentData.getOrDefault("overallPercentage", 0.0)).doubleValue();
                classTotalPercentage += pct;
                if (pct > highestPercentage) {
                    highestPercentage = pct;
                }
                if ("P".equals(studentData.get("result"))) {
                    passCount++;
                } else {
                    failCount++;
                }
            }
        }

        Map<String, Object> statistics = new HashMap<>();
        int totalStudents = studentResultsList.size();
        statistics.put("totalStudents", totalStudents);
        statistics.put("classAveragePercentage", totalStudents > 0 ? round2(classTotalPercentage / totalStudents) : 0.0);
        statistics.put("highestPercentage", round2(highestPercentage));
        statistics.put("passCount", passCount);
        statistics.put("failCount", failCount);
        statistics.put("passPercentage", totalStudents > 0 ? round2(((double) passCount / totalStudents) * 100.0) : 0.0);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("className", className.trim());
        responseData.put("section", section.trim());
        responseData.put("schoolId", schoolId.trim());
        responseData.put("internalExamTypes", internalExamTypes);
        responseData.put("annualExamTypes", annualExamTypes);
        responseData.put("allExamTypes", allExamTypes);
        responseData.put("gradeScale", grades);
        responseData.put("students", studentResultsList);
        responseData.put("statistics", statistics);

        return new ResponseEntity<>(new ResponseMessage("Cumulative marksheets calculated successfully.", responseData), HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> calculateStudentCumulativeMarksheet(String className, String section, int roll, String schoolId) {
        List<MarksRegister> registers = this.marksRegisterRepository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(className.trim(), section.trim(), schoolId.trim());
        if (registers == null || registers.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No records found for class " + className + " section " + section + ".", null), HttpStatus.NOT_FOUND);
        }

        List<GradeSystem> grades = this.gradeSystemRepository.findBySchoolId(schoolId.trim());
        if (grades == null) {
            grades = Collections.emptyList();
        }

        LinkedHashMap<String, String> uniqueExamTypes = new LinkedHashMap<>();
        for (MarksRegister reg : registers) {
            if (reg.getExamType() != null && !reg.getExamType().trim().isEmpty()) {
                String trimmed = reg.getExamType().trim();
                uniqueExamTypes.putIfAbsent(trimmed.toLowerCase(), trimmed);
            }
        }
        List<String> internalExamTypes = new ArrayList<>();
        List<String> annualExamTypes = new ArrayList<>();

        for (String exam : uniqueExamTypes.values()) {
            String lower = exam.toLowerCase();
            if (lower.contains("annual") || lower.contains("final")) {
                annualExamTypes.add(exam);
            } else {
                internalExamTypes.add(exam);
            }
        }

        Map<String, Object> studentData = this.buildStudentCumulativeData(
                roll, registers, grades, className.trim(), section.trim(), schoolId.trim(), internalExamTypes, annualExamTypes);
        if (studentData == null) {
            return new ResponseEntity<>(new ResponseMessage("Student with roll " + roll + " not found.", null), HttpStatus.NOT_FOUND);
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("className", className.trim());
        responseData.put("section", section.trim());
        responseData.put("schoolId", schoolId.trim());
        responseData.put("internalExamTypes", internalExamTypes);
        responseData.put("annualExamTypes", annualExamTypes);
        responseData.put("gradeScale", grades);
        responseData.put("student", studentData);

        return new ResponseEntity<>(new ResponseMessage("Student marksheet calculated successfully.", responseData), HttpStatus.OK);
    }

    private Map<String, Object> buildStudentCumulativeData(
            int roll,
            List<MarksRegister> registers,
            List<GradeSystem> grades,
            String className,
            String section,
            String schoolId,
            List<String> internalExamTypes,
            List<String> annualExamTypes) {

        String studentName = null;
        String admissionNo = null;

        for (MarksRegister reg : registers) {
            if (reg.getMarks() != null) {
                for (MarksRegister.StudentMark sm : reg.getMarks()) {
                    if (sm.getRoll() == roll) {
                        studentName = sm.getName();
                        if (reg.getAdmissionNo() != null && !reg.getAdmissionNo().isEmpty()) {
                            admissionNo = reg.getAdmissionNo();
                        }
                        break;
                    }
                }
            }
            if (studentName != null && admissionNo != null) break;
        }

        if (studentName == null) {
            return null;
        }

        StudentDetailsResponse.StudentData studentDetails = this.fetchStudentDetails(String.valueOf(roll), className, section, schoolId);
        if (studentDetails != null && (admissionNo == null || admissionNo.isEmpty())) {
            admissionNo = studentDetails.getAdmissionNumber();
        }

        // Case-insensitive trimmed subject deduplication to avoid double-counting "Math" and "math"
        LinkedHashMap<String, String> subjectMap = new LinkedHashMap<>();
        for (MarksRegister reg : registers) {
            if (reg.getSubject() != null && !reg.getSubject().trim().isEmpty()) {
                String trimmed = reg.getSubject().trim();
                subjectMap.putIfAbsent(trimmed.toLowerCase(), trimmed);
            }
        }

        List<Map<String, Object>> subjectBreakdown = new ArrayList<>();
        double grandTotalInternalObtained = 0.0;
        double grandTotalInternalFull = 0.0;
        double grandTotalAnnualObtained = 0.0;
        double grandTotalAnnualFull = 0.0;

        for (String subject : subjectMap.values()) {
            Map<String, Object> subMap = new HashMap<>();
            subMap.put("subject", subject);

            Map<String, Object> internalExamsMap = new HashMap<>();
            double subInternalObtained = 0.0;
            double subInternalFull = 0.0;

            for (String intExam : internalExamTypes) {
                Map<String, Object> examScore = new HashMap<>();
                Optional<MarksRegister> regOpt = registers.stream().filter(r ->
                        r.getSubject() != null && r.getSubject().trim().equalsIgnoreCase(subject)
                                && r.getExamType() != null && r.getExamType().trim().equalsIgnoreCase(intExam)
                ).findFirst();

                if (regOpt.isPresent()) {
                    MarksRegister reg = regOpt.get();
                    double totalFull = resolveTotalFullMarks(reg);

                    Optional<MarksRegister.StudentMark> smOpt = reg.getMarks() != null
                            ? reg.getMarks().stream().filter(m -> m.getRoll() == roll).findFirst()
                            : Optional.empty();

                    if (smOpt.isPresent()) {
                        MarksRegister.StudentMark sm = smOpt.get();
                        double written = round2(sm.getMark());
                        double proj = round2(sm.getProjectMark() != null ? sm.getProjectMark() : 0.0);
                        double total = round2(written + proj);

                        examScore.put("writtenMarks", written);
                        examScore.put("projectMarks", proj);
                        examScore.put("totalMarks", total);
                        examScore.put("fullMarks", round2(totalFull));
                        examScore.put("present", true);

                        subInternalObtained += total;
                        subInternalFull += totalFull;
                    } else {
                        examScore.put("writtenMarks", 0.0);
                        examScore.put("projectMarks", 0.0);
                        examScore.put("totalMarks", 0.0);
                        examScore.put("fullMarks", round2(totalFull));
                        examScore.put("present", false);
                        subInternalFull += totalFull;
                    }
                } else {
                    examScore.put("totalMarks", 0.0);
                    examScore.put("fullMarks", 0.0);
                    examScore.put("present", false);
                }
                internalExamsMap.put(intExam, examScore);
            }

            subMap.put("internalExams", internalExamsMap);
            subMap.put("totalInternalObtained", round2(subInternalObtained));
            subMap.put("totalInternalFullMarks", round2(subInternalFull));
            double subInternalPct = subInternalFull > 0.0 ? (subInternalObtained / subInternalFull) * 100.0 : 0.0;
            subMap.put("internalPercentage", round2(subInternalPct));
            subMap.put("internalGrade", subInternalFull > 0.0 ? this.determineGrade(subInternalPct, grades) : "N/A");

            grandTotalInternalObtained += subInternalObtained;
            grandTotalInternalFull += subInternalFull;

            // Annual Exam(s)
            double subAnnualObtained = 0.0;
            double subAnnualFull = 0.0;
            double subAnnualWritten = 0.0;
            double subAnnualProject = 0.0;
            boolean anyAnnualPresent = false;
            Map<String, Object> annualExamsByType = new HashMap<>();

            for (String annExam : annualExamTypes) {
                Map<String, Object> examScore = new HashMap<>();
                Optional<MarksRegister> regOpt = registers.stream().filter(r ->
                        r.getSubject() != null && r.getSubject().trim().equalsIgnoreCase(subject)
                                && r.getExamType() != null && r.getExamType().trim().equalsIgnoreCase(annExam)
                ).findFirst();

                if (regOpt.isPresent()) {
                    MarksRegister reg = regOpt.get();
                    double totalFull = resolveTotalFullMarks(reg);

                    Optional<MarksRegister.StudentMark> smOpt = reg.getMarks() != null
                            ? reg.getMarks().stream().filter(m -> m.getRoll() == roll).findFirst()
                            : Optional.empty();

                    if (smOpt.isPresent()) {
                        MarksRegister.StudentMark sm = smOpt.get();
                        double written = round2(sm.getMark());
                        double proj = round2(sm.getProjectMark() != null ? sm.getProjectMark() : 0.0);
                        double total = round2(written + proj);

                        examScore.put("writtenMarks", written);
                        examScore.put("projectMarks", proj);
                        examScore.put("totalMarks", total);
                        examScore.put("fullMarks", round2(totalFull));
                        examScore.put("present", true);

                        subAnnualWritten += written;
                        subAnnualProject += proj;
                        subAnnualObtained += total;
                        subAnnualFull += totalFull;
                        anyAnnualPresent = true;
                    } else {
                        examScore.put("writtenMarks", 0.0);
                        examScore.put("projectMarks", 0.0);
                        examScore.put("totalMarks", 0.0);
                        examScore.put("fullMarks", round2(totalFull));
                        examScore.put("present", false);
                        subAnnualFull += totalFull;
                    }
                } else {
                    examScore.put("totalMarks", 0.0);
                    examScore.put("fullMarks", 0.0);
                    examScore.put("present", false);
                }
                annualExamsByType.put(annExam, examScore);
            }

            Map<String, Object> annualExamSummary = new HashMap<>();
            if (subAnnualFull > 0.0) {
                annualExamSummary.put("writtenMarks", round2(subAnnualWritten));
                annualExamSummary.put("projectMarks", round2(subAnnualProject));
                annualExamSummary.put("totalMarks", round2(subAnnualObtained));
                annualExamSummary.put("fullMarks", round2(subAnnualFull));
                annualExamSummary.put("present", anyAnnualPresent);
            }

            subMap.put("annualExam", annualExamSummary);
            subMap.put("annualExams", annualExamsByType);
            subMap.put("annualObtained", round2(subAnnualObtained));
            subMap.put("annualFullMarks", round2(subAnnualFull));

            grandTotalAnnualObtained += subAnnualObtained;
            grandTotalAnnualFull += subAnnualFull;

            double subCumulativeObtained = subInternalObtained + subAnnualObtained;
            double subCumulativeFull = subInternalFull + subAnnualFull;
            double subCumulativePct = subCumulativeFull > 0.0 ? (subCumulativeObtained / subCumulativeFull) * 100.0 : 0.0;

            subMap.put("cumulativeObtained", round2(subCumulativeObtained));
            subMap.put("cumulativeFullMarks", round2(subCumulativeFull));
            subMap.put("cumulativePercentage", round2(subCumulativePct));
            subMap.put("cumulativeGrade", subCumulativeFull > 0.0 ? this.determineGrade(subCumulativePct, grades) : "N/A");

            subjectBreakdown.add(subMap);
        }

        double grandTotalCumulativeObtained = grandTotalInternalObtained + grandTotalAnnualObtained;
        double grandTotalCumulativeFull = grandTotalInternalFull + grandTotalAnnualFull;

        double overallInternalPct = grandTotalInternalFull > 0.0 ? (grandTotalInternalObtained / grandTotalInternalFull) * 100.0 : 0.0;
        double overallCumulativePct = grandTotalCumulativeFull > 0.0 ? (grandTotalCumulativeObtained / grandTotalCumulativeFull) * 100.0 : 0.0;

        boolean hasInternal = grandTotalInternalFull > 0.0;
        boolean internalAllPassed = hasInternal && subjectBreakdown.stream()
                .filter(s -> ((Number) s.get("totalInternalFullMarks")).doubleValue() > 0.0)
                .noneMatch(s -> isFailingGrade(String.valueOf(s.get("internalGrade")), grades));
        boolean cumulativeAllPassed = grandTotalCumulativeFull > 0.0 && subjectBreakdown.stream()
                .filter(s -> ((Number) s.get("cumulativeFullMarks")).doubleValue() > 0.0)
                .noneMatch(s -> isFailingGrade(String.valueOf(s.get("cumulativeGrade")), grades));

        Map<String, Object> studentData = new HashMap<>();
        studentData.put("roll", roll);
        studentData.put("name", studentName);
        studentData.put("admissionNo", admissionNo != null ? admissionNo : "N/A");
        studentData.put("className", className);
        studentData.put("section", section);
        studentData.put("studentDetails", studentDetails);
        studentData.put("subjects", subjectBreakdown);

        // Internal Totals
        studentData.put("totalInternalObtained", round2(grandTotalInternalObtained));
        studentData.put("totalInternalFullMarks", round2(grandTotalInternalFull));
        studentData.put("overallInternalPercentage", round2(overallInternalPct));
        studentData.put("overallInternalGrade", hasInternal ? this.determineGrade(overallInternalPct, grades) : "N/A");
        studentData.put("internalResult", hasInternal ? (internalAllPassed ? "P" : "F") : "N/A");

        // Annual / Cumulative Totals
        studentData.put("totalAnnualObtained", round2(grandTotalAnnualObtained));
        studentData.put("totalAnnualFullMarks", round2(grandTotalAnnualFull));
        studentData.put("grandTotalObtained", round2(grandTotalCumulativeObtained));
        studentData.put("grandTotalFullMarks", round2(grandTotalCumulativeFull));
        studentData.put("overallPercentage", round2(overallCumulativePct));
        studentData.put("overallGrade", grandTotalCumulativeFull > 0.0 ? this.determineGrade(overallCumulativePct, grades) : "N/A");
        studentData.put("result", cumulativeAllPassed ? "P" : "F");

        return studentData;
    }

    // =========================================================================
    // Dynamic N-Exam Annual Report Card Engine (Supports 1, 2, 3, 4, or N Exams)
    // =========================================================================

    private record AnnualSubjectEntry(
            String displaySubject,
            double fullMarks,
            Map<Integer, MarksRegister.StudentMark> marksByRoll
    ) {}

    private record AnnualExamContext(
            List<String> orderedExamKeys,
            Map<String, String> examDisplayNames,
            Map<String, LinkedHashMap<String, AnnualSubjectEntry>> examSubjects,
            List<Map<String, Object>> activeExams,
            LinkedHashMap<String, String> subjectDisplayNames,
            Map<String, Double> passingMarkByExam,
            double totalAnnualFullMarks,
            List<GradeSystem> grades,
            List<GradeTierTemplate> sevenTierTemplates,
            List<Map<String, Object>> dynamicGradeScale,
            String academicYearRange,
            TreeSet<Integer> rollNumbers
    ) {}

    public ResponseEntity<ResponseMessage> generateAnnualReportCard(String className, String section, String schoolId) {
        if (isBlank(className) || isBlank(section) || isBlank(schoolId)) {
            return new ResponseEntity<>(
                    new ResponseMessage("Class name, section, and school ID are required.", null),
                    HttpStatus.BAD_REQUEST);
        }

        String trimmedClass = className.trim();
        String trimmedSection = section.trim();
        String trimmedSchoolId = schoolId.trim();

        AnnualExamContext ctx = this.buildAnnualExamContext(trimmedClass, trimmedSection, trimmedSchoolId);
        if (ctx == null) {
            return new ResponseEntity<>(
                    new ResponseMessage("No populated exam records found for class " + trimmedClass + " section " + trimmedSection + ".", null),
                    HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> students = new ArrayList<>();
        int passedCount = 0;
        int failedCount = 0;
        double percentageSum = 0.0;
        double highestObtained = 0.0;

        for (Integer roll : ctx.rollNumbers()) {
            Map<String, Object> studentData = this.buildStudentAnnualData(roll, trimmedClass, trimmedSection, trimmedSchoolId, ctx);
            if (studentData != null) {
                students.add(studentData);
                double pct = ((Number) studentData.getOrDefault("overallPercentage", 0.0)).doubleValue();
                double obt = ((Number) studentData.getOrDefault("grandTotalObtained", 0.0)).doubleValue();
                percentageSum += pct;
                if (obt > highestObtained) {
                    highestObtained = obt;
                }
                if ("P".equals(studentData.get("result"))) {
                    passedCount++;
                } else {
                    failedCount++;
                }
            }
        }

        if (students.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseMessage("No student marks found for class " + trimmedClass + " section " + trimmedSection + ".", null),
                    HttpStatus.NOT_FOUND);
        }

        int totalStudents = students.size();
        Map<String, Object> statistics = new LinkedHashMap<>();
        statistics.put("totalStudents", totalStudents);
        statistics.put("passedCount", passedCount);
        statistics.put("failedCount", failedCount);
        statistics.put("passRate", round2(((double) passedCount / totalStudents) * 100.0));
        statistics.put("averagePercentage", round2(percentageSum / totalStudents));
        statistics.put("highestObtained", round2(highestObtained));

        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("className", trimmedClass);
        responseData.put("section", trimmedSection);
        responseData.put("schoolId", trimmedSchoolId);
        responseData.put("academicYearRange", ctx.academicYearRange());
        responseData.put("activeExams", ctx.activeExams());
        responseData.put("totalAnnualFullMarks", round2(ctx.totalAnnualFullMarks()));
        responseData.put("gradeScale", ctx.grades());
        responseData.put("dynamicGradeScale", ctx.dynamicGradeScale());
        responseData.put("statistics", statistics);
        responseData.put("students", students);

        return new ResponseEntity<>(
                new ResponseMessage("Annual report cards generated successfully.", responseData),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> generateStudentAnnualReportCard(String className, String section, int roll, String schoolId) {
        if (isBlank(className) || isBlank(section) || isBlank(schoolId)) {
            return new ResponseEntity<>(
                    new ResponseMessage("Class name, section, and school ID are required.", null),
                    HttpStatus.BAD_REQUEST);
        }

        String trimmedClass = className.trim();
        String trimmedSection = section.trim();
        String trimmedSchoolId = schoolId.trim();

        AnnualExamContext ctx = this.buildAnnualExamContext(trimmedClass, trimmedSection, trimmedSchoolId);
        if (ctx == null) {
            return new ResponseEntity<>(
                    new ResponseMessage("No populated exam records found for class " + trimmedClass + " section " + trimmedSection + ".", null),
                    HttpStatus.NOT_FOUND);
        }

        Map<String, Object> studentData = this.buildStudentAnnualData(roll, trimmedClass, trimmedSection, trimmedSchoolId, ctx);
        if (studentData == null) {
            return new ResponseEntity<>(
                    new ResponseMessage("Student with roll number " + roll + " not found.", null),
                    HttpStatus.NOT_FOUND);
        }

        boolean passed = "P".equals(studentData.get("result"));
        double pct = ((Number) studentData.getOrDefault("overallPercentage", 0.0)).doubleValue();
        double obt = ((Number) studentData.getOrDefault("grandTotalObtained", 0.0)).doubleValue();

        Map<String, Object> statistics = new LinkedHashMap<>();
        statistics.put("totalStudents", 1);
        statistics.put("passedCount", passed ? 1 : 0);
        statistics.put("failedCount", passed ? 0 : 1);
        statistics.put("passRate", passed ? 100.0 : 0.0);
        statistics.put("averagePercentage", round2(pct));
        statistics.put("highestObtained", round2(obt));

        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("className", trimmedClass);
        responseData.put("section", trimmedSection);
        responseData.put("schoolId", trimmedSchoolId);
        responseData.put("academicYearRange", ctx.academicYearRange());
        responseData.put("activeExams", ctx.activeExams());
        responseData.put("totalAnnualFullMarks", round2(ctx.totalAnnualFullMarks()));
        responseData.put("gradeScale", ctx.grades());
        responseData.put("dynamicGradeScale", ctx.dynamicGradeScale());
        responseData.put("statistics", statistics);
        responseData.put("student", studentData);
        responseData.put("students", Collections.singletonList(studentData));

        return new ResponseEntity<>(
                new ResponseMessage("Student annual report card generated successfully.", responseData),
                HttpStatus.OK);
    }

    private AnnualExamContext buildAnnualExamContext(String trimmedClass, String trimmedSection, String trimmedSchoolId) {
        List<MarksRegister> rawRegisters = this.marksRegisterRepository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(trimmedClass, trimmedSection, trimmedSchoolId);
        if (rawRegisters == null || rawRegisters.isEmpty()) {
            return null;
        }

        Map<String, String> examDisplayNames = new LinkedHashMap<>();
        Map<String, LinkedHashMap<String, AnnualSubjectEntry>> examSubjects = new LinkedHashMap<>();
        TreeSet<Integer> rollNumbers = new TreeSet<>();

        for (MarksRegister reg : rawRegisters) {
            if (reg == null || isBlank(reg.getExamType()) || isBlank(reg.getSubject())) {
                continue;
            }
            if (reg.getMarks() == null || reg.getMarks().stream().noneMatch(Objects::nonNull)) {
                continue;
            }
            double regFull = this.resolveAnnualRegisterFullMarks(reg);
            if (regFull <= 0.0) {
                continue;
            }

            String trimmedExam = reg.getExamType().trim();
            String examKey = trimmedExam.toLowerCase(Locale.ROOT);
            String trimmedSubject = reg.getSubject().trim();
            String subjectKey = trimmedSubject.toLowerCase(Locale.ROOT);

            examDisplayNames.putIfAbsent(examKey, trimmedExam);
            LinkedHashMap<String, AnnualSubjectEntry> subjectMap = examSubjects.computeIfAbsent(examKey, k -> new LinkedHashMap<>());
            // Count full marks once per (exam, subject) while merging distinct rolls if split across registers
            AnnualSubjectEntry subjectEntry = subjectMap.computeIfAbsent(
                    subjectKey,
                    k -> new AnnualSubjectEntry(trimmedSubject, regFull, new LinkedHashMap<>()));
            for (MarksRegister.StudentMark sm : reg.getMarks()) {
                if (sm != null) {
                    subjectEntry.marksByRoll().putIfAbsent(sm.getRoll(), sm);
                    rollNumbers.add(sm.getRoll());
                }
            }
        }

        if (examSubjects.isEmpty() || rollNumbers.isEmpty()) {
            return null;
        }

        List<String> orderedExamKeys = new ArrayList<>(examSubjects.keySet());
        orderedExamKeys.sort((a, b) -> this.compareExamsChronologically(examDisplayNames.get(a), examDisplayNames.get(b)));

        LinkedHashMap<String, String> subjectDisplayNames = new LinkedHashMap<>();
        Map<String, Double> passingMarkByExam = new HashMap<>();
        List<Map<String, Object>> activeExams = new ArrayList<>();
        double totalAnnualFullMarks = 0.0;

        for (String examKey : orderedExamKeys) {
            String displayExam = examDisplayNames.get(examKey);
            LinkedHashMap<String, AnnualSubjectEntry> subjEntries = examSubjects.get(examKey);
            double examFullMarks = 0.0;
            for (Map.Entry<String, AnnualSubjectEntry> entry : subjEntries.entrySet()) {
                AnnualSubjectEntry subj = entry.getValue();
                examFullMarks += subj.fullMarks();
                subjectDisplayNames.putIfAbsent(entry.getKey(), subj.displaySubject());
            }

            Double examPassingMark = this.lookupPassingMark(trimmedSchoolId, trimmedClass, displayExam);
            passingMarkByExam.put(examKey, examPassingMark);

            Map<String, Object> activeExam = new LinkedHashMap<>();
            activeExam.put("examType", displayExam);
            activeExam.put("totalFullMarks", round2(examFullMarks));
            activeExam.put("subjectCount", subjEntries.size());
            activeExam.put("passingMark", examPassingMark);
            activeExams.add(activeExam);

            totalAnnualFullMarks += examFullMarks;
        }

        List<GradeSystem> grades = this.gradeSystemRepository.findBySchoolId(trimmedSchoolId);
        if (grades == null) {
            grades = Collections.emptyList();
        }

        List<GradeTierTemplate> sevenTierTemplates = resolveSevenTierTemplates(grades);
        List<Map<String, Object>> dynamicGradeScale = this.buildDynamicSevenGradeScaleFromTemplates(sevenTierTemplates, totalAnnualFullMarks);
        String academicYearRange = this.resolveDynamicAcademicYearRange();

        return new AnnualExamContext(
                orderedExamKeys,
                examDisplayNames,
                examSubjects,
                activeExams,
                subjectDisplayNames,
                passingMarkByExam,
                totalAnnualFullMarks,
                grades,
                sevenTierTemplates,
                dynamicGradeScale,
                academicYearRange,
                rollNumbers
        );
    }

    private Map<String, Object> buildStudentAnnualData(
            int roll,
            String trimmedClass,
            String trimmedSection,
            String trimmedSchoolId,
            AnnualExamContext ctx) {

        String studentName = null;
        boolean foundRoll = false;
        for (String examKey : ctx.orderedExamKeys()) {
            for (AnnualSubjectEntry subjEntry : ctx.examSubjects().get(examKey).values()) {
                MarksRegister.StudentMark sm = subjEntry.marksByRoll().get(roll);
                if (sm != null) {
                    foundRoll = true;
                    String candidateName = sm.getName();
                    if (!isBlank(candidateName)) {
                        studentName = candidateName.trim();
                        break;
                    }
                }
            }
            if (studentName != null) {
                break;
            }
        }

        if (!foundRoll) {
            return null;
        }

        StudentDetailsResponse.StudentData studentDetails = this.fetchStudentDetails(
                String.valueOf(roll), trimmedClass, trimmedSection, trimmedSchoolId);
        if (isBlank(studentName) && studentDetails != null && !isBlank(studentDetails.getStudentName())) {
            studentName = studentDetails.getStudentName().trim();
        }
        if (isBlank(studentName)) {
            studentName = "Student " + roll;
        }

        List<Map<String, Object>> examSummaries = new ArrayList<>();
        double grandTotalFull = 0.0;
        double grandTotalObtained = 0.0;

        for (String examKey : ctx.orderedExamKeys()) {
            String examLabel = ctx.examDisplayNames().get(examKey);
            LinkedHashMap<String, AnnualSubjectEntry> subjEntries = ctx.examSubjects().get(examKey);
            double examFull = 0.0;
            double examObtained = 0.0;
            boolean examPresent = false;

            for (AnnualSubjectEntry subjEntry : subjEntries.values()) {
                double regFull = subjEntry.fullMarks();
                examFull += regFull;
                MarksRegister.StudentMark sm = subjEntry.marksByRoll().get(roll);
                if (sm != null) {
                    double written = round2(Math.max(0.0, sm.getMark()));
                    double project = round2(sm.getProjectMark() != null ? Math.max(0.0, sm.getProjectMark()) : 0.0);
                    examObtained += round2(written + project);
                    examPresent = true;
                }
            }

            double examPct = examFull > 0.0 ? round2((examObtained / examFull) * 100.0) : 0.0;
            String examGrade = examFull > 0.0 ? this.determineAnnualPercentageGrade(examPct, ctx.sevenTierTemplates()) : "-";
            Double examPassingMark = ctx.passingMarkByExam().get(examKey);
            boolean examPassed = examPresent && examFull > 0.0
                    && this.isAnnualPercentagePassed(examPct, examGrade, examPassingMark, ctx.sevenTierTemplates(), ctx.grades());

            Map<String, Object> summary = new LinkedHashMap<>();
            summary.put("examType", examLabel);
            summary.put("totalFullMarks", round2(examFull));
            summary.put("totalObtained", round2(examObtained));
            summary.put("percentage", examPct);
            summary.put("grade", examGrade);
            summary.put("passingMark", examPassingMark);
            summary.put("passed", examPassed);
            summary.put("present", examPresent);
            examSummaries.add(summary);

            grandTotalFull += examFull;
            grandTotalObtained += examObtained;
        }

        List<Map<String, Object>> subjects = new ArrayList<>();
        for (Map.Entry<String, String> subjEntry : ctx.subjectDisplayNames().entrySet()) {
            String subjectKey = subjEntry.getKey();
            String subjectLabel = subjEntry.getValue();
            List<Map<String, Object>> examMarks = new ArrayList<>();
            double subAnnualObtained = 0.0;
            double subAnnualFull = 0.0;
            Double effectivePassingMark = null;

            for (String examKey : ctx.orderedExamKeys()) {
                String examLabel = ctx.examDisplayNames().get(examKey);
                AnnualSubjectEntry subjectBucket = ctx.examSubjects().get(examKey).get(subjectKey);
                Map<String, Object> cell = new LinkedHashMap<>();
                cell.put("examType", examLabel);

                if (subjectBucket != null) {
                    double regFull = subjectBucket.fullMarks();
                    subAnnualFull += regFull;
                    Double examPm = ctx.passingMarkByExam().get(examKey);
                    if (examPm != null && examPm > 0.0) {
                        effectivePassingMark = effectivePassingMark == null
                                ? examPm
                                : Math.max(effectivePassingMark, examPm);
                    }

                    MarksRegister.StudentMark sm = subjectBucket.marksByRoll().get(roll);
                    cell.put("conducted", true);
                    if (sm != null) {
                        double written = round2(Math.max(0.0, sm.getMark()));
                        double project = round2(sm.getProjectMark() != null ? Math.max(0.0, sm.getProjectMark()) : 0.0);
                        double total = round2(written + project);
                        double pct = regFull > 0.0 ? round2((total / regFull) * 100.0) : 0.0;
                        String grade = this.determineAnnualPercentageGrade(pct, ctx.sevenTierTemplates());

                        cell.put("present", true);
                        cell.put("writtenMarks", written);
                        cell.put("projectMarks", project);
                        cell.put("totalMarks", total);
                        cell.put("fullMarks", round2(regFull));
                        cell.put("percentage", pct);
                        cell.put("grade", grade);
                        subAnnualObtained += total;
                    } else {
                        String absentGrade = this.determineAnnualPercentageGrade(0.0, ctx.sevenTierTemplates());
                        cell.put("present", false);
                        cell.put("writtenMarks", 0.0);
                        cell.put("projectMarks", 0.0);
                        cell.put("totalMarks", 0.0);
                        cell.put("fullMarks", round2(regFull));
                        cell.put("percentage", 0.0);
                        cell.put("grade", absentGrade);
                    }
                } else {
                    cell.put("conducted", false);
                    cell.put("present", false);
                    cell.put("writtenMarks", 0.0);
                    cell.put("projectMarks", 0.0);
                    cell.put("totalMarks", 0.0);
                    cell.put("fullMarks", 0.0);
                    cell.put("percentage", 0.0);
                    cell.put("grade", "-");
                }
                examMarks.add(cell);
            }

            double subAnnualPct = subAnnualFull > 0.0 ? round2((subAnnualObtained / subAnnualFull) * 100.0) : 0.0;
            String subAnnualGrade = subAnnualFull > 0.0 ? this.determineAnnualPercentageGrade(subAnnualPct, ctx.sevenTierTemplates()) : "-";
            boolean subPassed = subAnnualFull > 0.0
                    && this.isAnnualPercentagePassed(subAnnualPct, subAnnualGrade, effectivePassingMark, ctx.sevenTierTemplates(), ctx.grades());

            Map<String, Object> subjectRow = new LinkedHashMap<>();
            subjectRow.put("subject", subjectLabel);
            subjectRow.put("examMarks", examMarks);
            subjectRow.put("annualObtained", round2(subAnnualObtained));
            subjectRow.put("annualFullMarks", round2(subAnnualFull));
            subjectRow.put("annualPercentage", subAnnualPct);
            subjectRow.put("annualGrade", subAnnualGrade);
            subjectRow.put("passed", subPassed);
            subjects.add(subjectRow);
        }

        double roundedGrandFull = round2(grandTotalFull);
        double roundedGrandObtained = round2(grandTotalObtained);
        double overallPct = roundedGrandFull > 0.0 ? round2((roundedGrandObtained / roundedGrandFull) * 100.0) : 0.0;
        Map<String, Object> overallTier = this.resolveDynamicGradeTier(roundedGrandObtained, roundedGrandFull, ctx.dynamicGradeScale());
        String overallGrade = (roundedGrandFull > 0.0 && overallTier != null)
                ? String.valueOf(overallTier.get("letterGrade"))
                : "D";
        boolean overallPassed = roundedGrandFull > 0.0
                && roundedGrandObtained > 0.0
                && !this.isFailingDynamicTier(overallTier, overallGrade, ctx.grades());

        Map<String, Object> studentData = new LinkedHashMap<>();
        studentData.put("rollNumber", roll);
        studentData.put("studentName", studentName);
        studentData.put("studentDetails", studentDetails);
        studentData.put("examSummaries", examSummaries);
        studentData.put("subjects", subjects);
        studentData.put("grandTotalFullMarks", roundedGrandFull);
        studentData.put("grandTotalObtained", roundedGrandObtained);
        studentData.put("overallPercentage", overallPct);
        studentData.put("overallGrade", overallGrade);
        studentData.put("result", overallPassed ? "P" : "F");
        return studentData;
    }

    double resolveAnnualRegisterFullMarks(MarksRegister register) {
        if (register == null) {
            return 0.0;
        }
        double writtenFull = this.resolveWrittenFullMark(register);
        double projFull = this.resolveProjectFullMark(register);
        if (writtenFull > 0.0 || projFull > 0.0) {
            return writtenFull + projFull;
        }
        if (register.getFullMarks() > 0.0) {
            return register.getFullMarks();
        }
        return 0.0;
    }

    String resolveDynamicAcademicYearRange() {
        int currentYear = Year.now().getValue();
        return (currentYear - 1) + " to " + currentYear;
    }

    int compareExamsChronologically(String examA, String examB) {
        int rankA = this.resolveExamChronologicalRank(examA);
        int rankB = this.resolveExamChronologicalRank(examB);
        if (rankA != rankB) {
            return Integer.compare(rankA, rankB);
        }
        return compareNaturalIgnoreCase(examA, examB);
    }

    int resolveExamChronologicalRank(String examType) {
        if (isBlank(examType)) {
            return 500;
        }
        String lower = examType.trim().toLowerCase(Locale.ROOT);

        // Most specific end-of-year phases first
        if (lower.contains("pre-final") || lower.contains("prefinal") || lower.contains("pre final")
                || lower.contains("pre-board") || lower.contains("preboard") || lower.contains("pre board")) {
            return 900;
        }
        // Half-yearly / Mid-Term / SA1 checked before "yearly" and "final"
        if (lower.contains("half-yearly") || lower.contains("half yearly") || lower.contains("halfyearly")
                || lower.contains("mid-term") || lower.contains("mid term") || lower.contains("midterm")
                || MID_WORD_PATTERN.matcher(lower).find()
                || SA1_PATTERN.matcher(lower).find()) {
            return 25;
        }
        if (lower.contains("final") || lower.contains("annual")
                || SA2_PATTERN.matcher(lower).find()
                || YEARLY_WORD_PATTERN.matcher(lower).find()) {
            return 1000;
        }

        Matcher ordinalMatcher = ORDINAL_NUMBER_PATTERN.matcher(lower);
        if (ordinalMatcher.find()) {
            int n = Integer.parseInt(ordinalMatcher.group(1));
            if (n > 0) {
                return n * 10;
            }
        }

        for (Map.Entry<Pattern, Integer> entry : ORDINAL_WORD_PATTERNS) {
            if (entry.getKey().matcher(lower).find()) {
                return entry.getValue() * 10;
            }
        }

        Matcher keywordMatcher = KEYWORD_NUMBER_PATTERN.matcher(lower);
        if (keywordMatcher.find()) {
            int n = Integer.parseInt(keywordMatcher.group(1));
            if (n > 0) {
                return n * 10;
            }
        }

        return 500;
    }

    private static int compareNaturalIgnoreCase(String a, String b) {
        String s1 = a == null ? "" : a.trim().toLowerCase(Locale.ROOT);
        String s2 = b == null ? "" : b.trim().toLowerCase(Locale.ROOT);
        Matcher m1 = ALPHANUM_CHUNK_PATTERN.matcher(s1);
        Matcher m2 = ALPHANUM_CHUNK_PATTERN.matcher(s2);

        while (m1.find() && m2.find()) {
            String c1 = m1.group(1);
            String c2 = m2.group(1);
            boolean d1 = Character.isDigit(c1.charAt(0));
            boolean d2 = Character.isDigit(c2.charAt(0));
            int cmp;
            if (d1 && d2) {
                try {
                    cmp = Long.compare(Long.parseLong(c1), Long.parseLong(c2));
                } catch (NumberFormatException e) {
                    cmp = c1.compareTo(c2);
                }
            } else {
                cmp = c1.compareTo(c2);
            }
            if (cmp != 0) {
                return cmp;
            }
        }
        return Integer.compare(s1.length(), s2.length());
    }

    List<Map<String, Object>> buildDynamicSevenGradeScale(List<GradeSystem> schoolGrades, double totalAnnualFullMarks) {
        return this.buildDynamicSevenGradeScaleFromTemplates(resolveSevenTierTemplates(schoolGrades), totalAnnualFullMarks);
    }

    private List<Map<String, Object>> buildDynamicSevenGradeScaleFromTemplates(List<GradeTierTemplate> templates, double totalAnnualFullMarks) {
        long totalMax = Math.max(0L, Math.round(totalAnnualFullMarks));

        List<Map<String, Object>> scale = new ArrayList<>(ANNUAL_GRADE_TIER_COUNT);
        long prevMin = totalMax + 1L;
        int prevPctStart = 101;

        for (int i = 0; i < ANNUAL_GRADE_TIER_COUNT; i++) {
            GradeTierTemplate tier = templates.get(i);
            int pctStart = (i == ANNUAL_GRADE_TIER_COUNT - 1) ? 0 : Math.max(0, Math.min(100, tier.percentageStart()));
            int pctEnd = (i == 0) ? 100 : Math.max(0, prevPctStart - 1);

            long tierMax = (i == 0) ? totalMax : Math.max(0L, prevMin - 1L);
            long rawMin = (pctStart == 0 || totalMax == 0L)
                    ? 0L
                    : ((long) pctStart * totalMax + 99L) / 100L;
            long tierMin = (i == ANNUAL_GRADE_TIER_COUNT - 1)
                    ? 0L
                    : Math.min(tierMax, Math.max(0L, rawMin));

            Map<String, Object> tierMap = new LinkedHashMap<>();
            tierMap.put("minMarks", tierMin);
            tierMap.put("maxMarks", tierMax);
            tierMap.put("percentageStart", (double) pctStart);
            tierMap.put("percentageEnd", (double) pctEnd);
            tierMap.put("letterGrade", tier.letterGrade());
            tierMap.put("performanceIndicator", tier.performanceIndicator());
            scale.add(tierMap);

            prevMin = tierMin;
            prevPctStart = pctStart;
        }

        return scale;
    }

    String determineAnnualPercentageGrade(double percentage, List<GradeTierTemplate> templates) {
        List<GradeTierTemplate> activeTemplates = (templates != null && templates.size() == ANNUAL_GRADE_TIER_COUNT)
                ? templates
                : CANONICAL_SEVEN_GRADES;
        double clamped = Math.max(0.0, Math.min(100.0, percentage));
        int flooredScore = (int) Math.floor(clamped);

        for (int i = 0; i < activeTemplates.size(); i++) {
            GradeTierTemplate tier = activeTemplates.get(i);
            int pctStart = (i == activeTemplates.size() - 1) ? 0 : Math.max(0, Math.min(100, tier.percentageStart()));
            if (flooredScore >= pctStart) {
                return tier.letterGrade();
            }
        }
        return activeTemplates.get(activeTemplates.size() - 1).letterGrade();
    }

    private boolean isAnnualPercentagePassed(
            double percentage,
            String letterGrade,
            Double passingMark,
            List<GradeTierTemplate> templates,
            List<GradeSystem> schoolGrades) {
        if (isBlank(letterGrade)) {
            return false;
        }
        String cleanGrade = letterGrade.trim();
        if (templates != null) {
            for (GradeTierTemplate tier : templates) {
                if (tier.letterGrade().equalsIgnoreCase(cleanGrade)) {
                    String indicator = tier.performanceIndicator().trim().toLowerCase(Locale.ROOT);
                    if (indicator.contains("fail") || indicator.contains("disqualified") || indicator.contains("unsatisfactory")) {
                        return false;
                    }
                }
            }
        }
        return this.isSubjectPassed(percentage, cleanGrade, passingMark, schoolGrades);
    }

    private static List<GradeTierTemplate> resolveSevenTierTemplates(List<GradeSystem> schoolGrades) {
        if (schoolGrades == null || schoolGrades.size() != ANNUAL_GRADE_TIER_COUNT) {
            return CANONICAL_SEVEN_GRADES;
        }
        Set<Integer> distinctStarts = new HashSet<>();
        List<GradeSystem> valid = new ArrayList<>(ANNUAL_GRADE_TIER_COUNT);
        for (GradeSystem g : schoolGrades) {
            if (g == null || isBlank(g.getLetterGrade())) {
                return CANONICAL_SEVEN_GRADES;
            }
            int start = g.getScaleStartMarks();
            if (start < 0 || start > 100 || !distinctStarts.add(start)) {
                return CANONICAL_SEVEN_GRADES;
            }
            valid.add(g);
        }
        valid.sort(Comparator.comparingInt(GradeSystem::getScaleStartMarks).reversed());
        List<GradeTierTemplate> templates = new ArrayList<>(ANNUAL_GRADE_TIER_COUNT);
        for (GradeSystem g : valid) {
            templates.add(new GradeTierTemplate(
                    g.getScaleStartMarks(),
                    g.getLetterGrade().trim(),
                    g.getPerformanceIndicator() != null ? g.getPerformanceIndicator().trim() : ""
            ));
        }
        return templates;
    }

    Map<String, Object> resolveDynamicGradeTier(double obtainedMarks, double totalAnnualFullMarks, List<Map<String, Object>> dynamicScale) {
        if (dynamicScale == null || dynamicScale.isEmpty()) {
            return null;
        }
        long totalMax = Math.max(0L, Math.round(totalAnnualFullMarks));
        long floored = (long) Math.floor(Math.max(0.0, Math.min((double) totalMax, obtainedMarks)));

        if (totalMax == 0L || floored <= 0L) {
            return dynamicScale.get(dynamicScale.size() - 1);
        }

        for (Map<String, Object> tier : dynamicScale) {
            long min = ((Number) tier.get("minMarks")).longValue();
            long max = ((Number) tier.get("maxMarks")).longValue();
            if (floored >= min && floored <= max) {
                return tier;
            }
        }
        for (Map<String, Object> tier : dynamicScale) {
            long min = ((Number) tier.get("minMarks")).longValue();
            if (floored >= min) {
                return tier;
            }
        }
        return dynamicScale.get(dynamicScale.size() - 1);
    }

    private boolean isFailingDynamicTier(Map<String, Object> tier, String letterGrade, List<GradeSystem> schoolGrades) {
        if (tier != null) {
            String indicator = String.valueOf(tier.getOrDefault("performanceIndicator", "")).trim().toLowerCase(Locale.ROOT);
            if (indicator.contains("fail") || indicator.contains("disqualified") || indicator.contains("unsatisfactory")) {
                return true;
            }
        }
        return this.isFailingGrade(letterGrade, schoolGrades);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
