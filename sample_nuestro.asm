inicio: mov edx, ds
        mov [4], 1
        mov cx, 5
otro:   mul [4], cx
        add cx, -1
        jnz otro
        mov al, 1
        mov cx, 1025
        add edx, 4
        sys 2
        stop