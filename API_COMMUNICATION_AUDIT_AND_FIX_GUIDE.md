# SchoolSaaS Complete Endpoint Catalog & Frontend ↔ Backend Communication Audit Guide

> **Last Audited:** 2026-11-23
> **Frontend:** `Saas_School_Frontend_New - Copy` (React + Vite on `http://localhost:3001`, `.env` `VITE_API_BASE_URL=""`)
> **Backend:** 9 Spring Boot Microservices under `Recovered-SchoolSaaS-20261121-152414/` (Ports `8081`–`8099`)
> **Database:** Local Docker MongoDB (`localhost:27017`, cloned from `192.168.11.157:27017` — 9 SchoolSaaS DBs, 1,251 users)

---

## 1. Architecture & Proxy Routing Map

| Vite Proxy Prefix | Target Local Backend | Microservice Folder | MongoDB Database (`localhost:27017`) | Frontend Config File |
| :--- | :--- | :--- | :--- | :--- |
| `/service1` | `http://localhost:8085` | `Admission-0.0.1-SNAPSHOT` | `sas_schoolAdmissionMain` | `src/common/main.js`, `src/common/result.js` |
| `/service2` | `http://localhost:8089` | `teacher-0.0.1-SNAPSHOT` | `sas_teachersdb` | `src/common/teacherEdpLibraryan.js` |
| `/service3` | `http://localhost:8090` | `Settings-0.0.1-SNAPSHOT` | `sas_setting` | `src/common/setting.js` |
| `/service4` | `http://localhost:8092` | `auth-service-0.0.2-SNAPSHOT` | `auth_sas` | `src/common/auth.js` |
| `/service5` | `http://localhost:8081` | `AcademicMain-0.0.1-SNAPSHOT` | `sas_academicMain` | `src/common/index.js` |
| `/service6` | `http://localhost:8094` | `Result-0.0.1-SNAPSHOT` | `sas_result1` | `src/common/result.js` |
| `/service7` | `http://localhost:8084` | `RoutineMain-0.0.1-SNAPSHOT` | `sas_routineMain` | `src/common/routines.js`, `src/common/result.js` |
| `/service8` | `http://localhost:8082` | `NewLibrary-0.0.1-SNAPSHOT` | `sas_libraryDatabase` | `src/common/library.js` |
| `/service9` | `http://localhost:8099` | `SchoolAccount-0.0.2-SNAPSHOT` | `sas_schoolAccount` | `src/common/main.js` (`accountApi`) |

---

## 2. Communication Issues Audit & Fix Status Tracker

### A. Runtime / Environment Blockers
| # | Issue | Affected Services | Root Cause | Status |
| :--- | :--- | :--- | :--- | :--- |
| R1 | Empty local databases | All 9 services | Fresh Docker MongoDB had no data while real data lived on `192.168.11.157:27017` | FIXED (All 9 DBs cloned to `localhost:27017`) |
| R2 | Stale MongoDB Atlas URIs | `Admission (8085)`, `Teacher (8089)` | `target/classes/application.properties` (Admission) and `src` (Teacher) pointed to empty Atlas cluster | FIXED ON DISK (Restart required) |
| R3 | Spring Boot 3 CORS HTTP 500 | `Result (8094)`, `SchoolAccount (8099)` | `CorsConfig.java` set `.allowCredentials(true)` while controllers had `@CrossOrigin(origins={"*"})` | FIXED & COMPILED (Restart required) |

