; 좌표계 설정
CoordMode, Mouse, Screen
CoordMode, Pixel, Screen

; GUI 설정
Gui, +AlwaysOnTop ;항상 위
Gui, Add, Text, x10 y10 w100 h20, 언어 설정
; Gui, Add, ComboBox, vLanguage g, x130 y10, 한국어|English
Gui, Add, button, x10 y60 w40 h40 gChangeSkillSetting, 시작 ;버튼 추가
Gui, Add, button, x50 y60 w40 h40 gSelectHero, 영웅
Gui, Add, button, x90 y60 w40 h40 gHeroBack, 뒤로
Gui, Add, button, x130 y60 w40 h40 gSelectPlusBtn, +
Gui, Add, button, x170 y60 w40 h40 gSelectSkill, 스킬
Gui, Show, w300 h300, TopWar Macro
return

GetMonitorInformation() {
    ;다중 모니터 대응
    SysGet, MonitorCount, MonitorCount

    ; ToolTip, 모니터개수 : %MonitorCount%
    ; Sleep, 500
    ; ToolTip

    monitorObj := Object()

    if(MonitorCount > 1) ;다중모니터
    {
        SysGet, Monitor, Monitor, 2
        monitorObj["MonitorX1"] := MonitorLeft
        monitorObj["MonitorY1"] := MonitorTop
        monitorObj["MonitorX2"] := MonitorRight
        monitorObj["MonitorY2"] := MonitorBottom
    }
    else ;단일모니터
    {
        monitorObj["MonitorX1"] := 0
        monitorObj["MonitorY1"] := 0
        monitorObj["MonitorX2"] := A_ScreenWidth
        monitorObj["MonitorY2"] := A_ScreenHeight
    }

    return monitorObj
}

FindImage(fullPath)
{
    ; 이미지 경로 확인
    ; ToolTip, 이미지경로 : %fullPath%
    ; Sleep, 3000
    ; ToolTip

    monitorObj := GetMonitorInformation()

    detectRange := "탐지범위 : (" monitorObj["MonitorX1"] "," monitorObj["MonitorY1"] "," monitorObj["MonitorX2"] "," monitorObj["MonitorY2"] ")"
    ToolTip, %detectRange%
    Sleep, 500
    ToolTip

    ; 이미지 검색 실행
    ImageSearch, foundX, foundY, monitorObj["MonitorX1"], monitorObj["MonitorY1"], monitorObj["MonitorX2"], monitorObj["MonitorY2"], %fullPath%
    ToolTip, ImageSearch = %ErrorLevel%
    Sleep, 500
    ToolTip
    
    result := Object()
    result["level"] := ErrorLevel
    result["x"] := foundX
    result["y"] := foundY
    
    return result
}

; 버튼 찾기 함수
FindButton(targetImage)
{
    fullPath := A_ScriptDir
    fullPath .= "\images\btn\"
    fullPath .= targetImage
    fullPath .= ".png"

    result := FindImage(fullPath)
    vx := result["x"]
    vy := result["y"]

    return result
}

; 버튼 클릭 함수
ClickButton(targetImage)
{
    result := FindButton(targetImage)
    vx := result["x"] + 5
    vy := result["y"] + 5
    
    if (result["level"] = 0)
    {
        ; 이미지를 찾았을 때 클릭 이벤트 실행
        MouseClick, left, %vx%, %vy%
    }
    else
    {
        ;MsgBox, 이미지를 찾을 수 없습니다.
    }
}

; 영웅 메뉴 버튼 클릭
ClickHeroMenuButton() 
{
    ClickButton("btn_hero")
}

ClickHero(name) 
{
    fullPath := A_ScriptDir
    fullPath .= "\images\hero\"
    fullPath .= name
    fullPath .= ".png"

    result := FindImage(fullPath)
    vx := result["x"] + 10
    vy := result["y"] + 10
    
    if (result["level"] = 0)
    {
        ; 이미지를 찾았을 때 클릭 이벤트 실행
        MouseClick, left, %vx%, %vy%
    }
    else
    {
        backBtn = FindButton("btn_hero_back")
        backBtnX := backBtn["x"]
        backBtnY := backBtn["y"]

        MouseClick, left, %backBtnX%, %backBtnY%
    }
}

ClickSkill(skillName)
{
    fullPath := A_ScriptDir
    fullPath .= "\images\skill\"
    fullPath .= skillName
    fullPath .= ".png"

    result := FindImage(fullPath)
    vx := result["x"] + 10
    vy := result["y"] + 10
    
    if (result["level"] = 0)
    {
        ; 이미지를 찾았을 때 클릭 이벤트 실행
        MouseClick, left, %vx%, %vy%
    }
    else
    {
        
    }
}

ChangeSkillSetting:
    ClickHeroMenuButton()
    ; ClickButton("btn_hero_back")
return

SelectHero:
    ClickHero("타이윈")
return

HeroBack:
    ClickButton("btn_hero_back")
return

SelectPlusBtn:
    ClickButton("btn_skill_empty")
return

SelectSkill:
    ClickSkill("레어-필드")
return

;종료 설정
GuiClose:
ExitApp