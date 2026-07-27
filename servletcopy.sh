#!/usr/bin/env bash
set -euo pipefail

CWE_DIR="java/ql/src/Security/CWE"
SERVLET_DIR="vulnerable-app/src/main/java/com/example/vulnapp/servlets"

for f in "$SERVLET_DIR"/CWE_*.java; do
  base=$(basename "$f" .java)   # e.g. CWE_020_ExternalAPISinkExample
  rest=${base#CWE_}             # 020_ExternalAPISinkExample
  id=${rest%%_*}               # 020
  name=${rest#*_}              # ExternalAPISinkExample
  dest="$CWE_DIR/CWE-$id/$name.java"

  if [ -f "$dest" ]; then
    cp "$f" "$dest"
    echo "copied  $base  ->  $dest"
  else
    echo "MISSING original for $base (expected $dest)" >&2
  fi
done