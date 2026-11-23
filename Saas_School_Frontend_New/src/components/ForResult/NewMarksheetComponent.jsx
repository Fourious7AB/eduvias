import React from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import { Button } from "../ui/button";
import { useTheme } from "@/context/ThemeContext";
import { useSelector } from "react-redux";
import { saveAs } from "file-saver";
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
import { Download } from "lucide-react";
import {
  formatDateDDMMYYYY,
  recalculateMarksheet,
  resolveSubjectFullMarks,
} from "@/utils/result/resultHelpers";

const styles = StyleSheet.create({
  page: {
    backgroundColor: "#fff",
    padding: 16,
    fontSize: 10,
    fontFamily: "Helvetica",
  },
  header: {
    border: "1pt solid #222",
    padding: 8,
    marginBottom: 8,
  },
  schoolName: {
    fontSize: 14,
    fontWeight: "bold",
    textAlign: "center",
    marginBottom: 2,
  },
  reportTitle: {
    fontSize: 14,
    fontWeight: "medium",
    textAlign: "center",
    paddingTop: 4,
    paddingBottom: 2,
  },
  reportsubTitle: {
    fontSize: 10,
    fontWeight: "normal",
    textAlign: "center",
    marginBottom: 4,
  },
  sectionTitle: {
    backgroundColor: "#ffbb00",
    fontWeight: "normal",
    paddingTop: 4,
    paddingBottom: 4,
    fontSize: 12,
    borderTop: "1pt solid #222",
    borderBottom: "1pt solid #222",
    textAlign: "center",
  },
  sectionAcademicTitle: {
    fontWeight: "normal",
    paddingTop: 4,
    paddingBottom: 4,
    fontSize: 12,
    textAlign: "center",
  },
  detailsRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 2,
  },
  detailsCol: {
    width: "48%",
    padding: 4,
  },
  table: {
    borderRight: "1pt solid #222",
    borderLeft: "1pt solid #222",
    borderTop: "1pt solid #222",
    marginTop: 6,
    marginBottom: 6,
  },
  tableHeader: {
    flexDirection: "row",
    borderBottom: "1pt solid #222",
    borderTop: "1pt solid #222",
  },
  tableCellHeader: {
    flex: 1,
    fontWeight: "normal",
    padding: 2,
    borderRight: "1pt solid #222",
    textAlign: "center",
  },
  tableRow: {
    flexDirection: "row",
    borderBottom: "1pt solid #222",
  },
  tableCell: {
    flex: 1,
    padding: 2,
    fontSize: 8,
    borderRight: "1pt solid #222",
    textAlign: "center",
  },
  coScholasticHeader: {
    backgroundColor: "#b6d6f6",
    fontWeight: "medium",
    padding: 2,
    fontSize: 10,
    textAlign: "center",
    borderBottom: "1pt solid #222",
  },
  coScholasticRow: {
    flexDirection: "row",
    borderBottom: "1pt solid #222",
  },
  coScholasticCell: {
    flex: 2,
    padding: 2,
    borderRight: "1pt solid #222",
  },
  coScholasticCellGrade: {
    flex: 1,
    padding: 2,
    textAlign: "center",
  },
  signatureRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 24,
    fontSize: 10,
  },
  gradingTable: {
    marginTop: 12,
    border: "1pt solid #222",
  },
  gradingHeader: {
    backgroundColor: "#8c6bc5",
    padding: 4,
    color: "white",
    textAlign: "center",
    fontSize: 10,
    borderBottom: "1pt solid #222",
  },
  gradingRow: {
    flexDirection: "row",
    borderBottom: "1pt solid #222",
  },
  gradingCell: {
    flex: 1,
    padding: 3,
    fontSize: 8,
    borderRight: "1pt solid #222",
    textAlign: "center",
  },
  notesSection: {
    marginTop: 12,
    fontSize: 9,
  },
  noteItem: {
    flexDirection: "row",
    marginBottom: 4,
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

  return (
    <Document>
      <Page size="A4" style={styles.page}>
        {/* Header */}
        <View style={styles.header}>
          <View
            style={{
              flexDirection: "row",
              alignItems: "center",
              justifyContent: "space-between",
            }}
          >
            {/* Left Section: Logo */}
            <View style={{ alignItems: "center", width: 90 }}>
              {institute?.schoolLogo ? (
                <Image
                  src={institute.schoolLogo}
                  style={{ width: 70, height: 70 }}
                />
              ) : (
                <View style={{ width: 70, height: 70 }} />
              )}
            </View>

            {/* Center Section: School Name and Details */}
            <View style={{ flex: 1, alignItems: "center" }}>
              <Text style={styles.schoolName}>
                {institute?.schoolName || "School Report Card"}
              </Text>
              {institute?.schoolAddress ? (
                <Text
                  style={{
                    fontSize: 9,
                    textAlign: "center",
                    marginBottom: 2,
                    fontStyle: "italic",
                    width: "70%",
                  }}
                >
                  {institute.schoolAddress}
                </Text>
              ) : null}
              {institute?.schoolEmail ? (
                <Text style={{ fontSize: 9, textAlign: "center" }}>
                  {institute.schoolEmail}
                </Text>
              ) : null}
              {institute?.schoolPhone ? (
                <Text style={{ fontSize: 9, textAlign: "center" }}>
                  Contact No : {institute.schoolPhone}
                </Text>
              ) : null}

              {institute?.schoolWebsite ? (
                <View
                  style={{
                    flexDirection: "row",
                    justifyContent: "center",
                    marginTop: 4,
                  }}
                >
                  <Text style={{ fontSize: 9 }}>School Website: </Text>
                  <Link
                    src={
                      institute.schoolWebsite.startsWith("http")
                        ? institute.schoolWebsite
                        : `https://${institute.schoolWebsite}`
                    }
                    style={{
                      fontSize: 9,
                      color: "#e74c3c",
                      textDecoration: "underline",
                    }}
                  >
                    {institute.schoolWebsite}
                  </Link>
                </View>
              ) : null}
            </View>

            {/* Right Section: School Code */}
            <View style={{ alignItems: "center", width: 100 }}>
              {institute?.schoolRegistrationNumber ? (
                <Text style={{ fontSize: 8, marginBottom: 2 }}>
                  Registration No : {institute.schoolRegistrationNumber}
                </Text>
              ) : null}
              {institute?.schoolCode ? (
                <Text style={{ fontSize: 8, marginBottom: 2 }}>
                  School Code : {institute.schoolCode}
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
              <Text style={{ paddingBottom: 4 }}>
                Admission No : {details.admissionNumber || "-"}
              </Text>
              <Text style={{ paddingBottom: 4, textTransform: "capitalize" }}>
                Student Name : {studentName}
              </Text>
              <Text style={{ paddingBottom: 4, textTransform: "capitalize" }}>
                Mother&apos;s Name : {details.motherName || "-"}
              </Text>
              <Text style={{ paddingBottom: 4, textTransform: "capitalize" }}>
                Father&apos;s Name : {details.fatherName || "-"}
              </Text>
              <Text style={{ paddingBottom: 4 }}>
                Address : {formattedAddress}
              </Text>
            </View>
            <View style={{ ...styles.detailsCol, borderLeft: "1pt solid #222" }}>
              <Text style={{ paddingBottom: 4 }}>Roll No : {studentRoll}</Text>
              <Text style={{ paddingBottom: 4 }}>DOB : {formattedDob}</Text>
              <Text style={{ paddingBottom: 4 }}>
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
          <View style={styles.tableHeader}>
            <Text style={styles.tableCellHeader}>Subject</Text>
            <Text style={styles.tableCellHeader}>Subject&apos;s Full Marks</Text>
            <Text style={styles.tableCellHeader}>Written</Text>
            <Text style={styles.tableCellHeader}>Project&apos;s Full Marks</Text>
            <Text style={styles.tableCellHeader}>Project</Text>
            <Text style={styles.tableCellHeader}>Total</Text>
            <Text style={styles.tableCellHeader}>Grade</Text>
          </View>
          {(marksheetData?.subjects || []).map((subject, idx) => (
            <View style={styles.tableRow} key={idx}>
              <Text style={[styles.tableCell, styles.capitalize]}>
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
              <Text style={styles.tableCell}>
                {subject?.grade || (subject?.isAdditional ? "Additional" : "-")}
              </Text>
            </View>
          ))}
        </View>

        {/* Grand Total & Result */}
        <View style={styles.table}>
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
                padding: 4,
                textAlign: "left",
                borderRight: "none",
                fontSize: 10,
              }}
            >
              Class Teacher Remarks :
            </Text>
          </View>
        </View>

        {/* Grading Scale (when configured) */}
        {Array.isArray(gradeSystem) && gradeSystem.length > 0 ? (
          <View style={styles.gradingTable}>
            <Text style={styles.gradingHeader}>Grade Scale</Text>
            <View style={styles.gradingRow}>
              <Text style={styles.gradingCell}>Score Range (%)</Text>
              <Text style={styles.gradingCell}>Grade</Text>
              <Text style={styles.gradingCell}>Performance Indicator</Text>
            </View>
            {gradeSystem.map((g, idx) => (
              <View style={styles.gradingRow} key={idx}>
                <Text style={styles.gradingCell}>
                  {g.scaleStartMarks} - {g.scaleEndMarks}
                </Text>
                <Text style={styles.gradingCell}>{g.letterGrade}</Text>
                <Text style={styles.gradingCell}>{g.performanceIndicator}</Text>
              </View>
            ))}
          </View>
        ) : null}

        {/* Remarks, Date, Place, Signatures */}
        <View
          style={{
            border: "1pt solid #222",
            borderRadius: 4,
            padding: 8,
            marginTop: 10,
          }}
        >
          <View style={styles.detailsRow}>
            <Text>Date :</Text>
          </View>
          <View
            style={[
              styles.signatureRow,
              { alignItems: "flex-end", marginTop: 10 },
            ]}
          >
            <Text>Place :</Text>
            <Text>Class Teacher</Text>
            <View style={{ alignItems: "center", width: 100 }}>
              {signaturePreview ? (
                <Image
                  src={signaturePreview}
                  style={{ width: 90, height: 45, marginBottom: 0 }}
                />
              ) : (
                <View style={{ height: 40 }} />
              )}
              <Text>Principal</Text>
            </View>
          </View>
        </View>

        {/* Notes section */}
        <View style={styles.notesSection}>
          <Text style={{ fontSize: 10, fontWeight: "bold", marginBottom: 4 }}>
            KEEP NOTES:
          </Text>
          <View style={styles.noteItem}>
            <Text style={{ width: 20 }}>P</Text>
            <Text>- For Successful Students</Text>
          </View>
          <View style={styles.noteItem}>
            <Text style={{ width: 20 }}>F</Text>
            <Text>- For Unsuccessful Students</Text>
          </View>
        </View>

        <View style={{ marginTop: 10, fontSize: 8 }}>
          <Text style={{ marginBottom: 4 }}>
            Note: This is a computer-generated report card and does not require a
            signature.
          </Text>
          <Text>
            For any discrepancies, please contact the school administration within
            7 days of issue.
          </Text>
        </View>

        <View
          style={{
            position: "absolute",
            bottom: 16,
            left: 0,
            right: 0,
            textAlign: "center",
          }}
        >
          <Text style={{ fontSize: 8, color: "#666" }}>
            Generated by {institute?.schoolName || "School"} Management System
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

  const handleDownload = async () => {
    const blob = await pdf(
      <PdfDocument
        marksheetData={allMarkSheetData}
        gradeSystem={gradeSystem}
        institute={institute}
        totalExamMarks={totalExamMarks}
        signaturePreview={signaturePreview}
      />
    ).toBlob();
    saveAs(
      blob,
      `Marksheet_${safeStudentName}_${safeClass}_${safeSection}_${safeExam}.pdf`
    );
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button
          size="sm"
          className="bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-700 hover:to-pink-600 transition-all duration-300"
        >
          <Download className="h-4 w-4" />
          <span className="sr-only">Download</span>
        </Button>
      </DialogTrigger>

      <DialogContent
        className={`sm:max-w-[900px] max-w-[95%] ${
          darkMode ? "bg-gray-800 text-white border-gray-700" : "bg-white"
        }`}
      >
        <DialogHeader>
          <DialogTitle
            className={`border-b pb-3 ${
              darkMode
                ? "border-gray-700 text-white"
                : "border-gray-200 text-gray-900"
            }`}
          >
            Download{" "}
            <span className="font-bold text-blue-500 underline capitalize">
              {safeStudentName}&apos;s {safeExam}
            </span>{" "}
            Marksheet
          </DialogTitle>
        </DialogHeader>
        <PDFViewer width="100%" height={700} showToolbar={true}>
          <PdfDocument
            institute={institute}
            marksheetData={allMarkSheetData}
            gradeSystem={gradeSystem}
            totalExamMarks={totalExamMarks}
            signaturePreview={signaturePreview}
          />
        </PDFViewer>
        <div className="mt-4 flex justify-end">
          <Button
            onClick={handleDownload}
            className="bg-green-600 hover:bg-green-700 text-white"
          >
            Download PDF
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default NewMarksheetComponent;