### B. Frontend ↔ Backend Route, Verb & State Mismatches
| # | Frontend Location | Broken Frontend Call | Correct Backend Endpoint / Fix | Status |
| :--- | :--- | :--- | :--- | :--- |
| F1 | `src/helper/AllFetchFunction.jsx:45` & `src/pages/academic/SectionPage.jsx:59` | `GET /service5/api/v1/Academic/sections/{schoolId}` (`404` for universal sections) | `GET /service5/api/v1/Academic/sections/all/{schoolId}` | FIXED & VERIFIED (`200 OK`, A–E) |
| F2 | `src/pages/academic/SectionPage.jsx:69` | `useEffect(() => fetchSectionData(), [sectionData])` | Change dependency array to `[schoolId]` (fixes infinite API loop) | FIXED & VERIFIED (`npm run build`) |
| F3 | `src/helper/AllFetchFunction.jsx:185` | `GET /service7/api/v1/routine/class-time/{schoolId}` (`404`, hits `/{id}`) | `GET /service7/api/v1/routine/class-time/all/{schoolId}` | FIXED & VERIFIED (`200 OK`) |
| F4 | `src/pages/Routines/ClassRoutinesPage.jsx:59-64` | `GET /service7/api/v1/routine/{schoolId}` (`404`) + `response.data.data` | `GET /service7/api/v1/routine/all/{schoolId}` + `response?.data?.data ?? response?.data` | FIXED & VERIFIED (`200 OK`, 55 routines) |
| F5 | `src/common/result.js:11` + `src/pages/Result/MarkRegisterPage.jsx:55` | `GET /service1/attendance/{schoolId}/attendance/students` (double `/attendance` → `404`) | Set `searchResultApi = `${BASE_URL}/service1`` → `GET /service1/{schoolId}/attendance/students` | FIXED & VERIFIED (`200 OK`, 26 students) |
| F6 | `src/pages/AllDashboard/TeacherDashboardPage.jsx:27` | `GET /service7/{schoolId}/api/v1/routine/teacher/pdf` (swapped path → `404`) | `GET /service7/api/v1/routine/teacher/{schoolId}/pdf` | FIXED & VERIFIED (`200 OK`, 237 KB PDF) |
| F7 | `src/pages/AllDashboard/StudentDashboardPage.jsx:249` | `GET /service9/student-admission-fee/admissionNumber/{admNo}/{schoolId}` (`404`) | Added `GET /student-admission-fee/admissionNumber/{admissionNumber}/{schoolId}` in `SchoolAccount` | FIXED & VERIFIED (`200 OK`) |
| F8 | `src/pages/Accounts/ExpensePage.jsx:423` | `DELETE /service9/expenses/{id}/{schoolId}` (`404`) | Updated frontend to `/expenses/de/{id}/{schoolId}` + added `/{id}/{schoolId}` alias on `ExpenseController` | FIXED & COMPILED |
| F9 | `src/pages/Accounts/BankTransactionPage.jsx:403` | `DELETE /service9/bank-transactions/{id}/{schoolId}` (`404`) | Updated `BankTransactionController` to accept `/delete/{id}/{schoolId}` and `/{id}/{schoolId}` | FIXED & COMPILED |
| F10 | `src/pages/Edp/ManageEdpPage.jsx:113` | `POST /service2/api/edp/{id}/{schoolId}` (`405 Method Not Allowed`) | Changed to `axios.put(...)` (`PUT /api/edp/{id}/{schoolId}`) | FIXED & VERIFIED (`npm run build`) |
| F11 | `src/pages/Routines/ExamRoutinesPage.jsx:346` | Hardcoded `http://192.168.0.141:8084/api/exam-routines/{id}/{schoolId}` | Uses `${routineUrlApi.getExamRoutine.url}/${classItem.id}/${schoolId}` | FIXED & VERIFIED (`npm run build`) |
| F12 | `src/pages/Auth/SignUpStep2Page.jsx:405` | Hardcoded `http://localhost:5000/create-order` | Uses `${mainUrlApi.signUp.url}/create-order` (`/service1/api/signups/create-order`) | FIXED & VERIFIED (`npm run build`) |

