# ==============================================================================
# start-frontend.ps1 - Launch React + Vite Frontend
# ==============================================================================

$frontendDir = Join-Path (Split-Path -Parent $PSScriptRoot) "Saas_School_Frontend_New"

Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host " Starting School SaaS Frontend (React + Vite)..." -ForegroundColor Cyan
Write-Host " Location: $frontendDir" -ForegroundColor Cyan
Write-Host "=================================================================" -ForegroundColor Cyan

if (-not (Test-Path $frontendDir)) {
    Write-Host " [ERROR] Frontend directory not found at $frontendDir" -ForegroundColor Red
    exit 1
}

Push-Location $frontendDir
Write-Host "Starting Vite development server on http://localhost:5173 ..." -ForegroundColor Green
npm run dev
Pop-Location
