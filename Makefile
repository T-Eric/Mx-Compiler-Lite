.PHONY: build
build:
	find src -name '*.java' | xargs javac -d bin -cp /ulib/antlr-4.13.1-complete.jar
.PHONY: run
run:
	java -cp /ulib/antlr-4.13.1-complete.jar:bin sema