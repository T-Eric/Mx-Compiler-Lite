CLASSFILE = bin/
SRC = src/
ANTLRPACK = /ulib/antlr-4.13.1-complete.jar
MAIN = sema
ASMBUILTIN = builtins/asmbuiltin.s

.PHONY: build
build:
	find $(SRC) -name '*.java' | xargs javac -d $(CLASSFILE) -cp $(ANTLRPACK)
.PHONY: run
run:
	java -cp $(CLASSFILE):$(ANTLRPACK) $(MAIN)
	if [ $$? -eq 0 ]; then \
		cat $(ASMBUILTIN); \
	fi