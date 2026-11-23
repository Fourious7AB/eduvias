import { useTheme } from "@/context/ThemeContext";
import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
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
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { Loader2, Plus, Search } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import PaginationComponent from "@/components/pagination/PaginationComponent";
import DeleteComponent from "@/components/DeleteData/DeleteComponent";
import resultUrlApi from "@/common/result";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  capitalizeFirst,
  extractApiError,
  formatDateDDMMYYYY,
} from "@/utils/result/resultHelpers";

const getRecordId = (item) => item?.id || item?._id;

const SetResultDatePage = () => {
  const { theme } = useTheme();
  const [loading, setLoading] = useState(false);
  const [postApiLoading, setPostApiLoading] = useState(false);
  const schoolId = useSelector((state) => state?.auth?.schoolId);
  const role = useSelector((state) => state.auth.user);
  const userDetails = useSelector((state) => state.auth.userDetails) || {};
  const allClass = useSelector((state) => state.class.classNames) || [];
  const examTypeData = useSelector((state) => state.examType.examType) || [];

  const [resultPublishDates, setResultPublishDates] = useState([]);
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);

  // Search state
  const [searchData, setSearchData] = useState({
    className: "",
    examType: "",
  });

  const [input, setInput] = useState({
    className: "",
    examType: "",
    resultPublishDate: "",
  });

  const [editInput, setEditInput] = useState({
    className: "",
    examType: "",
    resultPublishDate: "",
  });

  const dataLength = resultPublishDates?.length || 0;

  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);
  const [editId, setEditId] = useState(null);

  const handleEditClick = (item) => {
    setEditId(getRecordId(item));
    let formattedDate = "";
    if (item.resultPublishDate) {
      const str = String(item.resultPublishDate).trim();
      const isoMatch = /^(\d{4}-\d{2}-\d{2})/.exec(str);
      formattedDate = isoMatch
        ? isoMatch[1]
        : new Date(str).toISOString().split("T")[0];
    }
    setEditInput({
      className: item.className || "",
      examType: item.examType || "",
      resultPublishDate: formattedDate,
    });
    setIsEditDialogOpen(true);
  };

  const handleResetSearch = () => {
    setSearchData({
      className: "",
      examType: "",
    });
    setCurrentPage(1);
    fetchResultDateData();
  };

  const changeEventHandler = (e) => {
    setInput({
      ...input,
      [e.target.name]: e.target.value,
    });
  };

  const fetchResultDateData = async () => {
    if (!schoolId || !resultUrlApi.resultDate?.getAll?.url) return;
    try {
      setLoading(true);
      const response = await axios.get(
        `${resultUrlApi.resultDate.getAll.url}/${schoolId}`
      );
      const records = Array.isArray(response?.data?.data)
        ? response.data.data
        : [];
      setResultPublishDates(records);
    } catch (error) {
      toast.error(extractApiError(error, "Failed to fetch result dates"));
      setResultPublishDates([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (schoolId) {
      fetchResultDateData();
    }
  }, [schoolId]);

  const addNewResultDate = async (e) => {
    e.preventDefault();
    if (!schoolId) return;
    if (!input.className || !input.examType || !input.resultPublishDate) {
      toast.error("Please select class, exam type, and publish date");
      return;
    }

    try {
      setPostApiLoading(true);
      const response = await axios.post(
        `${resultUrlApi.resultDate.create.url}/${schoolId}`,
        input,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      setInput({
        className: "",
        examType: "",
        resultPublishDate: "",
      });
      setIsAddDialogOpen(false);
      toast.success(
        response?.data?.message || "Result Date added successfully!"
      );
      fetchResultDateData();
    } catch (error) {
      toast.error(extractApiError(error, "Error adding Result Date"));
    } finally {
      setPostApiLoading(false);
    }
  };

  const updateResultDate = async (e) => {
    e.preventDefault();
    if (!schoolId || !editId) return;
    if (
      !editInput.className ||
      !editInput.examType ||
      !editInput.resultPublishDate
    ) {
      toast.error("Please select class, exam type, and publish date");
      return;
    }

    try {
      setPostApiLoading(true);
      const response = await axios.put(
        `${resultUrlApi.resultDate.update.url}/${schoolId}/${editId}`,
        editInput,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response) {
        toast.success(
          response?.data?.message || "Result Date updated successfully!"
        );
        setIsEditDialogOpen(false);
        setEditId(null);
        fetchResultDateData();
      }
    } catch (error) {
      toast.error(extractApiError(error, "Failed to update Result Date"));
    } finally {
      setPostApiLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!schoolId) return;

    const params = {
      className:
        role === "student" ? userDetails?.className || "" : searchData.className,
      examType: searchData.examType,
    };

    try {
      setLoading(true);
      const response = await axios.get(
        `${resultUrlApi.resultDate.search.url}/${schoolId}`,
        { params }
      );
      const records = Array.isArray(response?.data?.data)
        ? response.data.data
        : [];
      setResultPublishDates(records);
      setCurrentPage(1);
    } catch (error) {
      toast.error(extractApiError(error, "Search failed"));
      setResultPublishDates([]);
      setCurrentPage(1);
    } finally {
      setLoading(false);
    }
  };

  // Pagination state
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [currentPage, setCurrentPage] = useState(1);

  const totalPages = Math.max(1, Math.ceil(dataLength / rowsPerPage));
  const safeCurrentPage = Math.min(currentPage, totalPages);
  const paginatedData = resultPublishDates?.slice(
    (safeCurrentPage - 1) * rowsPerPage,
    safeCurrentPage * rowsPerPage
  );

  const handleRowsPerPageChange = (newRowsPerPage) => {
    setRowsPerPage(newRowsPerPage);
    const newTotalPages = Math.max(1, Math.ceil(dataLength / newRowsPerPage));
    setCurrentPage((prev) => Math.max(1, Math.min(prev, newTotalPages)));
  };

  const headers = ["S.No", "Class Name", "Exam Type", "Result Publish Date"];
  if (["edp", "admin"].includes(role)) {
    headers.push("Action");
  }

  return (
    <div
      className={`min-h-screen ${
        theme === "light"
          ? "bg-gray-900 text-white"
          : "bg-gray-50 text-gray-800"
      }`}
    >
      <div className="container mx-auto py-6 px-4">
        {/* Page Header with Gradient */}
        <div
          className={`mb-6 ${
            theme === "light" ? "text-white" : "text-gray-800"
          }`}
        >
          <h1 className="text-2xl md:text-3xl font-bold relative inline-block">
            Set And View Result Date
            <span className="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-purple-500 to-pink-500"></span>
          </h1>
          <p className="mt-2 text-sm md:text-base opacity-80">
            Set and view result date for different exams
          </p>
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
            className={`searchBox p-4 border-b ${
              theme === "light" ? "border-gray-700" : "border-gray-200"
            }`}
          >
            <h2
              className={`text-xl font-bold mb-4 ${
                theme === "light" ? "text-white" : "text-gray-800"
              }`}
            >
              Search Result Date
            </h2>

            <form
              onSubmit={handleSearch}
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
                          {capitalizeFirst(item.examTypeName)} -{" "}
                          {item.examMarks}
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>

              {/* Search Buttons */}
              <div className="flex gap-2 w-full">
                <Button
                  type="button"
                  onClick={handleResetSearch}
                  disabled={loading}
                  variant="outline"
                  className={`w-1/2 rounded-md transition-all duration-300 ${
                    theme === "light"
                      ? "bg-gray-700 hover:bg-gray-600 text-white border-gray-600"
                      : "bg-white hover:bg-gray-100 text-gray-800 border-gray-300"
                  }`}
                >
                  Reset
                </Button>
                <Button
                  type="submit"
                  disabled={loading}
                  className="w-1/2 bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-700 hover:to-pink-600 transition-all duration-300 shadow-md hover:shadow-lg"
                >
                  {loading ? (
                    <>
                      <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                      <span className="hidden sm:inline">Searching...</span>
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
            className={`flex flex-col sm:flex-row justify-between items-center p-4 sm:p-6 border-b ${
              theme === "light" ? "border-gray-700" : "border-gray-200"
            }`}
          >
            <div className="gradeAvailable mb-4 sm:mb-0">
              <h2
                className={`text-xl font-bold ${
                  theme === "light" ? "text-white" : "text-gray-800"
                }`}
              >
                Result Publish Date
              </h2>
              <p
                className={`text-sm mt-1 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                {resultPublishDates?.length || 0} Result Publish Date
                {(resultPublishDates?.length || 0) === 1 ? "" : "s"}
              </p>
            </div>

            <div className=" flex gap-4">
              {["edp", "admin"].includes(role) && (
                <div className="flex items-center">
                  <Dialog
                    open={isAddDialogOpen}
                    onOpenChange={(open) => {
                      setIsAddDialogOpen(open);
                      if (!open) {
                        setInput({
                          className: "",
                          examType: "",
                          resultPublishDate: "",
                        });
                      }
                    }}
                  >
                    <DialogTrigger asChild>
                      <Button className="addClasroom bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-700 hover:to-pink-600 text-white shadow-md hover:shadow-lg transition-all duration-300">
                        <Plus className="mr-2 h-4 w-4" />
                        <span>Add Result Date</span>
                      </Button>
                    </DialogTrigger>

                    <DialogContent
                      className={`sm:max-w-[425px] ${
                        theme === "light"
                          ? "bg-gray-800 text-white border-gray-700"
                          : "bg-white text-gray-800 border-gray-200"
                      }`}
                    >
                      <DialogHeader>
                        <DialogTitle
                          className={
                            theme === "light" ? "text-white" : "text-gray-800"
                          }
                        >
                          Set New Result Date
                        </DialogTitle>
                      </DialogHeader>

                      <form onSubmit={addNewResultDate}>
                        <div className="grid gap-6 py-4">
                          {/* Class Selection */}
                          <div className="flex flex-col gap-2">
                            <Label
                              htmlFor="className"
                              className={
                                theme === "light"
                                  ? "text-gray-300"
                                  : "text-gray-700"
                              }
                            >
                              Select a Class
                            </Label>
                            <Select
                              value={input.className}
                              onValueChange={(value) =>
                                setInput({ ...input, className: value })
                              }
                            >
                              <SelectTrigger
                                className={`w-full ${
                                  theme === "light"
                                    ? "bg-gray-700 border-gray-600 text-white"
                                    : "bg-white border-gray-300"
                                }`}
                              >
                                <SelectValue placeholder="Select a class" />
                              </SelectTrigger>
                              <SelectContent
                                className={
                                  theme === "light"
                                    ? "bg-gray-700 text-white border-gray-600"
                                    : "bg-white text-gray-800 border-gray-200"
                                }
                              >
                                <SelectGroup>
                                  <SelectLabel
                                    className={
                                      theme === "light"
                                        ? "text-gray-300"
                                        : "text-gray-700"
                                    }
                                  >
                                    Class
                                  </SelectLabel>
                                  {allClass?.map((item, index) => (
                                    <SelectItem
                                      key={index}
                                      value={item}
                                      className={
                                        theme === "light"
                                          ? "text-white hover:bg-gray-600"
                                          : "text-gray-800 hover:bg-gray-100"
                                      }
                                    >
                                      {capitalizeFirst(item)}
                                    </SelectItem>
                                  ))}
                                </SelectGroup>
                              </SelectContent>
                            </Select>
                          </div>

                          {/* Exam Type Dropdown */}
                          <div>
                            <label
                              className={`block text-sm font-medium mb-1 ${
                                theme === "light"
                                  ? "text-gray-300"
                                  : "text-gray-700"
                              }`}
                            >
                              Exam Type
                            </label>
                            <Select
                              value={input.examType}
                              onValueChange={(value) =>
                                setInput({ ...input, examType: value })
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
                                    <SelectItem
                                      key={index}
                                      value={item.examTypeName}
                                    >
                                      {capitalizeFirst(item.examTypeName)} -{" "}
                                      {item.examMarks}
                                    </SelectItem>
                                  ))}
                                </SelectGroup>
                              </SelectContent>
                            </Select>
                          </div>

                          {/* Result Publish Date Input */}
                          <div className="flex flex-col gap-2">
                            <Label
                              htmlFor="resultPublishDate"
                              className={
                                theme === "light"
                                  ? "text-gray-300"
                                  : "text-gray-700"
                              }
                            >
                              Result Publish Date
                            </Label>
                            <Input
                              id="resultPublishDate"
                              name="resultPublishDate"
                              type="date"
                              required
                              className={`w-full ${
                                theme === "light"
                                  ? "bg-gray-700 border-gray-600 text-white"
                                  : "bg-white border-gray-300"
                              }`}
                              value={input.resultPublishDate}
                              onChange={changeEventHandler}
                            />
                          </div>
                        </div>

                        <DialogFooter>
                          {postApiLoading ? (
                            <Button
                              disabled
                              className="w-full flex items-center justify-center bg-gray-500 text-white cursor-not-allowed"
                            >
                              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                              <span>Saving...</span>
                            </Button>
                          ) : (
                            <Button
                              type="submit"
                              className="w-full bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-700 hover:to-pink-600 text-white shadow-md hover:shadow-lg transition-all duration-300"
                            >
                              Save Result Date
                            </Button>
                          )}
                        </DialogFooter>
                      </form>
                    </DialogContent>
                  </Dialog>
                </div>
              )}
            </div>
          </div>

          {/* Loading State */}
          {loading ? (
            <div className="flex justify-center items-center p-12">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-purple-500"></div>
            </div>
          ) : resultPublishDates?.length === 0 ? (
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
                No result date found
              </p>
              <p
                className={`text-sm mt-2 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                Add your first result date to get started
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
                    {headers.map((header, index) => (
                      <TableHead
                        key={index}
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-gray-200" : "text-gray-700"
                        } font-semibold text-sm ${
                          header === "Action" ? "text-right" : "text-left"
                        } ${header === "S.No" ? "hidden sm:table-cell" : ""}`}
                      >
                        {header}
                      </TableHead>
                    ))}
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {paginatedData?.map((item, index) => {
                    const rowId = getRecordId(item);
                    return (
                      <TableRow
                        key={rowId || index}
                        className={`transition-colors hover:bg-opacity-10 ${
                          theme === "light"
                            ? "hover:bg-gray-600 border-t border-gray-700"
                            : "hover:bg-gray-100 border-t border-gray-200"
                        }`}
                      >
                        <TableCell
                          className={`px-4 py-3 hidden sm:table-cell ${
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
                          {capitalizeFirst(item.className)}
                        </TableCell>
                        <TableCell
                          className={`px-4 py-3 ${
                            theme === "light" ? "text-white" : "text-gray-800"
                          }`}
                        >
                          <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-100 text-purple-800">
                            {capitalizeFirst(item.examType)}
                          </span>
                        </TableCell>
                        <TableCell
                          className={`px-4 py-3 ${
                            theme === "light" ? "text-white" : "text-gray-800"
                          }`}
                        >
                          {formatDateDDMMYYYY(item.resultPublishDate)}
                        </TableCell>
                        {["edp", "admin"].includes(role) && (
                          <TableCell className="px-4 py-3 text-right">
                            <div className="flex justify-end items-center gap-2">
                              <Dialog
                                open={isEditDialogOpen && editId === rowId}
                                onOpenChange={(open) => {
                                  setIsEditDialogOpen(open);
                                  if (!open) setEditId(null);
                                }}
                              >
                                <DialogTrigger asChild>
                                  <button
                                    onClick={() => handleEditClick(item)}
                                    className="p-2 rounded-full bg-blue-50 text-blue-600 hover:bg-blue-100 transition-colors"
                                  >
                                    <svg
                                      className="w-4 h-4"
                                      fill="none"
                                      stroke="currentColor"
                                      viewBox="0 0 24 24"
                                      xmlns="http://www.w3.org/2000/svg"
                                    >
                                      <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth="2"
                                        d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"
                                      ></path>
                                    </svg>
                                  </button>
                                </DialogTrigger>
                                <DialogContent
                                  className={`sm:max-w-[425px] ${
                                    theme === "light"
                                      ? "bg-gray-800 text-white border-gray-700"
                                      : "bg-white text-gray-800 border-gray-200"
                                  }`}
                                >
                                  <DialogHeader>
                                    <DialogTitle
                                      className={
                                        theme === "light"
                                          ? "text-white"
                                          : "text-gray-800"
                                      }
                                    >
                                      Edit Result Date
                                    </DialogTitle>
                                  </DialogHeader>
                                  <form onSubmit={updateResultDate}>
                                    <div className="grid gap-6 py-4">
                                      <div className="flex flex-col gap-2">
                                        <Label
                                          htmlFor="editClassName"
                                          className={
                                            theme === "light"
                                              ? "text-gray-300"
                                              : "text-gray-700"
                                          }
                                        >
                                          Select a Class
                                        </Label>
                                        <Select
                                          value={editInput.className}
                                          onValueChange={(value) =>
                                            setEditInput({
                                              ...editInput,
                                              className: value,
                                            })
                                          }
                                        >
                                          <SelectTrigger
                                            className={`w-full ${
                                              theme === "light"
                                                ? "bg-gray-700 border-gray-600 text-white"
                                                : "bg-white border-gray-300"
                                            }`}
                                          >
                                            <SelectValue placeholder="Select a class" />
                                          </SelectTrigger>
                                          <SelectContent
                                            className={
                                              theme === "light"
                                                ? "bg-gray-700 text-white border-gray-600"
                                                : "bg-white text-gray-800 border-gray-200"
                                            }
                                          >
                                            {allClass?.map((cls, idx) => (
                                              <SelectItem
                                                key={idx}
                                                value={cls}
                                                className={
                                                  theme === "light"
                                                    ? "text-white hover:bg-gray-600"
                                                    : "text-gray-800 hover:bg-gray-100"
                                                }
                                              >
                                                {capitalizeFirst(cls)}
                                              </SelectItem>
                                            ))}
                                          </SelectContent>
                                        </Select>
                                      </div>
                                      <div>
                                        <Label
                                          htmlFor="editExamType"
                                          className={
                                            theme === "light"
                                              ? "text-gray-300"
                                              : "text-gray-700"
                                          }
                                        >
                                          Exam Type
                                        </Label>
                                        <Select
                                          value={editInput.examType}
                                          onValueChange={(value) =>
                                            setEditInput({
                                              ...editInput,
                                              examType: value,
                                            })
                                          }
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
                                            {examTypeData?.map((exam, idx) => (
                                              <SelectItem
                                                key={idx}
                                                value={exam.examTypeName}
                                              >
                                                {capitalizeFirst(
                                                  exam.examTypeName
                                                )}{" "}
                                                - {exam.examMarks}
                                              </SelectItem>
                                            ))}
                                          </SelectContent>
                                        </Select>
                                      </div>
                                      <div className="flex flex-col gap-2">
                                        <Label
                                          htmlFor="editResultPublishDate"
                                          className={
                                            theme === "light"
                                              ? "text-gray-300"
                                              : "text-gray-700"
                                          }
                                        >
                                          Result Publish Date
                                        </Label>
                                        <Input
                                          id="editResultPublishDate"
                                          type="date"
                                          required
                                          className={`w-full ${
                                            theme === "light"
                                              ? "bg-gray-700 border-gray-600 text-white"
                                              : "bg-white border-gray-300"
                                          }`}
                                          value={editInput.resultPublishDate}
                                          onChange={(e) =>
                                            setEditInput({
                                              ...editInput,
                                              resultPublishDate: e.target.value,
                                            })
                                          }
                                        />
                                      </div>
                                    </div>
                                    <DialogFooter>
                                      <Button
                                        type="submit"
                                        disabled={postApiLoading}
                                        className="w-full bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-700 hover:to-pink-600 text-white shadow-md hover:shadow-lg transition-all duration-300"
                                      >
                                        {postApiLoading ? "Saving..." : "Update"}
                                      </Button>
                                    </DialogFooter>
                                  </form>
                                </DialogContent>
                              </Dialog>

                              <DeleteComponent
                                name={`${item.className} - ${item.examType}`}
                                customData={`${item.className} - ${item.examType}`}
                                deletePath={`${resultUrlApi.resultDate.delete.url}/${schoolId}/${rowId}`}
                                onDelete={fetchResultDateData}
                                buttonClassName="p-2 rounded-full bg-red-50 text-red-600 hover:bg-red-100 transition-colors"
                              />
                            </div>
                          </TableCell>
                        )}
                      </TableRow>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
          )}

          {/* Pagination */}
          {resultPublishDates?.length > 0 && (
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

export default SetResultDatePage;
