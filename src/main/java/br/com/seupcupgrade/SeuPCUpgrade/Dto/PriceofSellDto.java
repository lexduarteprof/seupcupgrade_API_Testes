package br.com.seupcupgrade.SeuPCUpgrade.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceofSellDto {

    @NotNull
    private double priceOfSell;

}
