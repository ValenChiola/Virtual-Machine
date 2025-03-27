inicio: xor efx     ,efx
        xor ebx     ,ebx
        xor ecx     ,ecx
        xor eax     ,eax
        mov [4]     ,0

leer:   mov efx     ,ds
        mov ch      ,4
        mov cl      ,2
        mov al      ,0x01
        sys 1
        mov ebx      ,ds
        mov ecx      ,[4]
        jmp mul

mul:    cmp [4]     ,0
        jz  final
        add eax     ,ds
        sub [4]     ,1
        jmp mul

final: STOP