; NSIS 설치 스크립트 시작
!define APPNAME "Topwar4j"
!define COMPANY "Hacademy"
!define DESCRIPTION "Topwar4j Installer"
!define VERSION "1.0.0"
!define INSTALL_DIR "$PROGRAMFILES64\${APPNAME}"
!define UNINSTALLER "$INSTDIR\Uninstall.exe"

; 🚀 설치 파일 생성
Outfile "Topwar4j_Setup.exe"

; 📌 관리자 권한 요청 (UAC)
RequestExecutionLevel admin

; 🚀 설치 디렉토리
InstallDir ${INSTALL_DIR}

; 기존 설치 경로 확인 후 삭제
Section "UninstallPrevious"
    IfFileExists "$INSTDIR\topwar4j.exe" 0 +3
    MessageBox MB_OK "기존 버전을 삭제하고 새로 설치합니다."
    RMDir /r "$INSTDIR"
SectionEnd

; 사용자가 직접 설치 경로 선택 가능
ShowInstDetails show
ShowUninstDetails show

Section "Install"
    SetOutPath $INSTDIR

    ; 📌 실행 파일 복사
    File "topwar4j.exe"
    
    ; 📌 아이콘 파일 복사
    File "KID.ico"
    
    ; 📌 이미지 폴더 복사
    SetOutPath "$INSTDIR\images"
    File /r "..\images\*"

    ; 📌 이미지 폴더 복사
    SetOutPath "$INSTDIR\lib"
    File "lib\*"
    
    ; 📌 JRE 폴더 복사 
    setOutPath "$INSTDIR\jre"
    File /r "jre\*"

    ; 📌 바탕화면 및 시작 메뉴에 바로가기 추가
    ; CreateShortcut "$DESKTOP\${APPNAME}.lnk" "$INSTDIR\topwar4j.exe" "" "$INSTDIR\KID.ico"
    ; CreateShortcut "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk" "$INSTDIR\topwar4j.exe" "" "$INSTDIR\KID.ico"
    CreateShortcut "$DESKTOP\${APPNAME}.lnk" "$INSTDIR\topwar4j.exe" "" "$INSTDIR\KID.ico" 0 "" "" "$INSTDIR"
    CreateShortcut "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk" "$INSTDIR\topwar4j.exe" "" "$INSTDIR\KID.ico" 0 "" "" "$INSTDIR"

    ; 📌 레지스트리에 설치 정보 기록
    WriteRegStr HKLM "Software\${APPNAME}" "InstallDir" "$INSTDIR"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "DisplayName" "${APPNAME}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "UninstallString" "$INSTDIR\Uninstall.exe"

    ; 📌 제거 프로그램 생성
    WriteUninstaller "$INSTDIR\Uninstall.exe"
SectionEnd

Section "Uninstall"
    ; 📌 바로가기 삭제
    Delete "$DESKTOP\${APPNAME}.lnk"
    Delete "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk"

    ; 📌 프로그램 파일 삭제
    Delete "$INSTDIR\topwar4j.exe"
    RMDir /r "$INSTDIR\jre"
    RMDir /r "$INSTDIR\images"   
    Delete "$INSTDIR\KID.ico"
    Delete "$INSTDIR\Uninstall.exe"

    ; 📌 설치 폴더 삭제
    RMDir "$INSTDIR"

    ; 📌 레지스트리 항목 삭제
    DeleteRegKey HKLM "Software\${APPNAME}"
    DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}"
SectionEnd
