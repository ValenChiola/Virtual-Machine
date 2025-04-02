inicio: mov edx, ds
        mov [4], 1
        mov cl, 1
        mov ch, 4
        mov al, 1
        sys 1
        mov cx, [edx]
otro:   mul [4], cx
        add cx, -1
        jnz otro
        mov al, 1
        mov cl, 1
        mov ch, 4
        add edx, 4
        sys 2
        stop