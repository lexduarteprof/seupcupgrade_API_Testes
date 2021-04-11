package br.com.seupcupgrade.SeuPCUpgrade.Service;

import br.com.seupcupgrade.SeuPCUpgrade.Builder.ProdutoDtoBuilder;
import br.com.seupcupgrade.SeuPCUpgrade.Dto.PriceofSellDto;
import br.com.seupcupgrade.SeuPCUpgrade.Dto.ProdutoDto;
import br.com.seupcupgrade.SeuPCUpgrade.Entity.Produto;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.PriceofSellIsLessThanThePermitedByMinimalProfitNecessary;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.ProdutoAlreadyExistException;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.ProdutoNotFoundException;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.SoldQuantityHigherthanEstoquedException;
import br.com.seupcupgrade.SeuPCUpgrade.Mapper.ProdutoMapper;
import br.com.seupcupgrade.SeuPCUpgrade.Repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {


    ProdutoMapper produtoMapper = ProdutoMapper.INSTANCE;

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void whenProdutoInformedThenItShouldBeCreated() throws ProdutoAlreadyExistException {

        //given
        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto expectedsavedProduto = produtoMapper.toModelProduto(produtoDto);

        //when
        when(produtoRepository.findByprdtituloandfabricante(produtoDto.getPrdtitulo(),
                produtoDto.getPrdfabricante())).thenReturn(Optional.empty());

        when(produtoRepository.save(expectedsavedProduto)).thenReturn(expectedsavedProduto);

        //then
        ProdutoDto createdProdutoDto = produtoService.CriarProduto(produtoDto);

        //assertEquals(produtoDto.getPrdcodigo(), createdProdutoDto.getPrdcodigo());
        //assertEquals(produtoDto.getPrdtitulo(), createdProdutoDto.getPrdtitulo());

        assertThat(createdProdutoDto.getPrdcodigo(),
                is(equalTo(produtoDto.getPrdcodigo())));

        assertThat(createdProdutoDto.getPrdtitulo(),
                is(equalTo(produtoDto.getPrdtitulo())));

    }

    @Test
    void whenAlreadyRegistredProdutoInformedThenExcepition() throws ProdutoAlreadyExistException {

        //given
        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto exitentProduto = produtoMapper.toModelProduto(produtoDto);

        //when
        when(produtoRepository.findByprdtituloandfabricante(produtoDto.getPrdtitulo(),
                produtoDto.getPrdfabricante())).thenReturn(Optional.of(exitentProduto));

        //then
        assertThrows(ProdutoAlreadyExistException.class, () -> produtoService.CriarProduto(produtoDto));

    }

    @Test
    void whenValidSearchProdutoSpecificationThenReturnProduto() throws ProdutoNotFoundException {

        //given
        ProdutoDto expectedProdutofoundDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto exitentProduto = produtoMapper.toModelProduto(expectedProdutofoundDto);

        //when
        when(produtoRepository.findByprdtituloandfabricante(expectedProdutofoundDto.getPrdtitulo(),
                expectedProdutofoundDto.getPrdfabricante())).thenReturn(Optional.of(exitentProduto));

        //then
        ProdutoDto foundProduto = produtoService.findbyprdtituloeprdfabricante(expectedProdutofoundDto.getPrdtitulo(),
                expectedProdutofoundDto.getPrdfabricante());

        assertThat(foundProduto, is(equalTo(expectedProdutofoundDto)));

    }

    @Test
    void whenNotValidSearchProdutoSpecificationThenThrownException() throws ProdutoNotFoundException {

        //given
        ProdutoDto expectedProdutofoundDto = ProdutoDtoBuilder.builder().build().toDto();

        //when
        when(produtoRepository.findByprdtituloandfabricante(expectedProdutofoundDto.getPrdtitulo(),
                expectedProdutofoundDto.getPrdfabricante())).thenReturn(Optional.empty());

        //then

        assertThrows(ProdutoNotFoundException.class,
                () -> produtoService.findbyprdtituloeprdfabricante(expectedProdutofoundDto.getPrdtitulo(),
                        expectedProdutofoundDto.getPrdfabricante()));
    }

    @Test
    void whenListIsCalledThenReturnListofProdutos() {

        //given
        ProdutoDto expectedProdutofoundDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto exitentProduto = produtoMapper.toModelProduto(expectedProdutofoundDto);

        //when
        when(produtoRepository.findAll()).thenReturn(Collections.singletonList(exitentProduto));

        //then
        List<ProdutoDto> foundListProdutoDto = produtoService.getAll();

        assertThat(foundListProdutoDto, is(not(empty())));
        assertThat(foundListProdutoDto.get(0), is(equalTo(expectedProdutofoundDto)));

    }

    @Test
    void whenListIsCalledThenReturnEmptyListofProdutos() {

        //given
        ProdutoDto expectedProdutofoundDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto exitentProduto = produtoMapper.toModelProduto(expectedProdutofoundDto);

        //when
        when(produtoRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<ProdutoDto> foundListProdutoDto = produtoService.getAll();

        assertThat(foundListProdutoDto, is(empty()));

    }

    @Test
    void whenExclusionsValidaIDThenProductHaveTobeExcluded() throws ProdutoNotFoundException {

        //given
        ProdutoDto expectedDeletedProdutoDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto deletedProduto = produtoMapper.toModelProduto(expectedDeletedProdutoDto);

        //when
        when(produtoRepository.FindByProdutoByprdcodigo(expectedDeletedProdutoDto.getPrdcodigo()))
                .thenReturn(Optional.of(deletedProduto));

        doNothing().when(produtoRepository).deleteProdutoByprdcodigo(expectedDeletedProdutoDto.getPrdcodigo());

        produtoService.deleteByprdcodigo(expectedDeletedProdutoDto.getPrdcodigo());

        //then
        verify(produtoRepository, times(1))
                .FindByProdutoByprdcodigo(expectedDeletedProdutoDto.getPrdcodigo());

        verify(produtoRepository, times(1))
                .deleteProdutoByprdcodigo(expectedDeletedProdutoDto.getPrdcodigo());
    }

    @Test
    void whenExclusionsNoValidaIDThenThrowException() throws ProdutoNotFoundException {

        //given
        ProdutoDto expectedDeletedProdutoDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto deletedProduto = produtoMapper.toModelProduto(expectedDeletedProdutoDto);

        //when
        when(produtoRepository.FindByProdutoByprdcodigo(expectedDeletedProdutoDto.getPrdcodigo()))
                .thenReturn(Optional.empty());

        assertThrows(ProdutoNotFoundException.class,
                () -> produtoService.deleteByprdcodigo(expectedDeletedProdutoDto.getPrdcodigo()));

    }

    @Test
    void whensellProdutoThenDecrementQtEstoque() throws ProdutoNotFoundException, SoldQuantityHigherthanEstoquedException {

        //given
        ProdutoDto soldProdutoDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto expectedProduto = produtoMapper.toModelProduto(soldProdutoDto);

        int quantitytoSold = 3;
        int quantityAfterSold = soldProdutoDto.getPrdqtestoque() - quantitytoSold;

        //then

        when(produtoRepository.FindByProdutoByprdcodigo(soldProdutoDto.getPrdcodigo()))
                .thenReturn(Optional.of(expectedProduto));

        when(produtoRepository.save(expectedProduto)).thenReturn(expectedProduto);

        ProdutoDto updateprodutoDto = produtoService.sellProduto(
                soldProdutoDto.getPrdcodigo(),
                quantitytoSold);

        //testa se a quantidade após a venda é igual a esperada
        assertThat(quantityAfterSold, equalTo(updateprodutoDto.getPrdqtestoque()));

        //testa se a quantidade após a venda não é negativa, ou seja não foi vendido mais do que existe no estoque
        assertThat(updateprodutoDto.getPrdqtestoque(), greaterThan(-1));

    }

    @Test
    void WhenSellQuantityProdutoHigherThanStockedThrowsException() throws ProdutoNotFoundException, SoldQuantityHigherthanEstoquedException {

        //given
        ProdutoDto soldProdutoDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto expectedProduto = produtoMapper.toModelProduto(soldProdutoDto);

        int quantitytoSold = 20;
        int quantityAfterSold = soldProdutoDto.getPrdqtestoque() - quantitytoSold;

        //then

        when(produtoRepository.FindByProdutoByprdcodigo(soldProdutoDto.getPrdcodigo()))
                .thenReturn(Optional.of(expectedProduto));

        //Lança uma excessão pois a quantidade vendida será maior que a estocada
        assertThrows(SoldQuantityHigherthanEstoquedException.class,
                () -> produtoService.sellProduto(soldProdutoDto.getPrdcodigo(), quantitytoSold));

    }


    @Test
    void WhenSellProdutoWithNoValidCodigoThrowsException() throws ProdutoNotFoundException, SoldQuantityHigherthanEstoquedException {

        //given
        ProdutoDto soldProdutoDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto expectedProduto = produtoMapper.toModelProduto(soldProdutoDto);

        int quantitytoSold = 20;

        //then
        when(produtoRepository.FindByProdutoByprdcodigo(soldProdutoDto.getPrdcodigo()))
                .thenReturn(Optional.empty());

        //Lança uma excessão pois o Produto vendido não está cadastrado
        assertThrows(ProdutoNotFoundException.class,
                () -> produtoService.sellProduto(soldProdutoDto.getPrdcodigo(), quantitytoSold));

    }


    @Test
    void WhenSetProdutoPriceThenReturnProdutoUpdated()
            throws ProdutoNotFoundException, SoldQuantityHigherthanEstoquedException, PriceofSellIsLessThanThePermitedByMinimalProfitNecessary {

        //given
        ProdutoDto ProdutoDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto expectedProduto = produtoMapper.toModelProduto(ProdutoDto);

        PriceofSellDto priceofSellDto = new PriceofSellDto();
        priceofSellDto.setPriceOfSell(650);

        expectedProduto.setPrdpreco(priceofSellDto.getPriceOfSell());

        //when
        when(produtoRepository.FindByProdutoByprdcodigo(expectedProduto.getPrdcodigo()))
                .thenReturn(Optional.of(expectedProduto));

        when(produtoRepository.save(expectedProduto)).thenReturn(expectedProduto);

        ProdutoDto returnedProduto = produtoService.setProdutoPrice(
                ProdutoDto.getPrdcodigo(), priceofSellDto.getPriceOfSell());

        //then
        assertThat(returnedProduto.getPrdpreco(), is(equalTo(expectedProduto.getPrdpreco())));

    }

    @Test
    void WhenSetNoValidProdutoPriceThenThrowException()
            throws ProdutoNotFoundException, SoldQuantityHigherthanEstoquedException, PriceofSellIsLessThanThePermitedByMinimalProfitNecessary {

        //given
        ProdutoDto ProdutoDto = ProdutoDtoBuilder.builder().build().toDto();
        Produto expectedProduto = produtoMapper.toModelProduto(ProdutoDto);

        PriceofSellDto priceofSellDto = new PriceofSellDto();
        priceofSellDto.setPriceOfSell(100);

        expectedProduto.setPrdpreco(priceofSellDto.getPriceOfSell());

        //when
        when(produtoRepository.FindByProdutoByprdcodigo(expectedProduto.getPrdcodigo()))
                .thenReturn(Optional.of(expectedProduto));

        //then
        assertThrows(PriceofSellIsLessThanThePermitedByMinimalProfitNecessary.class,
                () -> produtoService.setProdutoPrice(ProdutoDto.getPrdcodigo(),
                        priceofSellDto.getPriceOfSell()));

    }

}
