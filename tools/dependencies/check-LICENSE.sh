mkdir dist || true

tar -zxf linkis-dist/target/apache-linkis*-bin.tar.gz --strip=1 -C dist

# List all modules(jars) that belong to the Linkis itself, these will be ignored when checking the dependency licenses

echo '=== Self modules: ' && ./mvnw --batch-mode --quiet -Dexec.executable='echo' -Dexec.args='${project.artifactId}-${project.version}.jar' exec:exec | tee self-modules.txt

echo '=== Distributed dependencies: ' && find dist/lib -name "*.jar" -exec basename {} \; | uniq | sort | tee all-dependencies.txt

# Exclude all self modules(jars) to generate all third-party dependencies

echo '=== Third party dependencies: ' && grep -vf self-modules.txt all-dependencies.txt | uniq | sort | tee third-party-dependencies.txt

# 1. Compare the third-party dependencies with known dependencies, expect that all third-party dependencies are KNOWN
# and the exit code of the command is 0, otherwise we should add its license to LICENSE file and add the dependency to
# known-dependencies.txt. 2. Unify the `sort` behaviour: here we'll sort them again in case that the behaviour of `sort`
# command in target OS is different from what we used to sort the file `known-dependencies.txt`, i.e. "sort the two file
# using the same command (and default arguments)"

diff -w -B -U0 <(sort < tools/dependencies/known-dependencies.txt) <(sort < third-party-dependencies.txt)
