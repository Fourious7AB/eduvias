const baseUrl = import.meta.env.VITE_REACT_SERVICE6_URL;
const baseUrl1 = import.meta.env.VITE_REACT_SERVICE1_URL;
const baseUrl2 = import.meta.env.VITE_REACT_SERVICE7_URL;
const externalResultDateBaseUrl = import.meta.env.VITE_REACT_RESULT_DATE_API || "";
const externalSchoolFeedBaseUrl = import.meta.env.VITE_REACT_BASE_URL_LOCAL || "";

const resultApi = `${baseUrl}/api`;
const searchResultApi = `${baseUrl1}`;
const searchResultApi1 = `${baseUrl1}`;
const examTypeApi = `${baseUrl2}`;

const resultDateBaseUrl = externalResultDateBaseUrl || `${resultApi}/result-publish-date`;
const additionalMarksBaseUrl = externalSchoolFeedBaseUrl
  ? `${externalSchoolFeedBaseUrl}/api/additionalSubjectMarkRegister`
  : `${resultApi}/additional-marks-register`;

const resultUrlApi = {
  // for grade system
  allGradeSystem: {
    url: `${resultApi}/grades`,
  },

  // for exam criteria (set marks for exam type)
  criteria: {
    url: `${resultApi}/schools/criteria`,
  },

  // for marksheet
  marksheet: {
    url: `${resultApi}/marksheet`,
  },

  // for annual report card
  annualMarksheet: {
    url: `${resultApi}/marksheet/annual`,
  },

  // Mark register
  searchStudentForMark: {
    url: `${searchResultApi}`,
  },
  searchStudentForMark1: {
    url: `${searchResultApi1}`,
  },
  markRegister: {
    url: `${resultApi}/marks-register`,
  },
  fetchExamType: {
    url: `${examTypeApi}/api/v1/routine/exam-type`,
  },

  // Additional subject mark register (backed by Result service locally, or schoolfeed if configured)
  additionalMarkRegister: {
    url: additionalMarksBaseUrl,
  },

  // view marks
  viewMarks: {
    url: `${resultApi}/marks-register`,
  },

  // Preserved untouched for ExamRoutinesPage.jsx
  deleteExamRoutine: {
    url: `${examTypeApi}/api/exam-routines`,
  },

  // Result Date (backed by Result service locally, or schoolfeed if configured)
  resultDate: {
    create: { url: `${resultDateBaseUrl}/create` },
    update: { url: `${resultDateBaseUrl}/update` },
    delete: { url: `${resultDateBaseUrl}/delete` },
    getAll: { url: `${resultDateBaseUrl}/getAll` },
    search: { url: `${resultDateBaseUrl}/search` },
  },
};

export default resultUrlApi;
