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
import AddNewComponents from "@/components/Addnew/AddNewComponents";
import DeleteComponent from "@/components/DeleteData/DeleteComponent";
import EditDataComponent from "@/components/EditData/EditDataComponent";
import { useDispatch, useSelector } from "react-redux";
import { setGradeSystem } from "@/utils/result/resultSlice";
import resultUrlApi from "@/common/result";
import TourButton from "@/components/Tour/TourButton";
import { gradeSystemPageSteps } from "@/components/Tour/Steps/ResultSteps/Steps";
import { extractApiError } from "@/utils/result/resultHelpers";

const GradeSystemPage = () => {
  const { theme } = useTheme();
  const [loading, setLoading] = useState(false);
  const [postApiLoading, setPostApiLoading] = useState(false);
  const role = useSelector((state) => state.auth.user);
  const gradeSystem = useSelector((state) => state.result.gradeSystem) || [];
  const schoolId = useSelector((state) => state?.auth?.schoolId);
  const dispatch = useDispatch();

  const [input, setInput] = useState({
    scaleStartMarks: "",
    scaleEndMarks: "",
    letterGrade: "",
    performanceIndicator: "",
  });

  const [editInput, setEditInput] = useState({
    scaleStartMarks: "",
    scaleEndMarks: "",
    letterGrade: "",
    performanceIndicator: "",
  });

  const dataLength = gradeSystem?.length || 0;

  const handleEditClick = (item) => {
    setEditInput({
      scaleStartMarks:
        item.scaleStartMarks !== null && item.scaleStartMarks !== undefined
          ? String(item.scaleStartMarks)
          : "",
      scaleEndMarks:
        item.scaleEndMarks !== null && item.scaleEndMarks !== undefined
          ? String(item.scaleEndMarks)
          : "",
      letterGrade: item.letterGrade || "",
      performanceIndicator: item.performanceIndicator || "",
    });
  };

  const changeEventHandler = (e) => {
    setInput({
      ...input,
      [e.target.name]: e.target.value,
    });
  };

  const validateGradePayload = (values) => {
    const start = Number(values.scaleStartMarks);
    const end = Number(values.scaleEndMarks);
    if (
      values.scaleStartMarks === "" ||
      values.scaleEndMarks === "" ||
      !Number.isInteger(start) ||
      !Number.isInteger(end)
    ) {
      toast.error("Scale start and end marks must be whole numbers (0-100)");
      return null;
    }
    if (start < 0 || end < 0 || start > 100 || end > 100) {
      toast.error("Marks scale must be between 0 and 100");
      return null;
    }
    if (start > end) {
      toast.error("Scale start marks cannot be greater than scale end marks");
      return null;
    }
    const letterGrade = String(values.letterGrade || "").trim();
    const performanceIndicator = String(values.performanceIndicator || "").trim();
    if (!letterGrade || !performanceIndicator) {
      toast.error("Letter grade and performance indicator are required");
      return null;
    }
    return {
      scaleStartMarks: start,
      scaleEndMarks: end,
      letterGrade,
      performanceIndicator,
    };
  };

  const fetchGradeSystemData = async () => {
    if (!schoolId) return;
    try {
      setLoading(true);
      const response = await axios.get(
        `${resultUrlApi.allGradeSystem.url}/${schoolId}`
      );
      if (response?.data) {
        dispatch(setGradeSystem(response.data.data || []));
      }
    } catch (error) {
      toast.error(extractApiError(error, "Failed to fetch grade system"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (schoolId) {
      fetchGradeSystemData();
    }
  }, [schoolId]);

  const addNewGradeSystem = async (e) => {
    e.preventDefault();
    if (!schoolId) return;
    const payload = validateGradePayload(input);
    if (!payload) return;

    try {
      setPostApiLoading(true);
      const response = await axios.post(
        `${resultUrlApi.allGradeSystem.url}/${schoolId}`,
        payload,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      setInput({
        scaleStartMarks: "",
        scaleEndMarks: "",
        letterGrade: "",
        performanceIndicator: "",
      });
      toast.success(
        response?.data?.message || "Grade System added successfully!"
      );
      fetchGradeSystemData();
    } catch (error) {
      toast.error(extractApiError(error, "Error adding grade"));
    } finally {
      setPostApiLoading(false);
    }
  };

  const updateGradeSystemData = async (e, id) => {
    e.preventDefault();
    if (!schoolId) return;
    const payload = validateGradePayload(editInput);
    if (!payload) return;

    try {
      setPostApiLoading(true);
      const response = await axios.put(
        `${resultUrlApi.allGradeSystem.url}/${id}/${schoolId}`,
        payload,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response) {
        toast.success(
          response?.data?.message || "Grade system updated successfully!"
        );
        fetchGradeSystemData();
      }
    } catch (error) {
      toast.error(extractApiError(error, "Failed to update grade"));
    } finally {
      setPostApiLoading(false);
    }
  };

  const dialogHeader = "Grade System";
  const formData = [
    {
      name: "scaleStartMarks",
      label: "Scale start mark",
      type: "number",
      placeholder: "enter start mark (0-100)",
      required: true,
    },
    {
      name: "scaleEndMarks",
      label: "Scale end mark",
      type: "number",
      placeholder: "enter end mark (0-100)",
      required: true,
    },
    {
      name: "letterGrade",
      label: "Letter grade",
      type: "text",
      placeholder: "ex: A+",
      required: true,
    },
    {
      name: "performanceIndicator",
      label: "Performance indicator",
      type: "text",
      placeholder: "ex: Excellent",
      required: true,
    },
  ];

  // Pagination state
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [currentPage, setCurrentPage] = useState(1);

  const totalPages = Math.max(1, Math.ceil(dataLength / rowsPerPage));
  const safeCurrentPage = Math.min(currentPage, totalPages);
  const paginatedData = gradeSystem?.slice(
    (safeCurrentPage - 1) * rowsPerPage,
    safeCurrentPage * rowsPerPage
  );

  const handleRowsPerPageChange = (newRowsPerPage) => {
    setRowsPerPage(newRowsPerPage);
    const newTotalPages = Math.max(1, Math.ceil(dataLength / newRowsPerPage));
    setCurrentPage((prev) => Math.max(1, Math.min(prev, newTotalPages)));
  };

  const headers = ["S.No", "Score %", "Letter Grade", "Performance Indicator"];
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
      <div className="container mx-auto py-8 px-4">
        {/* Page Header with Gradient */}
        <div
          className={`mb-6 ${
            theme === "light" ? "text-white" : "text-gray-800"
          }`}
        >
          <h1 className="text-2xl md:text-3xl font-bold relative inline-block">
            Grade System
            <span className="absolute bottom-[-5px] left-0 w-full h-1 bg-gradient-to-r from-purple-500 to-pink-500"></span>
          </h1>
          <p className="mt-2 text-sm md:text-base opacity-80">
            Manage your school's grading criteria and performance indicators
          </p>
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
                Grade System
              </h2>
              <p
                className={`text-sm mt-1 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                {gradeSystem?.length || 0} grade criteria defined
              </p>
            </div>

            <div className=" flex gap-4">
              {["edp", "admin"].includes(role) && (
                <div className="flex items-center">
                  <AddNewComponents
                    onChangeFunctin={changeEventHandler}
                    onSubmitFunction={addNewGradeSystem}
                    dialogHeader={dialogHeader}
                    formData={formData}
                    inputValue={input}
                    postApiLoading={postApiLoading}
                    buttonClassName={`flex items-center gap-2 px-4 py-2 rounded-lg bg-gradient-to-r from-purple-600 to-pink-500 text-white hover:from-purple-700 hover:to-pink-600 transition-all duration-300 shadow-md hover:shadow-lg`}
                    buttonText="Add New Grade"
                  />
                </div>
              )}

              {/* Tour button */}
              <TourButton
                steps={gradeSystemPageSteps}
                tourName={"gradeSystemTour"}
              />
            </div>
          </div>

          {/* Loading State */}
          {loading ? (
            <div className="flex justify-center items-center p-12">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-purple-500"></div>
            </div>
          ) : gradeSystem?.length === 0 ? (
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
                No grade criteria found
              </p>
              <p
                className={`text-sm mt-2 ${
                  theme === "light" ? "text-gray-400" : "text-gray-500"
                }`}
              >
                Add your first grade criteria to get started
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
                        className={`px-4 py-3 hidden sm:table-cell ${
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
                        {item.scaleStartMarks} - {item.scaleEndMarks}
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-100 text-purple-800">
                          {item.letterGrade}
                        </span>
                      </TableCell>
                      <TableCell
                        className={`px-4 py-3 ${
                          theme === "light" ? "text-white" : "text-gray-800"
                        }`}
                      >
                        {item.performanceIndicator}
                      </TableCell>
                      {["edp", "admin"].includes(role) && (
                        <TableCell className="px-4 py-3 text-right">
                          <div className="flex justify-end items-center gap-2">
                            <EditDataComponent
                              formData={formData}
                              postApiLoading={postApiLoading}
                              onChangeFunctin={(e) =>
                                setEditInput({
                                  ...editInput,
                                  [e.target.name]: e.target.value,
                                })
                              }
                              inputValue={editInput}
                              onSubmitFunction={(e) =>
                                updateGradeSystemData(e, item.id)
                              }
                              onEditClick={() => handleEditClick(item)}
                              buttonClassName="p-2 rounded-full bg-blue-50 text-blue-600 hover:bg-blue-100 transition-colors"
                            />

                            <DeleteComponent
                              name={item.performanceIndicator}
                              customData={`${item.letterGrade} (${item.scaleStartMarks}-${item.scaleEndMarks})`}
                              deletePath={`${resultUrlApi.allGradeSystem.url}/${item.id}/${schoolId}`}
                              onDelete={() => {
                                const updatedData = gradeSystem.filter(
                                  (data) => data.id !== item.id
                                );
                                dispatch(setGradeSystem(updatedData));
                                const nextTotal = Math.max(
                                  1,
                                  Math.ceil(updatedData.length / rowsPerPage)
                                );
                                setCurrentPage((prev) =>
                                  Math.max(1, Math.min(prev, nextTotal))
                                );
                              }}
                              buttonClassName="p-2 rounded-full bg-red-50 text-red-600 hover:bg-red-100 transition-colors"
                            />
                          </div>
                        </TableCell>
                      )}
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}

          {/* Pagination */}
          {gradeSystem?.length > 0 && (
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

export default GradeSystemPage;
