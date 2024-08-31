.PHONY: build
build:
	find ./ -name '*.java' | xargs javac -d bin -cp lib/antlr-4.13.1-complete.jar
.PHONY: run
run:
	java -cp lib/antlr-4.13.1-complete.jar:bin sema | cat builtin.s