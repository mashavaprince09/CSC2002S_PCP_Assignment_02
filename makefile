# Makefile for compiling and running serial program

# Directories
SRC_DIR = src/medleySimulation
BIN_DIR = bin/medleySimulation

# Source files
JAVA_FILES = $(wildcard $(SRC_DIR)/*.java)

# Compiled class files
CLASS_FILES = $(patsubst $(SRC_DIR)/%.java, $(BIN_DIR)/%.class, $(JAVA_FILES))

# Compilation flags
JAVAC_FLAGS = -d bin -sourcepath src

# Main class
MAIN_CLASS = medleySimulation.MedleySimulation


# Targets
.PHONY: all clean run directories

all: directories $(CLASS_FILES)

directories:
	@mkdir -p $(BIN_DIR)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	javac $(JAVAC_FLAGS) $<

clean:
	rm -rf bin/* src/medleySimulation/*.class  C:/Users/masha/AppData/Roaming/Code/User/workspaceStorage/ab8cc4193f0bb2978c4f571108e8507c/redhat.java/jdt_ws/PCP2SkeletonCode_891ba943/bin/MedleySimulation/*

run: all
	java -classpath bin $(MAIN_CLASS) $(ARGS)
