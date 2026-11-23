import resultUrlApi from "@/common/result";
import PaginationComponent from "@/components/pagination/PaginationComponent";
import { markRegisterPageSteps } from "@/components/Tour/Steps/ResultSteps/Steps";
import TourButton from "@/components/Tour/TourButton";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { useTheme } from "@/context/ThemeContext";
import { setExamType } from "@/utils/routines/examTypeSlice";
import { capitalizeFirst, extractApiError } from "@/utils/result/resultHelpers";
import axios from "axios";
import { Save } from "lucide-react";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "sonner";

const MarkRegisterPage = () => {
  const { theme } = useTheme();
  const [loading, setLoading] = useState(false);
  const [postApiLoading, setPostApiLoading] = useState(false);
  const [searchClass, setSearchClass] = useState([]);
  const [searchData, setSearchData] = useState({
    className: "",
    section: "",
  });
  const [selectedExamType, setSelectedExamType] = useState("");
  const [selectedSubject, setSelectedSubject] = useState("");
  const [subjectMarks, setSubjectMarks] = useState({
    subjectMark: 0,
    projectMark: 0,
  });
  const [marksInputData, setMarksInputData] = useState({});
  const [projectMarksData, setProjectMarksData] = useState({});
  const [existingRegisterId, setExistingRegisterId] = useState(null);

  const allClass = useSelector((state) => state.class.classNames) || [];
  const allSection = useSelector((state) => state.section.sectionNames) || [];
  const examTypeData = useSelector((state) => state.examType.examType) || [];
  const teacherDetails = useSelector((state) => state.auth.userDetails) || {};
  const userRole = useSelector((state) => state.auth.user) || "teacher";

  const dispatch = useDispatch();
  const schoolId = useSelector((state) => state?.auth?.schoolId);

  const [examInfo, setExamInfo] = useState([]);
  const [subjectError, setSubjectError] = useState("");

  const dataLength = searchClass?.length || 0;

  const initBlankMarksForStudents = (students, projFull) => {
    setMarksInputData(
      students.reduce((acc, item) => ({ ...acc, [item.id]: "" }), {})
    );
    setProjectMarksData(
      students.reduce(
        (acc, item) => ({ ...acc, [item.id]: projFull === 0 ? "0" : "" }),
        {}
      )
    );
  };

  const searchMarkRegister = async (e) => {
    e.preventDefault();
    if (!schoolId) return;
    if (!searchData.className || !searchData.section) {
      toast.error("Please select class and section");
      return;
    }
    try {
      setLoading(true);
      const response = await axios.get(
        `${resultUrlApi.searchStudentForMark1.url}/${schoolId}/attendance/students`,
        {
          params: {
            className: searchData.className,
            section: searchData.section,
          },
        }
      );
      const rawStudents = Array.isArray(response?.data) ? response.data : [];
      const sortedData = [...rawStudents].sort(
        (a, b) => Number(a.rollNo) - Number(b.rollNo)
      );
      setSearchClass(sortedData);
      setCurrentPage(1);
      setExistingRegisterId(null);
      initBlankMarksForStudents(sortedData, subjectMarks.projectMark);
      toast.success(
        sortedData.length > 0
          ? "Students fetched successfully"
          : "No students found for this class and section"
      );
    } catch (error) {
      setSearchClass([]);
      setCurrentPage(1);
      toast.error(extractApiError(error, "Error searching students"));
    } finally {
      setLoading(false);
    }
  };

  const fetchExamTypeData = async () => {
    if (!schoolId) return;
    try {
      setLoading(true);
      const response = await axios.get(
        `${resultUrlApi.fetchExamType.url}/${schoolId}`
      );
      dispatch(setExamType(response?.data?.data || []));
    } catch (error) {
      toast.error(extractApiError(error, "Failed to fetch exam types"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (schoolId) {
      fetchExamTypeData();
    }
  }, [schoolId]);

  // Pagination state
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [currentPage, setCurrentPage] = useState(1);

  const totalPages = Math.max(1, Math.ceil(dataLength / rowsPerPage));
  const safeCurrentPage = Math.min(currentPage, totalPages);
  const paginatedData = searchClass?.slice(
    (safeCurrentPage - 1) * rowsPerPage,
    safeCurrentPage * rowsPerPage
  );

  const handleRowsPerPageChange = (newRowsPerPage) => {
    setRowsPerPage(newRowsPerPage);
    const newTotalPages = Math.max(1, Math.ceil(dataLength / newRowsPerPage));
    setCurrentPage((prev) => Math.max(1, Math.min(prev, newTotalPages)));
  };

  const fetchExamInfo = async () => {
    if (!schoolId || !searchData.className || !selectedExamType) {
      setExamInfo([]);
      setSelectedSubject("");
      setSubjectMarks({ subjectMark: 0, projectMark: 0 });
      setSubjectError("");
      setExistingRegisterId(null);
      return;
    }

    try {
      const response = await axios.get(
        `${resultUrlApi.criteria.url}/search/${schoolId}`,
        {
          params: {
            className: searchData.className,
            examType: selectedExamType,
          },
        }
      );
      if (response?.data?.success && Array.isArray(response?.data?.data)) {
        const subjects = response.data.data;
        setExamInfo(subjects);

        if (userRole === "teacher" && teacherDetails?.subject) {
          const teacherSubject = subjects.find(
            (subject) =>
              subject.subjectName?.trim().toLowerCase() ===
              teacherDetails.subject.trim().toLowerCase()
          );

          if (teacherSubject) {
            const projFull = Number(teacherSubject.projectMark) || 0;
            setSelectedSubject(teacherSubject.subjectName);
            setSubjectMarks({
              subjectMark: Number(teacherSubject.subjectMark) || 0,
              projectMark: projFull,
            });
            setSubjectError("");
            initBlankMarksForStudents(searchClass, projFull);
          } else {
            setSelectedSubject("");
            setSubjectMarks({ subjectMark: 0, projectMark: 0 });
            setExistingRegisterId(null);
            setSubjectError(
              "You can't give marks for this exam as your subject is not included"
            );
          }
        } else if (["admin", "edp"].includes(userRole)) {
          setSelectedSubject("");
          setSubjectMarks({ subjectMark: 0, projectMark: 0 });
          setSubjectError("");
          setExistingRegisterId(null);
        }
      } else {
        setExamInfo([]);
        setSelectedSubject("");
        setSubjectMarks({ subjectMark: 0, projectMark: 0 });
        setExistingRegisterId(null);
      }
    } catch (error) {
      setExamInfo([]);
      setSelectedSubject("");
      setSubjectMarks({ subjectMark: 0, projectMark: 0 });
      setSubjectError("");
      setExistingRegisterId(null);
      toast.error(
        extractApiError(
          error,
          "No marks criteria found for this class and exam type"
        )
      );
    }
  };

  useEffect(() => {
    fetchExamInfo();
  }, [schoolId, searchData.className, selectedExamType, userRole]);

  // Auto-load existing marks if a register already exists for (className, section, examType, subject)
  useEffect(() => {
    const loadExistingMarks = async () => {
      if (
        !schoolId ||
        !searchData.className ||
        !searchData.section ||
        !selectedExamType ||
        !selectedSubject ||
        searchClass.length === 0
      ) {
        setExistingRegisterId(null);
        return;
      }

      try {
        const res = await axios.get(
          `${resultUrlApi.markRegister.url}/${schoolId}/get/search`,
          {
            params: {
              className: searchData.className,
              section: searchData.section,
              examType: selectedExamType,
              subject: selectedSubject,
            },
          }
        );
        const rows = res?.data?.data;
        if (Array.isArray(rows) && rows.length > 0 && rows[0]?.registerId) {
          setExistingRegisterId(rows[0].registerId);
          const byRoll = new Map(
            rows.map((r) => [Number(r.roll), r])
          );
          const nextWritten = {};
          const nextProject = {};
          searchClass.forEach((student) => {
            const match = byRoll.get(Number(student.rollNo));
            if (match) {
              nextWritten[student.id] =
                match.marks !== null && match.marks !== undefined
                  ? String(match.marks)
                  : "";
              nextProject[student.id] =
                subjectMarks.projectMark === 0
                  ? "0"
                  : match.projectMark !== null && match.projectMark !== undefined
                  ? String(match.projectMark)
                  : "0";
            } else {
              nextWritten[student.id] = "";
              nextProject[student.id] = subjectMarks.projectMark === 0 ? "0" : "";
            }
          });
          setMarksInputData(nextWritten);
          setProjectMarksData(nextProject);
        } else {
          setExistingRegisterId(null);
          initBlankMarksForStudents(searchClass, subjectMarks.projectMark);
        }
      } catch {
        // 404 means no existing register yet -> fresh create mode
        setExistingRegisterId(null);
        initBlankMarksForStudents(searchClass, subjectMarks.projectMark);
      }
    };

    loadExistingMarks();
  }, [
    schoolId,
    searchData.className,
    searchData.section,
    selectedExamType,
    selectedSubject,
    searchClass,
    subjectMarks.projectMark,
  ]);

  // Handle subject change for admin/edp
  const handleSubjectChange = (value) => {
    setSelectedSubject(value);
    const selectedSubjectInfo = examInfo.find(
      (subject) => subject.subjectName === value
    );
    const nextSubjectMark = Number(selectedSubjectInfo?.subjectMark) || 0;
    const nextProjectMark = Number(selectedSubjectInfo?.projectMark) || 0;
    setSubjectMarks({
      subjectMark: nextSubjectMark,
      projectMark: nextProjectMark,
    });
    // Reset current inputs so marks from a higher-max subject don't carry over
    initBlankMarksForStudents(searchClass, nextProjectMark);
  };

  // Validate marks input (supports decimals as backend uses double)
  const handleMarksChange = (id, value, type) => {
    if (value !== "" && !/^\d*\.?\d*$/.test(value)) {
      return;
    }

    const numValue = value === "" ? "" : parseFloat(value);

    if (type === "subject") {
      if (numValue !== "" && !Number.isNaN(numValue) && numValue > subjectMarks.subjectMark) {
        toast.error(`Subject mark cannot exceed ${subjectMarks.subjectMark}`);
        return;
      }
      setMarksInputData((prev) => ({ ...prev, [id]: value }));
    } else {
      if (numValue !== "" && !Number.isNaN(numValue) && numValue > subjectMarks.projectMark) {
        toast.error(`Project mark cannot exceed ${subjectMarks.projectMark}`);
        return;
      }
      setProjectMarksData((prev) => ({ ...prev, [id]: value }));
    }
  };

  const handleSaveAll = async () => {
    if (!schoolId) return;
    if (!searchData.className || !searchData.section) {
      toast.error("Please select class and section");
      return;
    }
    if (searchClass.length === 0) {
      toast.error("Please search and load students first");
      return;
    }
    if (!selectedExamType) {
      toast.error("Please select exam type");
      return;
    }
    if (!selectedSubject) {
      toast.error("Please select a subject");
      return;
    }

    const studentsWithMissingMarks = [];
    const studentsWithInvalidRoll = [];

    searchClass.forEach((student) => {
      const parsedRoll = Number(student.rollNo);
      if (!Number.isInteger(parsedRoll)) {
        studentsWithInvalidRoll.push(student.studentName);
      }

      const writtenStr = String(marksInputData[student.id] ?? "").trim();
      const projectStr =
        subjectMarks.projectMark === 0
          ? "0"
          : String(projectMarksData[student.id] ?? "").trim();

      if (writtenStr === "" || Number.isNaN(Number(writtenStr))) {
        studentsWithMissingMarks.push(student.studentName);
      } else if (
        subjectMarks.projectMark > 0 &&
        (projectStr === "" || Number.isNaN(Number(projectStr)))
      ) {
        studentsWithMissingMarks.push(student.studentName);
      }
    });

    if (studentsWithInvalidRoll.length > 0) {
      toast.error(
        `Invalid numeric roll number for: ${studentsWithInvalidRoll.join(", ")}`
      );
      return;
    }

    if (studentsWithMissingMarks.length > 0) {
      toast.error(
        `Marks are missing for: ${studentsWithMissingMarks.join(", ")}`
      );
      return;
    }

    try {
      setPostApiLoading(true);
      const payload = searchClass.map((student) => ({
        roll: Number(student.rollNo),
        name: student.studentName,
        mark: Number(marksInputData[student.id]),
        projectMark:
          subjectMarks.projectMark === 0
            ? 0
            : Number(projectMarksData[student.id] ?? 0),
      }));

      const resolvedTeacherName =
        userRole === "teacher"
          ? teacherDetails?.teachersName || "Teacher"
          : userRole === "edp"
          ? teacherDetails?.edpName || "EDP"
          : "Admin";

      const dataSend = {
        className: searchData.className,
        section: searchData.section,
        marks: payload,
        subject: selectedSubject,
        teacherName: resolvedTeacherName,
        examType: selectedExamType,
        subjectFullMark: String(subjectMarks.subjectMark),
        projectFullMark: String(subjectMarks.projectMark),
        fullMarks: Number(subjectMarks.subjectMark) + Number(subjectMarks.projectMark),
      };

      if (existingRegisterId) {
        const response = await axios.put(
          `${resultUrlApi.markRegister.url}/${existingRegisterId}/${schoolId}`,
          dataSend,
          {
            headers: { "Content-Type": "application/json" },
          }
        );
        if (response) {
          toast.success("Marks updated successfully for all students");
        }
      } else {
        const response = await axios.post(
          `${resultUrlApi.markRegister.url}/${schoolId}`,
          dataSend,
          {
            headers: { "Content-Type": "application/json" },
          }
        );
        if (response?.data?.data?.id) {
          setExistingRegisterId(response.data.data.id);
        }
        toast.success("Marks saved successfully for all students");
      }
    } catch (error) {
      toast.error(extractApiError(error, "Failed to save marks"));
    } finally {
      setPostApiLoading(false);
    }
  };

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
        <div
          className={`mb-6 ${
            theme === "light" ? "text-white" : "text-gray-800"
          }`}
        >
          <h1 className="text-2xl md:text-3xl font-bold relative inline-block">
            Mark Register
            <span className="absolute bottom-[-5px] left-0 w-full h-1 bg-gradient-to-r from-purple-500 to-pink-500"></span>
          </h1>
          <p className="mt-2 text-sm md:text-base opacity-80">
            Record and manage student marks for different subjects and exams
          </p>
        </div>

        {/* Search Section */}
        <div
          className={`mb-6 rounded-xl shadow-lg overflow-hidden transition-all duration-300 ${
            theme === "light"
              ? "bg-gray-800 border border-gray-700"
              : "bg-white border border-gray-200"
          } flex justify-between`}
        >
          <div
            className={`p-4 sm:p-6 border-b ${
              theme === "light" ? "border-gray-700" : "border-gray-200"
            }`}
          >
            <h2
              className={`text-xl font-bold mb-4 ${
                theme === "light" ? "text-white" : "text-gray-800"
              }`}
            >
              Search Class
            </h2>

            <form
              onSubmit={searchMarkRegister}
              className="searchBox flex flex-col sm:flex-row items-center gap-4"
            >
              <div className="w-full sm:w-auto">
                <Select
                  value={searchData.className}
                  onValueChange={(value) =>
                    setSearchData({ ...searchData, className: value })
                  }
                  required
                >
                  <SelectTrigger
                    className={`w-full sm:w-[180px] ${
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
                        <SelectItem key={index + 1} value={item}>
                          {capitalizeFirst(item)}
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>

              <div className="w-full sm:w-auto">
                <Select
                  value={searchData.section}
                  onValueChange={(value) =>
                    setSearchData({ ...searchData, section: value })
                  }
                  required
                >
                  <SelectTrigger
                    className={`w-full sm:w-[180px] ${
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
                        <SelectItem key={index + item} value={item}>
                          {capitalizeFirst(item)}
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>

              <Button
                type="submit"
                className="w-full sm:w-auto bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-700 hover:to-pink-600 transition-all duration-300 shadow-md hover:shadow-lg"
              >
                Search
              </Button>
            </form>
          </div>

          {/* Tour button */}
          <div className=" p-6">
            <TourButton
              steps={markRegisterPageSteps}
              tourName={"markRegisterPageTour"}
            />
          </div>
        </div>

        {/* Main Content Card */}
        <div
          className={`rounded-xl shadow-lg overflow-hidden transition-all duration-300 ${
            theme === "light"
              ? "bg-gray-800 border border-gray-700"
              : "bg-white border border-gray-200"
          }`}
        >
          {/* Card Header with Actions */}
          <div
            className={`flex flex-col md:flex-row justify-between items-center p-4 sm:p-6 border-b ${
              theme === "light" ? "border-gray-700" : "border-gray-200"
            }`}
          >
            <div className="registerAvailable mb-4 md:mb-0">
              <div className="flex items-center gap-2">
                <h2
                  className={`text-xl font-bold ${
                    theme === "light" ? "text-white" : "text-gray-800"
                  }`}
                >
                  Mark Register
                </h2>
                {existingRegisterId && (
                  <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-amber-100 text-amber-800">
                    Editing Existing Marks
                  </span>
                )}
              </div>
              <p
                className={`text-sm mt-1 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                {searchClass?.length || 0} students found
              </p>
              {subjectError && (
                <p className="text-xs mt-1 text-red-400">{subjectError}</p>
              )}
            </div>

            <div className="flex items-center gap-4">
              {userRole === "teacher" ? (
                <h1>
                  Subject:{" "}
                  {selectedSubject || teacherDetails?.subject || "Not selected"}
                </h1>
              ) : (
                <div className="w-full sm:w-auto">
                  <Select
                    onValueChange={handleSubjectChange}
                    value={selectedSubject}
                  >
                    <SelectTrigger
                      className={`w-full sm:w-[180px] ${
                        theme === "light"
                          ? "bg-gray-700 border-gray-600 text-white"
                          : "bg-white"
                      }`}
                    >
                      <SelectValue placeholder="Select Subject" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectGroup>
                        <SelectLabel>Subjects</SelectLabel>
                        {examInfo.map((item, index) => (
                          <SelectItem key={index} value={item.subjectName}>
                            {capitalizeFirst(item.subjectName)}
                          </SelectItem>
                        ))}
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                </div>
              )}
            </div>

            <div className="flex flex-col sm:flex-row gap-4 items-center">
              {/* Exam Type Dropdown */}
              <div>
                <Select
                  value={selectedExamType}
                  onValueChange={(value) => setSelectedExamType(value)}
                >
                  <SelectTrigger
                    className={`examType w-full sm:w-[180px] ${
                      theme === "light"
                        ? "bg-gray-700 border-gray-600 text-white"
                        : "bg-white"
                    }`}
                  >
                    <SelectValue placeholder="Select Exam Type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectLabel>Exam Types</SelectLabel>
                      {examTypeData.map((item, index) => (
                        <SelectItem key={index} value={item.examTypeName}>
                          {capitalizeFirst(item.examTypeName)}
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>

              {/* Save / Update All Button */}
              <Button
                onClick={handleSaveAll}
                disabled={postApiLoading || !!subjectError || searchClass.length === 0}
                className="saveMarks bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-700 hover:to-pink-600 transition-all duration-300 shadow-md hover:shadow-lg"
              >
                {postApiLoading ? (
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
                    {existingRegisterId ? "Updating..." : "Saving..."}
                  </>
                ) : (
                  <>
                    <Save className="mr-2 h-4 w-4" />
                    {existingRegisterId ? "Update All Marks" : "Save All Marks"}
                  </>
                )}
              </Button>
            </div>
          </div>

          {/* Loading State */}
          {loading ? (
            <div className="flex justify-center items-center p-12">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-purple-500"></div>
            </div>
          ) : searchClass?.length === 0 ? (
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
                No students found
              </p>
              <p
                className={`text-sm mt-2 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                Select a class and section to view students
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
                      `Subject's Mark (${subjectMarks.subjectMark})`,
                      `Project's Mark (${subjectMarks.projectMark})`,
                    ].map((header, index) => (
                      <TableHead
                        key={index + 1}
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
                  {paginatedData?.map((item, index) => (
                    <TableRow
                      key={item.id}
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
                        {item.rollNo}
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
                          {item.className} ({item.section})
                        </span>
                      </TableCell>

                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        <input
                          type="text"
                          name="subjectMarks"
                          placeholder={`Enter Marks (max: ${subjectMarks.subjectMark})`}
                          value={marksInputData[item.id] ?? ""}
                          onChange={(e) =>
                            handleMarksChange(
                              item.id,
                              e.target.value,
                              "subject"
                            )
                          }
                          className={`w-full p-2 border rounded-md shadow-sm focus:ring-2 focus:ring-purple-500 focus:border-transparent ${
                            theme === "light"
                              ? "text-white bg-gray-800 border border-gray-600"
                              : "text-black border border-gray-200"
                          }`}
                          disabled={!!subjectError || !selectedSubject}
                        />
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        <input
                          type="text"
                          name="projectMarks"
                          placeholder={
                            subjectMarks.projectMark === 0
                              ? "N/A (0)"
                              : `Enter Marks (max: ${subjectMarks.projectMark})`
                          }
                          value={
                            subjectMarks.projectMark === 0
                              ? "0"
                              : projectMarksData[item.id] ?? ""
                          }
                          onChange={(e) =>
                            handleMarksChange(
                              item.id,
                              e.target.value,
                              "project"
                            )
                          }
                          className={`w-full p-2 border rounded-md shadow-sm focus:ring-2 focus:ring-purple-500 focus:border-transparent ${
                            theme === "light"
                              ? "text-white bg-gray-800 border border-gray-600"
                              : "text-black border border-gray-200"
                          } ${subjectMarks.projectMark === 0 ? "opacity-60 cursor-not-allowed" : ""}`}
                          disabled={
                            !!subjectError ||
                            !selectedSubject ||
                            subjectMarks.projectMark === 0
                          }
                        />
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}

          {/* Pagination */}
          {searchClass?.length > 0 && (
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

export default MarkRegisterPage;