### C. Backend ↔ Backend Hardcoded Production / Dead Ngrok Calls
| # | Calling Service | File | Old URL | New Local Configurable URL | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| B1 | `auth-service` (`8092`) | `AuthController.java:113-123` + `pom.xml` (Lombok `1.18.36`) | 6 hardcoded `https://collage.vaisacademy.com/service1\|service2/...` | `@Value` properties → `http://localhost:8085` & `http://localhost:8089` | FIXED & VERIFIED (`200 OK` local login) |
| B2 | `Admission` (`8085`) | `AdmissionService.java`, `BulkAdmissionService.java`, `SignUpService.java` | `https://collage.vaisacademy.com/service4/api/auth/register` | `@Value("${auth.register.url:...}")` → `http://localhost:8092/api/auth/register` | FIXED & COMPILED |
| B3 | `Admission` (`8085`) | `application.properties:31-50` (7 properties) | `https://collage.vaisacademy.com/service9\|service5/...` | `http://localhost:8099/...` & `http://localhost:8081/...` | FIXED & COMPILED |
| B4 | `NewLibrary` (`8082`) | `BookIssueService.java` + `application.properties` | **DEAD ngrok** `https://731da7c71f22.ngrok-free.app/service1/admissions/validate/` | `@Value("${admission.validate.url:...}")` → `http://localhost:8085/admissions/validate/` | FIXED & COMPILED |
| B5 | `Result` (`8094`) | `application.properties:14` | `https://collage.vaisacademy.com/service1/admissions/student-details` | `http://localhost:8085/admissions/student-details` | FIXED & COMPILED |
| B6 | `RoutineMain` (`8084`) | `application.properties:16` | `https://collage.vaisacademy.com/service1/api/schools/sc/` | `http://localhost:8085/api/schools/sc/` | FIXED & COMPILED |
| B7 | `teacher` (`8089`) | `application.properties:31-42` | `https://collage.vaisacademy.com/service1\|service4/...` | `http://localhost:8085` & `http://localhost:8092` | FIXED & COMPILED |
| B8 | `SchoolAccount` (`8099`) | `application.properties:32` | **DEAD ngrok** `https://731da7c71f22.ngrok-free.app/service1/admissions/updateStatus/` | `http://localhost:8085/admissions/updateStatus/` | FIXED & COMPILED |

> **Important After Applying Fixes:** Restart the 7 updated Spring Boot microservices in IntelliJ IDEA (`Admission`, `auth-service`, `NewLibrary`, `Result`, `RoutineMain`, `SchoolAccount`, `teacher`) so their running JVMs pick up the freshly compiled `target/classes` bytecode and `application.properties`. Vite dev server (`localhost:3001`) picks up frontend edits automatically via HMR.

### D. Known Unimplemented / Stubbed Frontend Placeholders (No Backend Endpoint)
- `src/pages/Attendance/AttendancePage.jsx:50,62` — calls `axios.get("url")` / `axios.post("url")` (use `MarkAttendancePage.jsx` & `AttendanceReportPage.jsx` instead).
- `src/pages/Fees/AbsentFeesPage.jsx:60,72` — calls `axios.get("url")` / `axios.post("url")` (no backend controller exists).
- `src/pages/Fees/LibraryLateFeesPage.jsx:56,68` — calls `axios.get("url")` / `axios.post("url")` (no backend controller exists).
- `src/pages/studentInfo/GuardianInformationPage.jsx:85` — calls `axios.post("url")` (no backend controller exists).
- `src/pages/Routines/ClassRoutinesPage.jsx:77` — unused `editStudent` calls `axios.post("url")` (routine updates happen in `UpdateClassRoutineComponent.jsx`).
- `src/components/Chat/ChatWithSupport.jsx:36` — calls external support bot `http://147.93.116.69:5000/chat` (not part of the 9 local microservices).

---

## 3. Complete Backend Endpoint Catalog (All 9 Microservices)

