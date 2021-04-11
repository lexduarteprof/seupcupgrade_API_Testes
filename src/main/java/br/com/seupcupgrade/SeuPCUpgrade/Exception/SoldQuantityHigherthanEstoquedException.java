package br.com.seupcupgrade.SeuPCUpgrade.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SoldQuantityHigherthanEstoquedException extends Exception {

    public SoldQuantityHigherthanEstoquedException(Long prdcodigo, int prdquantsold) {
        super(String.format("A quantidade vendida de %s do produto de código %s é superior a quantidade existente no estoque!"
                , prdquantsold, prdcodigo));
    }


}
