// Isolated utility helpers for the Result section only.

export const DEFAULT_GRADE_SCALE = [
  { scaleStartMarks: 90, scaleEndMarks: 100, letterGrade: "AA", performanceIndicator: "Outstanding" },
  { scaleStartMarks: 80, scaleEndMarks: 89, letterGrade: "A+", performanceIndicator: "Excellent" },
  { scaleStartMarks: 70, scaleEndMarks: 79, letterGrade: "A", performanceIndicator: "Very Good" },
  { scaleStartMarks: 60, scaleEndMarks: 69, letterGrade: "B+", performanceIndicator: "Good" },
  { scaleStartMarks: 50, scaleEndMarks: 59, letterGrade: "B", performanceIndicator: "Average" },
  { scaleStartMarks: 40, scaleEndMarks: 49, letterGrade: "C", performanceIndicator: "Below Average" },
  { scaleStartMarks: 33, scaleEndMarks: 39, letterGrade: "D", performanceIndicator: "Pass" },
  { scaleStartMarks: 0, scaleEndMarks: 32, letterGrade: "F", performanceIndicator: "Fail" },
];

export const round2 = (value) => {
  const num = Number(value);
  if (!Number.isFinite(num)) return 0;
  return Math.round(num * 100) / 100;
};

export const capitalizeFirst = (str) => {
  if (str === null || str === undefined) return "";
  const text = String(str).trim();
  if (!text) return "";
  return text.charAt(0).toUpperCase() + text.slice(1);
};

export const extractApiError = (error, fallback = "Something went wrong") => {
  const data = error?.response?.data;
  if (typeof data === "string" && data.trim() && !data.trim().startsWith("<")) {
    return data.trim();
  }
  if (data && typeof data === "object") {
    if (typeof data.message === "string" && data.message.trim()) {
      return data.message.trim();
    }
    if (typeof data.error === "string" && data.error.trim()) {
      return data.error.trim();
    }
  }
  if (typeof error?.message === "string" && error.message.trim()) {
    return error.message.trim();
  }
  return fallback;
};

export const isFailingGrade = (gradeInfo, gradeSystem = []) => {
  const letter = (typeof gradeInfo === "string" ? gradeInfo : gradeInfo?.letterGrade || "")
    .toString()
    .trim()
    .toUpperCase();

  if (!letter || letter === "F" || letter === "FAIL") {
    return true;
  }

  const directIndicator = (typeof gradeInfo === "object" ? gradeInfo?.performanceIndicator || "" : "")
    .toString()
    .trim()
    .toLowerCase();
  if (directIndicator.includes("fail") || directIndicator.includes("disqualified")) {
    return true;
  }

  if (Array.isArray(gradeSystem) && gradeSystem.length > 0) {
    const matched = gradeSystem.find(
      (g) => (g?.letterGrade || "").toString().trim().toUpperCase() === letter
    );
    const indicator = (matched?.performanceIndicator || "").toString().trim().toLowerCase();
    if (indicator.includes("fail") || indicator.includes("disqualified")) {
      return true;
    }
  }

  return false;
};

export const getGradeFromPercentage = (percentage, gradeSystem = []) => {
  const clamped = Math.max(0, Math.min(100, Number(percentage) || 0));
  const floored = Math.floor(clamped);

  const scale = Array.isArray(gradeSystem) && gradeSystem.length > 0
    ? [...gradeSystem].sort(
        (a, b) => (parseFloat(b.scaleStartMarks) || 0) - (parseFloat(a.scaleStartMarks) || 0)
      )
    : DEFAULT_GRADE_SCALE;

  const exactMatch = scale.find((g) => {
    const start = parseFloat(g.scaleStartMarks);
    const end = parseFloat(g.scaleEndMarks);
    return floored >= start && floored <= end;
  });
  if (exactMatch) {
    return {
      letterGrade: exactMatch.letterGrade,
      performanceIndicator: exactMatch.performanceIndicator,
    };
  }

  // Gracefully resolve gaps in user-configured grade scales
  const lowerMatch = scale.find((g) => floored >= parseFloat(g.scaleStartMarks));
  const resolved = lowerMatch || scale[scale.length - 1];
  return resolved
    ? {
        letterGrade: resolved.letterGrade,
        performanceIndicator: resolved.performanceIndicator,
      }
    : { letterGrade: "F", performanceIndicator: "Fail" };
};

