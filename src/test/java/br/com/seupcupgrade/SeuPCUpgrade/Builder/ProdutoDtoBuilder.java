package br.com.seupcupgrade.SeuPCUpgrade.Builder;

import br.com.seupcupgrade.SeuPCUpgrade.Dto.ProdutoDto;
import com.sun.istack.NotNull;
import lombok.Builder;

import javax.validation.constraints.Size;

@Builder
public class ProdutoDtoBuilder {

    @Builder.Default
    private Long prdcodigo = 438L;

    @Builder.Default
    @Size(min=1, max=200)
    private String prdtitulo = "Placa de Vídeo NVIDIA gtx 1050";

    @Builder.Default
    @Size(min=1, max=200)
    private String prdfabricante = "NVIDIA";

    @Builder.Default
    private Double prdpreco = 1000.0;

    @Builder.Default
    private Double prdprecoaqusicao = 500.0;

    @Builder.Default
    private int prdqtestoque = 10;

    @Builder.Default
    @Size(min=1, max=1000)
    private String prddescricao = "Placa de Vídeo Asus NVIDIA " +
            "GeForce GTX 1050 Ti OC Cerberus 4GB, GDDR5 - CERBERUS-GTX1050TI-O4G";

    public ProdutoDto toDto(){

        return new ProdutoDto(prdcodigo,prdtitulo,prdfabricante,
                prdpreco,prdprecoaqusicao,prdqtestoque,prddescricao);
    }

}
