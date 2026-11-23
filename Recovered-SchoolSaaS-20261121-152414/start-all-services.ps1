# ==============================================================================
# start-all-services.ps1 - Launch School SaaS Spring Boot Microservices
# ==============================================================================
param(
    [string[]]$Exclude = @()
)

$rootDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$logDir = Join-Path $rootDir "service-logs"
if (-not (Test-Path $logDir)) {
    New-Item -ItemType Directory -Path $logDir | Out-Null
}

$services = @(
    @{ Name = "AcademicMain";   Folder = "AcademicMain-0.0.1-SNAPSHOT";   Jar = "AcademicMain-0.0.1-SNAPSHOT.jar";   Port = 8081; ServiceRoute = "/service5" },
    @{ Name = "NewLibrary";     Folder = "NewLibrary-0.0.1-SNAPSHOT";     Jar = "NewLibrary-0.0.1-SNAPSHOT.jar";     Port = 8082; ServiceRoute = "/service8" },
    @{ Name = "RoutineMain";    Folder = "RoutineMain-0.0.1-SNAPSHOT";    Jar = "RoutineMain-0.0.1-SNAPSHOT.jar";    Port = 8084; ServiceRoute = "/service7" },
    @{ Name = "Admission";      Folder = "Admission-0.0.1-SNAPSHOT";      Jar = "Admission-0.0.1-SNAPSHOT.jar";      Port = 8085; ServiceRoute = "/service1" },
    @{ Name = "Teacher";        Folder = "teacher-0.0.1-SNAPSHOT";        Jar = "teacher-0.0.1-SNAPSHOT.jar";        Port = 8089; ServiceRoute = "/service2" },
    @{ Name = "Settings";       Folder = "Settings-0.0.1-SNAPSHOT";       Jar = "Settings-0.0.1-SNAPSHOT.jar";       Port = 8090; ServiceRoute = "/service3" },
    @{ Name = "Auth-Service";   Folder = "auth-service-0.0.2-SNAPSHOT";   Jar = "auth-service-0.0.2-SNAPSHOT.jar";   Port = 8092; ServiceRoute = "/service4" },
    @{ Name = "Result";         Folder = "Result-0.0.1-SNAPSHOT";         Jar = "Result-0.0.1-SNAPSHOT.jar";         Port = 8094; ServiceRoute = "/service6" },
    @{ Name = "SchoolAccount";  Folder = "SchoolAccount-0.0.2-SNAPSHOT";  Jar = "SchoolAccount-0.0.2-SNAPSHOT.jar";  Port = 8099; ServiceRoute = "/service9" }
)

Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host " Starting School SaaS Spring Boot Microservices..." -ForegroundColor Cyan
if ($Exclude.Count -gt 0) {
    Write-Host " Excluded service(s): $($Exclude -join ', ')" -ForegroundColor Yellow
}
Write-Host " Logs will be written to: $logDir" -ForegroundColor Cyan
Write-Host "=================================================================" -ForegroundColor Cyan

foreach ($s in $services) {
    if ($Exclude -contains $s.Name) {
        Write-Host " [SKIPPED] $($s.Name) (Excluded from startup)" -ForegroundColor DarkGray
        continue
    }

    $jarPath = Join-Path $rootDir "$($s.Folder)\IntelliJ-Project\target\$($s.Jar)"
    $workDir = Join-Path $rootDir "$($s.Folder)\IntelliJ-Project"
    $outFile = Join-Path $logDir "$($s.Name).log"
    $errFile = Join-Path $logDir "$($s.Name).err.log"

    if (-not (Test-Path $jarPath)) {
        Write-Host " [ERROR] JAR not found for $($s.Name): $jarPath" -ForegroundColor Red
        Write-Host " Please run .\build-all.ps1 first." -ForegroundColor Yellow
        continue
    }

    # Proactive Port Cleanup: Ensure port is not occupied before launching
    $conns = Get-NetTCPConnection -LocalPort $s.Port -ErrorAction SilentlyContinue
    if ($conns) {
        $pidsToKill = $conns | Select-Object -ExpandProperty OwningProcess -Unique | Where-Object { $_ -gt 0 }
        foreach ($targetPid in $pidsToKill) {
            Write-Host " [CLEANUP] Port $($s.Port) in use by PID $targetPid. Stopping existing process..." -ForegroundColor Yellow
            Stop-Process -Id $targetPid -Force -ErrorAction SilentlyContinue
        }
        Start-Sleep -Milliseconds 500
    }

    Write-Host " Starting $($s.Name) on port $($s.Port) ($($s.ServiceRoute))..." -ForegroundColor Yellow

    $proc = Start-Process -FilePath "java" `
        -ArgumentList "-Xms128m -Xmx512m -jar `"$jarPath`"" `
        -WorkingDirectory $workDir `
        -RedirectStandardOutput $outFile `
        -RedirectStandardError $errFile `
        -PassThru

    Write-Host "  -> PID: $($proc.Id) | Logs: $outFile" -ForegroundColor Green
}

Write-Host "`nMicroservices process launch sequence completed." -ForegroundColor Green
Write-Host "Run .\stop-all-services.ps1 to stop microservices." -ForegroundColor Yellow
Write-Host "=================================================================" -ForegroundColor Cyan
