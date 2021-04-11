package br.com.seupcupgrade.SeuPCUpgrade.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PriceofSellIsLessThanThePermitedByMinimalProfitNecessary extends Exception {

    public PriceofSellIsLessThanThePermitedByMinimalProfitNecessary(long prdcodigo, double priceOfSell) {
        super(String.format("O valor de venda %s do produto de código %s é menor do que o permitido!"
                , priceOfSell, prdcodigo));
    }

}
