set ff=UNIX
mkdir -p bin
find src -name *.java | javac -d bin -cp /ulib/antlr-4.13.1-complete.jar @/dev/stdin
# find src -name *.java | javac -d bin -cp antlr/antlr-4.13.1-complete.jar @/dev/stdin
# echo "Built successfully."
# source cmd/sema.bash