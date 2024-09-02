SRC_DIR = src
BIN_DIR = bin
LIB_DIR = lib
FLAGS = -Xlint

all: server client

FW_DB = $(SRC_DIR)/Framework/Database/*.java
FW_SERVER = $(SRC_DIR)/Framework/Server/*.java
FW_NOTIFY = $(SRC_DIR)/Framework/Notify/*.java

server: 
	javac -cp ${LIB_DIR}/gson.jar $(SRC_DIR)/ServerApp/*.java $(FW_DB) $(FW_SERVER) $(FW_NOTIFY) $(SRC_DIR)/Data/*.java -d $(BIN_DIR)

client: 
	javac -cp ${LIB_DIR}/gson.jar $(SRC_DIR)/ClientApp/*.java $(SRC_DIR)/ClientApp/Commands/*.java $(FW_DB) $(FW_SERVER) $(FW_NOTIFY) $(SRC_DIR)/Data/*.java  -d $(BIN_DIR)

clean: 
	rm -rf $(BIN_DIR)/ClientApp/* $(BIN_DIR)/ServerApp/* $(BIN_DIR)/Framework/* $(BIN_DIR)/Data/*

run_server: 
	java -cp $(BIN_DIR):$(LIB_DIR)/gson.jar ServerApp.ServerMain

run_client: 
	java -cp $(BIN_DIR):$(LIB_DIR)/gson.jar ClientApp.ClientMain

# jar_server:
# 	echo "Manifest-Version: 1.0" > $(BIN_DIR)/server.mf
# 	echo "Main-Class: server.ServerMain" >> $(BIN_DIR)/server.mf
# 	echo "Class-Path: $(EXT_LIBS)" >> $(BIN_DIR)/server.mf

# 	jar -cvfm $(BIN_DIR)/server.jar $(BIN_DIR)/server.mf -C $(BIN_DIR) . $(LIB_DIR)/*

# jar_client:
# 	echo "Manifest-Version: 1.0" > $(BIN_DIR)/client.mf
# 	echo "Main-Class: client.ClientMain" >> $(BIN_DIR)/client.mf
# 	echo "Class-Path: $(EXT_LIBS)" >> $(BIN_DIR)/client.mf

# 	jar -cvfm $(BIN_DIR)/client.jar $(BIN_DIR)/client.mf -C $(BIN_DIR) . $(LIB_DIR)/*
