$ErrorActionPreference = 'Stop'
$Root = $PSScriptRoot
$OutDir = Join-Path $Root 'release-jars'
New-Item -ItemType Directory -Force -Path $OutDir | Out-Null

function Resolve-Jdk([string]$EnvVar, [string[]]$Patterns) {
    $val = [Environment]::GetEnvironmentVariable($EnvVar)
    if ($val -and (Test-Path (Join-Path $val 'bin\java.exe'))) { return $val }
    foreach ($pat in $Patterns) {
        $match = Get-ChildItem -Path $pat -Directory -ErrorAction SilentlyContinue | Sort-Object Name -Descending | Select-Object -First 1
        if ($match -and (Test-Path (Join-Path $match.FullName 'bin\java.exe'))) { return $match.FullName }
    }
    return $null
}

if (-not $env:JDK8) {
    $jdk8 = Resolve-Jdk 'JDK8' @('C:\Program Files\Eclipse Adoptium\jdk-8*', 'C:\Program Files\Java\jdk1.8*')
    if ($jdk8) { $env:JDK8 = $jdk8 }
}

function Invoke-Build([string]$Label, [string]$RelDir, [string]$JavaVar, [string]$JdkName) {
    $patterns = switch ($JavaVar) {
        'JAVA17_HOME' { @('C:\Program Files\Eclipse Adoptium\jdk-17*', 'C:\Program Files\Java\jdk-17*') }
        'JAVA21_HOME' { @('C:\Program Files\Eclipse Adoptium\jdk-21*', 'C:\Program Files\Java\jdk-21*') }
        'JAVA25_HOME' { @('C:\Program Files\Eclipse Adoptium\jdk-25*', 'C:\Program Files\Java\jdk-25*') }
    }
    $jdk = Resolve-Jdk $JavaVar $patterns
    if (-not $jdk) { throw "Missing $JdkName. Set $JavaVar or install Temurin $JdkName." }
    Write-Host "`n=== Building $Label ($RelDir) with $jdk ===" -ForegroundColor Cyan
    Push-Location (Join-Path $Root $RelDir)
    try {
        $env:JAVA_HOME = $jdk
        $env:PATH = "$jdk\bin;$env:PATH"
        & .\gradlew.bat build --stacktrace
        if ($LASTEXITCODE -ne 0) { throw "Build failed for $Label" }
        $jar = Get-ChildItem -Path 'build\libs\*.jar' | Where-Object { $_.Name -notmatch 'sources|dev|javadoc' } | Sort-Object LastWriteTime -Descending | Select-Object -First 1
        if ($jar) {
            Copy-Item $jar.FullName -Destination $OutDir -Force
            Write-Host "[OK] Copied $($jar.Name) -> release-jars\" -ForegroundColor Green
        }
    } finally {
        Pop-Location
    }
}

Invoke-Build 'Forge 1.20.1' '1.20.1\forge' 'JAVA17_HOME' 'JDK 17'
Invoke-Build 'Fabric 1.20.1' '1.20.1\fabric' 'JAVA17_HOME' 'JDK 17'
Invoke-Build 'Forge 1.21.1' '1.21.1\forge' 'JAVA21_HOME' 'JDK 21'
Invoke-Build 'NeoForge 1.21.1' '1.21.1\neoforge' 'JAVA21_HOME' 'JDK 21'
Invoke-Build 'Fabric 1.21.1' '1.21.1\fabric' 'JAVA21_HOME' 'JDK 21'
Invoke-Build 'Forge 1.21.11' '1.21.11\forge' 'JAVA21_HOME' 'JDK 21'
Invoke-Build 'NeoForge 1.21.11' '1.21.11\neoforge' 'JAVA21_HOME' 'JDK 21'
Invoke-Build 'Fabric 1.21.11' '1.21.11\fabric' 'JAVA21_HOME' 'JDK 21'
Invoke-Build 'Forge 26.1.2' '26.1\forge' 'JAVA25_HOME' 'JDK 25'
Invoke-Build 'NeoForge 26.1.2' '26.1\neoforge' 'JAVA25_HOME' 'JDK 25'
Invoke-Build 'Fabric 26.1.2' '26.1\fabric' 'JAVA25_HOME' 'JDK 25'
Invoke-Build 'Forge 26.2' '26.2\forge' 'JAVA25_HOME' 'JDK 25'
Invoke-Build 'NeoForge 26.2' '26.2\neoforge' 'JAVA25_HOME' 'JDK 25'
Invoke-Build 'Fabric 26.2' '26.2\fabric' 'JAVA25_HOME' 'JDK 25'
Invoke-Build 'Forge 26.3' '26.3\forge' 'JAVA25_HOME' 'JDK 25'
Invoke-Build 'NeoForge 26.3' '26.3\neoforge' 'JAVA25_HOME' 'JDK 25'
Invoke-Build 'Fabric 26.3' '26.3\fabric' 'JAVA25_HOME' 'JDK 25'

Write-Host "`nAll 17 Camera Lock-On builds completed! Output: $OutDir" -ForegroundColor Green
