package com.result.Result.service;

import com.result.Result.Dto.StudentDetailsResponse;
import com.result.Result.model.Criteria;
import com.result.Result.model.GradeSystem;
import com.result.Result.model.MarksRegister;
import com.result.Result.model.ResponseMessage;
import com.result.Result.repo.GradeSystemRepository;
import com.result.Result.repo.MarksRegisterRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MarksheetService {
    @Autowired
    private MarksRegisterRepository marksRegisterRepository;
    @Autowired
    private GradeSystemRepository gradeSystemRepository;
    @Autowired(required = false)
    private CriteriaService criteriaService;
    @Value(value="${fetch.student.details:http://localhost:8085/admissions/student-details}")
    private String fetchStudentDetailsUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired(required = false)
    private GradeSystemService gradeSystemService;

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
}
