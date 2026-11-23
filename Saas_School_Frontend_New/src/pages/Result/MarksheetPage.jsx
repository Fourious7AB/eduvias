import React, { useEffect, useState } from "react";
import { useTheme } from "@/context/ThemeContext";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import axios from "axios";
import { toast } from "sonner";
import PaginationComponent from "@/components/pagination/PaginationComponent";
import { Button } from "@/components/ui/button";
import { Search, Eye, X } from "lucide-react";
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
import resultUrlApi from "@/common/result";
import mainUrlApi from "@/common/main";
import {
  DEFAULT_GRADE_SCALE,
  capitalizeFirst,
  extractApiError,
  formatDateDDMMYYYY,
  getPercentageBadgeClass,
  parseLocalDateOnly,
  recalculateMarksheet,
} from "@/utils/result/resultHelpers";

const MarksheetPage = () => {
  const { theme } = useTheme();
  const [loading, setLoading] = useState(false);
  const schoolId = useSelector((state) => state?.auth?.schoolId);
  const allClass = useSelector((state) => state.class.classNames) || [];
  const allSection = useSelector((state) => state.section.sectionNames) || [];
  const examTypeData = useSelector((state) => state.examType.examType) || [];
  const [gradeSystem, setGradeSystem] = useState([]);
  const [passingMark, setPassingMark] = useState(null);

  const role = useSelector((state) => state.auth.user);
  const userDetails = useSelector((state) => state.auth.userDetails) || {};
  const [signaturePreview, setSignaturePreview] = useState(null);

  // Search state
  const [searchData, setSearchData] = useState({
    className: "",
    section: "",
    examType: "",
  });

  // Results state
  const [marksheetData, setMarksheetData] = useState([]);
  const [publishMessage, setPublishMessage] = useState("");
  const [lastSearchedContext, setLastSearchedContext] = useState({
    className: "",
    section: "",
    examType: "",
  });

  // Additional marksheet data
  const [additioinalMarksheetData, setAdditioinalMarksheetData] = useState([]);

  // View state
  const [viewStudent, setViewStudent] = useState(null);

  // Pagination state
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [currentPage, setCurrentPage] = useState(1);

  const dataLength = marksheetData?.length || 0;
  const totalPages = Math.max(1, Math.ceil(dataLength / rowsPerPage));
  const safeCurrentPage = Math.min(currentPage, totalPages);
  const paginatedData = marksheetData?.slice(
    (safeCurrentPage - 1) * rowsPerPage,
    safeCurrentPage * rowsPerPage
  );

  const handleRowsPerPageChange = (newRowsPerPage) => {
    setRowsPerPage(newRowsPerPage);
    const newTotalPages = Math.max(1, Math.ceil(dataLength / newRowsPerPage));
    setCurrentPage((prev) => Math.max(1, Math.min(prev, newTotalPages)));
  };

  // Headmaster Signature Photo (optional external schoolfeed service)
  const fetchHeadmasterSignature = async () => {
    if (!mainUrlApi.headmasterSignaturePhoto?.url || !schoolId) return;
    try {
      const response = await axios.get(
        `${mainUrlApi.headmasterSignaturePhoto.url}/${schoolId}`
      );
      if (response?.data?.success && response?.data?.data?.photo?.fileUrl) {
        setSignaturePreview(response.data.data.photo.fileUrl);
      }
    } catch (error) {
      if (error?.response?.status !== 404) {
        console.error("Error fetching headmaster signature:", error);
      }
    }
  };

  useEffect(() => {
    fetchHeadmasterSignature();
  }, [schoolId]);

  const searchMarkSheet = async (e) => {
    e.preventDefault();
    if (!schoolId) return;

    if (role === "student") {
      if (!searchData.examType) {
        toast.error("Please select exam type");
        return;
      }
      if (
        !userDetails?.className ||
        !userDetails?.section ||
        userDetails?.rollNo === undefined ||
        userDetails?.rollNo === null ||
        userDetails?.rollNo === ""
      ) {
        toast.error("Student profile details (class, section, roll no) are missing");
        return;
      }
    } else {
      if (!searchData.className || !searchData.section || !searchData.examType) {
        toast.error("Please select class, section and exam type");
        return;
      }
    }

    try {
      setLoading(true);
      setPublishMessage("");

      const targetClass =
        role === "student" ? userDetails.className : searchData.className;
      const targetSection =
        role === "student" ? userDetails.section : searchData.section;
      const targetExam = searchData.examType;

      let dateMsg = null;
      let showMarksheet = true;

      // Check publish date for students: block ONLY when a future date is explicitly configured
      if (role === "student" && resultUrlApi.resultDate?.search?.url) {
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
            const pDateOnly = parseLocalDateOnly(dateRecords[0].resultPublishDate);
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
              dateMsg = `Result publish date : ${formattedDate}, ${diffDays} day${diffDays === 1 ? "" : "s"} remaining..`;
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
        setCurrentPage(1);
        return;
      }

      const response =
        role === "student"
          ? await axios.get(
              `${resultUrlApi.marksheet.url}/student/${schoolId}`,
              {
                params: {
                  className: targetClass,
                  section: targetSection,
                  examType: targetExam,
                  roll: Number(userDetails.rollNo),
                },
              }
            )
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

      if (response?.data?.data?.markSheets) {
        const scale = response.data.data.gradeScale || [];
        const criteriaPassingMark = response.data.data.passingMark ?? null;
        const normalized = response.data.data.markSheets.map((student) =>
          recalculateMarksheet(student, scale, criteriaPassingMark)
        );
        setMarksheetData(normalized);
        setGradeSystem(scale);
        setPassingMark(criteriaPassingMark);
        setLastSearchedContext({
          className: targetClass,
          section: targetSection,
          examType: targetExam,
        });
        setCurrentPage(1);
        toast.success("Marksheet data retrieved successfully");
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
      setCurrentPage(1);
    } finally {
      setLoading(false);
    }
  };

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
    setViewStudent(studentData);
  };

  const handleCloseView = () => {
    setViewStudent(null);
  };

  const displayGradeScale =
    gradeSystem.length > 0 ? gradeSystem : DEFAULT_GRADE_SCALE;

  return (
    <div
      className={`min-h-screen ${
        theme === "light"
          ? "bg-gray-900 text-white"
          : "bg-gray-50 text-gray-800"
      }`}
    >
      <div className="container mx-auto py-8 px-4">
        {/* Page Header with Gradient */}
        <div className="mb-6 flex justify-between">
          <div
            className={`${theme === "light" ? "text-white" : "text-gray-800"}`}
          >
            <h1 className="text-2xl md:text-3xl font-bold relative inline-block">
              Student Marksheets
              <span className="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-purple-500 to-pink-500"></span>
            </h1>
            <p className="mt-2 text-sm md:text-base opacity-80">
              Generate and download student marksheets for different exams
            </p>
          </div>
        </div>

        {/* Search Section */}
        <div
          className={`mb-6 rounded-xl shadow-lg overflow-hidden transition-all duration-300 ${
            theme === "light"
              ? "bg-gray-800 border border-gray-700"
              : "bg-white border border-gray-200"
          }`}
        >
          <div
            className={`searchBox p-4 sm:p-6 border-b ${
              theme === "light" ? "border-gray-700" : "border-gray-200"
            }`}
          >
            <h2
              className={`text-xl font-bold mb-4 ${
                theme === "light" ? "text-white" : "text-gray-800"
              }`}
            >
              Search Marksheets
            </h2>

            <form
              onSubmit={searchMarkSheet}
              className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 items-end"
            >
              {/* Class Dropdown - Only show for non-student roles */}
              {role !== "student" && (
                <div>
                  <label
                    className={`block text-sm font-medium mb-1 ${
                      theme === "light" ? "text-gray-300" : "text-gray-700"
                    }`}
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
                      className={`w-full ${
                        theme === "light"
                          ? "bg-gray-700 border-gray-600 text-white"
                          : "bg-white"
                      }`}
                    >
                      <SelectValue placeholder="Select a class" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectGroup>
                        <SelectLabel>Class</SelectLabel>
                        {allClass.map((item, index) => (
                          <SelectItem key={index} value={item}>
                            {capitalizeFirst(item)}
                          </SelectItem>
                        ))}
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                </div>
              )}

              {/* Section Dropdown - Only show for non-student roles */}
              {role !== "student" && (
                <div>
                  <label
                    className={`block text-sm font-medium mb-1 ${
                      theme === "light" ? "text-gray-300" : "text-gray-700"
                    }`}
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
                      className={`w-full ${
                        theme === "light"
                          ? "bg-gray-700 border-gray-600 text-white"
                          : "bg-white"
                      }`}
                    >
                      <SelectValue placeholder="Select a section" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectGroup>
                        <SelectLabel>Section</SelectLabel>
                        {allSection.map((item, index) => (
                          <SelectItem key={index} value={item}>
                            {capitalizeFirst(item)}
                          </SelectItem>
                        ))}
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                </div>
              )}

              {/* Exam Type Dropdown - Show for all roles */}
              <div>
                <label
                  className={`block text-sm font-medium mb-1 ${
                    theme === "light" ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Exam Type
                </label>
                <Select
                  value={searchData.examType}
                  onValueChange={(value) =>
                    setSearchData({ ...searchData, examType: value })
                  }
                  required
                >
                  <SelectTrigger
                    className={`w-full ${
                      theme === "light"
                        ? "bg-gray-700 border-gray-600 text-white"
                        : "bg-white"
                    }`}
                  >
                    <SelectValue placeholder="Select exam type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectLabel>Exam Type</SelectLabel>
                      {examTypeData?.map((item, index) => (
                        <SelectItem key={index} value={item.examTypeName}>
                          {capitalizeFirst(item.examTypeName)}
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>

              {/* Search Button */}
              <div>
                <Button
                  type="submit"
                  disabled={loading}
                  className="w-full bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-700 hover:to-pink-600 transition-all duration-300 shadow-md hover:shadow-lg"
                >
                  {loading ? (
                    <>
                      <svg
                        className="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                      >
                        <circle
                          className="opacity-25"
                          cx="12"
                          cy="12"
                          r="10"
                          stroke="currentColor"
                          strokeWidth="4"
                        ></circle>
                        <path
                          className="opacity-75"
                          fill="currentColor"
                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                        ></path>
                      </svg>
                      Searching...
                    </>
                  ) : (
                    <>
                      <Search className="mr-2 h-4 w-4" />
                      Search
                    </>
                  )}
                </Button>
              </div>
            </form>
          </div>
        </div>

        {/* Results Card */}
        <div
          className={`rounded-xl shadow-lg overflow-hidden transition-all duration-300 ${
            theme === "light"
              ? "bg-gray-800 border border-gray-700"
              : "bg-white border border-gray-200"
          }`}
        >
          {/* Card Header */}
          <div
            className={`flex flex-col md:flex-row justify-between items-center p-4 sm:p-6 border-b ${
              theme === "light" ? "border-gray-700" : "border-gray-200"
            }`}
          >
            <div className="registerAvailable mb-4 md:mb-0">
              <h2
                className={`text-xl font-bold ${
                  theme === "light" ? "text-white" : "text-gray-800"
                }`}
              >
                Marksheet Results
              </h2>
              <p
                className={`text-sm mt-1 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                {marksheetData?.length || 0} records found
              </p>
            </div>

            {marksheetData.length > 0 && (
              <div
                className={`px-4 py-2 rounded-lg ${
                  theme === "light" ? "bg-gray-700" : "bg-gray-100"
                }`}
              >
                <span
                  className={`font-medium ${
                    theme === "light" ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Class:
                </span>
                <span
                  className={`ml-2 font-bold ${
                    theme === "light" ? "text-white" : "text-gray-900"
                  }`}
                >
                  {lastSearchedContext.className} ({lastSearchedContext.section})
                </span>
                <span className="mx-2 text-gray-500">|</span>
                <span
                  className={`font-medium ${
                    theme === "light" ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Exam:
                </span>
                <span
                  className={`ml-2 font-bold ${
                    theme === "light" ? "text-white" : "text-gray-900"
                  }`}
                >
                  {lastSearchedContext.examType}
                </span>
              </div>
            )}
          </div>

          {/* Loading / Publish Gate / Empty / Table */}
          {loading ? (
            <div className="flex justify-center items-center p-12">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-purple-500"></div>
            </div>
          ) : publishMessage ? (
            <div className="flex flex-col items-center justify-center p-12 text-center">
              <svg
                className="w-16 h-16 mb-4 text-purple-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                ></path>
              </svg>
              <p
                className={`text-xl font-bold ${
                  theme === "light" ? "text-purple-300" : "text-purple-600"
                }`}
              >
                {publishMessage}
              </p>
            </div>
          ) : marksheetData?.length === 0 ? (
            <div className="flex flex-col items-center justify-center p-12 text-center">
              <svg
                className="w-16 h-16 mb-4 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                ></path>
              </svg>
              <p
                className={`text-lg font-medium ${
                  theme === "light" ? "text-gray-300" : "text-gray-600"
                }`}
              >
                No marksheet data found
              </p>
              <p
                className={`text-sm mt-2 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                Use the search form above to view student marksheets
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <Table className="w-full">
                <TableHeader
                  className={`${
                    theme === "light" ? "bg-gray-700" : "bg-gray-50"
                  }`}
                >
                  <TableRow>
                    {[
                      "S.No",
                      "Roll No",
                      "Student Name",
                      "Class",
                      "Section",
                      "Total Marks",
                      "Grade",
                      "Result",
                      "Actions",
                    ].map((header, index) => (
                      <TableHead
                        key={index}
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-gray-200" : "text-gray-700"
                        } font-semibold text-sm ${
                          header === "Actions" ? "text-right" : "text-left"
                        }`}
                      >
                        {header}
                      </TableHead>
                    ))}
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {paginatedData?.map((item, index) => (
                    <TableRow
                      key={`${item.roll}-${index}`}
                      className={`transition-colors hover:bg-opacity-10 ${
                        theme === "light"
                          ? "hover:bg-gray-600 border-t border-gray-700"
                          : "hover:bg-gray-100 border-t border-gray-200"
                      }`}
                    >
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-gray-300" : "text-gray-600"
                        }`}
                      >
                        {(safeCurrentPage - 1) * rowsPerPage + index + 1}
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 font-medium ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        {item.roll}
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        {item.name}
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        {item.class}
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        {item.section}
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        <span
                          className={`inline-flex items-center px-3 py-1 rounded-md text-sm font-medium ${getPercentageBadgeClass(
                            item.overallPercentage
                          )}`}
                        >
                          {item.totalMarks}
                          {item.totalFullMarks ? ` / ${item.totalFullMarks}` : ""}
                          {item.overallPercentage !== undefined && (
                            <span className="ml-1 text-xs opacity-75">
                              ({item.overallPercentage}%)
                            </span>
                          )}
                        </span>
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-100 text-purple-800">
                          {item.overallGrade}
                        </span>
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        <span
                          className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                            item.result === "P"
                              ? "bg-green-100 text-green-800"
                              : "bg-red-100 text-red-800"
                          }`}
                        >
                          {item.result === "P" ? "PASS" : "FAIL"}
                        </span>
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 text-right ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        <div className="flex justify-end space-x-2">
                          <Button
                            variant="outline"
                            size="sm"
                            className={`${
                              theme === "light"
                                ? "border-gray-600 text-gray-300 hover:bg-gray-700"
                                : "border-gray-300 hover:bg-gray-100"
                            }`}
                            onClick={() => handleViewStudent(item)}
                          >
                            <Eye className="h-4 w-4" />
                            <span className="sr-only">View</span>
                          </Button>

                          <NewMarksheetComponent
                            marksheetData={item}
                            gradeSystem={gradeSystem}
                            passingMark={passingMark}
                            signaturePreview={signaturePreview}
                            additionalMarksheetData={additioinalMarksheetData}
                          />
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}

          {/* Pagination */}
          {marksheetData?.length > 0 && (
            <div
              className={`p-4 border-t ${
                theme === "light" ? "border-gray-700" : "border-gray-200"
              }`}
            >
              <PaginationComponent
                currentPage={safeCurrentPage}
                rowsPerPage={rowsPerPage}
                totalPages={totalPages}
                onRowsPerPageChange={handleRowsPerPageChange}
                onPageChange={setCurrentPage}
                className={`${
                  theme === "light" ? "text-white" : "text-gray-800"
                }`}
              />
            </div>
          )}
        </div>
      </div>

      {/* View Student Modal */}
      {viewStudent && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black bg-opacity-50">
          <div
            className={`relative w-full max-w-4xl rounded-lg shadow-xl overflow-hidden ${
              theme === "light"
                ? "bg-gray-800 text-white"
                : "bg-white text-gray-800"
            }`}
          >
            {/* Modal Header */}
            <div
              className={`flex justify-between items-center p-4 border-b ${
                theme === "light" ? "border-gray-700" : "border-gray-200"
              }`}
            >
              <h3 className="text-xl font-bold">Student Marksheet Details</h3>
              <button
                onClick={handleCloseView}
                className={`p-1 rounded-full hover:bg-opacity-10 ${
                  theme === "light" ? "hover:bg-gray-600" : "hover:bg-gray-200"
                }`}
              >
                <X className="h-6 w-6" />
              </button>
            </div>

            {/* Modal Body */}
            <div className="p-6 max-h-[80vh] overflow-y-auto">
              {/* Student Info */}
              <div
                className={`mb-6 p-4 rounded-lg ${
                  theme === "light" ? "bg-gray-700" : "bg-gray-100"
                }`}
              >
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <h4
                      className={`text-sm font-medium ${
                        theme === "light" ? "text-gray-400" : "text-gray-500"
                      }`}
                    >
                      Student Name
                    </h4>
                    <p className="text-lg font-semibold">{viewStudent.name}</p>
                  </div>
                  <div>
                    <h4
                      className={`text-sm font-medium ${
                        theme === "light" ? "text-gray-400" : "text-gray-500"
                      }`}
                    >
                      Roll Number
                    </h4>
                    <p className="text-lg font-semibold">{viewStudent.roll}</p>
                  </div>
                  <div>
                    <h4
                      className={`text-sm font-medium ${
                        theme === "light" ? "text-gray-400" : "text-gray-500"
                      }`}
                    >
                      Class & Section
                    </h4>
                    <p className="text-lg font-semibold">
                      {viewStudent.class} ({viewStudent.section})
                    </p>
                  </div>
                  <div>
                    <h4
                      className={`text-sm font-medium ${
                        theme === "light" ? "text-gray-400" : "text-gray-500"
                      }`}
                    >
                      Exam Type
                    </h4>
                    <p className="text-lg font-semibold">
                      {viewStudent.examType}
                    </p>
                  </div>
                </div>
              </div>

              {/* Subject Marks */}
              <h4 className="text-lg font-bold mb-4">Subject Marks</h4>
              <div className="overflow-x-auto mb-6">
                <table
                  className={`w-full border-collapse ${
                    theme === "light" ? "text-white" : "text-gray-800"
                  }`}
                >
                  <thead>
                    <tr
                      className={`${
                        theme === "light" ? "bg-gray-700" : "bg-gray-100"
                      }`}
                    >
                      <th className="px-4 py-3 text-left">Subject</th>
                      <th className="px-4 py-3 text-center">Full Marks</th>
                      <th className="px-4 py-3 text-center">Written Marks</th>
                      <th className="px-4 py-3 text-center">Project Marks</th>
                      <th className="px-4 py-3 text-center">Total Marks</th>
                      <th className="px-4 py-3 text-center">Grade</th>
                    </tr>
                  </thead>
                  <tbody>
                    {viewStudent.subjects.map((subject, index) => {
                      const isFail = subject.passed === false;
                      return (
                        <tr
                          key={index}
                          className={`border-t ${
                            theme === "light"
                              ? "border-gray-700"
                              : "border-gray-200"
                          } ${isFail ? "bg-red-900 bg-opacity-20" : ""}`}
                        >
                          <td className="px-4 py-3 font-medium">
                            {subject.subject}
                            {subject.isAdditional && (
                              <span className="ml-2 text-xs px-1.5 py-0.5 rounded bg-indigo-100 text-indigo-800">
                                Additional
                              </span>
                            )}
                          </td>
                          <td className="px-4 py-3 text-center">
                            <div className="flex flex-col">
                              <span className="text-xs font-medium mb-1 text-green-500">
                                W: {subject.subjectFullMark ?? 0}
                              </span>
                              {parseFloat(subject.projectFullMark) > 0 && (
                                <span className="text-xs font-medium text-blue-500">
                                  P: {subject.projectFullMark}
                                </span>
                              )}
                              <span className="text-xs font-semibold text-amber-500">
                                Total: {subject.fullMarks}
                              </span>
                            </div>
                          </td>
                          <td className="px-4 py-3 text-center">
                            {subject.writtenMarks}
                          </td>
                          <td className="px-4 py-3 text-center">
                            {subject.projectMarks}
                          </td>
                          <td className="px-4 py-3 text-center">
                            <span
                              className={`inline-flex items-center justify-center px-2.5 py-0.5 rounded-md text-sm font-medium ${
                                isFail
                                  ? "bg-red-100 text-red-800"
                                  : getPercentageBadgeClass(subject.percentage)
                              }`}
                            >
                              {subject.marksObtained ?? subject.totalMarks}
                              {subject.percentage !== undefined && (
                                <span className="ml-1 text-xs opacity-70">
                                  ({subject.percentage}%)
                                </span>
                              )}
                            </span>
                          </td>
                          <td className="px-4 py-3 text-center">
                            <span
                              className={`inline-flex items-center justify-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                                isFail
                                  ? "bg-red-100 text-red-800"
                                  : "bg-purple-100 text-purple-800"
                              }`}
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

              {/* Result Summary */}
              <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
                <div
                  className={`p-4 rounded-lg ${
                    theme === "light" ? "bg-gray-700" : "bg-gray-100"
                  }`}
                >
                  <h4
                    className={`text-sm font-medium mb-1 ${
                      theme === "light" ? "text-gray-400" : "text-gray-500"
                    }`}
                  >
                    Total Marks
                  </h4>
                  <p className="text-2xl font-bold">
                    {viewStudent.totalMarks} / {viewStudent.totalFullMarks}
                  </p>
                </div>
                <div
                  className={`p-4 rounded-lg ${
                    theme === "light" ? "bg-gray-700" : "bg-gray-100"
                  }`}
                >
                  <h4
                    className={`text-sm font-medium mb-1 ${
                      theme === "light" ? "text-gray-400" : "text-gray-500"
                    }`}
                  >
                    Percentage
                  </h4>
                  <p className="text-2xl font-bold">
                    {viewStudent.overallPercentage}%
                  </p>
                </div>
                <div
                  className={`p-4 rounded-lg ${
                    theme === "light" ? "bg-gray-700" : "bg-gray-100"
                  }`}
                >
                  <h4
                    className={`text-sm font-medium mb-1 ${
                      theme === "light" ? "text-gray-400" : "text-gray-500"
                    }`}
                  >
                    Overall Grade
                  </h4>
                  <p className="text-2xl font-bold">
                    {viewStudent.overallGrade}
                  </p>
                </div>
                <div
                  className={`p-4 rounded-lg ${
                    theme === "light" ? "bg-gray-700" : "bg-gray-100"
                  }`}
                >
                  <h4
                    className={`text-sm font-medium mb-1 ${
                      theme === "light" ? "text-gray-400" : "text-gray-500"
                    }`}
                  >
                    Result
                  </h4>
                  <p
                    className={`text-2xl font-bold ${
                      viewStudent.result === "P"
                        ? "text-green-500"
                        : "text-red-500"
                    }`}
                  >
                    {viewStudent.result === "P" ? "PASS" : "FAIL"}
                  </p>
                </div>
              </div>

              {/* Grade Scale */}
              <h4 className="text-lg font-bold mb-4">Grade Scale</h4>
              <div className="overflow-x-auto mb-6">
                <table
                  className={`w-full border-collapse ${
                    theme === "light" ? "text-white" : "text-gray-800"
                  }`}
                >
                  <thead>
                    <tr
                      className={`${
                        theme === "light" ? "bg-gray-700" : "bg-gray-100"
                      }`}
                    >
                      <th className="px-4 py-3 text-left">Marks Range (%)</th>
                      <th className="px-4 py-3 text-left">Grade</th>
                      <th className="px-4 py-3 text-left">Performance</th>
                    </tr>
                  </thead>
                  <tbody>
                    {displayGradeScale.map((grade, index) => (
                      <tr
                        key={index}
                        className={`border-t ${
                          theme === "light"
                            ? "border-gray-700"
                            : "border-gray-200"
                        }`}
                      >
                        <td className="px-4 py-2">{`${grade.scaleStartMarks}-${grade.scaleEndMarks}`}</td>
                        <td className="px-4 py-2">
                          <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-100 text-purple-800">
                            {grade.letterGrade}
                          </span>
                        </td>
                        <td className="px-4 py-2">
                          {grade.performanceIndicator}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Modal Footer */}
            <div
              className={`flex justify-end p-4 border-t ${
                theme === "light" ? "border-gray-700" : "border-gray-200"
              }`}
            >
              <Button
                variant="outline"
                className={`mr-2 ${
                  theme === "light"
                    ? "border-gray-600 text-gray-300 hover:bg-gray-700"
                    : "border-gray-300 hover:bg-gray-100"
                }`}
                onClick={handleCloseView}
              >
                Close
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MarksheetPage;
