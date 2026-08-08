$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$mavenBin = Join-Path $scriptDir "..\tools\apache-maven-3.9.6\bin\mvn.cmd"

if (Test-Path $mavenBin) {
    & $mavenBin $args
} else {
    mvn $args
}
