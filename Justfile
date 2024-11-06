build_gen:
  @if [ ! -e /tmp/generate_subproblem ]; then \
    gcc /src/main/resources/or-library/phub1/generate.c -o /tmp/generate_subproblem; \
    chmod +x /tmp/generate_subproblem; \
  fi

subproblem N H: build_gen
  /tmp/generate_subproblem {{N}} {{H}} < /src/main/resources/or-library/phub1/APdata200 > ./src/main/resources/subproblems/phub1_{{N}}.{{H}}.txt

run:
  @mvn compile exec:java -Dexec.mainClass="uimp.muia.rpm.Run"

debug:
  @mvn compile exec:java -Dexec.mainClass="uimp.muia.rpm.Run" -Dorg.slf4j.simpleLogger.defaultLogLevel=TRACE
