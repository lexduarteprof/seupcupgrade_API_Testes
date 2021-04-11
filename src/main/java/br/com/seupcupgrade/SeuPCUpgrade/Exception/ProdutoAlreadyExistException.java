package br.com.seupcupgrade.SeuPCUpgrade.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProdutoAlreadyExistException extends Exception {

    public ProdutoAlreadyExistException(String PrdTituloeFabricante) {
        super(String.format("O Produto com o título e fabricante %s já está registrado no sistema!",
                PrdTituloeFabricante));
    }

    public ProdutoAlreadyExistException(Long Id) {
        super(String.format("O Produto com o Id: %s já está registrado no sistema!",
                Id));
    }

}
