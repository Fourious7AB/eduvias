import { useMemo, useState } from "react";
import PropTypes from "prop-types";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import { useTheme } from "@/context/ThemeContext";
import { useSelector } from "react-redux";
import { saveAs } from "file-saver";
import { toast } from "sonner";
import {
  Page,
  Text,
  View,
  Document,
  StyleSheet,
  PDFViewer,
  Image,
  pdf,
  Link,
} from "@react-pdf/renderer";
import { Download, ExternalLink, Loader2 } from "lucide-react";
import principalSignature from "@/assets/principalSignature.png";
import {
  buildDynamicSevenGradeScale,
  formatDateDDMMYYYY,
  getDynamicAcademicYearRange,
  getGradeFromDynamicScale,
  isFailingGrade,
  round2,
} from "@/utils/result/resultHelpers";
import "@/pages/Result/MarksheetPage.css";

const formatPdfMark = (value) => {
  const num = Number(value);
  if (!Number.isFinite(num)) return "0";
  return Number.isInteger(num) ? String(num) : String(Number(num.toFixed(2)));
};

const styles = StyleSheet.create({
  page: {
    backgroundColor: "#fff",
    paddingTop: 10,
    paddingBottom: 22,
    paddingHorizontal: 14,
    fontSize: 9,
    fontFamily: "Helvetica",
  },
  header: {
    border: "1pt solid #222",
    paddingVertical: 6,
    paddingHorizontal: 10,
    marginBottom: 5,
  },
  schoolName: {
    fontSize: 14.5,
    fontWeight: "bold",
    textAlign: "center",
    marginBottom: 2.5,
  },
  schoolAddress: {
    fontSize: 9,
    fontStyle: "italic",
    textAlign: "center",
    lineHeight: 1.28,
    maxWidth: 215,
    marginBottom: 2,
  },
  schoolEmail: {
    fontSize: 9,
    textAlign: "center",
    lineHeight: 1.22,
    marginBottom: 1.5,
  },
  schoolPhone: {
    fontSize: 9,
    textAlign: "center",
    lineHeight: 1.22,
  },
  reportTitle: {
    fontSize: 12,
    fontWeight: "bold",
    textAlign: "center",
    paddingTop: 3,
    paddingBottom: 1,
  },
  reportsubTitle: {
    fontSize: 9,
    fontWeight: "normal",
    textAlign: "center",
    marginBottom: 3,
  },
  sectionTitle: {
    backgroundColor: "#ffbb00",
    fontWeight: "bold",
    paddingVertical: 2.5,
    fontSize: 10,
    borderTop: "1pt solid #222",
    borderBottom: "1pt solid #222",
    textAlign: "center",
  },
  sectionAcademicTitle: {
    fontWeight: "bold",
    paddingVertical: 2.5,
    fontSize: 10,
    textAlign: "center",
  },
  detailsRow: {
    flexDirection: "row",
    justifyContent: "space-between",
  },
  detailsCol: {
    width: "48%",
    paddingHorizontal: 5,
    paddingVertical: 3,
  },
  detailText: {
    fontSize: 8.5,
    paddingBottom: 2,
  },
  detailTextLast: {
    fontSize: 8.5,
  },
  table: {
    borderRight: "1pt solid #222",
    borderLeft: "1pt solid #222",
    borderTop: "1pt solid #222",
    marginTop: 4,
    marginBottom: 4,
  },
  tableHeader: {
    flexDirection: "row",
    borderBottom: "1pt solid #222",
    borderTop: "1pt solid #222",
  },
  tableCellHeader: {
    flex: 1,
    fontWeight: "bold",
    fontSize: 8,
    paddingVertical: 2.2,
    paddingHorizontal: 3,
    borderRight: "1pt solid #222",
    textAlign: "center",
  },
  tableRow: {
    flexDirection: "row",
    borderBottom: "1pt solid #222",
  },
  tableCell: {
    flex: 1,
    paddingVertical: 2,
    paddingHorizontal: 3,
    fontSize: 8,
    borderRight: "1pt solid #222",
    textAlign: "center",
  },
  tableCellCompact: {
    flex: 1,
    paddingVertical: 1.4,
    paddingHorizontal: 2,
    fontSize: 7.5,
    borderRight: "1pt solid #222",
    textAlign: "center",
  },
  coScholasticHeader: {
    backgroundColor: "#b6d6f6",
    fontWeight: "bold",
    paddingVertical: 2.5,
    fontSize: 9.5,
    textAlign: "center",
    borderBottom: "1pt solid #222",
  },
  coScholasticRow: {
    flexDirection: "row",
    borderBottom: "1pt solid #222",
  },
  coScholasticCell: {
    flex: 2,
    paddingVertical: 2,
    paddingHorizontal: 4,
    fontSize: 8.5,
    borderRight: "1pt solid #222",
  },
  coScholasticCellGrade: {
    flex: 1,
    paddingVertical: 2,
    paddingHorizontal: 4,
    fontSize: 8.5,
    textAlign: "center",
  },
  signatureBox: {
    border: "1pt solid #222",
    borderRadius: 4,
    paddingVertical: 5,
    paddingHorizontal: 8,
    marginTop: 5,
  },
  signatureRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "flex-end",
    marginTop: 4,
    fontSize: 9,
  },
  gradingTable: {
    marginTop: 4,
    borderLeft: "1pt solid #222",
    borderRight: "1pt solid #222",
    borderTop: "1pt solid #222",
  },
  gradingHeader: {
    backgroundColor: "#8c6bc5",
    paddingVertical: 2.5,
    color: "white",
    fontWeight: "bold",
    textAlign: "center",
    fontSize: 9.5,
    borderBottom: "1pt solid #222",
  },
  gradingRow: {
    flexDirection: "row",
    borderBottom: "1pt solid #222",
  },
  gradingCell: {
    flex: 1,
    paddingVertical: 1.8,
    paddingHorizontal: 2,
    fontSize: 7.5,
    borderRight: "1pt solid #222",
    textAlign: "center",
  },
  notesSection: {
    marginTop: 5,
    fontSize: 8,
  },
  noteRow: {
    flexDirection: "row",
    gap: 18,
    marginBottom: 3,
  },
  capitalize: {
    textTransform: "capitalize",
  },
});

