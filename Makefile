# ===========================
# Makefile per al projecte PROP
# ===========================

SRC_DIR = src
BIN_DIR = bin
PRUEBAS_DIR = Pruebas

MAIN = Main
MAIN_PKG = prop.enquestes.presentacio.Main

JAVAC = javac
JAVA = java

JUNIT = lib/junit-4.13.2.jar
HAMCREST = lib/hamcrest-core-1.3.jar


# ========= Regla per defecte =========
default: build


# ========= Compilar fonts =========
build:
	mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SRC_DIR)/prop/enquestes/**/*.java


# ========= Executar programa =========
run:
	$(JAVA) -cp $(BIN_DIR) $(MAIN_PKG)


# ========= Executar joc de prova =========
# Exemple:
#   make testfile FILE=prova1
#	el output de sortida mostrara tot el que s'escriu per pantalla com el menu per tant hi haura
#	molta informació, en cadascun de les proves tenen un export per veure la informacio reduida
testfile:
	$(JAVA) -cp $(BIN_DIR) $(MAIN_PKG) < $(PRUEBAS_DIR)/$(FILE).txt > $(PRUEBAS_DIR)/salida_$(FILE).txt


# ========= Compilar tests JUnit (només si existeixen) =========
test-build:
	if [ -d test ]; then \
		mkdir -p $(BIN_DIR); \
		$(JAVAC) -cp "$(BIN_DIR):$(JUNIT):$(HAMCREST)" -d $(BIN_DIR) $$(find test -name "*.java"); \
	else \
		echo "No existeix carpeta test/. No hi ha tests per compilar."; \
	fi


# ========= Executar Tests =========
test:
	$(JAVA) -cp "$(BIN_DIR):$(JUNIT):$(HAMCREST)" org.junit.runner.JUnitCore AllTests


# ========= Netejar =========
clean:
	rm -rf $(BIN_DIR)