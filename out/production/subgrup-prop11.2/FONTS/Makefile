JAVAC = javac
JAVA = java
SRC_DIR = .
BIN_DIR = ../EXE
TEST_DIR = $(BIN_DIR)/Tests
MAIN_CLASS = FONTS.Main
LIB_DIR = ./lib
JUNIT_DIR = ./JUnit
CLASSPATH = $(LIB_DIR)/junit-4.13.2.jar:$(LIB_DIR)/hamcrest-core-1.3.jar:$(BIN_DIR)

# Archivos fuente principales (excluyendo JUnit)
SOURCES := $(shell find $(SRC_DIR) -name "*.java" ! -path "$(SRC_DIR)/JUnit/*")

# Archivos fuente de los tests
TEST_SOURCES := $(shell find $(JUNIT_DIR) -name "*.java")

# Reglas
all:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) -cp $(SRC_DIR) $(SOURCES)

tests: all
	@mkdir -p $(TEST_DIR)
	$(JAVAC) -d $(TEST_DIR) -cp $(CLASSPATH) $(TEST_SOURCES)
	$(JAVA) -cp $(CLASSPATH):$(TEST_DIR) org.junit.runner.JUnitCore FONTS.JUnit.DistribucionTest
	$(JAVA) -cp $(CLASSPATH):$(TEST_DIR) org.junit.runner.JUnitCore FONTS.JUnit.EstanteriaTest
	$(JAVA) -cp $(CLASSPATH):$(TEST_DIR) org.junit.runner.JUnitCore FONTS.JUnit.InventarioTest
	$(JAVA) -cp $(CLASSPATH):$(TEST_DIR) org.junit.runner.JUnitCore FONTS.JUnit.ProductoTest
	$(JAVA) -cp $(CLASSPATH):$(TEST_DIR) org.junit.runner.JUnitCore FONTS.JUnit.RelacionTest

run: all
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

clean:
	rm -rf $(BIN_DIR)/FONTS

.PHONY: all tests run clean