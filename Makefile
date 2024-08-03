SRC_DIR = src
BUILD_DIR = build

all: server client

server: 
	javac $(SRC_DIR)/Server/*.java -d $(BUILD_DIR)

client: 
	javac $(SRC_DIR)/Client/*.java -d $(BUILD_DIR)

clean: 
	rm -rf $(BUILD_DIR)/Client/* $(BUILD_DIR)/Server/*

run_server: 
	java -cp $(BUILD_DIR) Server.ServerMain

run_client: 
	java -cp $(BUILD_DIR) Client.ClientMain 0.0.0.0