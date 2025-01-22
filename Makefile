
# Variables
SRC_DIR = src
BIN_DIR = bin
JAVAC = javac
MAIN_CLASS = Main
JAVA_FILES = $(wildcard $(SRC_DIR)/*.java)
CLASS_FILES = $(patsubst $(SRC_DIR)/%.java,$(BIN_DIR)/%.class,$(JAVA_FILES))
JAVA = java

# Default target
all: compile

# Rule to compile all Java files at once
$(BIN_DIR):
	@mkdir -p $(BIN_DIR)

compile: $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(JAVA_FILES)

# Run the application
run: all
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

# Clean target to remove compiled files
clean:
	rm -rf $(BIN_DIR)/*.class

.PHONY: all clean compile
