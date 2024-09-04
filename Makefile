SRC_DIR = src
BIN_DIR = bin
LIB_DIR = lib

EXT_LIBS = $(wildcard $(LIB_DIR)/*.jar)
JAVAC = javac -cp $(SRC_DIR):$(EXT_LIBS) -d $(BIN_DIR) -source 1.8 -target 1.8 -Xlint:-options
JAVA = java -cp $(BIN_DIR):$(EXT_LIBS)

all: server client

FW_DB = $(SRC_DIR)/Framework/Database/*.java
FW_SERVER = $(SRC_DIR)/Framework/Server/*.java
FW_NOTIFY = $(SRC_DIR)/Framework/Notify/*.java

server: 
	$(JAVAC) $(SRC_DIR)/Data/*.java $(SRC_DIR)/ServerApp/*.java $(FW_DB) $(FW_SERVER) $(FW_NOTIFY) 

client: 
	$(JAVAC) $(SRC_DIR)/Data/*.java $(SRC_DIR)/ClientApp/*.java $(SRC_DIR)/ClientApp/Commands/*.java $(FW_NOTIFY) 

clean: 
	rm -rf $(BIN_DIR)/ClientApp/* $(BIN_DIR)/ServerApp/* $(BIN_DIR)/Framework/* $(BIN_DIR)/Data/* $(BIN_DIR)/*

run_server: 
	$(JAVA) ServerApp.ServerMain

run_client: 
	$(JAVA) ClientApp.ClientMain


jar: jar_server jar_client

jar_server:
	echo "Manifest-Version: 1.0" > $(BIN_DIR)/server.mf
	echo "Main-Class: ServerApp.ServerMain" >> $(BIN_DIR)/server.mf
	echo "Class-Path: $(EXT_LIBS)" >> $(BIN_DIR)/server.mf
	
	jar -cvfm $(BIN_DIR)/server.jar $(BIN_DIR)/server.mf -C $(BIN_DIR) .
	mv $(BIN_DIR)/server.jar .
	mv $(BIN_DIR)/server.mf .

jar_client:
	echo "Manifest-Version: 1.0" > $(BIN_DIR)/client.mf
	echo "Main-Class: ClientApp.ClientMain" >> $(BIN_DIR)/client.mf
	echo "Class-Path: $(EXT_LIBS)" >> $(BIN_DIR)/client.mf

	jar -cvfm $(BIN_DIR)/client.jar $(BIN_DIR)/client.mf -C $(BIN_DIR) .
	mv $(BIN_DIR)/client.jar .
	mv $(BIN_DIR)/client.mf .


run_jar_server:
	java -jar server.jar

run_jar_client:
	java -jar client.jar