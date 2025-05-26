# Máquina Virtual 1°parte

Este proyecto consiste en la implementación de una máquina virtual desarrollada en Java, como parte del trabajo práctico para la materia **Arquitectura de Computadoras**.

## 📦 Contenido

El archivo comprimido `.zip` entregado contiene:
- Todo el código fuente del proyecto.
- El archivo ejecutable `vm.jar`.
- Un script de ayuda `vm.sh` para compilar y ejecutar la máquina virtual en entornos tipo Unix (Linux/macOS).

## 🖥️ Sistema Operativo

El proyecto es **multiplataforma**. Puede ejecutarse en cualquier sistema operativo (Windows, Linux, macOS) que tenga instalado un entorno capaz de ejecutar archivos `.jar`.

## ✅ Requerimientos Previos

- Tener instalada una versión de **Java** (JDK o JRE).  
  Cualquier versión moderna de Java debería ser suficiente para ejecutar el programa.

> ⚠️ Asegurarse de tener configurado `java` en el PATH del sistema para poder invocar el comando desde la terminal.

## 🚀 Ejecución

Para ejecutar la máquina virtual desde la terminal, usar el siguiente comando:

```bash
java -jar vm.jar archivo.vmx archivo.vmi [-d] [m=<memoria>] [-p <arg1> <arg2> ...]
```

Si se desea compilar nuevamente utilice **vm.sh**:

```bash
./vm.sh archivo.vmx archivo.vmi [-d] [m=<memoria>] [-p <arg1> <arg2> ...]
```

## 🐙 Github
[Virtual Machine - Lucas Cardozo y Valentino Chiola](https://github.com/ValenChiola/Virtual-Machine)
