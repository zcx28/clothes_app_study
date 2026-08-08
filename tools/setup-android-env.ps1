param(
    [switch] $WhatIf
)

$ErrorActionPreference = 'Stop'

$javaHome = 'D:\Android\Sdk\jbr'
$sdkRoot = 'D:\Android\AndroidSdk'
$androidUserHome = 'D:\Android\AndroidUserHome'
$gradleUserHome = 'D:\Android\GradleUserHome'

$requiredPaths = @(
    $javaHome,
    (Join-Path $javaHome 'bin'),
    $sdkRoot,
    (Join-Path $sdkRoot 'platform-tools'),
    (Join-Path $sdkRoot 'emulator'),
    (Join-Path $sdkRoot 'cmdline-tools\latest\bin'),
    $androidUserHome,
    (Join-Path $androidUserHome 'avd'),
    $gradleUserHome,
    'D:\Android\Sdk\bin'
)

$missingPaths = @($requiredPaths | Where-Object { -not (Test-Path -LiteralPath $_) })
if ($missingPaths.Count -gt 0) {
    throw "Android environment paths are missing:`n$($missingPaths -join "`n")"
}

$userScope = [EnvironmentVariableTarget]::User

function Set-UserEnvironmentVariable {
    param(
        [Parameter(Mandatory)] [string] $Name,
        [Parameter(Mandatory)] [string] $Value
    )

    if ($WhatIf) {
        Write-Output "What if: set user environment variable $Name to $Value"
    } else {
        [Environment]::SetEnvironmentVariable($Name, $Value, $userScope)
    }
}

Set-UserEnvironmentVariable -Name 'JAVA_HOME' -Value $javaHome
Set-UserEnvironmentVariable -Name 'ANDROID_HOME' -Value $sdkRoot
Set-UserEnvironmentVariable -Name 'ANDROID_SDK_ROOT' -Value $sdkRoot
Set-UserEnvironmentVariable -Name 'ANDROID_USER_HOME' -Value $androidUserHome
Set-UserEnvironmentVariable -Name 'ANDROID_AVD_HOME' -Value (Join-Path $androidUserHome 'avd')
Set-UserEnvironmentVariable -Name 'GRADLE_USER_HOME' -Value $gradleUserHome

$existingUserPath = [Environment]::GetEnvironmentVariable('Path', $userScope)
$pathEntries = @()
if ($existingUserPath) {
    $pathEntries = @($existingUserPath -split ';' | Where-Object { $_ -and $_.Trim() })
}

$pathToAdd = @(
    (Join-Path $javaHome 'bin'),
    (Join-Path $sdkRoot 'platform-tools'),
    (Join-Path $sdkRoot 'emulator'),
    (Join-Path $sdkRoot 'cmdline-tools\latest\bin'),
    'D:\Android\Sdk\bin'
)

foreach ($entry in $pathToAdd) {
    if (-not ($pathEntries | Where-Object { $_.TrimEnd('\') -ieq $entry.TrimEnd('\') })) {
        $pathEntries += $entry
    }
}

Set-UserEnvironmentVariable -Name 'Path' -Value ($pathEntries -join ';')

# Also configure this PowerShell process so verification works immediately.
$env:JAVA_HOME = $javaHome
$env:ANDROID_HOME = $sdkRoot
$env:ANDROID_SDK_ROOT = $sdkRoot
$env:ANDROID_USER_HOME = $androidUserHome
$env:ANDROID_AVD_HOME = Join-Path $androidUserHome 'avd'
$env:GRADLE_USER_HOME = $gradleUserHome
$processPathEntries = @($env:Path -split ';' | Where-Object { $_ -and $_.Trim() })
foreach ($entry in $pathToAdd) {
    if (-not ($processPathEntries | Where-Object { $_.TrimEnd('\') -ieq $entry.TrimEnd('\') })) {
        $processPathEntries += $entry
    }
}
$env:Path = $processPathEntries -join ';'

[PSCustomObject]@{
    Scope = 'Current user'
    JAVA_HOME = [Environment]::GetEnvironmentVariable('JAVA_HOME', $userScope)
    ANDROID_HOME = [Environment]::GetEnvironmentVariable('ANDROID_HOME', $userScope)
    ANDROID_AVD_HOME = [Environment]::GetEnvironmentVariable('ANDROID_AVD_HOME', $userScope)
    GRADLE_USER_HOME = [Environment]::GetEnvironmentVariable('GRADLE_USER_HOME', $userScope)
    JavaPath = (Join-Path $javaHome 'bin\java.exe')
    AdbPath = (Join-Path $sdkRoot 'platform-tools\adb.exe')
    EmulatorPath = (Join-Path $sdkRoot 'emulator\emulator.exe')
} | Format-List
