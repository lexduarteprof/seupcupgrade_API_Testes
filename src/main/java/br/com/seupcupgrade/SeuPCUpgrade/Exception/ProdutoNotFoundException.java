package br.com.seupcupgrade.SeuPCUpgrade.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProdutoNotFoundException extends Exception {

    public ProdutoNotFoundException() {
        super("O Produto não está registrado no sistema!");
    }


}
