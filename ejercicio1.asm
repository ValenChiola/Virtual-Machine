inicio: mov efx     ,0      ;cantidad
        xor [4]     ,[4]    ;acumulador

otro:   mov edx     ,ds     ;donde quiero empezar a leer
        mov ch      ,4      ;tamano de las celdas (4 bytes)
        mov cl      ,1      ;cuantos datos voy a leer   
        mov al      ,0x01   ;en que formato voy a leer
        sys 1               ;
        cmp [edx]   ,0      ;modifico el CC sin alterar el registro para saber si es negativo
        jn  sigue           ;si el CC indica que el numero es negativo, ejecuto la funcion 'sigue'
        add [4]     ,[edx]  ;
        add [efx]   ,1      ;incremento en uno el valor de efx


sigue:  cmp efx     ,0      ;guardo en CC
        jz  fin             ;si es negativo me voy
        div [4]     ,efx    ;

fin:    mov edx     ,ds     ;hago que edx apunte a la direccion de ds
        add edx     ,4      ;la direccion de edx apunta a ds+4
        mov ch      ,4      ;
        mov cl      ,1      ;
        mov al      ,0x01   ;
        sys 2               ;
        stop                ;
        jmp otro            ;