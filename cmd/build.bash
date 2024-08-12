set ff=UNIX
mkdir -p bin
find src -name *.java | javac -d bin -cp /ulib/java/antlr-4.13.1-complete.jar @/dev/stdin
# echo "Built successfully."
source cmd/sema.bash