const AnnualPdfDocument = ({
  institute = {},
  studentData = {},
  className = "",
  section = "",
  academicYearRange = "",
  dynamicGradeScale = [],
  signaturePreview = null,
}) => {
  const details = studentData?.studentDetails || {};
  const studentName = details.studentName || studentData?.studentName || "-";
  const studentClass = details.className || className || "-";
  const studentSection = details.section || section || "-";
  const studentRoll = details.rollNo ?? studentData?.rollNumber ?? "-";
  const resolvedYearRange = academicYearRange || getDynamicAcademicYearRange();
  const sessionText = details.academicYear || resolvedYearRange;

  const addressParts = [
    details.villagePost,
    details.district,
    details.state,
    details.pincode,
    details.country,
  ].filter(Boolean);
  const formattedAddress = addressParts.length > 0 ? addressParts.join(", ") : "-";
  const formattedDob = formatDateDDMMYYYY(details.dob);
  const resolvedPrincipalSignature = signaturePreview || principalSignature;

  const cleanSchoolName = (institute?.schoolName || "").trim() || "School Report Card";
  const cleanSchoolAddress = (institute?.schoolAddress || "")
    .trim()
    .replace(/:-\s+/g, ":-\u00A0");
  const cleanSchoolEmail = (institute?.schoolEmail || "").trim();
  const cleanSchoolPhone = (institute?.schoolPhone || "").trim();
  const cleanSchoolWebsite = (institute?.schoolWebsite || "").trim();

  const examSummaries = Array.isArray(studentData?.examSummaries)
    ? studentData.examSummaries
    : [];
  const isDenseExams = examSummaries.length >= 4;
  const examCellStyle = isDenseExams ? styles.tableCellCompact : styles.tableCell;

  const grandTotalFull = Number(studentData?.grandTotalFullMarks) || 0;
  const grandTotalObtained = Number(studentData?.grandTotalObtained) || 0;
  const overallPercentage =
    studentData?.overallPercentage !== undefined && studentData?.overallPercentage !== null
      ? round2(studentData.overallPercentage)
      : grandTotalFull > 0
      ? round2((grandTotalObtained / grandTotalFull) * 100)
      : 0;

  const scaleToRender =
    Array.isArray(dynamicGradeScale) && dynamicGradeScale.length === 7
      ? dynamicGradeScale
      : buildDynamicSevenGradeScale([], grandTotalFull);

  const computedTier = getGradeFromDynamicScale(grandTotalObtained, scaleToRender);
  const overallGrade = studentData?.overallGrade || computedTier.letterGrade || "-";
  const isPass =
    studentData?.result !== undefined
      ? studentData.result === "P"
      : grandTotalFull > 0 && !isFailingGrade(computedTier, []);

  return (
    <Document>
      <Page size="A4" style={styles.page}>
        {/* Header */}
        <View style={styles.header} wrap={false}>
          <View
            style={{
              flexDirection: "row",
              alignItems: "center",
              justifyContent: "space-between",
            }}
          >
            {/* Left Section: Logo */}
            <View style={{ alignItems: "center", width: 105 }}>
              {institute?.schoolLogo ? (
                <Image
                  src={institute.schoolLogo}
                  style={{ width: 58, height: 58, objectFit: "contain" }}
                />
              ) : (
                <View style={{ width: 58, height: 58 }} />
              )}
            </View>

            {/* Center Section: School Name and Details */}
            <View style={{ flex: 1, alignItems: "center", paddingHorizontal: 4 }}>
              <Text style={styles.schoolName}>{cleanSchoolName}</Text>
              {cleanSchoolAddress ? (
                <Text
                  style={styles.schoolAddress}
                  hyphenationCallback={(word) => [word]}
                >
                  {cleanSchoolAddress}
                </Text>
              ) : null}
              {cleanSchoolEmail ? (
                <Text style={styles.schoolEmail}>{cleanSchoolEmail}</Text>
              ) : null}
              {cleanSchoolPhone ? (
                <Text style={styles.schoolPhone}>
                  Contact No : {cleanSchoolPhone}
                </Text>
              ) : null}

              {cleanSchoolWebsite ? (
                <View
                  style={{
                    flexDirection: "row",
                    justifyContent: "center",
                    marginTop: 2,
                  }}
                >
                  <Text style={{ fontSize: 8.5 }}>School Website: </Text>
                  <Link
                    src={
                      cleanSchoolWebsite.startsWith("http")
                        ? cleanSchoolWebsite
                        : `https://${cleanSchoolWebsite}`
                    }
                    style={{
                      fontSize: 8.5,
                      color: "#e74c3c",
                      textDecoration: "underline",
                    }}
                  >
                    {cleanSchoolWebsite}
                  </Link>
                </View>
              ) : null}
            </View>

            {/* Right Section: School Code */}
            <View style={{ alignItems: "center", width: 105 }}>
              {institute?.schoolRegistrationNumber ? (
                <Text style={{ fontSize: 8, marginBottom: 2, textAlign: "center" }}>
                  Registration No : {String(institute.schoolRegistrationNumber).trim()}
                </Text>
              ) : null}
              {institute?.schoolCode ? (
                <Text style={{ fontSize: 8, textAlign: "center" }}>
                  School Code : {String(institute.schoolCode).trim()}
                </Text>
              ) : null}
            </View>
          </View>
        </View>

        {/* Report Card Title & Student Details */}
        <View
          style={{
            height: "auto",
            backgroundColor: "#fff",
            marginBottom: 4,
            border: "1pt solid #222",
            borderRadius: 4,
          }}
          wrap={false}
        >
          <Text style={styles.reportTitle}>Annual Report Card</Text>
          <Text style={styles.reportsubTitle}>
            Class : {studentClass}     Section : {studentSection}        Session :{" "}
            {sessionText}
          </Text>

          <Text style={styles.sectionTitle}>Student Details</Text>
          <View style={styles.detailsRow}>
            <View style={styles.detailsCol}>
              <Text style={styles.detailText}>
                Admission No : {details.admissionNumber || "-"}
              </Text>
              <Text style={[styles.detailText, styles.capitalize]}>
                Student Name : {studentName}
              </Text>
              <Text style={[styles.detailText, styles.capitalize]}>
                Mother&apos;s Name : {details.motherName || "-"}
              </Text>
              <Text style={[styles.detailText, styles.capitalize]}>
                Father&apos;s Name : {details.fatherName || "-"}
              </Text>
              <Text style={styles.detailTextLast}>
                Address : {formattedAddress}
              </Text>
            </View>
            <View style={{ ...styles.detailsCol, borderLeft: "1pt solid #222" }}>
              <Text style={styles.detailText}>Roll No : {studentRoll}</Text>
              <Text style={styles.detailText}>DOB : {formattedDob}</Text>
              <Text style={styles.detailTextLast}>
                Contact no : {details.phone || "-"}
              </Text>
            </View>
          </View>
        </View>

        {/* Academic Performance : <prevYear> to <currYear> (Dynamic N-Exam Summary Table) */}
        <View style={styles.table} wrap={false}>
          <Text
            style={[styles.sectionAcademicTitle, { backgroundColor: "#8fd18f" }]}
          >
            Academic Performance : {resolvedYearRange}
          </Text>
          <View style={styles.tableHeader}>
            <Text style={[styles.tableCellHeader, { flex: 2.2 }]}>
              Internal Exams
            </Text>
            <Text style={styles.tableCellHeader}>Total Marks</Text>
            <Text style={styles.tableCellHeader}>Obtained Marks</Text>
            <Text style={[styles.tableCellHeader, { flex: 0.8, borderRight: "none" }]}>
              Grade
            </Text>
          </View>
          {examSummaries.map((exam, idx) => (
            <View style={styles.tableRow} key={`${exam?.examType || idx}-${idx}`}>
              <Text style={[examCellStyle, { flex: 2.2, textAlign: "left", paddingLeft: 6 }]}>
                {exam?.examType || "-"}
              </Text>
              <Text style={examCellStyle}>
                {formatPdfMark(exam?.totalFullMarks ?? 0)}
              </Text>
              <Text style={examCellStyle}>
                {exam?.present === false
                  ? "0 (Absent)"
                  : formatPdfMark(exam?.totalObtained ?? 0)}
              </Text>
              <Text style={[examCellStyle, { flex: 0.8, borderRight: "none" }]}>
                {exam?.grade || "-"}
              </Text>
            </View>
          ))}
        </View>

        {/* Grand Total & Result */}
        <View style={styles.table} wrap={false}>
          <Text style={styles.coScholasticHeader}>Grand Total &amp; Result</Text>
          {[
            ["TOTAL MARKS", `${formatPdfMark(grandTotalFull)}`],
            ["TOTAL MARKS OBTAINED", `${formatPdfMark(grandTotalObtained)}`],
            ["PERCENTAGE", `${overallPercentage}%`],
            ["GRADE", `${overallGrade}`],
            ["RESULT", isPass ? "PASS (P)" : "FAIL (F)"],
          ].map(([area, val], idx) => (
            <View style={styles.coScholasticRow} key={idx}>
              <Text style={styles.coScholasticCell}>{area}</Text>
              <Text style={styles.coScholasticCellGrade}>{val}</Text>
            </View>
          ))}
          <View style={styles.coScholasticRow}>
            <Text
              style={{
                flex: 3,
                paddingVertical: 2.5,
                paddingHorizontal: 4,
                textAlign: "left",
                borderRight: "none",
                fontSize: 8.5,
              }}
            >
              Class Teacher Remarks :
            </Text>
          </View>
        </View>

        {/* Dynamic Fixed 7-Tier Grade Scale */}
        <View style={styles.gradingTable} wrap={false}>
          <Text style={styles.gradingHeader}>Grade Scale</Text>
          <View style={styles.gradingRow}>
            <Text style={[styles.gradingCell, { fontWeight: "bold" }]}>
              Score Range (Marks)
            </Text>
            <Text style={[styles.gradingCell, { fontWeight: "bold" }]}>
              Grade
            </Text>
            <Text
              style={[
                styles.gradingCell,
                { fontWeight: "bold", borderRight: "none" },
              ]}
            >
              Performance Indicator
            </Text>
          </View>
          {scaleToRender.map((g, idx) => (
            <View style={styles.gradingRow} key={`${g.letterGrade}-${idx}`}>
              <Text style={styles.gradingCell}>
                {g.minMarks} - {g.maxMarks}
              </Text>
              <Text style={styles.gradingCell}>{g.letterGrade}</Text>
              <Text style={[styles.gradingCell, { borderRight: "none" }]}>
                {g.performanceIndicator}
              </Text>
            </View>
          ))}
        </View>

        {/* Remarks, Date, Place, Signatures (with Principal Signature) */}
        <View style={styles.signatureBox} wrap={false}>
          <View style={styles.detailsRow}>
            <Text style={{ fontSize: 9 }}>Date :</Text>
          </View>
          <View style={styles.signatureRow}>
            <Text>Place :</Text>
            <Text>Class Teacher</Text>
            <View style={{ alignItems: "center", width: 105 }}>
              {resolvedPrincipalSignature ? (
                <Image
                  src={resolvedPrincipalSignature}
                  style={{
                    width: 88,
                    height: 32,
                    objectFit: "contain",
                    marginBottom: 2,
                  }}
                />
              ) : (
                <View style={{ height: 32 }} />
              )}
              <Text>Principal</Text>
            </View>
          </View>
        </View>

        {/* Notes section */}
        <View style={styles.notesSection} wrap={false}>
          <Text style={{ fontSize: 8.5, fontWeight: "bold", marginBottom: 2 }}>
            KEEP NOTES:
          </Text>
          <View style={styles.noteRow}>
            <Text>P - For Successful Students</Text>
            <Text>F - For Unsuccessful Students</Text>
          </View>
          <Text style={{ fontSize: 7.5, marginBottom: 1.5 }}>
            Note: This is a computer-generated report card and does not require a
            signature.
          </Text>
          <Text style={{ fontSize: 7.5 }}>
            For any discrepancies, please contact the school administration within
            7 days of issue.
          </Text>
        </View>

        <View
          style={{
            position: "absolute",
            bottom: 8,
            left: 0,
            right: 0,
            textAlign: "center",
          }}
        >
          <Text style={{ fontSize: 7.5, color: "#666" }}>
            Generated by {cleanSchoolName} Management System
          </Text>
        </View>
      </Page>
    </Document>
  );
};

