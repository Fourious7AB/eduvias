package com.result.Result.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.result.Result.model.Criteria;
import com.result.Result.repo.CriteriaRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CriteriaServiceTest {
    @Mock
    private CriteriaRepository criteriaRepository;
    @InjectMocks
    private CriteriaService criteriaService;

    private Criteria.Subject subject(String name, double theory, double project) {
        Criteria.Subject s = new Criteria.Subject();
        s.setSubjectName(name);
        s.setSubjectMark(theory);
        s.setProjectMark(project);
        s.setFullMark(theory + project);
        return s;
    }

    @Test
    void updateCriteriaWithNullSectionDoesNotThrowNpeAndRemovesDeletedSubjects() {
        Criteria existing = new Criteria();
        existing.setId("c1");
        existing.setSchoolId("s1");
        existing.setClassName("LKG");
        existing.setSection(null);
        existing.setExamType("Unit 1");
        existing.setPassingMark(33.0);
        existing.setSubjects(Arrays.asList(subject("Math", 60, 40), subject("English", 80, 20)));

        Criteria other = new Criteria();
        other.setId("c2");
        other.setSchoolId("s1");
        other.setClassName("UKG");
        other.setSection(null);
        other.setExamType("Unit 1");

        when(criteriaRepository.findByIdAndSchoolId("c1", "s1")).thenReturn(Optional.of(existing));
        when(criteriaRepository.findAllBySchoolId("s1")).thenReturn(Arrays.asList(existing, other));
        when(criteriaRepository.save(any(Criteria.class))).thenAnswer(inv -> inv.getArgument(0));

        // Remove "English" and change examType -> triggers duplicate check across docs with section == null
        Criteria incoming = new Criteria();
        incoming.setClassName("LKG");
        incoming.setExamType("Unit 2");
        incoming.setPassingMark(35.0);
        incoming.setSubjects(Collections.singletonList(subject("Math", 70, 30)));

        Optional<Criteria> result = criteriaService.updateCriteria("s1", "c1", incoming);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getSubjects().size());
        assertEquals("Math", result.get().getSubjects().get(0).getSubjectName());
        assertEquals(70.0, result.get().getSubjects().get(0).getSubjectMark());
        assertNotNull(result.get().getSubjects().get(0).getCreateAt());
    }

    @Test
    void deleteSingleSubjectKeepsSiblingSubjectsAndDeletesDocWhenLastRemoved() {
        Criteria existing = new Criteria();
        existing.setId("c1");
        existing.setSchoolId("s1");
        existing.setClassName("LKG");
        existing.setExamType("Unit 1");
        existing.setPassingMark(33.0);
        existing.setSubjects(Arrays.asList(subject("Math", 60, 40), subject("English", 80, 20)));

        when(criteriaRepository.findByIdAndSchoolId("c1", "s1")).thenReturn(Optional.of(existing));
        when(criteriaRepository.save(any(Criteria.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Criteria> afterFirst = criteriaService.deleteSubjectFromCriteria("s1", "c1", "english");
        assertTrue(afterFirst.isPresent());
        assertEquals(1, afterFirst.get().getSubjects().size());
        verify(criteriaRepository, never()).deleteById("c1");

        Optional<Criteria> afterLast = criteriaService.deleteSubjectFromCriteria("s1", "c1", "Math");
        assertTrue(afterLast.isPresent());
        assertTrue(afterLast.get().getSubjects().isEmpty());
        verify(criteriaRepository, times(1)).deleteById("c1");
    }

    @Test
    void getAllSubjectsMatchesCaseInsensitivelyAndNullPassingMarkRejectedCleanly() {
        Criteria existing = new Criteria();
        existing.setId("c1");
        existing.setSchoolId("s1");
        existing.setClassName("LKG");
        existing.setExamType("1st Assessment Exams");
        existing.setSubjects(Collections.singletonList(subject("BIO", 100, 0)));

        when(criteriaRepository.findBySchoolIdAndClassNameIgnoreCaseAndExamTypeIgnoreCase("s1", "lkg", "1st assessment exams"))
                .thenReturn(Collections.singletonList(existing));

        List<Criteria.Subject> subjects = criteriaService.getAllSubjects("s1", " lkg ", " 1st assessment exams ");
        assertEquals(1, subjects.size());
        assertEquals("BIO", subjects.get(0).getSubjectName());

        Criteria invalid = new Criteria();
        invalid.setClassName("LKG");
        invalid.setExamType("Unit 1");
        invalid.setPassingMark(null);
        invalid.setSubjects(Collections.singletonList(subject("Math", 60, 40)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> criteriaService.createCriteria("s1", invalid));
        assertTrue(ex.getMessage().contains("Passing mark"));
    }
}
