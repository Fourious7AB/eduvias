import React, { useState, useEffect } from "react";
import { useTheme } from "@/context/ThemeContext";
import { useSelector } from "react-redux";
import { toast } from "sonner";
import axios from "axios";
import { FaPlus, FaEdit, FaSearch, FaGraduationCap } from "react-icons/fa";
import { Loader2, FileSpreadsheet } from "lucide-react";
import resultUrlApi from "@/common/result";
import { extractApiError } from "@/utils/result/resultHelpers";

// UI Components
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

// Custom Components
import DeleteComponent from "@/components/DeleteData/DeleteComponent";
import PaginationComponent from "@/components/pagination/PaginationComponent";

const createEmptySubjectRow = (id = 1) => ({
  id,
  subject: "",
  theoryMarks: 0,
  projectMarks: 0,
  totalMarks: 0,
});

const SetMarksForExamTypePage = () => {
  const { theme } = useTheme();
  const isDarkTheme = theme === "light";
  const schoolId = useSelector((state) => state?.auth?.schoolId);
  const role = useSelector((state) => state?.auth?.user);
  const canManage = role === "admin" || role === "edp";

  // Redux data
  const examTypeData = useSelector((state) => state.examType.examType) || [];
  const allSubjects = useSelector((state) => state.subject.subjectNames) || [];
  const allClasses = useSelector((state) => state.class.classNames) || [
    "Class 1",
    "Class 2",
    "Class 3",
    "Class 4",
    "Class 5",
  ];

  // State for marks data
  const [marksData, setMarksData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [addDialogOpen, setAddDialogOpen] = useState(false);
  const [updateDialogOpen, setUpdateDialogOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);

  // Separate states for Add and Update dialogs so they never leak into each other
  const [addClassName, setAddClassName] = useState("");
  const [addExamType, setAddExamType] = useState("");
  const [addPassingMarks, setAddPassingMarks] = useState("33");
  const [addSubjectEntries, setAddSubjectEntries] = useState([
    createEmptySubjectRow(1),
  ]);

  const [editPassingMarks, setEditPassingMarks] = useState("33");
  const [editSubjectEntries, setEditSubjectEntries] = useState([]);

  // Search state
  const [searchTerm, setSearchTerm] = useState("");
  const [searchExamType, setSearchExamType] = useState("_all");

  // Pagination state
  const [currentPage, setCurrentPage] = useState(1);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const fetchMarksData = async () => {
    if (!schoolId) return;
    setIsLoading(true);
    try {
      const response = await axios.get(
        `${resultUrlApi.criteria.url}/${schoolId}`
      );
      const rawList = Array.isArray(response.data) ? response.data : [];
      const transformedData = rawList.map((item) => ({
        id: item.id,
        examType: item.examType,
        className: item.className,
        passingMarks: item.passingMark,
        subjects: (item.subjects || []).map((subject) => ({
          subject: subject.subjectName,
          theoryMarks: subject.subjectMark ?? 0,
          projectMarks: subject.projectMark ?? 0,
          totalMarks:
            subject.fullMark ??
            (Number(subject.subjectMark || 0) + Number(subject.projectMark || 0)),
          createAt: subject.createAt || null,
        })),
      }));
      setMarksData(transformedData);
    } catch (error) {
      console.error("Error fetching marks data:", error);
      toast.error(extractApiError(error, "Failed to fetch marks data"));
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (schoolId) {
      fetchMarksData();
    }
  }, [schoolId]);

  const resetAddForm = () => {
    setAddClassName("");
    setAddExamType("");
    setAddPassingMarks("33");
    setAddSubjectEntries([createEmptySubjectRow(1)]);
  };

  const handleAddMarks = async (e) => {
    e.preventDefault();
    if (!addClassName || !addExamType) {
      toast.error("Please select class and exam type");
      return;
    }

    const parsedPassing = parseFloat(addPassingMarks);
    const passingMark = Number.isNaN(parsedPassing) ? 33 : parsedPassing;
    if (passingMark < 0) {
      toast.error("Passing marks cannot be negative");
      return;
    }

    const validEntries = addSubjectEntries.filter(
      (entry) => entry.subject && entry.subject.trim() !== ""
    );
    if (validEntries.length === 0) {
      toast.error("Please add at least one subject with marks");
      return;
    }

    for (const entry of validEntries) {
      const total = (parseFloat(entry.theoryMarks) || 0) + (parseFloat(entry.projectMarks) || 0);
      if (passingMark > total) {
        toast.error(
          `Passing mark (${passingMark}) cannot exceed total marks (${total}) for ${entry.subject}`
        );
        return;
      }
    }

    try {
      const payload = {
        schoolId,
        className: addClassName,
        examType: addExamType,
        passingMark,
        subjects: validEntries.map((item) => {
          const theory = parseFloat(item.theoryMarks) || 0;
          const project = parseFloat(item.projectMarks) || 0;
          return {
            subjectName: item.subject.trim(),
            subjectMark: theory,
            projectMark: project,
            fullMark: theory + project,
          };
        }),
      };

      const response = await axios.post(
        `${resultUrlApi.criteria.url}/${schoolId}`,
        payload
      );

      if (response.data) {
        toast.success("Marks structure added successfully!");
        fetchMarksData();
        setAddDialogOpen(false);
        resetAddForm();
      }
    } catch (error) {
      toast.error(extractApiError(error, "Failed to add marks"));
    }
  };

  const handleUpdateMarks = async (e) => {
    e.preventDefault();
    if (!selectedItem) return;

    const parsedPassing = parseFloat(editPassingMarks);
    const passingMark = Number.isNaN(parsedPassing) ? 0 : parsedPassing;
    if (passingMark < 0) {
      toast.error("Passing marks cannot be negative");
      return;
    }

    const validSubjects = editSubjectEntries.filter(
      (entry) => entry.subject && entry.subject.trim() !== ""
    );

    if (validSubjects.length === 0) {
      toast.error("Please add at least one subject with valid marks");
      return;
    }

    for (const entry of validSubjects) {
      const total = (parseFloat(entry.theoryMarks) || 0) + (parseFloat(entry.projectMarks) || 0);
      if (passingMark > total) {
        toast.error(
          `Passing mark (${passingMark}) cannot exceed total marks (${total}) for ${entry.subject}`
        );
        return;
      }
    }

    try {
      const payload = {
        schoolId,
        className: selectedItem.className,
        examType: selectedItem.examType,
        passingMark,
        subjects: validSubjects.map((subject) => {
          const theory = parseFloat(subject.theoryMarks) || 0;
          const project = parseFloat(subject.projectMarks) || 0;
          return {
            subjectName: subject.subject.trim(),
            subjectMark: theory,
            projectMark: project,
            fullMark: theory + project,
          };
        }),
      };

      const response = await axios.put(
        `${resultUrlApi.criteria.url}/${selectedItem.id}/${schoolId}`,
        payload
      );

      if (response.data) {
        toast.success("Marks structure updated successfully!");
        fetchMarksData();
        setUpdateDialogOpen(false);
        setSelectedItem(null);
      }
    } catch (error) {
      toast.error(extractApiError(error, "Failed to update marks"));
    }
  };

  // Filter data based on search terms (including subject names!)
  const normalizedTerm = searchTerm.trim().toLowerCase();
  const filteredRows = marksData.flatMap((item) => {
    const matchesExamType =
      searchExamType === "" ||
      searchExamType === "_all" ||
      item.examType === searchExamType;
    if (!matchesExamType) return [];

    const itemMatchesHeader =
      normalizedTerm === "" ||
      item?.examType?.toLowerCase()?.includes(normalizedTerm) ||
      item?.className?.toLowerCase()?.includes(normalizedTerm);

    return (item.subjects || [])
      .filter(
        (subject) =>
          itemMatchesHeader ||
          subject?.subject?.toLowerCase()?.includes(normalizedTerm)
      )
      .map((subject) => ({ item, subject }));
  });

  // Pagination logic on flattened rows so rows-per-page works accurately
  const totalPages = Math.max(1, Math.ceil(filteredRows.length / rowsPerPage));
  const safeCurrentPage = Math.min(currentPage, totalPages);
  const paginatedRows = filteredRows.slice(
    (safeCurrentPage - 1) * rowsPerPage,
    safeCurrentPage * rowsPerPage
  );

  const handleRowsPerPageChange = (newRowsPerPage) => {
    setRowsPerPage(newRowsPerPage);
    const nextTotal = Math.max(1, Math.ceil(filteredRows.length / newRowsPerPage));
    setCurrentPage((prev) => Math.max(1, Math.min(prev, nextTotal)));
  };

  const openUpdateDialog = (item) => {
    const initialSubjects =
      item.subjects?.map((subject, index) => ({
        id: index + 1,
        subject: subject.subject,
        theoryMarks: subject.theoryMarks ?? 0,
        projectMarks: subject.projectMarks ?? 0,
        totalMarks:
          subject.totalMarks ??
          (Number(subject.theoryMarks || 0) + Number(subject.projectMarks || 0)),
      })) || [];

    setSelectedItem({
      id: item.id,
      examType: item.examType,
      className: item.className,
      passingMarks: item.passingMarks ?? 33,
    });
    setEditPassingMarks(
      item.passingMarks !== null && item.passingMarks !== undefined
        ? String(item.passingMarks)
        : "33"
    );
    setEditSubjectEntries(
      initialSubjects.length > 0 ? initialSubjects : [createEmptySubjectRow(1)]
    );
    setUpdateDialogOpen(true);
  };

  const colSpanCount = canManage ? 9 : 8;

  return (
    <div
      className={`min-h-screen ${
        isDarkTheme ? "bg-[#0c1425] text-white" : "bg-gray-50 text-gray-900"
      }`}
    >
      <div className="container mx-auto px-4 py-8">
        {/* Page Header */}
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6">
          <div className="flex items-center mb-4 sm:mb-0">
            <FaGraduationCap
              className={`mr-2 h-6 w-6 ${
                isDarkTheme ? "text-indigo-400" : "text-indigo-600"
              }`}
            />
            <h1 className="text-2xl font-bold">Set Marks for Exam Types</h1>
          </div>
          {canManage && (
            <Dialog
              open={addDialogOpen}
              onOpenChange={(open) => {
                setAddDialogOpen(open);
                if (!open) resetAddForm();
              }}
            >
              <DialogTrigger asChild>
                <Button
                  className={`flex items-center gap-2 ${
                    isDarkTheme
                      ? "bg-indigo-600 hover:bg-indigo-700 text-white"
                      : "bg-gradient-to-r from-purple-600 to-indigo-600 hover:from-purple-700 hover:to-indigo-700 text-white"
                  } rounded-md shadow-md transition-all duration-300`}
                >
                  <FaPlus className="text-sm" />
                  <span>Add New Marks Structure</span>
                </Button>
              </DialogTrigger>
              <DialogContent
                className={`sm:max-w-[800px] max-w-[95%] rounded-xl ${
                  isDarkTheme
                    ? "bg-gray-800 border-gray-700 text-white"
                    : "bg-white border-gray-200"
                } max-h-[90vh] overflow-y-auto`}
              >
                <DialogHeader>
                  <DialogTitle
                    className={`text-xl font-bold pb-2 border-b flex items-center gap-2 ${
                      isDarkTheme
                        ? "text-white border-gray-700"
                        : "text-gray-800 border-gray-200"
                    }`}
                  >
                    <FileSpreadsheet
                      className={`${
                        isDarkTheme ? "text-indigo-400" : "text-indigo-600"
                      }`}
                    />
                    Add New Marks Structure
                  </DialogTitle>
                </DialogHeader>

                <div className="p-4">
                  <form onSubmit={handleAddMarks}>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                      {/* Class Selection */}
                      <div className="flex flex-col gap-2">
                        <Label
                          htmlFor="className"
                          className={
                            isDarkTheme ? "text-gray-200" : "text-gray-700"
                          }
                        >
                          Select Class <span className="text-red-500">*</span>
                        </Label>
                        <Select
                          value={addClassName}
                          onValueChange={setAddClassName}
                          required
                        >
                          <SelectTrigger
                            className={`w-full ${
                              isDarkTheme
                                ? "bg-gray-700 border-gray-600 text-white focus:ring-indigo-500"
                                : "bg-white border-gray-300 focus:ring-indigo-500"
                            }`}
                          >
                            <SelectValue placeholder="Select class" />
                          </SelectTrigger>
                          <SelectContent
                            className={
                              isDarkTheme
                                ? "bg-gray-800 border-gray-700 text-white"
                                : ""
                            }
                          >
                            <SelectGroup>
                              <SelectLabel>Classes</SelectLabel>
                              {allClasses.map((item, index) => (
                                <SelectItem key={index} value={item}>
                                  {item}
                                </SelectItem>
                              ))}
                            </SelectGroup>
                          </SelectContent>
                        </Select>
                      </div>

                      {/* Exam Type Selection */}
                      <div className="flex flex-col gap-2">
                        <Label
                          htmlFor="examType"
                          className={
                            isDarkTheme ? "text-gray-200" : "text-gray-700"
                          }
                        >
                          Select Exam Type{" "}
                          <span className="text-red-500">*</span>
                        </Label>
                        <Select
                          value={addExamType}
                          onValueChange={setAddExamType}
                          required
                        >
                          <SelectTrigger
                            className={`w-full ${
                              isDarkTheme
                                ? "bg-gray-700 border-gray-600 text-white focus:ring-indigo-500"
                                : "bg-white border-gray-300 focus:ring-indigo-500"
                            }`}
                          >
                            <SelectValue placeholder="Select exam type" />
                          </SelectTrigger>
                          <SelectContent
                            className={
                              isDarkTheme
                                ? "bg-gray-800 border-gray-700 text-white"
                                : ""
                            }
                          >
                            <SelectGroup>
                              <SelectLabel>Exam Type</SelectLabel>
                              {examTypeData?.map((item, index) => (
                                <SelectItem
                                  key={index}
                                  value={item.examTypeName}
                                >
                                  {item.examTypeName}
                                </SelectItem>
                              ))}
                            </SelectGroup>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>

                    {/* Global Passing Marks */}
                    <div className="mb-6">
                      <div className="flex flex-col gap-2">
                        <Label
                          htmlFor="passingMarks"
                          className={
                            isDarkTheme ? "text-gray-200" : "text-gray-700"
                          }
                        >
                          Global Passing Marks (%){" "}
                          <span className="text-red-500">*</span>
                          <span className="text-xs ml-2 font-normal">
                            (Applied to all subjects)
                          </span>
                        </Label>
                        <Input
                          id="passingMarks"
                          name="passingMarks"
                          type="number"
                          min="0"
                          max="100"
                          step="any"
                          value={addPassingMarks}
                          onChange={(e) => setAddPassingMarks(e.target.value)}
                          className={`w-full ${
                            isDarkTheme
                              ? "bg-gray-700 border-gray-600 text-white focus:ring-indigo-500"
                              : "bg-white border-gray-300 focus:ring-indigo-500"
                          }`}
                          required
                        />
                      </div>
                    </div>

                    {/* Subject Entries Section */}
                    <div
                      className={`p-4 rounded-lg mb-6 ${
                        isDarkTheme ? "bg-gray-700" : "bg-gray-50"
                      }`}
                    >
                      <div className="flex justify-between items-center mb-4">
                        <h3
                          className={`text-lg font-medium ${
                            isDarkTheme ? "text-white" : "text-gray-800"
                          }`}
                        >
                          Subject Marks Distribution
                        </h3>
                        <Button
                          type="button"
                          onClick={() => {
                            const newId =
                              addSubjectEntries.length > 0
                                ? Math.max(
                                    ...addSubjectEntries.map((entry) => entry.id)
                                  ) + 1
                                : 1;
                            setAddSubjectEntries([
                              ...addSubjectEntries,
                              createEmptySubjectRow(newId),
                            ]);
                          }}
                          className={`${
                            isDarkTheme
                              ? "bg-indigo-600 hover:bg-indigo-700 text-white"
                              : "bg-indigo-500 hover:bg-indigo-600 text-white"
                          }`}
                          size="sm"
                        >
                          <FaPlus className="mr-1" /> Add Subject
                        </Button>
                      </div>

                      {/* Column Headers */}
                      <div className="grid grid-cols-12 gap-2 mb-2 font-medium">
                        <div className="col-span-4">Subject</div>
                        <div className="col-span-2 text-center">
                          Theory Marks
                        </div>
                        <div className="col-span-2 text-center">
                          Project Marks
                        </div>
                        <div className="col-span-2 text-center">
                          Total Marks
                        </div>
                        <div className="col-span-2 text-center">Actions</div>
                      </div>

                      {/* Subject Entry Rows */}
                      {addSubjectEntries.map((entry, index) => (
                        <div
                          key={entry.id}
                          className={`grid grid-cols-12 gap-2 mb-3 p-2 rounded ${
                            isDarkTheme ? "bg-gray-800" : "bg-white"
                          }`}
                        >
                          {/* Subject Selection */}
                          <div className="col-span-4">
                            <Select
                              value={entry.subject}
                              onValueChange={(value) => {
                                const updatedEntries = [...addSubjectEntries];
                                updatedEntries[index] = {
                                  ...updatedEntries[index],
                                  subject: value,
                                };
                                setAddSubjectEntries(updatedEntries);
                              }}
                              required
                            >
                              <SelectTrigger
                                className={`w-full ${
                                  isDarkTheme
                                    ? "bg-gray-700 border-gray-600 text-white"
                                    : "bg-white border-gray-300"
                                }`}
                              >
                                <SelectValue placeholder="Select subject" />
                              </SelectTrigger>
                              <SelectContent
                                className={
                                  isDarkTheme
                                    ? "bg-gray-800 border-gray-700 text-white"
                                    : ""
                                }
                              >
                                <SelectGroup>
                                  {allSubjects
                                    .filter(
                                      (subject) =>
                                        !addSubjectEntries
                                          .filter((_, i) => i !== index)
                                          .map((e) => e.subject)
                                          .includes(subject)
                                    )
                                    .map((item, idx) => (
                                      <SelectItem key={idx} value={item}>
                                        {item}
                                      </SelectItem>
                                    ))}
                                </SelectGroup>
                              </SelectContent>
                            </Select>
                          </div>

                          {/* Theory Marks */}
                          <div className="col-span-2">
                            <Input
                              type="number"
                              min="0"
                              step="any"
                              value={entry.theoryMarks}
                              onChange={(e) => {
                                const theoryValue =
                                  parseFloat(e.target.value) || 0;
                                const updatedEntries = [...addSubjectEntries];
                                updatedEntries[index] = {
                                  ...updatedEntries[index],
                                  theoryMarks: theoryValue,
                                  totalMarks:
                                    theoryValue +
                                    (parseFloat(
                                      updatedEntries[index].projectMarks
                                    ) || 0),
                                };
                                setAddSubjectEntries(updatedEntries);
                              }}
                              className={`w-full ${
                                isDarkTheme
                                  ? "bg-gray-700 border-gray-600 text-white"
                                  : "bg-white border-gray-300"
                              }`}
                              required
                            />
                          </div>

                          {/* Project Marks */}
                          <div className="col-span-2">
                            <Input
                              type="number"
                              min="0"
                              step="any"
                              value={entry.projectMarks}
                              onChange={(e) => {
                                const projectValue =
                                  parseFloat(e.target.value) || 0;
                                const updatedEntries = [...addSubjectEntries];
                                updatedEntries[index] = {
                                  ...updatedEntries[index],
                                  projectMarks: projectValue,
                                  totalMarks:
                                    (parseFloat(
                                      updatedEntries[index].theoryMarks
                                    ) || 0) + projectValue,
                                };
                                setAddSubjectEntries(updatedEntries);
                              }}
                              className={`w-full ${
                                isDarkTheme
                                  ? "bg-gray-700 border-gray-600 text-white"
                                  : "bg-white border-gray-300"
                              }`}
                              required
                            />
                          </div>

                          {/* Total Marks */}
                          <div className="col-span-2">
                            <div
                              className={`w-full p-2 rounded-md border text-center ${
                                isDarkTheme
                                  ? "bg-gray-800 border-gray-600 text-indigo-400"
                                  : "bg-gray-100 border-gray-300 text-indigo-600"
                              }`}
                            >
                              {entry.totalMarks}
                            </div>
                          </div>

                          {/* Actions */}
                          <div className="col-span-2 flex justify-center items-center">
                            <button
                              type="button"
                              onClick={() => {
                                if (addSubjectEntries.length > 1) {
                                  setAddSubjectEntries(
                                    addSubjectEntries.filter(
                                      (e) => e.id !== entry.id
                                    )
                                  );
                                } else {
                                  toast.error(
                                    "At least one subject is required"
                                  );
                                }
                              }}
                              className={`p-1 rounded-full ${
                                isDarkTheme
                                  ? "bg-red-700 hover:bg-red-800 text-white"
                                  : "bg-red-500 hover:bg-red-600 text-white"
                              }`}
                            >
                              <svg
                                xmlns="http://www.w3.org/2000/svg"
                                className="h-5 w-5"
                                viewBox="0 0 20 20"
                                fill="currentColor"
                              >
                                <path
                                  fillRule="evenodd"
                                  d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                  clipRule="evenodd"
                                />
                              </svg>
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>

                    {/* Submit Button */}
                    <div
                      className={`flex justify-end gap-4 mt-6 pt-4 border-t ${
                        isDarkTheme ? "border-gray-700" : "border-gray-200"
                      }`}
                    >
                      <Button
                        type="button"
                        variant="outline"
                        onClick={() => {
                          setAddDialogOpen(false);
                          resetAddForm();
                        }}
                        className={`${
                          isDarkTheme
                            ? "bg-gray-700 text-white hover:bg-gray-600 border-gray-600"
                            : "bg-gray-100 text-gray-800 hover:bg-gray-200 border-gray-300"
                        }`}
                      >
                        Cancel
                      </Button>
                      <Button
                        type="submit"
                        className={`flex items-center gap-2 px-6 py-2 rounded-md transition-all ${
                          isDarkTheme
                            ? "bg-gradient-to-r from-indigo-500 to-purple-600 hover:from-indigo-600 hover:to-purple-700 text-white"
                            : "bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white"
                        }`}
                      >
                        Save Marks Structure
                      </Button>
                    </div>
                  </form>
                </div>
              </DialogContent>
            </Dialog>
          )}
        </div>

        {/* Search and Filter Section */}
        <div
          className={`p-4 rounded-lg mb-6 ${
            isDarkTheme
              ? "bg-gray-800"
              : "bg-white shadow-sm border border-gray-200"
          }`}
        >
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1">
              <div className="relative">
                <FaSearch
                  className={`absolute left-3 top-1/2 transform -translate-y-1/2 ${
                    isDarkTheme ? "text-gray-400" : "text-gray-500"
                  }`}
                />
                <Input
                  type="text"
                  placeholder="Search by class, subject, or exam type..."
                  value={searchTerm}
                  onChange={(e) => {
                    setSearchTerm(e.target.value);
                    setCurrentPage(1);
                  }}
                  className={`pl-10 ${
                    isDarkTheme
                      ? "bg-gray-700 border-gray-600 text-white focus:ring-indigo-500"
                      : "bg-white border-gray-300 focus:ring-indigo-500"
                  }`}
                />
              </div>
            </div>
            <div className="w-full md:w-64">
              <Select
                value={searchExamType}
                onValueChange={(value) => {
                  setSearchExamType(value);
                  setCurrentPage(1);
                }}
              >
                <SelectTrigger
                  className={`w-full ${
                    isDarkTheme
                      ? "bg-gray-700 border-gray-600 text-white focus:ring-indigo-500"
                      : "bg-white border-gray-300 focus:ring-indigo-500"
                  }`}
                >
                  <SelectValue placeholder="Filter by exam type" />
                </SelectTrigger>
                <SelectContent
                  className={
                    isDarkTheme ? "bg-gray-800 border-gray-700 text-white" : ""
                  }
                >
                  <SelectGroup>
                    <SelectItem value="_all">All Exam Types</SelectItem>
                    {examTypeData?.map((item, index) => (
                      <SelectItem key={index} value={item.examTypeName}>
                        {item.examTypeName}
                      </SelectItem>
                    ))}
                  </SelectGroup>
                </SelectContent>
              </Select>
            </div>
            <div>
              <Button
                onClick={() => {
                  setSearchTerm("");
                  setSearchExamType("_all");
                  setCurrentPage(1);
                }}
                className={`${
                  isDarkTheme
                    ? "bg-gray-700 text-white hover:bg-gray-600 border-gray-600"
                    : "bg-gray-100 text-gray-800 hover:bg-gray-200 border-gray-300"
                }`}
                variant="outline"
              >
                Clear Filters
              </Button>
            </div>
          </div>
        </div>

        {/* Marks Data Table */}
        <div
          className={`rounded-lg overflow-hidden ${
            isDarkTheme
              ? "bg-gray-800"
              : "bg-white shadow-sm border border-gray-200"
          }`}
        >
          <Table>
            <TableHeader className={isDarkTheme ? "bg-gray-800" : "bg-gray-50"}>
              <TableRow>
                <TableHead
                  className={`font-semibold ${
                    isDarkTheme ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Exam Type
                </TableHead>
                <TableHead
                  className={`font-semibold ${
                    isDarkTheme ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Class
                </TableHead>
                <TableHead
                  className={`font-semibold ${
                    isDarkTheme ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Subject
                </TableHead>
                <TableHead
                  className={`font-semibold text-center ${
                    isDarkTheme ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Theory Marks
                </TableHead>
                <TableHead
                  className={`font-semibold text-center ${
                    isDarkTheme ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Project Marks
                </TableHead>
                <TableHead
                  className={`font-semibold text-center ${
                    isDarkTheme ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Total Marks
                </TableHead>
                <TableHead
                  className={`font-semibold text-center ${
                    isDarkTheme ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Passing Marks
                </TableHead>
                <TableHead
                  className={`font-semibold text-center ${
                    isDarkTheme ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Created Date
                </TableHead>
                {canManage && (
                  <TableHead
                    className={`font-semibold text-right ${
                      isDarkTheme ? "text-gray-300" : "text-gray-700"
                    }`}
                  >
                    Actions
                  </TableHead>
                )}
              </TableRow>
            </TableHeader>
            <TableBody>
              {isLoading ? (
                <TableRow>
                  <TableCell colSpan={colSpanCount} className="text-center py-8">
                    <div className="flex justify-center items-center">
                      <Loader2
                        className={`h-8 w-8 animate-spin ${
                          isDarkTheme ? "text-indigo-400" : "text-indigo-600"
                        }`}
                      />
                    </div>
                  </TableCell>
                </TableRow>
              ) : paginatedRows.length === 0 ? (
                <TableRow>
                  <TableCell
                    colSpan={colSpanCount}
                    className={`text-center py-8 ${
                      isDarkTheme ? "text-gray-400" : "text-gray-500"
                    }`}
                  >
                    No marks data found. Add your first marks structure!
                  </TableCell>
                </TableRow>
              ) : (
                paginatedRows.map(({ item, subject }, rowIdx) => (
                  <TableRow
                    key={`${item.id}-${subject.subject}-${rowIdx}`}
                    className={
                      isDarkTheme
                        ? "border-gray-700 hover:bg-gray-800/50"
                        : "hover:bg-gray-50"
                    }
                  >
                    <TableCell className={isDarkTheme ? "text-white" : ""}>
                      {item.examType}
                    </TableCell>
                    <TableCell className={isDarkTheme ? "text-white" : ""}>
                      {item.className}
                    </TableCell>
                    <TableCell className={isDarkTheme ? "text-white" : ""}>
                      {subject.subject}
                    </TableCell>
                    <TableCell
                      className={`text-center ${
                        isDarkTheme ? "text-white" : ""
                      }`}
                    >
                      {subject.theoryMarks}
                    </TableCell>
                    <TableCell
                      className={`text-center ${
                        isDarkTheme ? "text-white" : ""
                      }`}
                    >
                      {subject.projectMarks}
                    </TableCell>
                    <TableCell
                      className={`text-center font-medium ${
                        isDarkTheme ? "text-indigo-400" : "text-indigo-600"
                      }`}
                    >
                      {subject.totalMarks}
                    </TableCell>
                    <TableCell
                      className={`text-center ${
                        isDarkTheme ? "text-white" : ""
                      }`}
                    >
                      {item.passingMarks}
                    </TableCell>
                    <TableCell
                      className={`text-center ${
                        isDarkTheme ? "text-gray-300" : "text-gray-500"
                      }`}
                    >
                      {subject.createAt || "-"}
                    </TableCell>
                    {canManage && (
                      <TableCell className="text-right">
                        <div className="flex justify-end gap-2">
                          <Button
                            onClick={() => openUpdateDialog(item)}
                            size="sm"
                            variant="outline"
                            title="Edit marks structure"
                            className={`p-2 ${
                              isDarkTheme
                                ? "bg-gray-700 hover:bg-gray-600 text-indigo-400 border-gray-600"
                                : "bg-gray-100 hover:bg-gray-200 text-indigo-600 border-gray-300"
                            }`}
                          >
                            <FaEdit className="h-4 w-4" />
                          </Button>

                          <DeleteComponent
                            id={item.id}
                            name={`${subject.subject} marks for ${item.className} (${item.examType})`}
                            customData={`${subject.subject} from ${item.className} - ${item.examType}`}
                            deletePath={`${resultUrlApi.criteria.url}/${item.id}/${schoolId}/subject/${encodeURIComponent(subject.subject)}`}
                            onDelete={() => {
                              fetchMarksData();
                            }}
                            buttonVariant="destructive"
                            buttonSize="sm"
                            buttonClassName={`p-2 ${
                              isDarkTheme
                                ? "bg-red-700 hover:bg-red-800 text-white"
                                : "bg-red-500 hover:bg-red-600 text-white"
                            }`}
                          />
                        </div>
                      </TableCell>
                    )}
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </div>

        {/* Pagination */}
        {!isLoading && filteredRows.length > 0 && (
          <div className="mt-6">
            <PaginationComponent
              currentPage={safeCurrentPage}
              totalPages={totalPages}
              onPageChange={setCurrentPage}
              rowsPerPage={rowsPerPage}
              onRowsPerPageChange={handleRowsPerPageChange}
              totalItems={filteredRows.length}
              isDarkTheme={isDarkTheme}
            />
          </div>
        )}
      </div>

      {/* Update Dialog */}
      <Dialog
        open={updateDialogOpen}
        onOpenChange={(open) => {
          setUpdateDialogOpen(open);
          if (!open) {
            setSelectedItem(null);
            setEditSubjectEntries([]);
          }
        }}
      >
        <DialogContent
          className={`sm:max-w-[800px] max-w-[95%] rounded-xl ${
            isDarkTheme
              ? "bg-gray-800 border-gray-700 text-white"
              : "bg-white border-gray-200"
          } max-h-[90vh] overflow-y-auto`}
        >
          <DialogHeader>
            <DialogTitle
              className={`text-xl font-bold pb-2 border-b flex items-center gap-2 ${
                isDarkTheme
                  ? "text-white border-gray-700"
                  : "text-gray-800 border-gray-200"
              }`}
            >
              <FaEdit
                className={isDarkTheme ? "text-indigo-400" : "text-indigo-600"}
              />
              Update Marks Structure
            </DialogTitle>
          </DialogHeader>

          {selectedItem && (
            <div className="p-4">
              <form onSubmit={handleUpdateMarks}>
                {/* Static Fields */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                  <div className="flex flex-col gap-2">
                    <Label>Class</Label>
                    <p
                      className={`font-medium p-2 rounded-md border ${
                        isDarkTheme
                          ? "bg-gray-700 border-gray-600 text-white"
                          : "bg-gray-50 border-gray-300 text-gray-800"
                      }`}
                    >
                      {selectedItem.className}
                    </p>
                  </div>
                  <div className="flex flex-col gap-2">
                    <Label>Exam Type</Label>
                    <p
                      className={`font-medium p-2 rounded-md border ${
                        isDarkTheme
                          ? "bg-gray-700 border-gray-600 text-white"
                          : "bg-gray-50 border-gray-300 text-gray-800"
                      }`}
                    >
                      {selectedItem.examType}
                    </p>
                  </div>
                </div>

                {/* Global Passing Marks */}
                <div className="mb-6">
                  <div className="flex flex-col gap-2">
                    <Label htmlFor="editPassingMarks">
                      Global Passing Marks (%){" "}
                      <span className="text-red-500">*</span>
                    </Label>
                    <Input
                      id="editPassingMarks"
                      name="passingMarks"
                      type="number"
                      min="0"
                      max="100"
                      step="any"
                      value={editPassingMarks}
                      onChange={(e) => setEditPassingMarks(e.target.value)}
                      className={`w-full ${
                        isDarkTheme
                          ? "bg-gray-700 border-gray-600 text-white"
                          : "bg-white border-gray-300"
                      }`}
                      required
                    />
                  </div>
                </div>

                {/* Subject Marks Distribution */}
                <div
                  className={`p-4 rounded-lg mb-6 ${
                    isDarkTheme ? "bg-gray-700" : "bg-gray-50"
                  }`}
                >
                  <div className="flex justify-between items-center mb-4">
                    <h3 className="text-lg font-medium">
                      Subject Marks Distribution
                    </h3>
                    <Button
                      type="button"
                      onClick={() => {
                        const newId =
                          editSubjectEntries.length > 0
                            ? Math.max(...editSubjectEntries.map((e) => e.id)) +
                              1
                            : 1;
                        setEditSubjectEntries([
                          ...editSubjectEntries,
                          createEmptySubjectRow(newId),
                        ]);
                      }}
                      className={
                        isDarkTheme ? "bg-indigo-600 hover:bg-indigo-700" : ""
                      }
                      size="sm"
                    >
                      <FaPlus className="mr-1" /> Add Subject
                    </Button>
                  </div>

                  {/* Subject Entries Table */}
                  <div className="grid grid-cols-12 gap-2 mb-2 font-medium">
                    <div className="col-span-4">Subject</div>
                    <div className="col-span-2 text-center">Theory Marks</div>
                    <div className="col-span-2 text-center">Project Marks</div>
                    <div className="col-span-2 text-center">Total Marks</div>
                    <div className="col-span-2 text-center">Actions</div>
                  </div>

                  {editSubjectEntries?.map((entry, index) => (
                    <div
                      key={entry.id}
                      className={`grid grid-cols-12 gap-2 mb-3 p-2 rounded ${
                        isDarkTheme ? "bg-gray-800" : "bg-white"
                      }`}
                    >
                      <div className="col-span-4">
                        <Select
                          value={entry.subject}
                          onValueChange={(value) => {
                            const updated = [...editSubjectEntries];
                            updated[index] = {
                              ...updated[index],
                              subject: value,
                            };
                            setEditSubjectEntries(updated);
                          }}
                        >
                          <SelectTrigger
                            className={`w-full ${
                              isDarkTheme
                                ? "bg-gray-700 border-gray-600 text-white"
                                : ""
                            }`}
                          >
                            <SelectValue placeholder="Select subject" />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectGroup>
                              {[
                                ...new Set([
                                  ...(entry.subject ? [entry.subject] : []),
                                  ...allSubjects,
                                ]),
                              ]
                                .filter(
                                  (subject) =>
                                    !editSubjectEntries
                                      .filter((_, i) => i !== index)
                                      .map((e) => e.subject)
                                      .includes(subject) ||
                                    subject === entry.subject
                                )
                                .map((item, idx) => (
                                  <SelectItem key={idx} value={item}>
                                    {item}
                                  </SelectItem>
                                ))}
                            </SelectGroup>
                          </SelectContent>
                        </Select>
                      </div>

                      <div className="col-span-2">
                        <Input
                          type="number"
                          min="0"
                          step="any"
                          value={entry.theoryMarks}
                          onChange={(e) => {
                            const value = parseFloat(e.target.value) || 0;
                            const updated = [...editSubjectEntries];
                            updated[index] = {
                              ...updated[index],
                              theoryMarks: value,
                              totalMarks:
                                value +
                                (parseFloat(updated[index].projectMarks) || 0),
                            };
                            setEditSubjectEntries(updated);
                          }}
                          className={
                            isDarkTheme
                              ? "bg-gray-700 border-gray-600 text-white"
                              : ""
                          }
                        />
                      </div>

                      <div className="col-span-2">
                        <Input
                          type="number"
                          min="0"
                          step="any"
                          value={entry.projectMarks}
                          onChange={(e) => {
                            const value = parseFloat(e.target.value) || 0;
                            const updated = [...editSubjectEntries];
                            updated[index] = {
                              ...updated[index],
                              projectMarks: value,
                              totalMarks:
                                (parseFloat(updated[index].theoryMarks) || 0) +
                                value,
                            };
                            setEditSubjectEntries(updated);
                          }}
                          className={
                            isDarkTheme
                              ? "bg-gray-700 border-gray-600 text-white"
                              : ""
                          }
                        />
                      </div>

                      <div className="col-span-2">
                        <div
                          className={`p-2 text-center rounded ${
                            isDarkTheme
                              ? "bg-gray-700 text-indigo-400"
                              : "bg-gray-100"
                          }`}
                        >
                          {entry.totalMarks}
                        </div>
                      </div>

                      <div className="col-span-2 flex justify-center">
                        <button
                          type="button"
                          onClick={() => {
                            if (editSubjectEntries.length > 1) {
                              setEditSubjectEntries(
                                editSubjectEntries.filter(
                                  (e) => e.id !== entry.id
                                )
                              );
                            } else {
                              toast.error("At least one subject is required");
                            }
                          }}
                          className={`p-1 rounded-full ${
                            isDarkTheme
                              ? "bg-red-700 hover:bg-red-800"
                              : "bg-red-500 hover:bg-red-600"
                          } text-white`}
                        >
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            className="h-5 w-5"
                            viewBox="0 0 20 20"
                            fill="currentColor"
                          >
                            <path
                              fillRule="evenodd"
                              d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                              clipRule="evenodd"
                            />
                          </svg>
                        </button>
                      </div>
                    </div>
                  ))}
                </div>

                {/* Action Buttons */}
                <div
                  className={`flex justify-end gap-4 pt-4 border-t ${
                    isDarkTheme ? "border-gray-700" : "border-gray-200"
                  }`}
                >
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => setUpdateDialogOpen(false)}
                    className={isDarkTheme ? "bg-gray-700 border-gray-600" : ""}
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    className="bg-indigo-600 hover:bg-indigo-700 text-white"
                  >
                    Update All Marks
                  </Button>
                </div>
              </form>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default SetMarksForExamTypePage;