AnnualPdfDocument.propTypes = {
  institute: PropTypes.shape({
    schoolLogo: PropTypes.string,
    schoolName: PropTypes.string,
    schoolAddress: PropTypes.string,
    schoolEmail: PropTypes.string,
    schoolPhone: PropTypes.string,
    schoolWebsite: PropTypes.string,
    schoolRegistrationNumber: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    schoolCode: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  }),
  studentData: PropTypes.shape({
    rollNumber: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    studentName: PropTypes.string,
    studentDetails: PropTypes.shape({
      studentName: PropTypes.string,
      className: PropTypes.string,
      section: PropTypes.string,
      rollNo: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
      admissionNumber: PropTypes.string,
      motherName: PropTypes.string,
      fatherName: PropTypes.string,
      dob: PropTypes.string,
      phone: PropTypes.string,
      academicYear: PropTypes.string,
      villagePost: PropTypes.string,
      district: PropTypes.string,
      state: PropTypes.string,
      pincode: PropTypes.string,
      country: PropTypes.string,
    }),
    examSummaries: PropTypes.arrayOf(
      PropTypes.shape({
        examType: PropTypes.string,
        totalFullMarks: PropTypes.number,
        totalObtained: PropTypes.number,
        percentage: PropTypes.number,
        grade: PropTypes.string,
        present: PropTypes.bool,
      })
    ),
    grandTotalFullMarks: PropTypes.number,
    grandTotalObtained: PropTypes.number,
    overallPercentage: PropTypes.number,
    overallGrade: PropTypes.string,
    result: PropTypes.string,
  }),
  className: PropTypes.string,
  section: PropTypes.string,
  academicYearRange: PropTypes.string,
  dynamicGradeScale: PropTypes.arrayOf(
    PropTypes.shape({
      minMarks: PropTypes.number,
      maxMarks: PropTypes.number,
      letterGrade: PropTypes.string,
      performanceIndicator: PropTypes.string,
    })
  ),
  signaturePreview: PropTypes.string,
};