### Service 1 — Admission (`localhost:8085` | Vite Prefix `/service1` | DB: `sas_schoolAdmissionMain`)
- **AdmissionController**
  - `GET /total-students`, `GET /admissions/total-students/{schoolId}`
  - `GET /admissions`, `GET /admissions/{schoolId}`, `POST /admissions/{schoolId}`, `POST /admissions/bulk/{schoolId}`
  - `PUT /admissions/{admissionNumber}/{schoolId}`
  - `GET /admissions/validate/{admissionNumber}`, `GET /admissions/validate/{admissionNumber}/{schoolId}`
  - `GET /admissions/number/{admissionNumber}`, `GET /admissions/number/{admissionNumber}/{schoolId}`
  - `PUT /admissions/number/{admissionNumber}`, `PUT /admissions/number/{admissionNumber}/{schoolId}`
  - `DELETE /admissions/number/{admissionNumber}`, `DELETE /admissions/number/{admissionNumber}/{schoolId}`
  - `GET /admissions/check-session/{admissionNumber}`, `GET /admissions/student-details`
  - `GET /admissions/generate-id-card/{admissionNumber}`, `GET /admissions/generate-id-card/{admissionNumber}/{schoolId}`, `GET /admissions/generate-class-id-cards/{schoolId}/{className}/{section}`
  - `PUT /admissions/updateStatus`, `PUT /admissions/updateStatus/{schoolId}`
  - `GET /admissions/class/{className}/section/{section}`, `GET /{schoolId}/admissions/class/{className}/section/{section}`
  - `POST|GET /admissions/{admissionNumber}/subjects/{schoolId}`, `DELETE /admissions/{admissionNumber}/subjects/{schoolId}/{subjectName}`
  - `POST|GET /admissions/{admissionNumber}/services/{schoolId}`, `DELETE /admissions/{admissionNumber}/services/{schoolId}/{serviceType}`
  - `POST /admissions/{admissionNumber}/promote/{schoolId}`, `POST /admissions/promote-bulk/{schoolId}`
  - `GET /admissions/{admissionNumber}/history/{schoolId}`, `GET /promotion-history/{admissionNumber}/{schoolId}`, `GET /social/{id}/{schoolId}`
- **AttendanceController**
  - `GET /attendance/students`, `GET /{schoolId}/attendance/students`
  - `GET /attendance/record`, `GET /attendance/record/{schoolId}`, `POST /attendance/record`, `POST /{schoolId}/attendance/record`
  - `GET /attendance/student-attendance`, `GET /{schoolId}/attendance/student-attendance`, `GET /attendance/student-attendance1`, `GET /{schoolId}/attendance/student-attendance1`
  - `GET /attendance/calculate/{custom|current-week|current-month|current-year}/{schoolId}`
  - `GET /attendance/student/{schoolId}/{rollNo}/{custom|current-year|month}`, `GET /attendance/student/current-month/{schoolId}/{rollNo}`
- **SchoolController** (`/api/schools`): `GET /`, `GET /{id}`, `GET /sc/{schoolId}`, `GET /student-and-school-count`, `POST /{schoolId}`, `PUT /{schoolId}`, `DELETE /{id}`
- **SignUpController** (`/api/signups`): `GET /`, `GET /{id}`, `GET /email/{email}`, `POST /`, `PUT /{id}`, `PUT /block/{id}`, `DELETE /{id}`, `POST /create-order`, `POST /capture`
- **PlanController** (`/api/v1/plan`): `GET /`, `GET /{id}`, `POST /`, `PUT /{id}`, `DELETE /{id}`
- **SubsPlanController** (`/api/v1/subscription`): `POST /create-Payment/{schoolId}`, `GET /all/payments`, `GET /all/payments/{schoolId}`, `POST /renew/{schoolId}`, `GET /view-plan/{id}`, `POST /verify-payment`
- **BulkAdmissionController** (`/bulk-admission`): `GET /template`
- **LookupController** (`/api/look-up`): `GET /entity/{id}/{schoolId}`
- **TransferCertificateController** (`/transfer-certificates`): `POST /request/{admissionNumber}/{schoolId}`, `GET /requests/{schoolId}`, `GET /requests/pending/{schoolId}`, `GET /request-status/{admissionNumber}/{schoolId}`, `POST /approve/{requestId}`, `POST /reject/{requestId}`, `POST|GET /{admissionNumber}/{schoolId}`, `GET /number`, `GET /school/{schoolId}`, `GET /download`, `GET /download/{id}/{schoolId}`

