# ==============================================================================
# build-all.ps1 - Compile & Package all 9 School SaaS Microservices
# ==============================================================================

$services = @(
    "AcademicMain-0.0.1-SNAPSHOT",
    "Admission-0.0.1-SNAPSHOT",
    "auth-service-0.0.2-SNAPSHOT",
    "NewLibrary-0.0.1-SNAPSHOT",
    "Result-0.0.1-SNAPSHOT",
    "RoutineMain-0.0.1-SNAPSHOT",
    "SchoolAccount-0.0.2-SNAPSHOT",
    "Settings-0.0.1-SNAPSHOT",
    "teacher-0.0.1-SNAPSHOT"
)

$rootDir = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host " Building all 9 School SaaS Spring Boot Microservices..." -ForegroundColor Cyan
Write-Host "=================================================================" -ForegroundColor Cyan

$successList = @()
$failList = @()

foreach ($s in $services) {
    $projDir = Join-Path $rootDir "$s\IntelliJ-Project"
    Write-Host "`n>>> Building $s in $projDir ..." -ForegroundColor Yellow
    
    Push-Location $projDir
    mvn clean package -DskipTests
    if ($LASTEXITCODE -eq 0) {
        Write-Host " [SUCCESS] $s packaged successfully." -ForegroundColor Green
        $successList += $s
    } else {
        Write-Host " [FAILED] $s build failed!" -ForegroundColor Red
        $failList += $s
    }
    Pop-Location
}

Write-Host "`n=================================================================" -ForegroundColor Cyan
Write-Host " Build Summary:" -ForegroundColor Cyan
Write-Host "   Succeeded: $($successList.Count) / $($services.Count)" -ForegroundColor Green
if ($failList.Count -gt 0) {
    Write-Host "   Failed: $($failList -join ', ')" -ForegroundColor Red
} else {
    Write-Host "   All 9 services built and packaged with complete SUCCESS!" -ForegroundColor Green
}
Write-Host "=================================================================" -ForegroundColor Cyan
