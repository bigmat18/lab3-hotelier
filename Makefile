SRC_DIR = src
BIN_DIR = bin
LIB_DIR = lib
FLAGS = -Xlint

all: server client

FW_DB = $(SRC_DIR)/Framework/Database/*.java
FW_SERVER = $(SRC_DIR)/Framework/Server/*.java
FW_NOTIFY = $(SRC_DIR)/Framework/Notify/*.java

server: 
	javac -cp ${LIB_DIR}/gson.jar $(SRC_DIR)/Server/*.java $(FW_DB) $(FW_SERVER) $(FW_NOTIFY) $(SRC_DIR)/Data/*.java -d $(BIN_DIR)

client: 
	javac -cp ${LIB_DIR}/gson.jar $(SRC_DIR)/Client/*.java $(FW_DB) $(FW_SERVER) $(FW_NOTIFY) $(SRC_DIR)/Data/*.java  -d $(BIN_DIR)

clean: 
	rm -rf $(BIN_DIR)/Client/* $(BIN_DIR)/Server/* $(BIN_DIR)/Framework/* $(BIN_DIR)/Data/*

run_server: 
	java -cp $(BIN_DIR):$(LIB_DIR)/gson.jar Server.ServerMain

run_client: 
	java -cp $(BIN_DIR):$(LIB_DIR)/gson.jar Client.ClientMain 0.0.0.0