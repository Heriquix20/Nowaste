@echo off
call mvn verify

if errorlevel 1 (
    echo Tests or coverage check failed. JaCoCo report may not be updated.
    exit /b 1
)

start "" "target\site\jacoco\index.html"