### Service 2 — Teacher & Staff (`localhost:8089` | Vite Prefix `/service2` | DB: `sas_teachersdb`)
- **TeachersController** (`/api`): `GET /teachers`, `GET|POST /teachers/{schoolId}`, `POST /teachers`, `GET /teachers/email/{email}[/{schoolId}]`, `GET /teachers/id/{id}[/{schoolId}]`, `GET /teachers/search[/{schoolId}]`, `PUT|DELETE /teachers/{id}[/{schoolId}]`, `POST /teachers/{teacherId}/{schoolId}/assignments[/multiple]`, `GET /teachers/{id}/{schoolId}/assignments`, `PUT|DELETE /teachers/{id}/{schoolId}/assignments/subject`, `GET /teacher-count/{schoolId}`
- **EdpController** (`/api`): `GET /edp`, `GET /edp-teacher-lib/count`, `GET|POST /edp/{schoolId}`, `GET /edp/email/{email}[/{schoolId}]`, `GET /edp/id/{id}[/{schoolId}]`, `PUT|DELETE /edp/{id}[/{schoolId}]`
- **LibrarianController** (`/api`): `GET|POST /lib`, `GET|POST /lib/{schoolId}`, `GET /lib/email/{email}[/{schoolId}]`, `GET /lib/{id}/{schoolId}`, `GET /lib/id/{id}`, `PUT|DELETE /lib/{id}[/{schoolId}]`
- **AccountsController** (`/api`): `GET /accounts`, `GET /counts/all`, `GET|POST /accounts/{schoolId}`, `GET /accounts/email/{email}[/{schoolId}]`, `GET /accounts/id/{id}[/{schoolId}]`, `PUT|DELETE /accounts/{id}[/{schoolId}]`
- **AssignmentController** (`/api`): `POST|GET /assignments`, `GET /assignments/school/{schoolId}`, `GET /assignments/teacher/{teacherId}`, `GET /assignments/school/{schoolId}/teacher/{teacherId}`, `GET /assignments/school/{schoolId}/class/{className}/section/{section}[/subject/{subject}]`, `GET /assignments/school/{schoolId}/date/{date}`, `GET|PUT|DELETE /assignments/{id}`, `PUT /assignments/{close|active}/{id}/{schoolId}`, `GET /assignments/school/{schoolId}/teacher/{teacherId}/due-today/count`, `GET /teacher-summary/{teacherId}/{schoolId}`
- **AssignmentSubmissionController** (`/api`): `POST /submissions`, `GET /submissions/assignment/{assignmentId}`, `GET /submissions/student/{studentId}`, `GET /submissions/school/{schoolId}/student/{studentId}`, `GET|DELETE /submissions/{id}`, `PUT /submissions/{id}/grade`
- **EntityLookupController** (`/api`): `GET /entity/{id}/{schoolId}`

### Service 3 — Settings (`localhost:8090` | Vite Prefix `/service3` | DB: `sas_setting`)
- **BloodGroupController** (`/api`): `GET /bloodgroups`, `GET|POST /bloodgroups/{schoolId}`, `PUT|DELETE /bloodgroups/{id}/{schoolId}`, `POST /bloodgroups/create-defaults`, `GET /bloodgroups/default-json`, `GET|POST /universal-bloodgroups`, `PUT|DELETE /universal-bloodgroups/{id}`
- **GenderController** (`/api`): `GET /genders`, `GET|POST /genders/{schoolId}`, `PUT|DELETE /genders/{id}/{schoolId}`, `POST /genders/create-defaults`, `GET /genders/default-json`, `POST /universal-genders`, `PUT|DELETE /universal-genders/{id}`
- **ReligionController** (`/api`): `GET /religions`, `GET|POST /religions/{schoolId}`, `PUT|DELETE /religions/{id}/{schoolId}`, `POST /religions/create-defaults`, `GET /religions/default-json`, `POST /universal-religions`, `PUT|DELETE /universal-religions/{id}`

### Service 4 — Auth-Service (`localhost:8092` | Vite Prefix `/service4` | DB: `auth_sas`)
- **AuthController** (`/api/auth`): `POST /register`, `POST /login`, `POST /logout`, `GET /test`, `POST /forgot-password`, `POST /reset-password`, `PUT /update/{username}`, `DELETE /delete/{username}`, `GET /all`, `GET /health`

