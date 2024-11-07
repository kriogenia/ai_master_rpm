generate := `mktemp`
resources := "src/main/resources"
or-library := resources / "or-library"

# Runs the project with whatever problem is set by default
run:
  @mvn -q compile exec:java -Dexec.mainClass="uimp.muia.rpm.Run"

# Builds the generate.c executable
build_gen:
  @gcc {{or-library}}/generate.c -o {{generate}}
  @chmod +x {{generate}}

# Generates a new subproblem with the given N and P
subproblem N P: build_gen
  @{{generate}} {{N}} {{P}} < {{or-library}}/APdata200.txt > {{resources}}/subproblems/phub_{{N}}.{{P}}.txt

# Runs the project in debug mode
debug $MAVEN_OPTS="-ea":
  @mvn compile exec:java -Dexec.mainClass="uimp.muia.rpm.Run" -Dexec.args="-ea" -Dorg.slf4j.simpleLogger.defaultLogLevel=TRACE
