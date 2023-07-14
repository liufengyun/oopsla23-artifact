#!/bin/bash

CWD=$(pwd)
log_dir="$CWD/logs"

mkdir -p "$log_dir"

for snippet in ./code-snippets/*; do
  filename=${snippet##*/}
  log="$log_dir/${filename%.scala}.log"
  /home/dotty/bin/scalac -Ysafe-init-global -Wconf:"msg=(Cyclic\sinitialization)|(Reading\smutable\sstate\sof)|(Access\suninitialized\sfield):w" $snippet 2>&1 | tee "$log"
done

report="$CWD/report.csv"

echo "Snippet Name,Expected,Actual,Status" >> "$report"

for logfile in $log_dir/*; do
  filename=${logfile##*/}
  testname=${filename%.log}
  if grep -q "Warning" $logfile; then
    # warning found
    if [[ $logfile == *"neg"* ]]; then
      # test passed
      # test name, expected, actual, passed
      echo "$testname,warning,warning,pass" >> "$report"
    else
      # test failed
      echo "$testname,no warning,warning,fail" >> "$report"
    fi
  else
    # no warning
    if [[ $logfile == *"pos"* ]]; then
      # test passed
      echo "$testname,no warning,no warning,pass" >> "$report"
    else
      # test failed
      echo "$testname,warning,no warning,fail" >> "$report"
    fi
  fi
done

csvtool readable "$report"