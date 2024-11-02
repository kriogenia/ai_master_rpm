build_gen:
  @if [ ! -e /tmp/generate_subproblem ]; then \
    sed -n "494,647p" ./src/main/resources/or-library/phub1.txt > /tmp/generate.c; \
    gcc /tmp/generate.c -o /tmp/generate_subproblem; \
    chmod +x /tmp/generate_subproblem; \
  fi

subproblem N: build_gen
  @if [ ! -e /tmp/APdata200 ]; then \
    sed -n "65,469p" ./src/main/resources/or-library/phub1.txt > /tmp/APdata200; \
  fi
  /tmp/generate_subproblem {{N}} 3 < /tmp/APdata200 > ./src/main/resources/subproblems/phub1_{{N}}.3.txt