### Service 5 — AcademicMain (`localhost:8081` | Vite Prefix `/service5` | DB: `sas_academicMain`)
- **ClassController** (`/api/v1/Academic`): `GET /classes`, `GET|POST /classes/{schoolId}`, `GET /classes/all/{schoolId}`, `GET|POST /classes/universal`, `PUT|DELETE /classes/{id}/{schoolId}`, `PUT /classes/{id}/`, `DELETE /classes/{id}`, `GET /class/{className}/schoolId/{schoolId}`
- **SectionsController** (`/api/v1/Academic`): `GET /sections`, `GET|POST /sections/{schoolId}`, `GET /sections/all/{schoolId}`, `GET|POST /sections/universal`, `PUT|DELETE /sections/{id}/{schoolId}`
- **SubjectController** (`/api/v1/Academic`): `GET /subjects`, `GET|POST /subjects/{schoolId}`, `GET /subjects/all/{schoolId}`, `GET|POST /subjects/universal`, `PUT|DELETE /subjects/{id}/{schoolId}`
- **ClassRoomController** (`/api/v1/Academic`): `GET /class-room`, `GET|POST /class-room/{schoolId}`, `PUT|DELETE /class-room/{id}/{schoolId}`, `GET /class-room/{schoolId}/search`
- **SubjectAssignController** (`/api/v1/Academic`): `GET /subject-assign`, `GET|POST /subject-assign/{schoolId}`, `PUT|DELETE /subject-assign/{id}/{schoolId}`, `GET /subject-assign/{schoolId}/search`
- **ServicesController** (`/api/services`): `GET|POST /`, `GET /{id}`, `GET|POST /universal`, `GET /school/{schoolId}`, `GET /all/{schoolId}`, `PUT /update/{id}`, `PUT /assigned/{id}`, `DELETE /unassigned/{id}`, `DELETE /assigned/{id}`

### Service 6 — Result (`localhost:8094` | Vite Prefix `/service6` | DB: `sas_result1`)
- **GradeSystemController** (`/api/grades`): `GET|POST /`, `PUT|DELETE /{id}`, `GET|POST /{schoolId}`, `PUT|DELETE /{id}/{schoolId}`
- **MarksRegisterController** (`/api/marks-register`): `GET|POST /`, `GET|PUT|DELETE /{id}`, `GET /search`, `GET /search/subject`, `POST /{schoolId}`, `GET /all/{schoolId}`, `GET|PUT|DELETE /{id}/{schoolId}`, `GET /{schoolId}/search`, `GET /{schoolId}/get/search`
- **MarksheetController** (`/api/marksheet`): `GET /student[/{schoolId}]`, `GET /class-section[/{schoolId}]`, `GET /cumulative/{schoolId}`, `GET /cumulative/student/{schoolId}`
- **CriteriaController** (`/api/schools/criteria`): `GET|POST /{schoolId}`, `GET|PUT|DELETE /{id}/{schoolId}`, `GET /search/{schoolId}`
- **ProjectMarksController** (`/api/schools/{schoolId}/projects`): `GET|POST /`, `GET|PUT|DELETE /{projectId}`

### Service 7 — RoutineMain (`localhost:8084` | Vite Prefix `/service7` | DB: `sas_routineMain`)
- **RoutineController** (`/api/v1/routine`): `GET|POST /`, `GET|PUT|DELETE /{id}`, `GET /search`, `POST /{schoolId}`, `GET /all/{schoolId}`, `GET|PUT|DELETE /{id}/{schoolId}`, `GET /{schoolId}/search`, `GET /{schoolId}/search/download`, `GET /teacher/{schoolId}`, `GET /teacher/{schoolId}/pdf`
- **ClassTimePageController** (`/api/v1/routine/class-time`): `GET|POST /`, `GET|PUT|DELETE /{id}`, `GET /all/{schoolId}`, `POST /{schoolId}`, `GET|PUT|DELETE /{id}/{schoolId}`
- **ExamRoutineController** (`/api/exam-routines`): `GET|POST /`, `PUT|DELETE /{id}`, `GET /search[/download]`, `POST /{schoolId}`, `GET /all/{schoolId}`, `PUT|DELETE /{id}/{schoolId}`, `GET /{schoolId}/search[/download]`
- **ExamTypeController** (`/api/v1/routine/exam-type`): `GET|POST /`, `GET /id/{id}`, `PUT|DELETE /{id}`, `GET|POST /{schoolId}`, `GET /{id}/school/{schoolId}`, `PUT|DELETE /{id}/{schoolId}`
- **NotificationController** (`/api/notifications`): `GET|POST /`, `GET /generate-pdf/{id}[/{schoolId}]`, `DELETE /{id}[/{schoolId}]`, `GET|POST /{schoolId}`

