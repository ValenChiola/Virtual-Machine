inicio: mov al, 0b01 ;seteo para leer en decimal
        mov edx, DS ;guardar en el data segment...
        add edx, 4 ;...en la posición 1
        mov ecx, 0x0401 ;1 celda de 4 bytes
        sys 0x1 ;system call para leer
        xor ac, ac ;reseteo el ac (ac = 0)
        mov eax, [edx] ;copio 4 bytes de memoria a registro
otro:   cmp eax, 0 ;comparo con cero
        jz fin ;si es cero terminé
        jnn sigue ;si no es negativo salta
        add ac, 1 ;si es negativo acumula 1
sigue:  shl eax, 1 ;desplazo un bit a la izquierda
        jmp otro ;continua el loop
fin:    add edx, 4 ;incremento para usar otra posición
        mov [edx], ac ;copia a memoria el ac
        mov al, 0b01 ;seteo para escribir decimal
        mov ecx, 0x0401 ;1 celda de 4 byte
        sys 0x2 ;system call para imprimir
        stop ;detiene la ejecución
