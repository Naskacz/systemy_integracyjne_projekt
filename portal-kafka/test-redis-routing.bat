@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

set "BASE_URL=http://localhost:8080"
set "SLEEP_TIME=8000"
set "POLLS=15"

echo === [KROK 2] Wysylanie zadania (sleepTime=%SLEEP_TIME% ms)... ===
set "TASK_ID="
for /f "delims=" %%i in ('curl -s -X POST "%BASE_URL%/api/task?sleepTime=%SLEEP_TIME%"') do (
    set "TASK_ID=%%i"
)

if "!TASK_ID!"=="" (
    echo [BLAD] Nie udalo sie pobrac TASK_ID. Sprawdz czy dziala port-forward na porcie 8080 oraz backend.
    exit /b 1
)

echo Utworzono TASK_ID: !TASK_ID!
echo.

echo === [KROK 3] Odpytywanie statusu co 1s (%POLLS% prob)... ===
for /L %%n in (1,1,%POLLS%) do (
    set "STATUS="
    for /f "delims=" %%s in ('curl -s "%BASE_URL%/api/task/!TASK_ID!/status"') do (
        set "STATUS=%%s"
    )
    echo Proba %%n: !STATUS!
    if "!STATUS!"=="ZAKOŃCZONE" goto status_complete
    if %%n LSS %POLLS% ping 127.0.0.1 -n 2 -w 1000 >nul
)
:status_complete
echo.

echo === [KROK 4a] Sprawdzenie klucza w Redisie... ===
kubectl get deployment redis >nul 2>&1
if errorlevel 1 (
    echo [BLAD] Nie znaleziono poda Redis.
    exit /b 1
)
kubectl exec deployment/redis -- redis-cli GET "task:status:!TASK_ID!"
echo.

echo === [KROK 4b] Ostatnie logi podow (app=portal-kafka)... ===
kubectl logs -l app=portal-kafka --tail=30

endlocal