### Service 8 — NewLibrary (`localhost:8082` | Vite Prefix `/service8` | DB: `sas_libraryDatabase`)
- **BookController** (`/api/v1/library`): `GET|POST /books`, `GET /books/id/{id}`, `PUT|DELETE /books/{id}`, `GET|POST /books/{schoolId}`, `GET|PUT|DELETE /books/{id}/{schoolId}`, `GET /books/count[/{schoolId}]`, `GET /books/quantity[/{schoolId}]`
- **CategoryController** (`/api/v1/library`): `GET|POST /categories`, `GET /categories/id/{id}`, `PUT|DELETE /categories/{id}`, `GET|POST /categories/{schoolId}`, `GET|PUT|DELETE /categories/{id}/{schoolId}`
- **BookIssueController** (`/api/v1/library`): `GET|POST /book-issues`, `GET /book-issues/id/{id}`, `PUT|DELETE /book-issues/{id}`, `GET|POST /book-issues/{schoolId}`, `GET|PUT|DELETE /book-issues/{id}/{schoolId}`, `GET /book-issues/pending-count/{studentId}[/{schoolId}]`, `GET /books/issued/count[/{schoolId}]`, `GET /books/issued/pending/count[/{schoolId}]`, `GET /fetch-librarian-summary/{schoolId}`
- **ReturnBookController** (`/api/v1/library`): `GET|POST /return-books`, `GET /return-books/{id}`, `POST /return-books/{schoolId}`, `GET /return-books/school/{schoolId}`, `GET /return-books/{id}/{schoolId}`, `GET /return-books/student/{studentId}/{schoolId}`

