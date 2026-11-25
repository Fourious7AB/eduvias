import { useCallback, useEffect, useId, useMemo, useRef, useState } from "react";
import { useTheme } from "@/context/ThemeContext";
import axios from "axios";
import { toast } from "sonner";
import {
  ChevronDown,
  ChevronLeft,
  ChevronRight,
  Clock,
  Eye,
  FileText,
  Loader2,
  RefreshCw,
  Search,
  X,
} from "lucide-react";
import { useSelector } from "react-redux";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import NewMarksheetComponent from "@/components/ForResult/NewMarksheetComponent";
import AnnualMarksheetComponent from "@/components/ForResult/AnnualMarksheetComponent";
import resultUrlApi from "@/common/result";
import mainUrlApi from "@/common/main";
import {
  DEFAULT_GRADE_SCALE,
  buildDynamicSevenGradeScale,
  capitalizeFirst,
  extractApiError,
  formatDateDDMMYYYY,
  getDynamicAcademicYearRange,
  parseLocalDateOnly,
  recalculateMarksheet,
} from "@/utils/result/resultHelpers";
import "./MarksheetPage.css";

const ROWS_PER_PAGE_OPTIONS = [5, 10, 20, 30, 50, 100];

const formatMark = (value) => {
  const num = Number(value);
  if (!Number.isFinite(num)) return "0";
  return Number.isInteger(num) ? String(num) : String(Number(num.toFixed(2)));
};

const formatPercent = (value) => {
  const num = Number(value);
  if (!Number.isFinite(num)) return "0.0%";
  return `${num.toFixed(1)}%`;
};

