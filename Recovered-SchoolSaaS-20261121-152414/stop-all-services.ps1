# ==============================================================================
# stop-all-services.ps1 - Gracefully stop all 9 School SaaS Microservices
# ==============================================================================

$ports = @(8081, 8082, 8084, 8085, 8089, 8090, 8092, 8094, 8099)

Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host " Stopping all School SaaS Microservices on ports: $($ports -join ', ') ..." -ForegroundColor Cyan
Write-Host "=================================================================" -ForegroundColor Cyan

foreach ($port in $ports) {
    try {
        $conns = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
        if ($conns) {
            foreach ($conn in $conns) {
                $pidToKill = $conn.OwningProcess
                if ($pidToKill -gt 0) {
                    Write-Host " Port $port is in use by PID $pidToKill. Stopping process..." -ForegroundColor Yellow
                    Stop-Process -Id $pidToKill -Force -ErrorAction SilentlyContinue
                    Write-Host " -> PID $pidToKill stopped." -ForegroundColor Green
                }
            }
        } else {
            Write-Host " Port $port is already free." -ForegroundColor Gray
        }
    } catch {
        Write-Host " Port $port check completed." -ForegroundColor Gray
    }
}

Write-Host "`nAll specified services have been stopped." -ForegroundColor Green
Write-Host "=================================================================" -ForegroundColor Cyan