### Service 9 — SchoolAccount (`localhost:8099` | Vite Prefix `/service9` | DB: `sas_schoolAccount`)
- **StudentAdmissionFeeController**: `POST /student-admission-fee/create[/{schoolId}]`, `GET /student-admission-fee/all[/{schoolId}]`, `GET|DELETE /student-admission-fee/{id}[/{schoolId}]`, `PUT /student-admission-fee/update-tuition/{id}[/{schoolId}]`, `PUT /student-admission-fee/add-tuition-payment/{id}[/{schoolId}]`, `GET /student-admission-fee/admissionNumber/{admissionNumber}[/{schoolId}]`, `GET /student-admission-fee/history/{admissionNumber}/{schoolId}`, `GET /student-admission-fee/calculate-dues/{admissionNumber}/{schoolId}`, `GET /student-admission-fee/previous-due/{admissionNumber}/{schoolId}`, `GET /student-admission-fee/by-academic-year/{schoolId}/{admissionNumber}`, `PUT /student-admission-fee/update-class/{admissionNumber}/{schoolId}`, `PUT /student-admission-fee/add-service/{admissionNumber}/{schoolId}`, `PUT /student-admission-fee/add-multiple-services/{admissionNumber}/{schoolId}`, `POST /student-admission-fee/promote/{schoolId}`, `POST /students/bulk-promote-fee/schools/{schoolId}`
- **FeesStructureController** (`/api`): `GET|POST /fees-structures`, `GET|PUT /fees-structures/id/{id}`, `DELETE /fees-structures/{id}`, `GET /fees-structures/class/{className}/section/{section}`, `POST /fees-structure/{schoolId}`, `GET /fees-structure/all/{schoolId}`, `GET /fees-structure/{className}/{schoolId}`, `PUT /fees-structure/{id}/{schoolId}`, `DELETE /fees-structure/delete/{id}/{schoolId}`
- **TuitionFeeController** (`/api`): `GET|POST /tuition-fees`, `GET|PUT /tuition-fees/id/{id}`, `DELETE /tuition-fees/{id}`, `GET /tuition-fees/class/{className}`, `GET|POST /tuition-fees/{schoolId}`, `PUT|DELETE /tuition-fees/{id}/{schoolId}`, `GET /tuition-fees/by-class/{className}/{schoolId}`
- **FeeSelectionController** (`/api/feeSelection`): `GET|POST /[/{schoolId}]`
- **ExpenseController** (`/expenses`): `GET|POST /`, `GET|PUT|DELETE /{id}`, `GET /month/{year}/{month}[/{schoolId}]`, `GET /yearly-breakdown/{year}[/{schoolId}]`, `GET /total`, `GET /cash-expenses[/{schoolId}]`, `GET /online-expenses[/{schoolId}]`, `POST /{schoolId}`, `GET /all/{schoolId}`, `GET /id/{id}/{schoolId}`, `PUT /{id}/{schoolId}`, `DELETE /de/{id}/{schoolId}`, `DELETE /{id}/{schoolId}`, `GET /summary/{schoolId}`
- **IncomePageController** (`/income`): `GET|POST /`, `GET|PUT|DELETE /{id}`, `GET /total/{year}/{month}[/{schoolId}]`, `GET /yearly-breakdown/{year}[/{schoolId}]`, `GET /total`, `GET /cash-in-hand[/{schoolId}]`, `GET /online-payments[/{schoolId}]`, `GET /summary[/{schoolId}]`, `GET /edp-summary/{schoolId}`, `POST /{schoolId}`, `GET /all/{schoolId}`, `GET|PUT|DELETE /{id}/{schoolId}`
- **BankTransactionController** (`/bank-transactions`): `GET|POST /`, `GET|PUT|DELETE /{id}`, `GET /month/{year}/{month}[/{schoolId}]`, `POST /{schoolId}`, `GET /all/{schoolId}`, `GET /id/{id}/{schoolId}`, `PUT /{schoolId}/{id}`, `DELETE /delete/{id}/{schoolId}`, `DELETE /{id}/{schoolId}`
- **AccountDetailsController** (`/account`): `POST /create[/{schoolId}]`, `GET /get[/{schoolId}]`, `PUT /update[/{schoolId}]`
- **AuditController** (`/audit`): `POST /logs`, `GET /logs/school/{schoolId}[/type/{entityType}|/daterange]`, `POST /discrepancies`, `GET /discrepancies/school/{schoolId}[/status/{status}|/type/{discrepancyType}]`, `PUT /discrepancies/{discrepancyId}/status`, `GET /reports/school/{schoolId}[/monthly/{year}/{month}|/quarterly/{year}/{quarter}|/yearly/{year}|/custom]`, `GET /statements/{pl|balance-sheet}/school/{schoolId}`, `GET /export/{pdf|excel}/{pl|balance-sheet}/school/{schoolId}`, `GET|POST /schedule/report/{schoolId}`, `DELETE /schedule/report/{scheduleId}`
- **SchoolWalletController** (`/wallet`): `GET /{schoolId}`, `GET /{schoolId}/balance`, `POST /{schoolId}/sub-wallet`, `POST /{schoolId}/payout`, `GET /{schoolId}/transactions`, `GET /{schoolId}/payouts`, `PUT /admin/payout/{payoutRequestId}`
- **PaymentController** (`/api/payments`): `POST /generate-link`, `POST /callback`, `POST /verify`, `POST /webhook`
- **PaymentGatewayController** (`/payment`): `POST /initialize`, `POST /webhook`, `POST /verify`
- **SuperAdminAccountController** (`/super-admin`): `GET /account`, `GET /pending-distributions`, `POST /distribute/{transactionId}`, `POST /distribute-all`, `POST /razorpay/{create-order|verify-signature|webhook}`
