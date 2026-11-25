import { useState } from "react";
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
  formatDateDDMMYYYY,
  recalculateMarksheet,
  resolveSubjectFullMarks,
} from "@/utils/result/resultHelpers";
import "@/pages/Result/MarksheetPage.css";

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
    paddingVertical: 2,
    paddingHorizontal: 2,
    borderRight: "1pt solid #222",
    textAlign: "center",
  },
  tableRow: {
    flexDirection: "row",
    borderBottom: "1pt solid #222",
  },
  tableCell: {
    flex: 1,
    paddingVertical: 1.8,
    paddingHorizontal: 2,
    fontSize: 8,
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

const PdfDocument = ({
  institute = {},
  marksheetData = {},
  gradeSystem = [],
  totalExamMarks = 0,
  signaturePreview,
}) => {
  const details = marksheetData?.studentDetails || {};
  const studentName = details.studentName || marksheetData?.name || "-";
  const studentClass = details.className || marksheetData?.class || "-";
  const studentSection = details.section || marksheetData?.section || "-";
  const studentRoll = details.rollNo ?? marksheetData?.roll ?? "-";
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
          <Text style={styles.reportTitle}>
            Report Card ({marksheetData?.examType || "-"})
          </Text>
          <Text style={styles.reportsubTitle}>
            Class : {studentClass}     Section : {studentSection}        Session :{" "}
            {details.academicYear || "-"}
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

        {/* Academic Performance Table */}
        <View style={styles.table}>
          <Text
            style={[styles.sectionAcademicTitle, { backgroundColor: "#8fd18f" }]}
          >
            Academic Performance : {marksheetData?.examType || "-"}
          </Text>
          <View style={styles.tableHeader} wrap={false}>
            <Text style={[styles.tableCellHeader, { flex: 1.4 }]}>Subject</Text>
            <Text style={styles.tableCellHeader}>Subject&apos;s Full Marks</Text>
            <Text style={styles.tableCellHeader}>Written</Text>
            <Text style={styles.tableCellHeader}>Project&apos;s Full Marks</Text>
            <Text style={styles.tableCellHeader}>Project</Text>
            <Text style={styles.tableCellHeader}>Total</Text>
            <Text style={[styles.tableCellHeader, { borderRight: "none" }]}>
              Grade
            </Text>
          </View>
          {(marksheetData?.subjects || []).map((subject, idx) => (
            <View style={styles.tableRow} key={idx} wrap={false}>
              <Text style={[styles.tableCell, styles.capitalize, { flex: 1.4 }]}>
                {subject?.subject || "-"}
              </Text>
              <Text style={styles.tableCell}>
                {subject?.subjectFullMark ?? subject?.fullMarks ?? 0}
              </Text>
              <Text style={styles.tableCell}>{subject?.writtenMarks ?? 0}</Text>
              <Text style={styles.tableCell}>
                {subject?.projectFullMark ?? 0}
              </Text>
              <Text style={styles.tableCell}>{subject?.projectMarks ?? 0}</Text>
              <Text style={styles.tableCell}>{subject?.totalMarks ?? 0}</Text>
              <Text style={[styles.tableCell, { borderRight: "none" }]}>
                {subject?.grade || (subject?.isAdditional ? "Additional" : "-")}
              </Text>
            </View>
          ))}
        </View>

        {/* Grand Total & Result */}
        <View style={styles.table} wrap={false}>
          <Text style={styles.coScholasticHeader}>Grand Total &amp; Result</Text>
          {Object.entries({
            "TOTAL MARKS": `${totalExamMarks}`,
            "TOTAL MARKS OBTAINED": `${marksheetData?.totalMarks ?? 0}`,
            PERCENTAGE: `${marksheetData?.overallPercentage ?? 0}%`,
            GRADE: `${marksheetData?.overallGrade || "-"}`,
            RESULT: `${marksheetData?.result === "P" ? "PASS (P)" : "FAIL (F)"}`,
          }).map(([area, val], idx) => (
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

        {/* Grading Scale (when configured) */}
        {Array.isArray(gradeSystem) && gradeSystem.length > 0 ? (
          <View style={styles.gradingTable} wrap={false}>
            <Text style={styles.gradingHeader}>Grade Scale</Text>
            <View style={styles.gradingRow}>
              <Text style={[styles.gradingCell, { fontWeight: "bold" }]}>
                Score Range (%)
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
            {gradeSystem.map((g, idx) => (
              <View style={styles.gradingRow} key={idx}>
                <Text style={styles.gradingCell}>
                  {g.scaleStartMarks} - {g.scaleEndMarks}
                </Text>
                <Text style={styles.gradingCell}>{g.letterGrade}</Text>
                <Text style={[styles.gradingCell, { borderRight: "none" }]}>
                  {g.performanceIndicator}
                </Text>
              </View>
            ))}
          </View>
        ) : null}

        {/* Remarks, Date, Place, Signatures */}
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

const NewMarksheetComponent = ({
  marksheetData,
  gradeSystem = [],
  passingMark = null,
  signaturePreview,
  additionalMarksheetData = [],
}) => {
  const { theme } = useTheme();
  const darkMode = theme === "light";
  const institute = useSelector((state) => state.institute.institute) || {};
  const [isDownloading, setIsDownloading] = useState(false);
  const [isOpeningTab, setIsOpeningTab] = useState(false);

  const additionalEntry = (additionalMarksheetData || []).find(
    (item) => Number(item.roll) === Number(marksheetData?.roll)
  );

  const mergedRaw =
    additionalEntry?.subjects?.length > 0
      ? {
          ...marksheetData,
          subjects: [
            ...(marksheetData?.subjects || []),
            ...additionalEntry.subjects,
          ],
        }
      : marksheetData;

  // Keep PDF totals, percentage, grade, and pass/fail 100% consistent with the View modal
  const allMarkSheetData = recalculateMarksheet(
    mergedRaw,
    gradeSystem,
    passingMark
  );

  const totalExamMarks =
    allMarkSheetData?.totalFullMarks ??
    (allMarkSheetData?.subjects || []).reduce(
      (acc, subject) => acc + resolveSubjectFullMarks(subject),
      0
    );

  const safeStudentName =
    marksheetData?.studentDetails?.studentName || marksheetData?.name || "Student";
  const safeClass =
    marksheetData?.studentDetails?.className || marksheetData?.class || "Class";
  const safeSection =
    marksheetData?.studentDetails?.section || marksheetData?.section || "Sec";
  const safeExam = marksheetData?.examType || "Exam";

  const createPdfBlob = () =>
    pdf(
      <PdfDocument
        marksheetData={allMarkSheetData}
        gradeSystem={gradeSystem}
        institute={institute}
        totalExamMarks={totalExamMarks}
        signaturePreview={signaturePreview}
      />
    ).toBlob();

  const handleDownload = async () => {
    try {
      setIsDownloading(true);
      const blob = await createPdfBlob();
      saveAs(
        blob,
        `Marksheet_${safeStudentName}_${safeClass}_${safeSection}_${safeExam}.pdf`
      );
    } catch {
      toast.error("Unable to generate marksheet PDF");
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
        saveAs(
          blob,
          `Marksheet_${safeStudentName}_${safeClass}_${safeSection}_${safeExam}.pdf`
        );
      }
      window.setTimeout(() => URL.revokeObjectURL(blobUrl), 60000);
    } catch {
      toast.error("Unable to open marksheet PDF");
    } finally {
      setIsOpeningTab(false);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <button
          type="button"
          className="ms-btn-icon ms-btn-primary inline-flex items-center justify-center gap-2 px-2 text-sm"
          aria-label={`Download marksheet PDF for ${safeStudentName}`}
        >
          <Download
            className="h-4 w-4 shrink-0"
            strokeWidth={1.5}
            aria-hidden="true"
          />
          <span className="ms-btn-mobile-label">PDF</span>
        </button>
      </DialogTrigger>

      <DialogContent
        data-theme={darkMode ? "dark" : "light"}
        className="marksheet-portal flex w-[calc(100vw-1rem)] max-w-[900px] max-h-[92dvh] flex-col gap-4 overflow-hidden rounded-2xl border border-[var(--ms-border-strong)] bg-[var(--ms-surface)] p-4 text-[var(--ms-text-primary)] shadow-[var(--ms-shadow-floating)] sm:w-[calc(100vw-2rem)] sm:p-6"
      >
        <DialogHeader className="space-y-1 border-b border-[var(--ms-border)] pb-3 pr-10 text-left">
          <div className="ms-signature-rule mb-2" aria-hidden="true" />
          <DialogTitle className="text-lg font-medium tracking-normal text-[var(--ms-text-primary)]">
            Marksheet PDF
          </DialogTitle>
          <DialogDescription className="text-sm text-[var(--ms-text-secondary)]">
            <span className="capitalize">{safeStudentName}</span> · Class{" "}
            {safeClass} ({safeSection}) · {safeExam}
          </DialogDescription>
        </DialogHeader>

        <div className="ms-pdf-frame">
          <PDFViewer
            width="100%"
            height="100%"
            showToolbar={true}
            className="h-full w-full border-0"
          >
            <PdfDocument
              institute={institute}
              marksheetData={allMarkSheetData}
              gradeSystem={gradeSystem}
              totalExamMarks={totalExamMarks}
              signaturePreview={signaturePreview}
            />
          </PDFViewer>
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

export default NewMarksheetComponent;
