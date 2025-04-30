#!/bin/bash

SRC_DIR="src"
OUT_DIR="bin"
MAIN_CLASS="Main"
JAR_NAME="vm.jar"

echo "🧹 Limpiando compilación anterior..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"
rm -f "$JAR_NAME"

echo "🔨 Compilando archivos .java..."
find "$SRC_DIR" -name "*.java" > sources.txt
javac -d "$OUT_DIR" @sources.txt

echo "📦 Generando $JAR_NAME..."
jar cfe "$JAR_NAME" "$MAIN_CLASS" -C "$OUT_DIR" .

echo "🚀 Ejecutando $JAR_NAME..."
java -jar "$JAR_NAME" "$@"

rm sources.txt > /dev/null 2>&1