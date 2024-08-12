SRC_DIR = src
BIN_DIR = bin
CMD_DIR = cmd

build_and_run:
	bash $(CMD_DIR)/build.bash

clean:
	rm -rf bin

.PHONY: build_and_run clean