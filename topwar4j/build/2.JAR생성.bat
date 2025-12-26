@echo off
:: 1. 상위 폴더(프로젝트 루트)로 이동
cd /d "%~dp0.."

echo [INFO] Maven Build를 시작합니다...

:: 2. 메이븐 빌드 실행 (clean 후 package)
:: 만약 mvn 명령어가 안 먹히면 mvn.cmd라고 입력해 보세요.
call mvn clean package -DskipTests

echo [INFO] 빌드가 완료되었습니다.
pause