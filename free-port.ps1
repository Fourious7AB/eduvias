<#
.SYNOPSIS
    Identifies and terminates any process occupying the specified port (defaults to 8099 for SchoolAccount).
.EXAMPLE
    .\free-port.ps1
    .\free-port.ps1 -Port 8099
#>
param(
    [Parameter(Position = 0)]
    [int]$Port = 8099
)

Write-Host "Checking port $Port..." -ForegroundColor Cyan
$conns = Get-NetTCPConnection -LocalPort $Port -ErrorAction SilentlyContinue

if ($conns) {
    $pidsToKill = $conns | Select-Object -ExpandProperty OwningProcess -Unique | Where-Object { $_ -gt 0 }
    foreach ($targetPid in $pidsToKill) {
        $proc = Get-Process -Id $targetPid -ErrorAction SilentlyContinue
        $pName = if ($proc) { $proc.ProcessName } else { "Unknown" }
        Write-Host "Port $Port is in use by PID $targetPid ($pName). Stopping process..." -ForegroundColor Yellow
        try {
            Stop-Process -Id $targetPid -Force -ErrorAction Stop
            Write-Host "-> Successfully terminated PID $targetPid ($pName)." -ForegroundColor Green
        } catch {
            Write-Host "-> Failed to terminate PID ${targetPid}: $($_.Exception.Message)" -ForegroundColor Red
        }
    }
    Start-Sleep -Milliseconds 500
} else {
    Write-Host "Port $Port is completely free and ready for use." -ForegroundColor Green
}
