# Compilador y opciones
JAVAC = javac
JAVA  = java

SRC_DIR  = .
OUT_DIR  = bin

# Clase principal (ajusta esto si tu main es otro)
MAIN_CLASS = prop.enquestes.presentacio.MainPresentacio

# Todos los .java dentro de prop/
SOURCES := $(shell find $(SRC_DIR)/prop -name "*.java")

.PHONY: all run clean

all: $(OUT_DIR)/.compiled

# Compila TODOS los .java a bin/, respetando los paquetes
$(OUT_DIR)/.compiled: $(SOURCES)
	mkdir -p $(OUT_DIR)
	$(JAVAC) -d $(OUT_DIR) $(SOURCES)
	touch $@

# Ejecutar el programa
run: all
	$(JAVA) -cp $(OUT_DIR) $(MAIN_CLASS)

# Limpiar clases compiladas
clean:
	rm -rf $(OUT_DIR)

