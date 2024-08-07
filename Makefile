SRC_DIR = src
BIN_DIR = bin
LIB_DIR = lib

all: server client

server: 
	javac -cp ${LIB_DIR}/gson.jar $(SRC_DIR)/Server/*.java $(SRC_DIR)/Server/Endpoints/*.java $(SRC_DIR)/Utils/*.java -d $(BIN_DIR)

client: 
	javac -cp ${LIB_DIR}/gson.jar $(SRC_DIR)/Client/*.java $(SRC_DIR)/Utils/*.java  -d $(BIN_DIR)

clean: 
	rm -rf $(BIN_DIR)/Client/* $(BIN_DIR)/Server/*

run_server: 
	java -cp $(BIN_DIR):$(LIB_DIR)/gson.jar Server.ServerMain

run_client: 
	java -cp $(BIN_DIR):$(LIB_DIR)/gson.jar Client.ClientMain 0.0.0.0