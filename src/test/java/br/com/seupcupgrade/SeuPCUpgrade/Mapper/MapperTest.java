package br.com.seupcupgrade.SeuPCUpgrade.Mapper;

import br.com.seupcupgrade.SeuPCUpgrade.Dto.ProdutoDto;
import br.com.seupcupgrade.SeuPCUpgrade.Entity.Produto;
import br.com.seupcupgrade.SeuPCUpgrade.Mapper.ProdutoMapper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class MapperTest {

    private ProdutoMapper produtoMapper = ProdutoMapper.INSTANCE;

    @Test
    public void TestMapper(){
        Produto produtoTest = new Produto();

        produtoTest.setPrdcodigo(1L);
        produtoTest.setPrdqtestoque(2);
        produtoTest.setPrddescricao("Desc produto");
        produtoTest.setPrdfabricante("Fabricante produto");
        produtoTest.setPrdpreco(2.);
        produtoTest.setPrdprecoaqusicao(1.);
        produtoTest.setPrdtitulo("titulo produto");

        ProdutoDto produtoDto;

        produtoDto = produtoMapper.toDTO(produtoTest);

        Produto produtofromDto = produtoMapper.toModelProduto(produtoDto);

        MatcherAssert.assertThat(produtoDto.getPrdtitulo(),
                Matchers.is(Matchers.equalTo(produtoTest.getPrdtitulo())));

        MatcherAssert.assertThat(produtofromDto.getPrdqtestoque(),
                Matchers.is(Matchers.equalTo(produtoDto.getPrdqtestoque())));


    }

}