const MarksheetPage = () => {
  const { theme } = useTheme();
  // Preserve existing app theme polarity where theme === "light" renders the dark visual palette
  const isDark = theme === "light";
  const visualTheme = isDark ? "dark" : "light";

  const idPrefix = useId();
  const internalTabId = `${idPrefix}-tab-internal`;
  const annualTabId = `${idPrefix}-tab-annual`;
  const classFieldId = `${idPrefix}-class`;
  const sectionFieldId = `${idPrefix}-section`;
  const examFieldId = `${idPrefix}-exam`;
  const rowsFieldId = `${idPrefix}-rows`;
  const resultsHeadingId = `${idPrefix}-results-heading`;
  const annualClassFieldId = `${idPrefix}-annual-class`;
  const annualSectionFieldId = `${idPrefix}-annual-section`;
  const annualRowsFieldId = `${idPrefix}-annual-rows`;
  const annualResultsHeadingId = `${idPrefix}-annual-results-heading`;
  const annualExamsHeadingId = `${idPrefix}-annual-exams-heading`;
  const annualMatrixHeadingId = `${idPrefix}-annual-matrix-heading`;
  const detailTitleId = `${idPrefix}-detail-title`;
  const subjectsHeadingId = `${idPrefix}-subjects-heading`;

  const internalTabRef = useRef(null);
  const annualTabRef = useRef(null);
  const detailDialogRef = useRef(null);

  // Mode toggle state: defaults to "internal" to preserve existing behavior
  const [reportMode, setReportMode] = useState("internal");

  const [loading, setLoading] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);
  const schoolId = useSelector((state) => state?.auth?.schoolId);
  const allClass = useSelector((state) => state.class.classNames) || [];
  const allSection = useSelector((state) => state.section.sectionNames) || [];
  const examTypeData = useSelector((state) => state.examType.examType) || [];
  const [gradeSystem, setGradeSystem] = useState([]);
  const [passingMark, setPassingMark] = useState(null);

  const role = useSelector((state) => state.auth.user);
  const userDetails = useSelector((state) => state.auth.userDetails) || {};
  const [signaturePreview, setSignaturePreview] = useState(null);

  const isStudent = role === "student";

  // Internal search state
  const [searchData, setSearchData] = useState({
    className: "",
    section: "",
    examType: "",
  });

  // Internal results state
  const [marksheetData, setMarksheetData] = useState([]);
  const [publishMessage, setPublishMessage] = useState("");
  const [lastSearchedContext, setLastSearchedContext] = useState({
    className: "",
    section: "",
    examType: "",
  });

  // Additional marksheet data
  const [additioinalMarksheetData, setAdditioinalMarksheetData] = useState([]);

  // Annual report card state (isolated from Internal mode)
  const [annualLoading, setAnnualLoading] = useState(false);
  const [annualHasSearched, setAnnualHasSearched] = useState(false);
  const [annualSearchData, setAnnualSearchData] = useState({
    className: "",
    section: "",
  });
  const [annualReport, setAnnualReport] = useState(null);
  const [annualLastContext, setAnnualLastContext] = useState({
    className: "",
    section: "",
  });
  const [annualRowsPerPage, setAnnualRowsPerPage] = useState(5);
  const [annualCurrentPage, setAnnualCurrentPage] = useState(1);

  // Shared native <dialog> View state (Internal or Annual)
  const [viewStudent, setViewStudent] = useState(null);
  const [viewAnnualStudent, setViewAnnualStudent] = useState(null);

  // Internal pagination state
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [currentPage, setCurrentPage] = useState(1);

  const dataLength = marksheetData?.length || 0;
  const totalPages = Math.max(1, Math.ceil(dataLength / rowsPerPage));
  const safeCurrentPage = Math.min(currentPage, totalPages);
  const paginatedData = marksheetData?.slice(
    (safeCurrentPage - 1) * rowsPerPage,
    safeCurrentPage * rowsPerPage
  );
  const rangeStart = dataLength === 0 ? 0 : (safeCurrentPage - 1) * rowsPerPage + 1;
  const rangeEnd = Math.min(safeCurrentPage * rowsPerPage, dataLength);

  const handleRowsPerPageChange = (newRowsPerPage) => {
    setRowsPerPage(newRowsPerPage);
    const newTotalPages = Math.max(1, Math.ceil(dataLength / newRowsPerPage));
    setCurrentPage((prev) => Math.max(1, Math.min(prev, newTotalPages)));
  };

  // Annual pagination derived values
  const annualStudents = useMemo(
    () => (Array.isArray(annualReport?.students) ? annualReport.students : []),
    [annualReport]
  );
  const annualActiveExams = useMemo(
    () => (Array.isArray(annualReport?.activeExams) ? annualReport.activeExams : []),
    [annualReport]
  );
  const annualStudentsLength = annualStudents.length;
  const annualTotalPages = Math.max(1, Math.ceil(annualStudentsLength / annualRowsPerPage));
  const annualSafePage = Math.min(annualCurrentPage, annualTotalPages);
  const paginatedAnnualStudents = annualStudents.slice(
    (annualSafePage - 1) * annualRowsPerPage,
    annualSafePage * annualRowsPerPage
  );
  const annualRangeStart =
    annualStudentsLength === 0 ? 0 : (annualSafePage - 1) * annualRowsPerPage + 1;
  const annualRangeEnd = Math.min(annualSafePage * annualRowsPerPage, annualStudentsLength);

  const handleAnnualRowsPerPageChange = (newRowsPerPage) => {
    setAnnualRowsPerPage(newRowsPerPage);
    const newTotalPages = Math.max(1, Math.ceil(annualStudentsLength / newRowsPerPage));
    setAnnualCurrentPage((prev) => Math.max(1, Math.min(prev, newTotalPages)));
  };

  // Annual class snapshot metrics computed from loaded annual report records
  const annualSummary = useMemo(() => {
    if (!annualStudents.length) {
      return null;
    }
    const total = annualStudents.length;
    let passed = 0;
    let percentageSum = 0;
    let topStudent = annualStudents[0];

    for (const student of annualStudents) {
      if (student.result === "P") passed += 1;
      const pct = Number(student.overallPercentage) || 0;
      percentageSum += pct;

      const topPct = Number(topStudent?.overallPercentage) || 0;
      const studentObt = Number(student.grandTotalObtained) || 0;
      const topObt = Number(topStudent?.grandTotalObtained) || 0;
      if (studentObt > topObt || (studentObt === topObt && pct > topPct)) {
        topStudent = student;
      }
    }

    const failed = total - passed;
    const passRate = (passed / total) * 100;
    const avgPercentage = percentageSum / total;

    return {
      total,
      passed,
      failed,
      passRate,
      avgPercentage,
      topStudent,
    };
  }, [annualStudents]);

  const handleSelectMode = (mode) => {
    setReportMode(mode);
    const targetRef = mode === "internal" ? internalTabRef : annualTabRef;
    targetRef.current?.scrollIntoView?.({
      block: "nearest",
      inline: "nearest",
      container: "nearest",
    });
  };

  // Selection-by-activation keyboard navigation for the report mode tablist
  const handleTabKeyDown = (e, currentTab) => {
    let nextRef = null;
    if (e.key === "ArrowLeft" || e.key === "ArrowRight") {
      nextRef = currentTab === "internal" ? annualTabRef : internalTabRef;
    } else if (e.key === "Home") {
      nextRef = internalTabRef;
    } else if (e.key === "End") {
      nextRef = annualTabRef;
    }
    if (nextRef?.current) {
      e.preventDefault();
      nextRef.current.focus();
      nextRef.current.scrollIntoView?.({
        block: "nearest",
        inline: "nearest",
        container: "nearest",
      });
    }
  };

  // Class snapshot metrics computed strictly from loaded marksheet records
  const classSummary = useMemo(() => {
    if (!Array.isArray(marksheetData) || marksheetData.length === 0) {
      return null;
    }
    const total = marksheetData.length;
    let passed = 0;
    let percentageSum = 0;
    let topStudent = marksheetData[0];

    for (const student of marksheetData) {
      if (student.result === "P") passed += 1;
      const pct = Number(student.overallPercentage) || 0;
      percentageSum += pct;

      const topPct = Number(topStudent?.overallPercentage) || 0;
      const studentMarks = Number(student.totalMarks) || 0;
      const topMarks = Number(topStudent?.totalMarks) || 0;
      if (pct > topPct || (pct === topPct && studentMarks > topMarks)) {
        topStudent = student;
      }
    }

    const failed = total - passed;
    const passRate = (passed / total) * 100;
    const avgPercentage = percentageSum / total;

    return {
      total,
      passed,
      failed,
      passRate,
      avgPercentage,
      topStudent,
    };
  }, [marksheetData]);

  // Headmaster Signature Photo (optional external schoolfeed service)
  useEffect(() => {
    if (!mainUrlApi.headmasterSignaturePhoto?.url || !schoolId) return;
    let isMounted = true;

    const fetchHeadmasterSignature = async () => {
      try {
        const response = await axios.get(
          `${mainUrlApi.headmasterSignaturePhoto.url}/${schoolId}`
        );
        if (
          isMounted &&
          response?.data?.success &&
          response?.data?.data?.photo?.fileUrl
        ) {
          setSignaturePreview(response.data.data.photo.fileUrl);
        }
      } catch (error) {
        if (error?.response?.status !== 404) {
          console.error("Error fetching headmaster signature:", error);
        }
      }
    };

    fetchHeadmasterSignature();
    return () => {
      isMounted = false;
    };
  }, [schoolId]);

  // Native <dialog> open/close synchronization (shared across Internal and Annual View modals)
  const isDialogOpen = Boolean(viewStudent || viewAnnualStudent);
  useEffect(() => {
    const dialog = detailDialogRef.current;
    if (!dialog) return;

    if (isDialogOpen && !dialog.open) {
      dialog.showModal();
    } else if (!isDialogOpen && dialog.open) {
      dialog.close();
    }
  }, [isDialogOpen]);

  // Fallback for browsers without native <dialog closedby="any"> (e.g. Safari)
  useEffect(() => {
    const dialog = detailDialogRef.current;
    if (!dialog || "closedBy" in HTMLDialogElement.prototype) return;

    const isOutside = (e) => {
      const rect = dialog.getBoundingClientRect();
      return (
        e.clientX < rect.left ||
        e.clientX > rect.right ||
        e.clientY < rect.top ||
        e.clientY > rect.bottom
      );
    };

    let pressedOutside = false;
    const handlePointerDown = (e) => {
      pressedOutside = isOutside(e);
    };
    const handlePointerUp = (e) => {
      if (pressedOutside && isOutside(e) && dialog.open) {
        dialog.close();
      }
      pressedOutside = false;
    };

    dialog.addEventListener("pointerdown", handlePointerDown);
    dialog.addEventListener("pointerup", handlePointerUp);
    return () => {
      dialog.removeEventListener("pointerdown", handlePointerDown);
      dialog.removeEventListener("pointerup", handlePointerUp);
    };
  }, []);

  const searchMarkSheet = async (e) => {
    e.preventDefault();
    if (!schoolId) return;

    if (isStudent) {
      if (!searchData.examType) {
        toast.error("Please select an exam type");
        return;
      }
      if (
        !userDetails?.className ||
        !userDetails?.section ||
        userDetails?.rollNo === undefined ||
        userDetails?.rollNo === null ||
        userDetails?.rollNo === ""
      ) {
        toast.error(
          "Student profile details (class, section, roll no) are missing"
        );
        return;
      }
    } else {
      if (!searchData.className || !searchData.section || !searchData.examType) {
        toast.error("Please select class, section, and exam type");
        return;
      }
    }

    try {
      setLoading(true);
      setPublishMessage("");

      const targetClass = isStudent
        ? userDetails.className
        : searchData.className;
      const targetSection = isStudent
        ? userDetails.section
        : searchData.section;
      const targetExam = searchData.examType;

      let dateMsg = null;
      let showMarksheet = true;

      // Check publish date for students: block ONLY when a future date is explicitly configured
      if (isStudent && resultUrlApi.resultDate?.search?.url) {
        try {
          const dateRes = await axios.get(
            `${resultUrlApi.resultDate.search.url}/${schoolId}`,
            {
              params: {
                className: targetClass,
                examType: targetExam,
              },
            }
          );

          const dateRecords = Array.isArray(dateRes?.data?.data)
            ? dateRes.data.data
            : [];
          if (dateRecords.length > 0 && dateRecords[0]?.resultPublishDate) {
            const pDateOnly = parseLocalDateOnly(
              dateRecords[0].resultPublishDate
            );
            const now = new Date();
            const cDateOnly = new Date(
              now.getFullYear(),
              now.getMonth(),
              now.getDate()
            );

            if (pDateOnly && cDateOnly < pDateOnly) {
              showMarksheet = false;
              const diffTime = pDateOnly.getTime() - cDateOnly.getTime();
              const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
              const formattedDate = formatDateDDMMYYYY(
                dateRecords[0].resultPublishDate
              );
              dateMsg = `Results publish on ${formattedDate} (${diffDays} ${
                diffDays === 1 ? "day" : "days"
              } remaining)`;
            }
          }
        } catch {
          // If publish-date service fails or no date configured, allow viewing rather than locking out
          showMarksheet = true;
        }
      }

      if (!showMarksheet) {
        setMarksheetData([]);
        setGradeSystem([]);
        setAdditioinalMarksheetData([]);
        setPublishMessage(dateMsg);
        setLastSearchedContext({
          className: targetClass,
          section: targetSection,
          examType: targetExam,
        });
        setHasSearched(true);
        setCurrentPage(1);
        return;
      }

      const response = isStudent
        ? await axios.get(`${resultUrlApi.marksheet.url}/student/${schoolId}`, {
            params: {
              className: targetClass,
              section: targetSection,
              examType: targetExam,
              roll: Number(userDetails.rollNo),
            },
          })
        : await axios.get(
            `${resultUrlApi.marksheet.url}/class-section/${schoolId}`,
            {
              params: {
                className: targetClass,
                section: targetSection,
                examType: targetExam,
              },
            }
          );

      // Fetch optional additional subject marks
      if (resultUrlApi.additionalMarkRegister?.url) {
        try {
          const additionalResponse = await axios.get(
            `${resultUrlApi.additionalMarkRegister.url}/additionalMarksheetData`,
            {
              params: {
                schoolId,
                className: targetClass,
                section: targetSection,
                examType: targetExam,
              },
            }
          );
          if (additionalResponse?.data?.success) {
            setAdditioinalMarksheetData(
              additionalResponse?.data?.data?.markSheets || []
            );
          } else {
            setAdditioinalMarksheetData([]);
          }
        } catch {
          setAdditioinalMarksheetData([]);
        }
      } else {
        setAdditioinalMarksheetData([]);
      }

      setLastSearchedContext({
        className: targetClass,
        section: targetSection,
        examType: targetExam,
      });
      setHasSearched(true);

      if (response?.data?.data?.markSheets) {
        const scale = response.data.data.gradeScale || [];
        const criteriaPassingMark = response.data.data.passingMark ?? null;
        const normalized = response.data.data.markSheets.map((student) =>
          recalculateMarksheet(student, scale, criteriaPassingMark)
        );
        setMarksheetData(normalized);
        setGradeSystem(scale);
        setPassingMark(criteriaPassingMark);
        setCurrentPage(1);
        toast.success("Marksheet data retrieved");
      } else {
        setMarksheetData([]);
        setGradeSystem([]);
        setPassingMark(null);
        setCurrentPage(1);
        toast.info("No marksheet data found");
      }
    } catch (error) {
      toast.error(extractApiError(error, "Error searching marksheet data"));
      setMarksheetData([]);
      setAdditioinalMarksheetData([]);
      setHasSearched(true);
      setCurrentPage(1);
    } finally {
      setLoading(false);
    }
  };

  const fetchAnnualReportCard = useCallback(
    async ({ classNameOverride, sectionOverride, showSuccessToast = true } = {}) => {
      if (!schoolId) return;

      if (isStudent) {
        if (
          !userDetails?.className ||
          !userDetails?.section ||
          userDetails?.rollNo === undefined ||
          userDetails?.rollNo === null ||
          userDetails?.rollNo === ""
        ) {
          toast.error(
            "Student profile details (class, section, roll no) are missing"
          );
          setAnnualHasSearched(true);
          return;
        }
      } else {
        const cls = classNameOverride ?? annualSearchData.className;
        const sec = sectionOverride ?? annualSearchData.section;
        if (!cls || !sec) {
          toast.error("Please select class and section");
          return;
        }
      }

      const targetClass = isStudent
        ? userDetails.className
        : classNameOverride ?? annualSearchData.className;
      const targetSection = isStudent
        ? userDetails.section
        : sectionOverride ?? annualSearchData.section;

      try {
        setAnnualLoading(true);
        const response = isStudent
          ? await axios.get(
              `${resultUrlApi.annualMarksheet.url}/student/${schoolId}`,
              {
                params: {
                  className: targetClass,
                  section: targetSection,
                  roll: Number(userDetails.rollNo),
                },
              }
            )
          : await axios.get(`${resultUrlApi.annualMarksheet.url}/${schoolId}`, {
              params: {
                className: targetClass,
                section: targetSection,
              },
            });

        const payload = response?.data?.data || null;
        setAnnualLastContext({
          className: targetClass,
          section: targetSection,
        });
        setAnnualHasSearched(true);
        setAnnualCurrentPage(1);

        if (payload && Array.isArray(payload.students) && payload.students.length > 0) {
          setAnnualReport(payload);
          if (showSuccessToast) {
            toast.success("Annual report card data retrieved");
          }
        } else {
          setAnnualReport(null);
          toast.info("No populated exam data found for annual report card");
        }
      } catch (error) {
        toast.error(
          extractApiError(error, "Error loading annual report card data")
        );
        setAnnualReport(null);
        setAnnualLastContext({
          className: targetClass,
          section: targetSection,
        });
        setAnnualHasSearched(true);
        setAnnualCurrentPage(1);
      } finally {
        setAnnualLoading(false);
      }
    },
    [
      schoolId,
      isStudent,
      userDetails?.className,
      userDetails?.section,
      userDetails?.rollNo,
      annualSearchData.className,
      annualSearchData.section,
    ]
  );

  const handleSearchAnnual = (e) => {
    e.preventDefault();
    fetchAnnualReportCard();
  };

  const hasStudentProfile = Boolean(
    userDetails?.className &&
      userDetails?.section &&
      userDetails?.rollNo !== undefined &&
      userDetails?.rollNo !== null &&
      userDetails?.rollNo !== ""
  );

  // Reset cached report state if school changes across sessions
  useEffect(() => {
    setAnnualReport(null);
    setAnnualHasSearched(false);
    setAnnualCurrentPage(1);
  }, [schoolId]);

  // Auto-load Student's Annual Report Card on first switch into Annual mode once profile is hydrated
  useEffect(() => {
    if (
      reportMode === "annual" &&
      isStudent &&
      schoolId &&
      hasStudentProfile &&
      !annualHasSearched &&
      !annualLoading
    ) {
      fetchAnnualReportCard({ showSuccessToast: false });
    }
  }, [
    reportMode,
    isStudent,
    schoolId,
    hasStudentProfile,
    annualHasSearched,
    annualLoading,
    fetchAnnualReportCard,
  ]);

  // View student details (merges additional subjects if present)
  const handleViewStudent = (student) => {
    const additionalData = additioinalMarksheetData.find(
      (item) => Number(item.roll) === Number(student.roll)
    );

    const merged =
      additionalData?.subjects?.length > 0
        ? {
            ...student,
            subjects: [...student.subjects, ...additionalData.subjects],
          }
        : student;

    const studentData = recalculateMarksheet(merged, gradeSystem, passingMark);
    setViewAnnualStudent(null);
    setViewStudent(studentData);
  };

  const handleViewAnnualStudent = (student) => {
    setViewStudent(null);
    setViewAnnualStudent(student);
  };

  const handleCloseView = () => {
    const dialog = detailDialogRef.current;
    if (dialog?.open) {
      dialog.close();
    } else {
      setViewStudent(null);
      setViewAnnualStudent(null);
    }
  };

  const handleDialogClose = () => {
    const dialog = detailDialogRef.current;
    if (dialog) {
      dialog.returnValue = "";
    }
    setViewStudent(null);
    setViewAnnualStudent(null);
  };

  const displayGradeScale =
    gradeSystem.length > 0 ? gradeSystem : DEFAULT_GRADE_SCALE;

  const annualDynamicScale = useMemo(() => {
    if (
      Array.isArray(annualReport?.dynamicGradeScale) &&
      annualReport.dynamicGradeScale.length === 7
    ) {
      return annualReport.dynamicGradeScale;
    }
    if (!annualReport) return [];
    return buildDynamicSevenGradeScale(
      annualReport.gradeScale || [],
      Number(annualReport.totalAnnualFullMarks) || 0
    );
  }, [annualReport]);

  const resolvedAnnualYearRange =
    annualReport?.academicYearRange || getDynamicAcademicYearRange();

  const selectTriggerClasses =
    "ms-control w-full border border-[var(--ms-border-strong)] bg-[var(--ms-surface)] text-[var(--ms-text-primary)] placeholder:text-[var(--ms-text-muted)] focus:ring-0 focus-visible:outline-none";

  const selectContentClasses =
    "marksheet-portal rounded-xl border border-[var(--ms-border-strong)] bg-[var(--ms-surface)] text-[var(--ms-text-primary)] shadow-[var(--ms-shadow-floating)]";

  const selectItemClasses =
    "rounded-md text-sm text-[var(--ms-text-primary)] focus:bg-[var(--ms-surface-raised)] focus:text-[var(--ms-text-primary)]";

  return (
    <div className="marksheet-scope" data-theme={visualTheme}>
      <div className="mx-auto w-full min-w-0 max-w-[1440px] px-4 pb-24 pt-6 sm:px-6 sm:pb-20 sm:pt-8 lg:px-8">
        {/* Page Header + Gradient-Matched Report Mode Toggle */}
        <header className="mb-6 flex min-w-0 flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
          <div className="flex min-w-0 flex-col gap-2">
            <div className="flex flex-col gap-2">
              <h1 className="ms-page-title">Student marksheets</h1>
              <span className="ms-signature-rule" aria-hidden="true" />
            </div>
            <p className="max-w-2xl text-sm text-[var(--ms-text-secondary)]">
              {reportMode === "annual"
                ? isStudent
                  ? "View your dynamic annual report card aggregated across completed exams"
                  : "Generate dynamic annual report cards aggregated across all completed internal exams"
                : isStudent
                ? "View your exam results, subject breakdown, and official report card"
                : "Look up class results, inspect subject marks, and export official report cards"}
            </p>
          </div>

          <div
            role="tablist"
            aria-label="Report card mode"
            className="ms-mode-tablist self-start lg:self-end"
          >
            <button
              ref={internalTabRef}
              id={internalTabId}
              type="button"
              role="tab"
              aria-selected={reportMode === "internal"}
              tabIndex={reportMode === "internal" ? 0 : -1}
              onClick={() => handleSelectMode("internal")}
              onKeyDown={(e) => handleTabKeyDown(e, "internal")}
              className="ms-mode-tab"
            >
              Internal Exam Report
            </button>
            <button
              ref={annualTabRef}
              id={annualTabId}
              type="button"
              role="tab"
              aria-selected={reportMode === "annual"}
              tabIndex={reportMode === "annual" ? 0 : -1}
              onClick={() => handleSelectMode("annual")}
              onKeyDown={(e) => handleTabKeyDown(e, "annual")}
              className="ms-mode-tab"
            >
              Annual Report Card
            </button>
          </div>
        </header>

        {/* Internal Exam Report Panel (Default) */}
        <div
          role="tabpanel"
          aria-labelledby={internalTabId}
          hidden={reportMode !== "internal"}
        >
        {/* Filter Bar Card */}
        <section
          aria-label="Filter marksheets"
          className="ms-card mb-6 p-4 sm:p-5"
        >
          <form
            onSubmit={searchMarkSheet}
            className={`grid grid-cols-1 items-end gap-4 ${
              isStudent
                ? "sm:grid-cols-[minmax(0,1fr)_auto]"
                : "sm:grid-cols-2 lg:grid-cols-[repeat(3,minmax(0,1fr))_auto]"
            }`}
          >
            {!isStudent && (
              <div className="min-w-0">
                <label
                  htmlFor={classFieldId}
                  className="mb-2 block text-xs font-medium text-[var(--ms-text-secondary)]"
                >
                  Class
                </label>
                <Select
                  value={searchData.className}
                  onValueChange={(value) =>
                    setSearchData({ ...searchData, className: value })
                  }
                  required
                >
                  <SelectTrigger
                    id={classFieldId}
                    className={selectTriggerClasses}
                  >
                    <SelectValue placeholder="Select class" />
                  </SelectTrigger>
                  <SelectContent
                    data-theme={visualTheme}
                    className={selectContentClasses}
                  >
                    <SelectGroup>
                      <SelectLabel className="text-xs font-medium text-[var(--ms-text-muted)]">
                        Class
                      </SelectLabel>
                      {allClass.map((item, index) => (
                        <SelectItem
                          key={index}
                          value={item}
                          className={selectItemClasses}
                        >
                          {capitalizeFirst(item)}
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>
            )}

            {!isStudent && (
              <div className="min-w-0">
                <label
                  htmlFor={sectionFieldId}
                  className="mb-2 block text-xs font-medium text-[var(--ms-text-secondary)]"
                >
                  Section
                </label>
                <Select
                  value={searchData.section}
                  onValueChange={(value) =>
                    setSearchData({ ...searchData, section: value })
                  }
                  required
                >
                  <SelectTrigger
                    id={sectionFieldId}
                    className={selectTriggerClasses}
                  >
                    <SelectValue placeholder="Select section" />
                  </SelectTrigger>
                  <SelectContent
                    data-theme={visualTheme}
                    className={selectContentClasses}
                  >
                    <SelectGroup>
                      <SelectLabel className="text-xs font-medium text-[var(--ms-text-muted)]">
                        Section
                      </SelectLabel>
                      {allSection.map((item, index) => (
                        <SelectItem
                          key={index}
                          value={item}
                          className={selectItemClasses}
                        >
                          {capitalizeFirst(item)}
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>
            )}

            <div className="min-w-0">
              <label
                htmlFor={examFieldId}
                className="mb-2 block text-xs font-medium text-[var(--ms-text-secondary)]"
              >
                Exam
              </label>
              <Select
                value={searchData.examType}
                onValueChange={(value) =>
                  setSearchData({ ...searchData, examType: value })
                }
                required
              >
                <SelectTrigger id={examFieldId} className={selectTriggerClasses}>
                  <SelectValue placeholder="Select exam" />
                </SelectTrigger>
                <SelectContent
                  data-theme={visualTheme}
                  className={selectContentClasses}
                >
                  <SelectGroup>
                    <SelectLabel className="text-xs font-medium text-[var(--ms-text-muted)]">
                      Exam
                    </SelectLabel>
                    {examTypeData?.map((item, index) => (
                      <SelectItem
                        key={index}
                        value={item.examTypeName}
                        className={selectItemClasses}
                      >
                        {capitalizeFirst(item.examTypeName)}
                      </SelectItem>
                    ))}
                  </SelectGroup>
                </SelectContent>
              </Select>
            </div>

            <div>
              <button
                type="submit"
                disabled={loading}
                className={`ms-control ms-btn-primary inline-flex w-full items-center justify-center gap-2 px-5 text-sm disabled:cursor-not-allowed disabled:opacity-50 ${
                  isStudent
                    ? "sm:w-auto sm:min-w-[136px]"
                    : "lg:w-auto lg:min-w-[136px]"
                }`}
              >
                {loading ? (
                  <>
                    <Loader2
                      className="h-4 w-4 animate-spin"
                      strokeWidth={1.5}
                      aria-hidden="true"
                    />
                    <span>Searching</span>
                  </>
                ) : (
                  <>
                    <Search
                      className="h-4 w-4"
                      strokeWidth={1.5}
                      aria-hidden="true"
                    />
                    <span>Search</span>
                  </>
                )}
              </button>
            </div>
          </form>
        </section>

        {/* Craft Moment #1: Class Result Snapshot Strip (non-student, populated results) */}
        {!isStudent && !loading && !publishMessage && classSummary && (
          <section
            aria-label="Class result summary"
            className="mb-6 grid grid-cols-2 gap-3 sm:gap-4 lg:grid-cols-4"
          >
            <div className="ms-card flex flex-col justify-between p-4 sm:p-5">
              <span className="text-xs text-[var(--ms-text-muted)]">
                Students
              </span>
              <p className="ms-num mt-2 text-2xl font-medium leading-none text-[var(--ms-text-primary)] sm:text-[28px]">
                {classSummary.total}
              </p>
              <span className="ms-num mt-2 text-xs text-[var(--ms-text-secondary)]">
                {classSummary.passed} passed · {classSummary.failed} failed
              </span>
            </div>

            <div className="ms-card flex flex-col justify-between p-4 sm:p-5">
              <span className="text-xs text-[var(--ms-text-muted)]">
                Pass rate
              </span>
              <p className="ms-num mt-2 text-2xl font-medium leading-none text-[var(--ms-text-primary)] sm:text-[28px]">
                {formatPercent(classSummary.passRate)}
              </p>
              <div
                className="mt-3 h-1 w-full overflow-hidden rounded-full bg-[var(--ms-surface-raised)]"
                role="progressbar"
                aria-label="Class pass rate"
                aria-valuenow={Math.round(classSummary.passRate)}
                aria-valuemin={0}
                aria-valuemax={100}
              >
                <div
                  className="h-full rounded-full bg-[var(--ms-positive)]"
                  style={{ width: `${Math.min(100, Math.max(0, classSummary.passRate))}%` }}
                />
              </div>
            </div>

            <div className="ms-card flex flex-col justify-between p-4 sm:p-5">
              <span className="text-xs text-[var(--ms-text-muted)]">
                Class average
              </span>
              <p className="ms-num mt-2 text-2xl font-medium leading-none text-[var(--ms-text-primary)] sm:text-[28px]">
                {formatPercent(classSummary.avgPercentage)}
              </p>
              <span className="mt-2 text-xs text-[var(--ms-text-secondary)]">
                Across loaded marksheets
              </span>
            </div>

            <div className="ms-card flex flex-col justify-between p-4 sm:p-5">
              <span className="text-xs text-[var(--ms-text-muted)]">
                Highest score
              </span>
              <p className="ms-num mt-2 text-2xl font-medium leading-none text-[var(--ms-text-primary)] sm:text-[28px]">
                {formatPercent(classSummary.topStudent?.overallPercentage)}
              </p>
              <span className="ms-num mt-2 truncate text-xs text-[var(--ms-text-secondary)]">
                {formatMark(classSummary.topStudent?.totalMarks)} /{" "}
                {formatMark(classSummary.topStudent?.totalFullMarks)} ·{" "}
                {classSummary.topStudent?.name || "Student"}
              </span>
            </div>
          </section>
        )}

        {/* Results Card */}
        <section
          aria-labelledby={resultsHeadingId}
          className="ms-card overflow-hidden"
        >
          {/* Card Header */}
          <div className="flex flex-col items-start justify-between gap-3 border-b border-[var(--ms-border)] p-4 sm:flex-row sm:items-center sm:p-5">
            <div className="min-w-0">
              <h2
                id={resultsHeadingId}
                className="text-base font-medium text-[var(--ms-text-primary)] sm:text-lg"
              >
                Marksheet results
              </h2>
              <p className="ms-num mt-1 text-xs text-[var(--ms-text-muted)]">
                {loading
                  ? "Fetching records"
                  : !hasSearched
                  ? "Select filters above to view records"
                  : `${dataLength} ${dataLength === 1 ? "record" : "records"}`}
              </p>
            </div>

            {hasSearched && lastSearchedContext.examType && (
              <div className="ms-surface-raised flex flex-wrap items-center gap-x-3 gap-y-1 rounded-lg px-3 py-2 text-xs text-[var(--ms-text-secondary)]">
                <span>
                  Class{" "}
                  <span className="font-medium text-[var(--ms-text-primary)]">
                    {lastSearchedContext.className} ({lastSearchedContext.section})
                  </span>
                </span>
                <span aria-hidden="true" className="text-[var(--ms-text-muted)]">
                  ·
                </span>
                <span>
                  Exam{" "}
                  <span className="font-medium text-[var(--ms-text-primary)]">
                    {lastSearchedContext.examType}
                  </span>
                </span>
              </div>
            )}
          </div>

          {/* Loading / Publish Gate / Empty / Ledger Table */}
          {loading ? (
            <div className="flex flex-col items-center justify-center gap-3 px-4 py-16 text-center">
              <Loader2
                className="h-5 w-5 animate-spin text-[var(--ms-text-muted)]"
                strokeWidth={1.5}
                aria-hidden="true"
              />
              <p className="text-sm text-[var(--ms-text-secondary)]">
                Loading marksheets
              </p>
            </div>
          ) : publishMessage ? (
            <div className="flex flex-col items-center justify-center gap-3 px-4 py-16 text-center">
              <div className="ms-surface-raised flex h-10 w-10 items-center justify-center rounded-xl">
                <Clock
                  className="h-5 w-5 text-[var(--ms-text-secondary)]"
                  strokeWidth={1.5}
                  aria-hidden="true"
                />
              </div>
              <p className="max-w-md text-base font-medium text-[var(--ms-text-primary)]">
                {publishMessage}
              </p>
              <p className="max-w-md text-xs text-[var(--ms-text-muted)]">
                Your marksheet will appear here automatically on the publish date
              </p>
            </div>
          ) : dataLength === 0 ? (
            <div className="flex flex-col items-center justify-center gap-3 px-4 py-16 text-center">
              <div className="ms-surface-raised flex h-10 w-10 items-center justify-center rounded-xl">
                <FileText
                  className="h-5 w-5 text-[var(--ms-text-muted)]"
                  strokeWidth={1.5}
                  aria-hidden="true"
                />
              </div>
              <p className="text-sm font-medium text-[var(--ms-text-primary)]">
                {hasSearched
                  ? "No marksheets found"
                  : "No marksheets loaded yet"}
              </p>
              <p className="max-w-md text-xs text-[var(--ms-text-muted)]">
                {hasSearched
                  ? `No records matched Class ${lastSearchedContext.className} (${lastSearchedContext.section}) · ${lastSearchedContext.examType}`
                  : isStudent
                  ? "Choose an exam above to load your report card"
                  : "Choose a class, section, and exam above to load student marksheets"}
              </p>
            </div>
          ) : (
            <div
              tabIndex={0}
              role="region"
              aria-labelledby={resultsHeadingId}
              className="ms-table-region"
            >
              <table className="ms-ledger-table">
                <thead>
                  <tr>
                    <th scope="col" className="w-12 text-left">
                      #
                    </th>
                    <th scope="col" className="text-right">
                      Roll
                    </th>
                    <th scope="col" className="text-left">
                      Student
                    </th>
                    <th scope="col" className="text-left">
                      Class
                    </th>
                    <th scope="col" className="text-left">
                      Section
                    </th>
                    <th scope="col" className="text-right">
                      Marks
                    </th>
                    <th scope="col" className="text-right">
                      Grade
                    </th>
                    <th scope="col" className="text-right">
                      Result
                    </th>
                    <th scope="col" className="text-right">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {paginatedData?.map((item, index) => {
                    const rowNumber =
                      (safeCurrentPage - 1) * rowsPerPage + index + 1;
                    const isPass = item.result === "P";
                    return (
                      <tr key={`${item.roll}-${index}`}>
                        <td className="ms-col-sno ms-mono text-xs text-[var(--ms-text-muted)]">
                          {rowNumber}
                        </td>
                        <td className="ms-col-roll ms-mono whitespace-nowrap text-right text-sm text-[var(--ms-text-secondary)]">
                          {item.roll}
                        </td>
                        <th
                          scope="row"
                          className="ms-col-student text-left font-medium text-[var(--ms-text-primary)]"
                        >
                          <div className="flex flex-wrap items-baseline justify-between gap-2">
                            <span className="capitalize">{item.name}</span>
                            <span className="ms-mobile-roll-meta ms-mono text-xs font-normal text-[var(--ms-text-muted)]">
                              Roll {item.roll}
                            </span>
                          </div>
                        </th>
                        <td
                          data-label="Class"
                          style={{ "--col-label": '"Class"' }}
                          className="whitespace-nowrap text-sm text-[var(--ms-text-secondary)]"
                        >
                          {item.class}
                        </td>
                        <td
                          data-label="Section"
                          style={{ "--col-label": '"Section"' }}
                          className="whitespace-nowrap text-sm text-[var(--ms-text-secondary)]"
                        >
                          {item.section}
                        </td>
                        <td
                          data-label="Marks"
                          style={{ "--col-label": '"Marks"' }}
                          className="ms-col-marks ms-num whitespace-nowrap text-right"
                        >
                          <span className="font-medium text-[var(--ms-text-primary)]">
                            {formatMark(item.totalMarks)}
                            {item.totalFullMarks
                              ? ` / ${formatMark(item.totalFullMarks)}`
                              : ""}
                          </span>
                          {item.overallPercentage !== undefined && (
                            <span className="ms-mono ml-2 text-xs text-[var(--ms-text-muted)]">
                              {formatPercent(item.overallPercentage)}
                            </span>
                          )}
                        </td>
                        <td
                          data-label="Grade"
                          style={{ "--col-label": '"Grade"' }}
                          className="whitespace-nowrap text-right"
                        >
                          <span
                            className="ms-grade-tag ms-mono"
                            data-fail={!isPass ? "true" : "false"}
                          >
                            {item.overallGrade || "—"}
                          </span>
                        </td>
                        <td
                          data-label="Result"
                          style={{ "--col-label": '"Result"' }}
                          className="whitespace-nowrap text-right"
                        >
                          <span
                            className="ms-status-pill"
                            data-status={isPass ? "pass" : "fail"}
                          >
                            {isPass ? "Pass" : "Fail"}
                          </span>
                        </td>
                        <td className="ms-col-actions whitespace-nowrap text-right">
                          <div className="ms-actions-group inline-flex items-center justify-end gap-2">
                            <button
                              type="button"
                              onClick={() => handleViewStudent(item)}
                              className="ms-btn-icon ms-btn-secondary inline-flex items-center justify-center gap-2 px-2 text-sm"
                              aria-label={`View marksheet details for ${item.name}`}
                            >
                              <Eye
                                className="h-4 w-4 shrink-0 text-[var(--ms-text-secondary)]"
                                strokeWidth={1.5}
                                aria-hidden="true"
                              />
                              <span className="ms-btn-mobile-label">View</span>
                            </button>

                            <NewMarksheetComponent
                              marksheetData={item}
                              gradeSystem={gradeSystem}
                              passingMark={passingMark}
                              signaturePreview={signaturePreview}
                              additionalMarksheetData={additioinalMarksheetData}
                            />
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}

          {/* Local Responsive Pagination Bar (strictly scoped to /marksheet) */}
          {dataLength > 0 && !loading && !publishMessage && (
            <div className="flex flex-col gap-3 border-t border-[var(--ms-border)] px-4 py-4 sm:flex-row sm:items-center sm:justify-between sm:px-5">
              <div className="flex flex-wrap items-center gap-3 text-xs text-[var(--ms-text-secondary)]">
                <div className="flex items-center gap-2">
                  <label
                    htmlFor={rowsFieldId}
                    className="text-xs text-[var(--ms-text-muted)]"
                  >
                    Rows
                  </label>
                  <select
                    id={rowsFieldId}
                    value={rowsPerPage}
                    onChange={(e) =>
                      handleRowsPerPageChange(Number(e.target.value))
                    }
                    className="ms-control ms-num border border-[var(--ms-border-strong)] bg-[var(--ms-surface)] px-2 text-xs text-[var(--ms-text-primary)]"
                  >
                    {ROWS_PER_PAGE_OPTIONS.map((option) => (
                      <option key={option} value={option}>
                        {option}
                      </option>
                    ))}
                  </select>
                </div>

                <span className="ms-num text-xs text-[var(--ms-text-muted)]">
                  Showing {rangeStart}–{rangeEnd} of {dataLength}
                </span>
              </div>

              <div className="flex items-center justify-between gap-3 sm:justify-end">
                <span className="ms-num text-xs text-[var(--ms-text-secondary)]">
                  Page{" "}
                  <span className="font-medium text-[var(--ms-text-primary)]">
                    {safeCurrentPage}
                  </span>{" "}
                  of {totalPages}
                </span>

                <div className="flex items-center gap-2">
                  <button
                    type="button"
                    disabled={safeCurrentPage <= 1}
                    onClick={() =>
                      setCurrentPage((prev) => Math.max(1, prev - 1))
                    }
                    className="ms-control ms-btn-secondary inline-flex items-center justify-center gap-1 px-3 text-xs disabled:cursor-not-allowed disabled:opacity-40"
                    aria-label="Previous page"
                  >
                    <ChevronLeft
                      className="h-4 w-4"
                      strokeWidth={1.5}
                      aria-hidden="true"
                    />
                    <span className="hidden sm:inline">Previous</span>
                  </button>
                  <button
                    type="button"
                    disabled={safeCurrentPage >= totalPages}
                    onClick={() =>
                      setCurrentPage((prev) => Math.min(totalPages, prev + 1))
                    }
                    className="ms-control ms-btn-secondary inline-flex items-center justify-center gap-1 px-3 text-xs disabled:cursor-not-allowed disabled:opacity-40"
                    aria-label="Next page"
                  >
                    <span className="hidden sm:inline">Next</span>
                    <ChevronRight
                      className="h-4 w-4"
                      strokeWidth={1.5}
                      aria-hidden="true"
                    />
                  </button>
                </div>
              </div>
            </div>
          )}
        </section>
        </div>

        {/* Annual Report Card Panel (Dynamic N-Exam Workspace) */}
        <div
          role="tabpanel"
          aria-labelledby={annualTabId}
          hidden={reportMode !== "annual"}
        >
          {/* Annual Filter Bar Card (Class + Section only for Staff/Admin; Reload bar for Student) */}
          <section
            aria-label="Filter annual report cards"
            className="ms-card mb-6 p-4 sm:p-5"
          >
            {isStudent ? (
              <div className="flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center">
                <div className="min-w-0">
                  <p className="text-sm font-medium text-[var(--ms-text-primary)]">
                    Annual Report Card · Academic Performance {resolvedAnnualYearRange}
                  </p>
                  <p className="ms-num mt-1 text-xs text-[var(--ms-text-secondary)]">
                    {userDetails?.className && userDetails?.section
                      ? `Class ${userDetails.className} (${userDetails.section}) · Roll ${userDetails.rollNo ?? "—"}`
                      : "Student class, section, and roll details are required to load your annual report card"}
                  </p>
                </div>
                <button
                  type="button"
                  disabled={annualLoading}
                  onClick={() => fetchAnnualReportCard()}
                  className="ms-control ms-btn-primary inline-flex w-full items-center justify-center gap-2 px-5 text-sm disabled:cursor-not-allowed disabled:opacity-50 sm:w-auto sm:min-w-[136px]"
                >
                  {annualLoading ? (
                    <>
                      <Loader2
                        className="h-4 w-4 animate-spin"
                        strokeWidth={1.5}
                        aria-hidden="true"
                      />
                      <span>Loading</span>
                    </>
                  ) : (
                    <>
                      <RefreshCw
                        className="h-4 w-4"
                        strokeWidth={1.5}
                        aria-hidden="true"
                      />
                      <span>Reload</span>
                    </>
                  )}
                </button>
              </div>
            ) : (
              <form
                onSubmit={handleSearchAnnual}
                className="grid grid-cols-1 items-end gap-4 sm:grid-cols-2 lg:grid-cols-[repeat(2,minmax(0,1fr))_auto]"
              >
                <div className="min-w-0">
                  <label
                    htmlFor={annualClassFieldId}
                    className="mb-2 block text-xs font-medium text-[var(--ms-text-secondary)]"
                  >
                    Class
                  </label>
                  <Select
                    value={annualSearchData.className}
                    onValueChange={(value) =>
                      setAnnualSearchData((prev) => ({ ...prev, className: value }))
                    }
                    required
                  >
                    <SelectTrigger
                      id={annualClassFieldId}
                      className={selectTriggerClasses}
                    >
                      <SelectValue placeholder="Select class" />
                    </SelectTrigger>
                    <SelectContent
                      data-theme={visualTheme}
                      className={selectContentClasses}
                    >
                      <SelectGroup>
                        <SelectLabel className="text-xs font-medium text-[var(--ms-text-muted)]">
                          Class
                        </SelectLabel>
                        {allClass.map((item, index) => (
                          <SelectItem
                            key={index}
                            value={item}
                            className={selectItemClasses}
                          >
                            {capitalizeFirst(item)}
                          </SelectItem>
                        ))}
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                </div>

                <div className="min-w-0">
                  <label
                    htmlFor={annualSectionFieldId}
                    className="mb-2 block text-xs font-medium text-[var(--ms-text-secondary)]"
                  >
                    Section
                  </label>
                  <Select
                    value={annualSearchData.section}
                    onValueChange={(value) =>
                      setAnnualSearchData((prev) => ({ ...prev, section: value }))
                    }
                    required
                  >
                    <SelectTrigger
                      id={annualSectionFieldId}
                      className={selectTriggerClasses}
                    >
                      <SelectValue placeholder="Select section" />
                    </SelectTrigger>
                    <SelectContent
                      data-theme={visualTheme}
                      className={selectContentClasses}
                    >
                      <SelectGroup>
                        <SelectLabel className="text-xs font-medium text-[var(--ms-text-muted)]">
                          Section
                        </SelectLabel>
                        {allSection.map((item, index) => (
                          <SelectItem
                            key={index}
                            value={item}
                            className={selectItemClasses}
                          >
                            {capitalizeFirst(item)}
                          </SelectItem>
                        ))}
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                </div>

                <div>
                  <button
                    type="submit"
                    disabled={annualLoading}
                    className="ms-control ms-btn-primary inline-flex w-full items-center justify-center gap-2 px-5 text-sm disabled:cursor-not-allowed disabled:opacity-50 lg:w-auto lg:min-w-[136px]"
                  >
                    {annualLoading ? (
                      <>
                        <Loader2
                          className="h-4 w-4 animate-spin"
                          strokeWidth={1.5}
                          aria-hidden="true"
                        />
                        <span>Searching</span>
                      </>
                    ) : (
                      <>
                        <Search
                          className="h-4 w-4"
                          strokeWidth={1.5}
                          aria-hidden="true"
                        />
                        <span>Search</span>
                      </>
                    )}
                  </button>
                </div>
              </form>
            )}
          </section>

          {/* Annual Class Snapshot KPI Strip (non-student, populated results) */}
          {!isStudent && !annualLoading && annualSummary && (
            <section
              aria-label="Annual class result summary"
              className="mb-6 grid grid-cols-2 gap-3 sm:gap-4 lg:grid-cols-4"
            >
              <div className="ms-card flex flex-col justify-between p-4 sm:p-5">
                <span className="text-xs text-[var(--ms-text-muted)]">
                  Students
                </span>
                <p className="ms-num mt-2 text-2xl font-medium leading-none text-[var(--ms-text-primary)] sm:text-[28px]">
                  {annualSummary.total}
                </p>
                <span className="ms-num mt-2 text-xs text-[var(--ms-text-secondary)]">
                  {annualSummary.passed} passed · {annualSummary.failed} failed
                </span>
              </div>

              <div className="ms-card flex flex-col justify-between p-4 sm:p-5">
                <span className="text-xs text-[var(--ms-text-muted)]">
                  Pass rate
                </span>
                <p className="ms-num mt-2 text-2xl font-medium leading-none text-[var(--ms-text-primary)] sm:text-[28px]">
                  {formatPercent(annualSummary.passRate)}
                </p>
                <div
                  className="mt-3 h-1 w-full overflow-hidden rounded-full bg-[var(--ms-surface-raised)]"
                  role="progressbar"
                  aria-label="Annual class pass rate"
                  aria-valuenow={Math.round(annualSummary.passRate)}
                  aria-valuemin={0}
                  aria-valuemax={100}
                >
                  <div
                    className="h-full rounded-full bg-[var(--ms-positive)]"
                    style={{
                      width: `${Math.min(100, Math.max(0, annualSummary.passRate))}%`,
                    }}
                  />
                </div>
              </div>

              <div className="ms-card flex flex-col justify-between p-4 sm:p-5">
                <span className="text-xs text-[var(--ms-text-muted)]">
                  Class average
                </span>
                <p className="ms-num mt-2 text-2xl font-medium leading-none text-[var(--ms-text-primary)] sm:text-[28px]">
                  {formatPercent(annualSummary.avgPercentage)}
                </p>
                <span className="mt-2 text-xs text-[var(--ms-text-secondary)]">
                  Across {annualActiveExams.length} completed{" "}
                  {annualActiveExams.length === 1 ? "exam" : "exams"}
                </span>
              </div>

              <div className="ms-card flex flex-col justify-between p-4 sm:p-5">
                <span className="text-xs text-[var(--ms-text-muted)]">
                  Highest score
                </span>
                <p className="ms-num mt-2 text-2xl font-medium leading-none text-[var(--ms-text-primary)] sm:text-[28px]">
                  {formatPercent(annualSummary.topStudent?.overallPercentage)}
                </p>
                <span className="ms-num mt-2 truncate text-xs text-[var(--ms-text-secondary)]">
                  {formatMark(annualSummary.topStudent?.grandTotalObtained)} /{" "}
                  {formatMark(annualSummary.topStudent?.grandTotalFullMarks)} ·{" "}
                  {annualSummary.topStudent?.studentName || "Student"}
                </span>
              </div>
            </section>
          )}

          {/* Annual Results Card */}
          <section
            aria-labelledby={annualResultsHeadingId}
            className="ms-card overflow-hidden"
          >
            {/* Card Header */}
            <div className="flex flex-col items-start justify-between gap-3 border-b border-[var(--ms-border)] p-4 sm:flex-row sm:items-center sm:p-5">
              <div className="min-w-0">
                <h2
                  id={annualResultsHeadingId}
                  className="text-base font-medium text-[var(--ms-text-primary)] sm:text-lg"
                >
                  Annual report cards
                </h2>
                <p className="ms-num mt-1 text-xs text-[var(--ms-text-muted)]">
                  {annualLoading
                    ? "Fetching completed exam records"
                    : !annualHasSearched
                    ? isStudent
                      ? "Loading your annual report card"
                      : "Select class and section above to generate annual report cards"
                    : `${annualStudentsLength} ${
                        annualStudentsLength === 1 ? "student" : "students"
                      } · ${annualActiveExams.length} completed ${
                        annualActiveExams.length === 1 ? "exam" : "exams"
                      }`}
                </p>
              </div>

              {annualReport && (
                <div className="ms-surface-raised flex flex-wrap items-center gap-x-3 gap-y-1 rounded-lg px-3 py-2 text-xs text-[var(--ms-text-secondary)]">
                  <span>
                    Class{" "}
                    <span className="font-medium text-[var(--ms-text-primary)]">
                      {annualReport.className} ({annualReport.section})
                    </span>
                  </span>
                  <span aria-hidden="true" className="text-[var(--ms-text-muted)]">
                    ·
                  </span>
                  <span>
                    Academic Performance{" "}
                    <span className="ms-mono font-medium text-[var(--ms-text-primary)]">
                      {resolvedAnnualYearRange}
                    </span>
                  </span>
                </div>
              )}
            </div>

            {/* Active Completed Exams Included Bar */}
            {!annualLoading && annualActiveExams.length > 0 && (
              <div className="flex flex-wrap items-center gap-2 border-b border-[var(--ms-border)] bg-[var(--ms-surface-hover)] px-4 py-3 sm:px-5">
                <span className="text-xs font-medium text-[var(--ms-text-secondary)]">
                  Completed exams included:
                </span>
                {annualActiveExams.map((exam) => (
                  <span key={exam.examType} className="ms-exam-pill">
                    <span>{exam.examType}</span>
                    <span className="ms-exam-pill-meta">
                      {formatMark(exam.totalFullMarks)} marks
                    </span>
                  </span>
                ))}
                <span className="ms-exam-pill ms-exam-pill-total">
                  <span>Annual Total</span>
                  <span className="ms-mono text-[var(--ms-text-primary)]">
                    {formatMark(annualReport?.totalAnnualFullMarks)} marks
                  </span>
                </span>
              </div>
            )}

            {/* Loading / Empty / Dynamic N-Exam Ledger Table */}
            {annualLoading ? (
              <div className="flex flex-col items-center justify-center gap-3 px-4 py-16 text-center">
                <Loader2
                  className="h-5 w-5 animate-spin text-[var(--ms-text-muted)]"
                  strokeWidth={1.5}
                  aria-hidden="true"
                />
                <p className="text-sm text-[var(--ms-text-secondary)]">
                  Calculating annual report cards
                </p>
              </div>
            ) : annualStudentsLength === 0 ? (
              <div className="flex flex-col items-center justify-center gap-3 px-4 py-16 text-center">
                <div className="ms-surface-raised flex h-10 w-10 items-center justify-center rounded-xl">
                  <FileText
                    className="h-5 w-5 text-[var(--ms-text-muted)]"
                    strokeWidth={1.5}
                    aria-hidden="true"
                  />
                </div>
                <p className="text-sm font-medium text-[var(--ms-text-primary)]">
                  {annualHasSearched
                    ? "No annual report cards available"
                    : "No annual report cards loaded yet"}
                </p>
                <p className="max-w-md text-xs text-[var(--ms-text-muted)]">
                  {annualHasSearched
                    ? `No populated exam marks were found for Class ${annualLastContext.className || "—"} (${annualLastContext.section || "—"}). Exams without entered marks are automatically excluded.`
                    : isStudent
                    ? "Use Reload above to fetch your annual report card"
                    : "Choose a class and section above to aggregate all completed internal exams dynamically"}
                </p>
              </div>
            ) : (
              <div
                tabIndex={0}
                role="region"
                aria-labelledby={annualResultsHeadingId}
                className="ms-table-region"
              >
                <table className="ms-ledger-table ms-annual-ledger">
                  <thead>
                    <tr>
                      <th scope="col" className="ms-col-sticky text-left">
                        Student
                      </th>
                      {annualActiveExams.map((exam) => (
                        <th
                          key={exam.examType}
                          scope="col"
                          className="text-right"
                        >
                          {exam.examType}
                        </th>
                      ))}
                      <th scope="col" className="text-right">
                        Annual Total
                      </th>
                      <th scope="col" className="text-right">
                        Grade
                      </th>
                      <th scope="col" className="text-right">
                        Result
                      </th>
                      <th scope="col" className="text-right">
                        Actions
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {paginatedAnnualStudents.map((student, index) => {
                      const isPass = student.result === "P";
                      const summaries = Array.isArray(student.examSummaries)
                        ? student.examSummaries
                        : [];
                      return (
                        <tr key={`${student.rollNumber}-${index}`}>
                          <th
                            scope="row"
                            className="ms-col-student ms-col-sticky text-left font-medium text-[var(--ms-text-primary)]"
                          >
                            <div className="flex flex-col gap-0.5">
                              <span className="capitalize">
                                {student.studentName}
                              </span>
                              <span className="ms-mono text-xs font-normal text-[var(--ms-text-muted)]">
                                Roll {student.rollNumber}
                              </span>
                            </div>
                          </th>

                          {annualActiveExams.map((exam, examIdx) => {
                            const summary =
                              summaries.find(
                                (item) => item.examType === exam.examType
                              ) || summaries[examIdx];
                            const isAbsent = summary?.present === false;
                            return (
                              <td
                                key={exam.examType}
                                data-label={exam.examType}
                                style={{
                                  "--col-label": JSON.stringify(exam.examType),
                                }}
                                className="ms-num whitespace-nowrap text-right text-sm"
                              >
                                {isAbsent ? (
                                  <>
                                    <span className="text-xs font-medium text-[var(--ms-negative)]">
                                      Absent
                                    </span>
                                    <span className="ms-mono ml-1.5 text-xs text-[var(--ms-text-muted)]">
                                      0 / {formatMark(summary?.totalFullMarks ?? exam.totalFullMarks)}
                                    </span>
                                  </>
                                ) : (
                                  <>
                                    <span className="font-medium text-[var(--ms-text-primary)]">
                                      {formatMark(summary?.totalObtained ?? 0)}
                                    </span>
                                    <span className="text-xs text-[var(--ms-text-muted)]">
                                      {" "}
                                      / {formatMark(summary?.totalFullMarks ?? exam.totalFullMarks)}
                                    </span>
                                    {summary?.grade && (
                                      <span className="ms-mono ml-2 text-xs text-[var(--ms-text-muted)]">
                                        {summary.grade}
                                      </span>
                                    )}
                                  </>
                                )}
                              </td>
                            );
                          })}

                          <td
                            data-label="Annual Total"
                            style={{ "--col-label": '"Annual Total"' }}
                            className="ms-col-marks ms-num whitespace-nowrap text-right"
                          >
                            <span className="font-medium text-[var(--ms-text-primary)]">
                              {formatMark(student.grandTotalObtained)} /{" "}
                              {formatMark(student.grandTotalFullMarks)}
                            </span>
                            <span className="ms-mono ml-2 text-xs text-[var(--ms-text-muted)]">
                              {formatPercent(student.overallPercentage)}
                            </span>
                          </td>

                          <td
                            data-label="Grade"
                            style={{ "--col-label": '"Grade"' }}
                            className="whitespace-nowrap text-right"
                          >
                            <span
                              className="ms-grade-tag ms-mono"
                              data-fail={!isPass ? "true" : "false"}
                            >
                              {student.overallGrade || "—"}
                            </span>
                          </td>

                          <td
                            data-label="Result"
                            style={{ "--col-label": '"Result"' }}
                            className="whitespace-nowrap text-right"
                          >
                            <span
                              className="ms-status-pill"
                              data-status={isPass ? "pass" : "fail"}
                            >
                              {isPass ? "Pass" : "Fail"}
                            </span>
                          </td>

                          <td className="ms-col-actions whitespace-nowrap text-right">
                            <div className="ms-actions-group inline-flex items-center justify-end gap-2">
                              <button
                                type="button"
                                onClick={() => handleViewAnnualStudent(student)}
                                className="ms-btn-icon ms-btn-secondary inline-flex items-center justify-center gap-2 px-2 text-sm"
                                aria-label={`View annual report details for ${student.studentName}`}
                              >
                                <Eye
                                  className="h-4 w-4 shrink-0 text-[var(--ms-text-secondary)]"
                                  strokeWidth={1.5}
                                  aria-hidden="true"
                                />
                                <span className="ms-btn-mobile-label">View</span>
                              </button>

                              <AnnualMarksheetComponent
                                studentData={student}
                                className={annualReport?.className || ""}
                                section={annualReport?.section || ""}
                                academicYearRange={resolvedAnnualYearRange}
                                gradeSystem={annualReport?.gradeScale || []}
                                dynamicGradeScale={annualDynamicScale}
                                signaturePreview={signaturePreview}
                              />
                            </div>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            )}

            {/* Annual Responsive Pagination Bar */}
            {annualStudentsLength > 0 && !annualLoading && (
              <div className="flex flex-col gap-3 border-t border-[var(--ms-border)] px-4 py-4 sm:flex-row sm:items-center sm:justify-between sm:px-5">
                <div className="flex flex-wrap items-center gap-3 text-xs text-[var(--ms-text-secondary)]">
                  <div className="flex items-center gap-2">
                    <label
                      htmlFor={annualRowsFieldId}
                      className="text-xs text-[var(--ms-text-muted)]"
                    >
                      Rows
                    </label>
                    <select
                      id={annualRowsFieldId}
                      value={annualRowsPerPage}
                      onChange={(e) =>
                        handleAnnualRowsPerPageChange(Number(e.target.value))
                      }
                      className="ms-control ms-num border border-[var(--ms-border-strong)] bg-[var(--ms-surface)] px-2 text-xs text-[var(--ms-text-primary)]"
                    >
                      {ROWS_PER_PAGE_OPTIONS.map((option) => (
                        <option key={option} value={option}>
                          {option}
                        </option>
                      ))}
                    </select>
                  </div>

                  <span className="ms-num text-xs text-[var(--ms-text-muted)]">
                    Showing {annualRangeStart}–{annualRangeEnd} of{" "}
                    {annualStudentsLength}
                  </span>
                </div>

                <div className="flex items-center justify-between gap-3 sm:justify-end">
                  <span className="ms-num text-xs text-[var(--ms-text-secondary)]">
                    Page{" "}
                    <span className="font-medium text-[var(--ms-text-primary)]">
                      {annualSafePage}
                    </span>{" "}
                    of {annualTotalPages}
                  </span>

                  <div className="flex items-center gap-2">
                    <button
                      type="button"
                      disabled={annualSafePage <= 1}
                      onClick={() =>
                        setAnnualCurrentPage((prev) => Math.max(1, prev - 1))
                      }
                      className="ms-control ms-btn-secondary inline-flex items-center justify-center gap-1 px-3 text-xs disabled:cursor-not-allowed disabled:opacity-40"
                      aria-label="Previous page"
                    >
                      <ChevronLeft
                        className="h-4 w-4"
                        strokeWidth={1.5}
                        aria-hidden="true"
                      />
                      <span className="hidden sm:inline">Previous</span>
                    </button>
                    <button
                      type="button"
                      disabled={annualSafePage >= annualTotalPages}
                      onClick={() =>
                        setAnnualCurrentPage((prev) =>
                          Math.min(annualTotalPages, prev + 1)
                        )
                      }
                      className="ms-control ms-btn-secondary inline-flex items-center justify-center gap-1 px-3 text-xs disabled:cursor-not-allowed disabled:opacity-40"
                      aria-label="Next page"
                    >
                      <span className="hidden sm:inline">Next</span>
                      <ChevronRight
                        className="h-4 w-4"
                        strokeWidth={1.5}
                        aria-hidden="true"
                      />
                    </button>
                  </div>
                </div>
              </div>
            )}
          </section>
        </div>
      </div>

      {/* Craft Moment #2: Native <dialog> Student Transcript Sheet */}
      <dialog
        ref={detailDialogRef}
        {...{ closedby: "any" }}
        aria-labelledby={detailTitleId}
        data-theme={visualTheme}
        onClose={handleDialogClose}
        className="marksheet-dialog"
      >
        {viewStudent && (
          <>
            {/* Sticky Modal Header with 2px Brass Signature Rule */}
            <div className="flex shrink-0 items-start justify-between gap-4 border-b border-[var(--ms-border)] px-4 py-4 sm:px-6">
              <div className="min-w-0">
                <div className="ms-signature-rule mb-2" aria-hidden="true" />
                <h1
                  id={detailTitleId}
                  className="text-lg font-medium text-[var(--ms-text-primary)] sm:text-xl"
                >
                  Student transcript
                </h1>
                <p className="mt-1 text-xs text-[var(--ms-text-secondary)]">
                  <span className="capitalize">{viewStudent.name}</span> ·{" "}
                  {viewStudent.examType}
                </p>
              </div>
              <button
                type="button"
                autoFocus
                onClick={handleCloseView}
                aria-label="Close student transcript"
                className="ms-btn-icon ms-btn-secondary inline-flex shrink-0 items-center justify-center text-[var(--ms-text-secondary)] hover:text-[var(--ms-text-primary)]"
              >
                <X className="h-4 w-4" strokeWidth={1.5} aria-hidden="true" />
              </button>
            </div>

            {/* Scrollable Body */}
            <div className="flex-1 space-y-6 overflow-y-auto px-4 py-4 sm:px-6 sm:py-6">
              {/* Student Metadata Strip */}
              <div className="ms-surface-raised grid grid-cols-2 gap-4 rounded-xl p-4 sm:grid-cols-4">
                <div className="col-span-2 min-w-0 sm:col-span-1">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Student name
                  </span>
                  <p className="mt-1 text-sm font-medium capitalize text-[var(--ms-text-primary)]">
                    {viewStudent.name}
                  </p>
                </div>
                <div className="min-w-0">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Roll number
                  </span>
                  <p className="ms-mono mt-1 text-sm font-medium text-[var(--ms-text-primary)]">
                    {viewStudent.roll}
                  </p>
                </div>
                <div className="min-w-0">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Class &amp; section
                  </span>
                  <p className="mt-1 text-sm font-medium text-[var(--ms-text-primary)]">
                    {viewStudent.class} ({viewStudent.section})
                  </p>
                </div>
                <div className="col-span-2 min-w-0 sm:col-span-1">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Exam
                  </span>
                  <p className="mt-1 text-sm font-medium text-[var(--ms-text-primary)]">
                    {viewStudent.examType}
                  </p>
                </div>
              </div>

              {/* Result Summary Strip */}
              <div className="grid grid-cols-2 gap-3 sm:gap-4 lg:grid-cols-4">
                <div className="ms-surface-raised rounded-xl p-4">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Total marks
                  </span>
                  <p className="ms-num mt-2 text-xl font-medium text-[var(--ms-text-primary)] sm:text-2xl">
                    {formatMark(viewStudent.totalMarks)}
                    <span className="text-sm font-normal text-[var(--ms-text-muted)]">
                      {" "}
                      / {formatMark(viewStudent.totalFullMarks)}
                    </span>
                  </p>
                </div>
                <div className="ms-surface-raised rounded-xl p-4">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Percentage
                  </span>
                  <p className="ms-num mt-2 text-xl font-medium text-[var(--ms-text-primary)] sm:text-2xl">
                    {formatPercent(viewStudent.overallPercentage)}
                  </p>
                </div>
                <div className="ms-surface-raised rounded-xl p-4">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Overall grade
                  </span>
                  <p className="ms-mono mt-2 text-xl font-medium text-[var(--ms-text-primary)] sm:text-2xl">
                    {viewStudent.overallGrade || "—"}
                  </p>
                </div>
                <div className="ms-surface-raised rounded-xl p-4">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Result
                  </span>
                  <p
                    className={`mt-2 text-xl font-medium sm:text-2xl ${
                      viewStudent.result === "P"
                        ? "text-[var(--ms-positive)]"
                        : "text-[var(--ms-negative)]"
                    }`}
                  >
                    {viewStudent.result === "P" ? "Pass" : "Fail"}
                  </p>
                </div>
              </div>

              {/* Subject Marks Ledger */}
              <div>
                <h2
                  id={subjectsHeadingId}
                  className="mb-3 text-sm font-medium text-[var(--ms-text-primary)]"
                >
                  Subject marks
                </h2>
                <div
                  tabIndex={0}
                  role="region"
                  aria-labelledby={subjectsHeadingId}
                  className="ms-subjects-region rounded-xl border border-[var(--ms-border)]"
                >
                  <table className="ms-subjects-table">
                    <thead>
                      <tr>
                        <th scope="col" className="text-left">
                          Subject
                        </th>
                        <th scope="col" className="text-right">
                          Written
                        </th>
                        <th scope="col" className="text-right">
                          Project
                        </th>
                        <th scope="col" className="text-right">
                          Total
                        </th>
                        <th scope="col" className="text-right">
                          Grade
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {(viewStudent.subjects || []).map((subject, index) => {
                        const isFail = subject.passed === false;
                        const hasProject =
                          parseFloat(subject.projectFullMark) > 0;
                        return (
                          <tr
                            key={`${subject.subject}-${index}`}
                            data-fail={isFail ? "true" : "false"}
                          >
                            <th
                              scope="row"
                              className="ms-sub-name text-left font-medium text-[var(--ms-text-primary)]"
                            >
                              <div className="flex flex-wrap items-center gap-2">
                                <span className="capitalize">
                                  {subject.subject}
                                </span>
                                {subject.isAdditional && (
                                  <span className="rounded border border-[var(--ms-border)] bg-[var(--ms-surface-raised)] px-2 py-1 text-xs font-normal leading-none text-[var(--ms-text-secondary)]">
                                    Additional
                                  </span>
                                )}
                              </div>
                            </th>
                            <td
                              data-label="Written"
                              style={{ "--col-label": '"Written"' }}
                              className="ms-num whitespace-nowrap text-right text-sm text-[var(--ms-text-primary)]"
                            >
                              {formatMark(subject.writtenMarks)}
                              <span className="text-xs text-[var(--ms-text-muted)]">
                                {" "}
                                / {formatMark(subject.subjectFullMark ?? 0)}
                              </span>
                            </td>
                            <td
                              data-label="Project"
                              style={{ "--col-label": '"Project"' }}
                              className="ms-num whitespace-nowrap text-right text-sm text-[var(--ms-text-primary)]"
                            >
                              {hasProject ? (
                                <>
                                  {formatMark(subject.projectMarks)}
                                  <span className="text-xs text-[var(--ms-text-muted)]">
                                    {" "}
                                    / {formatMark(subject.projectFullMark)}
                                  </span>
                                </>
                              ) : (
                                <span className="text-[var(--ms-text-muted)]">
                                  —
                                </span>
                              )}
                            </td>
                            <td
                              data-label="Total"
                              style={{ "--col-label": '"Total"' }}
                              className="ms-num whitespace-nowrap text-right text-sm"
                            >
                              <span
                                className={`font-medium ${
                                  isFail
                                    ? "text-[var(--ms-negative)]"
                                    : "text-[var(--ms-text-primary)]"
                                }`}
                              >
                                {formatMark(
                                  subject.marksObtained ?? subject.totalMarks
                                )}
                                <span className="text-xs font-normal text-[var(--ms-text-muted)]">
                                  {" "}
                                  / {formatMark(subject.fullMarks)}
                                </span>
                              </span>
                              {subject.percentage !== undefined && (
                                <span className="ms-mono ml-2 text-xs text-[var(--ms-text-muted)]">
                                  {formatPercent(subject.percentage)}
                                </span>
                              )}
                            </td>
                            <td
                              data-label="Grade"
                              style={{ "--col-label": '"Grade"' }}
                              className="whitespace-nowrap text-right"
                            >
                              <span
                                className="ms-grade-tag ms-mono"
                                data-fail={isFail ? "true" : "false"}
                              >
                                {subject.grade || "—"}
                              </span>
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>

              {/* Compact Grading Scale Reference */}
              <details className="rounded-xl border border-[var(--ms-border)] bg-[var(--ms-surface)]">
                <summary className="ms-details-summary flex items-center justify-between gap-2 px-4 py-3 text-sm font-medium text-[var(--ms-text-primary)]">
                  <span>Grading scale</span>
                  <ChevronDown
                    className="ms-details-chevron h-4 w-4 text-[var(--ms-text-muted)]"
                    strokeWidth={1.5}
                    aria-hidden="true"
                  />
                </summary>
                <div className="border-t border-[var(--ms-border)]">
                  <table className="w-full border-collapse text-sm">
                    <thead>
                      <tr className="bg-[var(--ms-surface-raised)] text-xs text-[var(--ms-text-secondary)]">
                        <th
                          scope="col"
                          className="px-4 py-2 text-left font-medium"
                        >
                          Range (%)
                        </th>
                        <th
                          scope="col"
                          className="px-4 py-2 text-left font-medium"
                        >
                          Grade
                        </th>
                        <th
                          scope="col"
                          className="px-4 py-2 text-left font-medium"
                        >
                          Performance
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {displayGradeScale.map((grade, index) => (
                        <tr
                          key={index}
                          className="border-t border-[var(--ms-border)]"
                        >
                          <td className="ms-mono px-4 py-2 text-xs text-[var(--ms-text-secondary)]">
                            {grade.scaleStartMarks}–{grade.scaleEndMarks}
                          </td>
                          <td className="px-4 py-2">
                            <span className="ms-grade-tag ms-mono">
                              {grade.letterGrade}
                            </span>
                          </td>
                          <td className="px-4 py-2 text-sm text-[var(--ms-text-primary)]">
                            {grade.performanceIndicator}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </details>
            </div>

            {/* Sticky Modal Footer */}
            <div className="flex shrink-0 items-center justify-end border-t border-[var(--ms-border)] px-4 py-3 sm:px-6">
              <button
                type="button"
                onClick={handleCloseView}
                className="ms-control ms-btn-secondary w-full px-4 text-sm sm:w-auto"
              >
                Close
              </button>
            </div>
          </>
        )}

        {viewAnnualStudent && (
          <>
            {/* Sticky Modal Header */}
            <div className="flex shrink-0 items-start justify-between gap-4 border-b border-[var(--ms-border)] px-4 py-4 sm:px-6">
              <div className="min-w-0">
                <div className="ms-signature-rule mb-2" aria-hidden="true" />
                <h1
                  id={detailTitleId}
                  className="text-lg font-medium text-[var(--ms-text-primary)] sm:text-xl"
                >
                  Annual report card
                </h1>
                <p className="mt-1 text-xs text-[var(--ms-text-secondary)]">
                  <span className="capitalize">
                    {viewAnnualStudent.studentName}
                  </span>{" "}
                  · Class {annualReport?.className} ({annualReport?.section}) ·
                  Academic Performance {resolvedAnnualYearRange}
                </p>
              </div>
              <button
                type="button"
                autoFocus
                onClick={handleCloseView}
                aria-label="Close annual report card"
                className="ms-btn-icon ms-btn-secondary inline-flex shrink-0 items-center justify-center text-[var(--ms-text-secondary)] hover:text-[var(--ms-text-primary)]"
              >
                <X className="h-4 w-4" strokeWidth={1.5} aria-hidden="true" />
              </button>
            </div>

            {/* Scrollable Body */}
            <div className="flex-1 space-y-6 overflow-y-auto px-4 py-4 sm:px-6 sm:py-6">
              {/* Student Metadata Strip */}
              <div className="ms-surface-raised grid grid-cols-2 gap-4 rounded-xl p-4 sm:grid-cols-4">
                <div className="col-span-2 min-w-0 sm:col-span-1">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Student name
                  </span>
                  <p className="mt-1 text-sm font-medium capitalize text-[var(--ms-text-primary)]">
                    {viewAnnualStudent.studentName}
                  </p>
                </div>
                <div className="min-w-0">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Roll number
                  </span>
                  <p className="ms-mono mt-1 text-sm font-medium text-[var(--ms-text-primary)]">
                    {viewAnnualStudent.rollNumber}
                  </p>
                </div>
                <div className="min-w-0">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Class &amp; section
                  </span>
                  <p className="mt-1 text-sm font-medium text-[var(--ms-text-primary)]">
                    {annualReport?.className} ({annualReport?.section})
                  </p>
                </div>
                <div className="col-span-2 min-w-0 sm:col-span-1">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Academic performance
                  </span>
                  <p className="ms-num mt-1 text-sm font-medium text-[var(--ms-text-primary)]">
                    {resolvedAnnualYearRange} · {annualActiveExams.length}{" "}
                    {annualActiveExams.length === 1 ? "exam" : "exams"}
                  </p>
                </div>
              </div>

              {/* Annual Result Summary Strip */}
              <div className="grid grid-cols-2 gap-3 sm:gap-4 lg:grid-cols-4">
                <div className="ms-surface-raised rounded-xl p-4">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Grand total
                  </span>
                  <p className="ms-num mt-2 text-xl font-medium text-[var(--ms-text-primary)] sm:text-2xl">
                    {formatMark(viewAnnualStudent.grandTotalObtained)}
                    <span className="text-sm font-normal text-[var(--ms-text-muted)]">
                      {" "}
                      / {formatMark(viewAnnualStudent.grandTotalFullMarks)}
                    </span>
                  </p>
                </div>
                <div className="ms-surface-raised rounded-xl p-4">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Percentage
                  </span>
                  <p className="ms-num mt-2 text-xl font-medium text-[var(--ms-text-primary)] sm:text-2xl">
                    {formatPercent(viewAnnualStudent.overallPercentage)}
                  </p>
                </div>
                <div className="ms-surface-raised rounded-xl p-4">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Annual grade
                  </span>
                  <p className="ms-mono mt-2 text-xl font-medium text-[var(--ms-text-primary)] sm:text-2xl">
                    {viewAnnualStudent.overallGrade || "—"}
                  </p>
                </div>
                <div className="ms-surface-raised rounded-xl p-4">
                  <span className="block text-xs text-[var(--ms-text-muted)]">
                    Result
                  </span>
                  <p
                    className={`mt-2 text-xl font-medium sm:text-2xl ${
                      viewAnnualStudent.result === "P"
                        ? "text-[var(--ms-positive)]"
                        : "text-[var(--ms-negative)]"
                    }`}
                  >
                    {viewAnnualStudent.result === "P" ? "Pass" : "Fail"}
                  </p>
                </div>
              </div>

              {/* Completed Internal Exams Summary Table */}
              <div>
                <h2
                  id={annualExamsHeadingId}
                  className="mb-3 text-sm font-medium text-[var(--ms-text-primary)]"
                >
                  Completed internal exams
                </h2>
                <div
                  tabIndex={0}
                  role="region"
                  aria-labelledby={annualExamsHeadingId}
                  className="ms-subjects-region rounded-xl border border-[var(--ms-border)]"
                >
                  <table className="ms-subjects-table">
                    <thead>
                      <tr>
                        <th scope="col" className="text-left">
                          Internal Exam
                        </th>
                        <th scope="col" className="text-right">
                          Full Marks
                        </th>
                        <th scope="col" className="text-right">
                          Obtained
                        </th>
                        <th scope="col" className="text-right">
                          Percentage
                        </th>
                        <th scope="col" className="text-right">
                          Grade
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {(annualActiveExams.length > 0
                        ? annualActiveExams.map((activeExam, idx) => {
                            const summaries = Array.isArray(
                              viewAnnualStudent.examSummaries
                            )
                              ? viewAnnualStudent.examSummaries
                              : [];
                            return (
                              summaries.find(
                                (item) => item.examType === activeExam.examType
                              ) ||
                              summaries[idx] || {
                                examType: activeExam.examType,
                                totalFullMarks: activeExam.totalFullMarks,
                                totalObtained: 0,
                                percentage: 0,
                                grade: "—",
                                passed: false,
                                present: false,
                              }
                            );
                          })
                        : viewAnnualStudent.examSummaries || []
                      ).map(
                        (exam, idx) => {
                          const examFail = exam.passed === false;
                          return (
                            <tr
                              key={`${exam.examType}-${idx}`}
                              data-fail={examFail ? "true" : "false"}
                            >
                              <th
                                scope="row"
                                className="ms-sub-name text-left font-medium text-[var(--ms-text-primary)]"
                              >
                                {exam.examType}
                              </th>
                              <td
                                data-label="Full Marks"
                                style={{ "--col-label": '"Full Marks"' }}
                                className="ms-num whitespace-nowrap text-right text-sm text-[var(--ms-text-secondary)]"
                              >
                                {formatMark(exam.totalFullMarks)}
                              </td>
                              <td
                                data-label="Obtained"
                                style={{ "--col-label": '"Obtained"' }}
                                className="ms-num whitespace-nowrap text-right text-sm font-medium text-[var(--ms-text-primary)]"
                              >
                                {exam.present === false ? (
                                  <span className="text-[var(--ms-negative)]">
                                    Absent (0)
                                  </span>
                                ) : (
                                  formatMark(exam.totalObtained)
                                )}
                              </td>
                              <td
                                data-label="Percentage"
                                style={{ "--col-label": '"Percentage"' }}
                                className="ms-mono whitespace-nowrap text-right text-sm text-[var(--ms-text-secondary)]"
                              >
                                {formatPercent(exam.percentage)}
                              </td>
                              <td
                                data-label="Grade"
                                style={{ "--col-label": '"Grade"' }}
                                className="whitespace-nowrap text-right"
                              >
                                <span
                                  className="ms-grade-tag ms-mono"
                                  data-fail={examFail ? "true" : "false"}
                                >
                                  {exam.grade || "—"}
                                </span>
                              </td>
                            </tr>
                          );
                        }
                      )}
                    </tbody>
                    <tfoot>
                      <tr>
                        <th scope="row" className="text-left text-sm">
                          Grand Total
                        </th>
                        <td
                          data-label="Full Marks"
                          style={{ "--col-label": '"Full Marks"' }}
                          className="ms-num whitespace-nowrap text-right text-sm"
                        >
                          {formatMark(viewAnnualStudent.grandTotalFullMarks)}
                        </td>
                        <td
                          data-label="Obtained"
                          style={{ "--col-label": '"Obtained"' }}
                          className="ms-num whitespace-nowrap text-right text-sm"
                        >
                          {formatMark(viewAnnualStudent.grandTotalObtained)}
                        </td>
                        <td
                          data-label="Percentage"
                          style={{ "--col-label": '"Percentage"' }}
                          className="ms-mono whitespace-nowrap text-right text-sm"
                        >
                          {formatPercent(viewAnnualStudent.overallPercentage)}
                        </td>
                        <td
                          data-label="Grade"
                          style={{ "--col-label": '"Grade"' }}
                          className="whitespace-nowrap text-right"
                        >
                          <span
                            className="ms-grade-tag ms-mono"
                            data-fail={
                              viewAnnualStudent.result !== "P" ? "true" : "false"
                            }
                          >
                            {viewAnnualStudent.overallGrade || "—"}
                          </span>
                        </td>
                      </tr>
                    </tfoot>
                  </table>
                </div>
              </div>

              {/* Subject-Wise × Internal-Exam-Wise Marks Matrix */}
              <div>
                <h2
                  id={annualMatrixHeadingId}
                  className="mb-3 text-sm font-medium text-[var(--ms-text-primary)]"
                >
                  Subject-wise internal exam marks
                </h2>
                <div
                  tabIndex={0}
                  role="region"
                  aria-labelledby={annualMatrixHeadingId}
                  className="ms-subjects-region rounded-xl border border-[var(--ms-border)]"
                >
                  <table className="ms-subjects-table ms-matrix-table">
                    <thead>
                      <tr>
                        <th scope="col" className="ms-sub-sticky text-left">
                          Subject
                        </th>
                        {annualActiveExams.map((exam) => (
                          <th
                            key={exam.examType}
                            scope="col"
                            className="text-right"
                          >
                            {exam.examType}
                          </th>
                        ))}
                        <th scope="col" className="text-right">
                          Annual Total
                        </th>
                        <th scope="col" className="text-right">
                          %
                        </th>
                        <th scope="col" className="text-right">
                          Grade
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {(viewAnnualStudent.subjects || []).map(
                        (subject, idx) => {
                          const isFail = subject.passed === false;
                          const examCells = Array.isArray(subject.examMarks)
                            ? subject.examMarks
                            : [];
                          return (
                            <tr
                              key={`${subject.subject}-${idx}`}
                              data-fail={isFail ? "true" : "false"}
                            >
                              <th
                                scope="row"
                                className="ms-sub-name ms-sub-sticky text-left font-medium text-[var(--ms-text-primary)]"
                              >
                                <span className="capitalize">
                                  {subject.subject}
                                </span>
                              </th>
                              {annualActiveExams.map((exam, examIdx) => {
                                const cell =
                                  examCells.find(
                                    (c) => c.examType === exam.examType
                                  ) || examCells[examIdx];
                                return (
                                  <td
                                    key={exam.examType}
                                    data-label={exam.examType}
                                    style={{
                                      "--col-label": JSON.stringify(
                                        exam.examType
                                      ),
                                    }}
                                    className="ms-num whitespace-nowrap text-right text-sm"
                                  >
                                    {!cell || cell.conducted === false ? (
                                      <span
                                        className="text-[var(--ms-text-muted)]"
                                        aria-label="Not conducted"
                                      >
                                        —
                                      </span>
                                    ) : cell.present === false ? (
                                      <>
                                        <span className="text-xs font-medium text-[var(--ms-negative)]">
                                          Absent
                                        </span>
                                        <span className="ms-mono ml-1 text-xs text-[var(--ms-text-muted)]">
                                          (0 / {formatMark(cell.fullMarks)})
                                        </span>
                                      </>
                                    ) : (
                                      <>
                                        <span className="font-medium text-[var(--ms-text-primary)]">
                                          {formatMark(cell.totalMarks)}
                                        </span>
                                        <span className="text-xs text-[var(--ms-text-muted)]">
                                          {" "}
                                          / {formatMark(cell.fullMarks)}
                                        </span>
                                      </>
                                    )}
                                  </td>
                                );
                              })}
                              <td
                                data-label="Annual Total"
                                style={{ "--col-label": '"Annual Total"' }}
                                className="ms-num whitespace-nowrap text-right text-sm"
                              >
                                <span
                                  className={`font-medium ${
                                    isFail
                                      ? "text-[var(--ms-negative)]"
                                      : "text-[var(--ms-text-primary)]"
                                  }`}
                                >
                                  {formatMark(subject.annualObtained)}
                                </span>
                                <span className="text-xs text-[var(--ms-text-muted)]">
                                  {" "}
                                  / {formatMark(subject.annualFullMarks)}
                                </span>
                              </td>
                              <td
                                data-label="Percentage"
                                style={{ "--col-label": '"Percentage"' }}
                                className="ms-mono whitespace-nowrap text-right text-sm text-[var(--ms-text-secondary)]"
                              >
                                {formatPercent(subject.annualPercentage)}
                              </td>
                              <td
                                data-label="Grade"
                                style={{ "--col-label": '"Grade"' }}
                                className="whitespace-nowrap text-right"
                              >
                                <span
                                  className="ms-grade-tag ms-mono"
                                  data-fail={isFail ? "true" : "false"}
                                >
                                  {subject.annualGrade || "—"}
                                </span>
                              </td>
                            </tr>
                          );
                        }
                      )}
                    </tbody>
                  </table>
                </div>
              </div>

              {/* Collapsible Dynamic 7-Tier Grade Scale */}
              <details className="rounded-xl border border-[var(--ms-border)] bg-[var(--ms-surface)]">
                <summary className="ms-details-summary flex items-center justify-between gap-2 px-4 py-3 text-sm font-medium text-[var(--ms-text-primary)]">
                  <span>
                    Dynamic grading scale (7 tiers · out of{" "}
                    {formatMark(viewAnnualStudent.grandTotalFullMarks)} marks)
                  </span>
                  <ChevronDown
                    className="ms-details-chevron h-4 w-4 text-[var(--ms-text-muted)]"
                    strokeWidth={1.5}
                    aria-hidden="true"
                  />
                </summary>
                <div className="border-t border-[var(--ms-border)]">
                  <table className="w-full border-collapse text-sm">
                    <thead>
                      <tr className="bg-[var(--ms-surface-raised)] text-xs text-[var(--ms-text-secondary)]">
                        <th
                          scope="col"
                          className="px-4 py-2 text-left font-medium"
                        >
                          Marks Range
                        </th>
                        <th
                          scope="col"
                          className="px-4 py-2 text-left font-medium"
                        >
                          Grade
                        </th>
                        <th
                          scope="col"
                          className="px-4 py-2 text-left font-medium"
                        >
                          Performance
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {annualDynamicScale.map((tier, index) => (
                        <tr
                          key={`${tier.letterGrade}-${index}`}
                          className="border-t border-[var(--ms-border)]"
                        >
                          <td className="ms-mono px-4 py-2 text-xs text-[var(--ms-text-secondary)]">
                            {tier.minMarks}–{tier.maxMarks}
                          </td>
                          <td className="px-4 py-2">
                            <span className="ms-grade-tag ms-mono">
                              {tier.letterGrade}
                            </span>
                          </td>
                          <td className="px-4 py-2 text-sm text-[var(--ms-text-primary)]">
                            {tier.performanceIndicator}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </details>
            </div>

            {/* Sticky Modal Footer */}
            <div className="flex shrink-0 items-center justify-end border-t border-[var(--ms-border)] px-4 py-3 sm:px-6">
              <button
                type="button"
                onClick={handleCloseView}
                className="ms-control ms-btn-secondary w-full px-4 text-sm sm:w-auto"
              >
                Close
              </button>
            </div>
          </>
        )}
      </dialog>
    </div>
  );
};

export default MarksheetPage;
