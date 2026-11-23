import React, { useState } from "react";
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
import { Search } from "lucide-react";
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
import resultUrlApi from "@/common/result";
import TourButton from "@/components/Tour/TourButton";
import { viewMarksPageSteps } from "@/components/Tour/Steps/ResultSteps/Steps";
import {
  capitalizeFirst,
  extractApiError,
  getPercentageBadgeClass,
} from "@/utils/result/resultHelpers";

const ViewMarksPage = () => {
  const schoolId = useSelector((state) => state?.auth?.schoolId);
  const { theme } = useTheme();
  const [loading, setLoading] = useState(false);
  const allClass = useSelector((state) => state.class.classNames) || [];
  const allSection = useSelector((state) => state.section.sectionNames) || [];
  const examTypeData = useSelector((state) => state.examType.examType) || [];
  const userDetails = useSelector((state) => state.auth.userDetails) || {};
  const allSubject = useSelector((state) => state.subject.subjectNames) || [];
  const role = useSelector((state) => state.auth.user);
  const [searchReasult, setSearchReasult] = useState([]);
  const [lastSearchedContext, setLastSearchedContext] = useState({
    className: "",
    section: "",
    examType: "",
  });
  const [searchData, setSearchData] = useState({
    className: "",
    section: "",
    subject: "",
    examType: "",
  });

  const dataLength = searchReasult?.length || 0;

  const searchViewMark = async (e) => {
    e.preventDefault();
    if (!schoolId) return;

    if (role === "student") {
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
        toast.error("Student profile details (class, section, roll no) are missing");
        return;
      }
    } else if (role === "teacher") {
      if (!searchData.className || !searchData.section || !searchData.examType) {
        toast.error("Please select class, section, and exam type");
        return;
      }
      if (!userDetails?.subject) {
        toast.error("No subject assigned to your teacher profile");
        return;
      }
    } else if (role === "edp" || role === "admin") {
      if (
        !searchData.className ||
        !searchData.section ||
        !searchData.subject ||
        !searchData.examType
      ) {
        toast.error("Please select class, section, subject, and exam type");
        return;
      }
    } else {
      toast.error("Invalid role");
      return;
    }

    try {
      setLoading(true);
      let response;
      const targetClass =
        role === "student" ? userDetails.className : searchData.className;
      const targetSection =
        role === "student" ? userDetails.section : searchData.section;

      if (role === "teacher") {
        response = await axios.get(
          `${resultUrlApi.viewMarks.url}/${schoolId}/get/search`,
          {
            params: {
              className: targetClass,
              section: targetSection,
              examType: searchData.examType,
              subject: userDetails.subject,
            },
          }
        );
      } else if (role === "edp" || role === "admin") {
        response = await axios.get(
          `${resultUrlApi.viewMarks.url}/${schoolId}/get/search`,
          {
            params: {
              className: targetClass,
              section: targetSection,
              subject: searchData.subject,
              examType: searchData.examType,
            },
          }
        );
      } else if (role === "student") {
        response = await axios.get(
          `${resultUrlApi.viewMarks.url}/${schoolId}/search`,
          {
            params: {
              className: targetClass,
              section: targetSection,
              examType: searchData.examType,
              roll: Number(userDetails.rollNo),
            },
          }
        );
      }

      const records = response?.data?.data || [];
      setSearchReasult(records);
      setLastSearchedContext({
        className: targetClass,
        section: targetSection,
        examType: searchData.examType,
      });
      setCurrentPage(1);
      toast.success("Marks data retrieved successfully");
    } catch (error) {
      setSearchReasult([]);
      setCurrentPage(1);
      toast.error(extractApiError(error, "Error searching data"));
    } finally {
      setLoading(false);
    }
  };

  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [currentPage, setCurrentPage] = useState(1);

  const totalPages = Math.max(1, Math.ceil(dataLength / rowsPerPage));
  const safeCurrentPage = Math.min(currentPage, totalPages);
  const paginatedData = searchReasult?.slice(
    (safeCurrentPage - 1) * rowsPerPage,
    safeCurrentPage * rowsPerPage
  );

  const handleRowsPerPageChange = (newRowsPerPage) => {
    setRowsPerPage(newRowsPerPage);
    const newTotalPages = Math.max(1, Math.ceil(dataLength / newRowsPerPage));
    setCurrentPage((prev) => Math.max(1, Math.min(prev, newTotalPages)));
  };

  const isSingleSubjectView = role !== "student";
  const headerSubjectFull = searchReasult[0]?.subjectFullMark;
  const headerProjectFull = searchReasult[0]?.projectFullMark;

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
        <div className="mb-6 flex justify-between ">
          <div
            className={`${theme === "light" ? "text-white" : "text-gray-800"}`}
          >
            <h1 className="text-2xl md:text-3xl font-bold relative inline-block">
              View Marks
              <span className="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-purple-500 to-pink-500"></span>
            </h1>
            <p className="mt-2 text-sm md:text-base opacity-80">
              View and analyze student performance across different subjects and
              exams
            </p>
          </div>

          <div>
            <TourButton
              steps={viewMarksPageSteps}
              tourName={"viewMarksPageTour"}
            />
          </div>
        </div>

        {/* Search Section */}
        <div
          className={` mb-6 rounded-xl shadow-lg overflow-hidden transition-all duration-300 ${
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
              Search Marks
            </h2>

            <form
              onSubmit={searchViewMark}
              className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 items-end"
            >
              {/* Class Dropdown (Hidden for students; auto-filled from profile) */}
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

              {/* Section Dropdown (Hidden for students) */}
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

              {/* Subject Dropdown (Only for admin/edp) */}
              {(role === "edp" || role === "admin") && (
                <div>
                  <label
                    className={`block text-sm font-medium mb-1 ${
                      theme === "light" ? "text-gray-300" : "text-gray-700"
                    }`}
                  >
                    Subject
                  </label>
                  <Select
                    value={searchData.subject}
                    onValueChange={(value) =>
                      setSearchData({ ...searchData, subject: value })
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
                      <SelectValue placeholder="Select a subject" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectGroup>
                        <SelectLabel>Subject</SelectLabel>
                        {allSubject?.map((item, index) => (
                          <SelectItem key={index} value={item}>
                            {capitalizeFirst(item)}
                          </SelectItem>
                        ))}
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                </div>
              )}

              {/* Exam Type Dropdown */}
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
              <div
                className={`${
                  role === "edp" || role === "admin"
                    ? "col-span-1"
                    : "col-span-1 sm:col-span-2"
                }`}
              >
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
                      Search Marks
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
                Marks Results
              </h2>
              <p
                className={`text-sm mt-1 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                {searchReasult?.length || 0} records found
              </p>
            </div>

            {isSingleSubjectView && searchReasult.length > 0 && (
              <div>
                <h1 className="font-semibold">
                  Full Marks : {searchReasult[0]?.fullMarks || 0}
                </h1>
              </div>
            )}

            {searchReasult.length > 0 && (
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

          {/* Loading State */}
          {loading ? (
            <div className="flex justify-center items-center p-12">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-purple-500"></div>
            </div>
          ) : searchReasult?.length === 0 ? (
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
                No marks data found
              </p>
              <p
                className={`text-sm mt-2 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                Use the search form above to view student marks
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
                      "Class (Section)",
                      "Subject",
                      isSingleSubjectView && headerSubjectFull != null
                        ? `Subject's Marks (${headerSubjectFull})`
                        : "Subject's Marks",
                      isSingleSubjectView && headerProjectFull != null
                        ? `Project's Marks (${headerProjectFull})`
                        : "Project's Marks",
                      "Total",
                      "Teacher Name",
                    ].map((header, index) => (
                      <TableHead
                        key={index}
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-gray-200" : "text-gray-700"
                        } font-semibold text-sm text-left`}
                      >
                        {header}
                      </TableHead>
                    ))}
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {paginatedData?.map((item, index) => {
                    const written = Number(item.marks) || 0;
                    const project = Number(item.projectMark) || 0;
                    const writtenFull =
                      parseFloat(item.subjectFullMark) ||
                      (parseFloat(item.fullMarks) || 0);
                    const projectFull = parseFloat(item.projectFullMark) || 0;
                    const rowTotalFull =
                      parseFloat(item.subjectFullMark) > 0 ||
                      parseFloat(item.projectFullMark) > 0
                        ? (parseFloat(item.subjectFullMark) || 0) + projectFull
                        : parseFloat(item.fullMarks) || 100;
                    const writtenPct =
                      writtenFull > 0 ? (written / writtenFull) * 100 : 0;

                    return (
                      <TableRow
                        key={`${item.roll}-${item.subject}-${index}`}
                        className={`transition-colors hover:bg-opacity-10 ${
                          theme === "light"
                            ? "hover:bg-gray-600 border-t border-gray-700"
                            : "hover:bg-gray-100 border-t border-gray-200"
                        }`}
                      >
                        <TableCell
                          className={`px-4 py-3 ${
                            theme === "light"
                              ? "text-gray-300"
                              : "text-gray-600"
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
                          {item.studentName}
                        </TableCell>
                        <TableCell
                          className={`px-4 py-3 ${
                            theme === "light" ? "text-white" : "text-gray-800"
                          }`}
                        >
                          <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-100 text-purple-800">
                            {item.class || lastSearchedContext.className} (
                            {item.section || lastSearchedContext.section})
                          </span>
                        </TableCell>
                        <TableCell
                          className={`px-4 py-3 ${
                            theme === "light" ? "text-white" : "text-gray-800"
                          }`}
                        >
                          {capitalizeFirst(item.subject)}
                        </TableCell>
                        <TableCell
                          className={`px-4 py-3 ${
                            theme === "light" ? "text-white" : "text-gray-800"
                          }`}
                        >
                          <span
                            className={`inline-flex items-center px-3 py-1 rounded-md text-sm font-medium ${getPercentageBadgeClass(
                              writtenPct
                            )}`}
                          >
                            {written}
                            {!isSingleSubjectView && writtenFull > 0 && (
                              <span className="ml-1 text-xs opacity-70">
                                / {writtenFull}
                              </span>
                            )}
                          </span>
                        </TableCell>
                        <TableCell
                          className={`px-4 py-3 ${
                            theme === "light" ? "text-white" : "text-gray-800"
                          }`}
                        >
                          {project}
                          {!isSingleSubjectView && (
                            <span className="ml-1 text-xs opacity-70">
                              / {projectFull}
                            </span>
                          )}
                        </TableCell>
                        <TableCell
                          className={`px-4 py-3 font-medium ${
                            theme === "light" ? "text-white" : "text-gray-800"
                          }`}
                        >
                          {written + project} / {rowTotalFull}
                        </TableCell>
                        <TableCell
                          className={`px-4 py-3 ${
                            theme === "light" ? "text-white" : "text-gray-800"
                          }`}
                        >
                          {item.Teacher || "-"}
                        </TableCell>
                      </TableRow>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
          )}

          {/* Pagination */}
          {searchReasult?.length > 0 && (
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
    </div>
  );
};

export default ViewMarksPage;