const AnnualMarksheetComponent = ({
  studentData,
  className = "",
  section = "",
  academicYearRange = "",
  gradeSystem = [],
  dynamicGradeScale = [],
  signaturePreview = null,
}) => {
  const { theme } = useTheme();
  const darkMode = theme === "light";
  const institute = useSelector((state) => state.institute.institute) || {};
  const [open, setOpen] = useState(false);
  const [isDownloading, setIsDownloading] = useState(false);
  const [isOpeningTab, setIsOpeningTab] = useState(false);

  const resolvedYearRange = academicYearRange || getDynamicAcademicYearRange();
  const resolvedScale = useMemo(() => {
    if (Array.isArray(dynamicGradeScale) && dynamicGradeScale.length === 7) {
      return dynamicGradeScale;
    }
    return buildDynamicSevenGradeScale(
      gradeSystem,
      Number(studentData?.grandTotalFullMarks) || 0
    );
  }, [dynamicGradeScale, gradeSystem, studentData?.grandTotalFullMarks]);

  const safeStudentName =
    studentData?.studentDetails?.studentName || studentData?.studentName || "Student";
  const safeClass = studentData?.studentDetails?.className || className || "Class";
  const safeSection = studentData?.studentDetails?.section || section || "Sec";
  const safeYearTag = resolvedYearRange.replace(/\s+/g, "_");
  const sanitizeFilePart = (val) =>
    String(val || "")
      .trim()
      .replace(/[\\/:*?"<>|\s]+/g, "_")
      .replace(/^_+|_+$/g, "") || "Report";
  const fileName = `Annual_Report_Card_${sanitizeFilePart(safeStudentName)}_${sanitizeFilePart(safeClass)}_${sanitizeFilePart(safeSection)}_${sanitizeFilePart(safeYearTag)}.pdf`;

  const createPdfBlob = () =>
    pdf(
      <AnnualPdfDocument
        institute={institute}
        studentData={studentData}
        className={safeClass}
        section={safeSection}
        academicYearRange={resolvedYearRange}
        dynamicGradeScale={resolvedScale}
        signaturePreview={signaturePreview}
      />
    ).toBlob();

  const handleDownload = async () => {
    try {
      setIsDownloading(true);
      const blob = await createPdfBlob();
      saveAs(blob, fileName);
    } catch {
      toast.error("Unable to generate annual report card PDF");
    } finally {
      setIsDownloading(false);
    }
  };

  const handleOpenInNewTab = async () => {
    try {
      setIsOpeningTab(true);
      const blob = await createPdfBlob();
      const blobUrl = URL.createObjectURL(blob);
      const opened = window.open(blobUrl, "_blank", "noopener,noreferrer");
      if (!opened) {
        saveAs(blob, fileName);
      }
      window.setTimeout(() => URL.revokeObjectURL(blobUrl), 60000);
    } catch {
      toast.error("Unable to open annual report card PDF");
    } finally {
      setIsOpeningTab(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <button
          type="button"
          className="ms-btn-icon ms-btn-primary inline-flex items-center justify-center gap-2 px-2 text-sm"
          aria-label={`Download annual report card PDF for ${safeStudentName}`}
        >
          <Download
            className="h-4 w-4 shrink-0"
            strokeWidth={1.5}
            aria-hidden="true"
          />
          <span className="ms-btn-mobile-label">Annual PDF</span>
        </button>
      </DialogTrigger>

      <DialogContent
        data-theme={darkMode ? "dark" : "light"}
        className="marksheet-portal flex w-[calc(100vw-1rem)] max-w-[900px] max-h-[92dvh] flex-col gap-4 overflow-hidden rounded-2xl border border-[var(--ms-border-strong)] bg-[var(--ms-surface)] p-4 text-[var(--ms-text-primary)] shadow-[var(--ms-shadow-floating)] sm:w-[calc(100vw-2rem)] sm:p-6"
      >
        <DialogHeader className="space-y-1 border-b border-[var(--ms-border)] pb-3 pr-10 text-left">
          <div className="ms-signature-rule mb-2" aria-hidden="true" />
          <DialogTitle className="text-lg font-medium tracking-normal text-[var(--ms-text-primary)]">
            Annual Report Card PDF
          </DialogTitle>
          <DialogDescription className="text-sm text-[var(--ms-text-secondary)]">
            <span className="capitalize">{safeStudentName}</span> · Class{" "}
            {safeClass} ({safeSection}) · Academic Performance {resolvedYearRange}
          </DialogDescription>
        </DialogHeader>

        <div className="ms-pdf-frame">
          {open ? (
            <PDFViewer
              width="100%"
              height="100%"
              showToolbar={true}
              className="h-full w-full border-0"
            >
              <AnnualPdfDocument
                institute={institute}
                studentData={studentData}
                className={safeClass}
                section={safeSection}
                academicYearRange={resolvedYearRange}
                dynamicGradeScale={resolvedScale}
                signaturePreview={signaturePreview}
              />
            </PDFViewer>
          ) : null}
        </div>

        <div className="flex flex-col-reverse gap-2 border-t border-[var(--ms-border)] pt-3 sm:flex-row sm:items-center sm:justify-between">
          <p className="text-xs text-[var(--ms-text-muted)]">
            Preview unavailable on some mobile browsers — use open or download
            below
          </p>
          <div className="flex flex-col-reverse gap-2 sm:flex-row sm:items-center">
            <button
              type="button"
              onClick={handleOpenInNewTab}
              disabled={isOpeningTab || isDownloading}
              className="ms-control ms-btn-secondary inline-flex w-full items-center justify-center gap-2 px-4 text-sm disabled:cursor-not-allowed disabled:opacity-50 sm:w-auto"
            >
              {isOpeningTab ? (
                <Loader2
                  className="h-4 w-4 animate-spin"
                  strokeWidth={1.5}
                  aria-hidden="true"
                />
              ) : (
                <ExternalLink
                  className="h-4 w-4"
                  strokeWidth={1.5}
                  aria-hidden="true"
                />
              )}
              <span>Open in tab</span>
            </button>

            <button
              type="button"
              onClick={handleDownload}
              disabled={isDownloading || isOpeningTab}
              className="ms-control ms-btn-primary inline-flex w-full items-center justify-center gap-2 px-4 text-sm disabled:cursor-not-allowed disabled:opacity-50 sm:w-auto"
            >
              {isDownloading ? (
                <Loader2
                  className="h-4 w-4 animate-spin"
                  strokeWidth={1.5}
                  aria-hidden="true"
                />
              ) : (
                <Download
                  className="h-4 w-4"
                  strokeWidth={1.5}
                  aria-hidden="true"
                />
              )}
              <span>{isDownloading ? "Preparing PDF" : "Download PDF"}</span>
            </button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

AnnualMarksheetComponent.propTypes = {
  studentData: AnnualPdfDocument.propTypes.studentData.isRequired,
  className: PropTypes.string,
  section: PropTypes.string,
  academicYearRange: PropTypes.string,
  gradeSystem: PropTypes.arrayOf(PropTypes.object),
  dynamicGradeScale: AnnualPdfDocument.propTypes.dynamicGradeScale,
  signaturePreview: PropTypes.string,
};

export default AnnualMarksheetComponent;
