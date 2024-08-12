SRC_DIR = src
BIN_DIR = bin
CMD_DIR = cmd

build:
	bash $(CMD_DIR)/build.bash

run:
	bash $(CMD_DIR)/sema.bash

clean:
	rm -rf bin

.PHONY: build run clean