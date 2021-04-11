package br.com.seupcupgrade.SeuPCUpgrade.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDto {

    private Long prdcodigo;

    @NotNull
    @Size(min = 1, max = 200)
    private String prdtitulo;

    @NotNull
    @Size(min = 1, max = 200)
    private String prdfabricante;

    @NotNull
    private Double prdpreco;

    @NotNull
    private Double prdprecoaqusicao;

    @NotNull
    private int prdqtestoque;

    @NotNull
    @Size(min = 1, max = 1000)
    private String prddescricao;

}