export const resolveSubjectFullMarks = (subj) => {
  const writtenFull = parseFloat(subj?.subjectFullMark);
  const projectFull = parseFloat(subj?.projectFullMark);
  const w = Number.isFinite(writtenFull) && writtenFull > 0 ? writtenFull : 0;
  const p = Number.isFinite(projectFull) && projectFull > 0 ? projectFull : 0;
  if (w + p > 0) {
    return round2(w + p);
  }
  const fallbackFull = parseFloat(subj?.fullMarks);
  return Number.isFinite(fallbackFull) && fallbackFull > 0 ? round2(fallbackFull) : 100;
};

export const recalculateMarksheet = (student, gradeSystem = [], passingMarkOverride = null) => {
  if (!student || !Array.isArray(student.subjects)) return student;

  const passingMark =
    passingMarkOverride !== null && passingMarkOverride !== undefined
      ? passingMarkOverride
      : student.passingMark ?? null;

  let totalObtained = 0;
  let totalFull = 0;
  let anyFail = false;

  const recalcSubjects = student.subjects.map((subj) => {
    const fullMarks = resolveSubjectFullMarks(subj);
    const writtenObt = parseFloat(subj.writtenMarks) || 0;
    const projectObt = parseFloat(subj.projectMarks) || 0;
    const marksObtained = round2(writtenObt + projectObt);

    const percentage = fullMarks > 0 ? round2((marksObtained / fullMarks) * 100) : 0;
    const gradeInfo = getGradeFromPercentage(percentage, gradeSystem);
    const failed =
      isFailingGrade(gradeInfo, gradeSystem) ||
      (passingMark !== null &&
        passingMark !== undefined &&
        Number(passingMark) > 0 &&
        percentage < Number(passingMark));

    if (failed) anyFail = true;

    totalObtained += marksObtained;
    totalFull += fullMarks;

    return {
      ...subj,
      subjectFullMark:
        subj.subjectFullMark !== null && subj.subjectFullMark !== undefined
          ? subj.subjectFullMark
          : fullMarks,
      projectFullMark:
        subj.projectFullMark !== null && subj.projectFullMark !== undefined
          ? subj.projectFullMark
          : 0,
      fullMarks,
      marksObtained,
      totalMarks: marksObtained,
      percentage,
      grade: gradeInfo.letterGrade,
      performanceIndicator: gradeInfo.performanceIndicator,
      passed: !failed,
    };
  });

  const overallPercentage = totalFull > 0 ? round2((totalObtained / totalFull) * 100) : 0;
  const overallGradeInfo = getGradeFromPercentage(overallPercentage, gradeSystem);
  const result = anyFail || recalcSubjects.length === 0 ? "F" : "P";

  return {
    ...student,
    subjects: recalcSubjects,
    totalMarks: round2(totalObtained),
    totalFullMarks: round2(totalFull),
    overallPercentage,
    overallGrade: overallGradeInfo.letterGrade,
    passingMark,
    result,
  };
};

export const getPercentageBadgeClass = (percentage) => {
  const pct = Number(percentage) || 0;
  if (pct >= 80) return "bg-green-100 text-green-800";
  if (pct >= 60) return "bg-blue-100 text-blue-800";
  if (pct >= 40) return "bg-yellow-100 text-yellow-800";
  return "bg-red-100 text-red-800";
};

export const parseLocalDateOnly = (dateStr) => {
  if (!dateStr) return null;
  const str = String(dateStr).trim();
  const isoDateMatch = /^(\d{4})-(\d{2})-(\d{2})/.exec(str);
  if (isoDateMatch) {
    const year = parseInt(isoDateMatch[1], 10);
    const month = parseInt(isoDateMatch[2], 10) - 1;
    const day = parseInt(isoDateMatch[3], 10);
    return new Date(year, month, day);
  }
  const parsed = new Date(str);
  if (Number.isNaN(parsed.getTime())) return null;
  return new Date(parsed.getFullYear(), parsed.getMonth(), parsed.getDate());
};

export const formatDateDDMMYYYY = (dateStr) => {
  const dateObj = parseLocalDateOnly(dateStr);
  if (!dateObj) return "-";
  const dd = String(dateObj.getDate()).padStart(2, "0");
  const mm = String(dateObj.getMonth() + 1).padStart(2, "0");
  const yyyy = dateObj.getFullYear();
  return `${dd}-${mm}-${yyyy}`;
};
