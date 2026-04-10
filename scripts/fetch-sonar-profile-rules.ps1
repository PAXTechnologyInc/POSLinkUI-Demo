#Requires -Version 5.1
# Sonar profile rules export - see docs/sonarqube-rules-export/README.md and scripts/fetch_sonar_profile_rules.py
param(
    [string]$SonarHost = "http://172.16.3.60:9090",
    [string]$ProjectKey = "POSLinkUI-Demo",
    [string]$SonarToken = $env:SONAR_TOKEN
)

$ErrorActionPreference = "Stop"

if ([string]::IsNullOrWhiteSpace($SonarToken)) {
    Write-Error "Missing SONAR_TOKEN: set the environment variable or pass -SonarToken (SonarQube user token with analysis/read permissions)."
    exit 1
}

$base = $SonarHost.TrimEnd("/")
$pair = "${SonarToken}:"
$b64 = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($pair))
$headers = @{ Authorization = "Basic $b64" }

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$outDir = Join-Path $repoRoot "docs\sonarqube-rules-export"
New-Item -ItemType Directory -Force -Path $outDir | Out-Null

function Invoke-SonarGet {
    param([string]$RelativeUrl)
    $uri = "$base$RelativeUrl"
    return Invoke-RestMethod -Uri $uri -Headers $headers -Method Get
}

function Get-ActivatedRulesForProfile {
    param([string]$ProfileKey)
    $page = 1
    $ps = 500
    $all = New-Object System.Collections.Generic.List[object]
    do {
        $enc = [uri]::EscapeDataString($ProfileKey)
        # Use -f so '&' is not parsed as PowerShell statement separator after $enc.
        $url = '/api/rules/search?activation=true&qprofile={0}&ps={1}&p={2}' -f $enc, $ps, $page
        $resp = Invoke-SonarGet -RelativeUrl $url
        foreach ($r in $resp.rules) { $all.Add($r) }
        $total = [int]$resp.total
        if ($all.Count -ge $total) { break }
        $page++
    } while ($true)
    return $all
}

function Write-RulesMarkdown {
    param(
        [string]$Path,
        [string]$Title,
        [object[]]$Rules
    )
    $sb = New-Object System.Text.StringBuilder
    [void]$sb.AppendLine(('# {0}' -f $Title))
    [void]$sb.AppendLine("")
    [void]$sb.AppendLine('| Rule Key | Name | Severity | Type |')
    [void]$sb.AppendLine('|----------|------|----------|------|')
    foreach ($r in ($Rules | Sort-Object key)) {
        $name = ($r.name -replace '\|', '/' )
        # Single-quoted format avoids '|' inside double quotes being parsed as pipeline.
        [void]$sb.AppendLine(('| {0} | {1} | {2} | {3} |' -f $r.key, $name, $r.severity, $r.type))
    }
    [void]$sb.AppendLine("")
    [void]$sb.AppendLine(('**Count:** {0}' -f $Rules.Count))
    Set-Content -Path $Path -Value $sb.ToString() -Encoding UTF8
}

function Write-RulesTsv {
    param(
        [string]$Path,
        [object[]]$Rules
    )
    $tab = [char]9
    $nl = [char]10
    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add(("key{0}name{0}severity{0}type{0}lang" -f $tab))
    foreach ($r in ($Rules | Sort-Object key)) {
        $name = $r.name -replace $tab, ' ' -replace "`r`n", ' ' -replace "`n", ' ' -replace "`r", ' '
        $lines.Add(('{0}{5}{1}{5}{2}{5}{3}{5}{4}' -f $r.key, $name, $r.severity, $r.type, $r.lang, $tab))
    }
    Set-Content -Path $Path -Value ($lines -join $nl) -Encoding UTF8
}

Write-Host "Fetching quality profiles for project=$ProjectKey ..."
$qpResp = Invoke-SonarGet -RelativeUrl "/api/qualityprofiles/search?project=$([uri]::EscapeDataString($ProjectKey))"

if (-not $qpResp.profiles) {
    $detail = $qpResp | ConvertTo-Json -Depth 3 -Compress
    Write-Error "API returned no quality profiles. Check project key, token, and Sonar version. Response: $detail"
    exit 1
}

$targets = @("kotlin", "java")
$meta = @{
    exportedAtUtc = (Get-Date).ToUniversalTime().ToString("o")
    sonarHost     = $base
    projectKey    = $ProjectKey
    languages     = @{}
}

foreach ($lang in $targets) {
    $candidates = @($qpResp.profiles | Where-Object { $_.language -eq $lang })
    if ($candidates.Count -eq 0) {
        Write-Warning "No quality profile for language '$lang'; skipping."
        continue
    }
    # Prefer default profile; else first candidate (usually unique per project).
    $profile = @($candidates | Where-Object { $_.isDefault -eq $true }) | Select-Object -First 1
    if (-not $profile) { $profile = $candidates[0] }

    Write-Host "Language=$lang profile key=$($profile.key) name=$($profile.name) ..."
    $rules = @(Get-ActivatedRulesForProfile -ProfileKey $profile.key)
    Write-Host "  -> $($rules.Count) activated rules"

    $meta.languages[$lang] = @{
        profileKey  = $profile.key
        profileName = $profile.name
        ruleCount   = $rules.Count
    }

    $mdPath = Join-Path $outDir "rules-$lang.md"
    $tsvPath = Join-Path $outDir "rules-$lang.tsv"
    Write-RulesMarkdown -Path $mdPath -Title "Sonar activated rules: $lang ($($profile.name))" -Rules $rules
    Write-RulesTsv -Path $tsvPath -Rules $rules
}

$metaPath = Join-Path $outDir "EXPORT_META.json"
$meta | ConvertTo-Json -Depth 6 | Set-Content -Path $metaPath -Encoding UTF8

Write-Host "Done. Output: $outDir"
