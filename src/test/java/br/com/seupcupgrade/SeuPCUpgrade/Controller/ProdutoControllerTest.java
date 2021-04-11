package br.com.seupcupgrade.SeuPCUpgrade.Controller;

import br.com.seupcupgrade.SeuPCUpgrade.Builder.ProdutoDtoBuilder;
import br.com.seupcupgrade.SeuPCUpgrade.Dto.PriceofSellDto;
import br.com.seupcupgrade.SeuPCUpgrade.Dto.ProdutoDto;
import br.com.seupcupgrade.SeuPCUpgrade.Dto.QuantityDto;
import br.com.seupcupgrade.SeuPCUpgrade.Entity.Produto;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.ProdutoAlreadyExistException;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.ProdutoNotFoundException;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.SoldQuantityHigherthanEstoquedException;
import br.com.seupcupgrade.SeuPCUpgrade.Service.ProdutoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.seupcupgrade.SeuPCUpgrade.Utils.JsonConvertionUtils.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {

    private static final String PRODUTO_API_URL_PATH = "/api/seupcupgrade";
    private static final long VALID_PRODUTO_ID = 1L;
    private static final long INVALID_PRODUTO_ID = 34L;
    private static final String PRODUTO_API_SUBPATH_INCREMENT_URL = "";
    private static final String PRODUTO_API_SUBPATH_DECREMENT_URL = "";

    private MockMvc mockMvc;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void whenPostThenProdutoCreated() throws Exception {

        //given
        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();

        //when
        when(produtoService.CriarProduto(produtoDto)).thenReturn(produtoDto);

        //System.out.println(asJsonString(produtoDto));

        //then
        mockMvc.perform(post(PRODUTO_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(produtoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prdtitulo", Matchers.is(produtoDto.getPrdtitulo())))
                .andExpect(jsonPath("$.prdfabricante", Matchers.is(produtoDto.getPrdfabricante())));

    }

    @Test
    public void whenPostWhitouRequiredFieldThenErrorReturned() throws Exception {

        //given
        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();
        produtoDto.setPrdfabricante(null);

        //then
        mockMvc.perform(post(PRODUTO_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(produtoDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void whenGetProdutowhitvalidParamThenOKStatusReturned() throws Exception {

        //given
        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();

        //when
        when(produtoService.findbyprdtituloeprdfabricante(produtoDto.getPrdtitulo(),
                produtoDto.getPrdfabricante())).thenReturn(produtoDto);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUTO_API_URL_PATH +
                "/buscarportitulofabricante?titulo=" + produtoDto.getPrdtitulo() +
                "&fabricante=" + produtoDto.getPrdfabricante())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prdtitulo", Matchers.is(produtoDto.getPrdtitulo())))
                .andExpect(jsonPath("$.prdfabricante", Matchers.is(produtoDto.getPrdfabricante())));

    }

    @Test
    void whenGetProdutoNotRegisteredThenReturnNotFound() throws Exception {

        //given
        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();

        //when
        when(produtoService.findbyprdtituloeprdfabricante(produtoDto.getPrdtitulo(),
                produtoDto.getPrdfabricante())).thenThrow(new ProdutoNotFoundException());

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUTO_API_URL_PATH +
                "/buscarportitulofabricante?titulo=" + produtoDto.getPrdtitulo() +
                "&fabricante=" + produtoDto.getPrdfabricante())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenGetListofProdutoIsCalledThenReturnOKStatus() throws Exception {

        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();

        //when
        when(produtoService.getAll()).thenReturn(Collections.singletonList(produtoDto));

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUTO_API_URL_PATH +
                "/buscartodos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].prdtitulo", Matchers.is(produtoDto.getPrdtitulo())))
                .andExpect(jsonPath("$[0].prdfabricante", Matchers.is(produtoDto.getPrdfabricante())));

    }

    @Test
    void whenDeleteValidProdutothenReturnStatusNoContent() throws Exception {

        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();

        //when
        doNothing().when(produtoService).deleteByprdcodigo(produtoDto.getPrdcodigo());

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUTO_API_URL_PATH +
                "/deletebyId/" + produtoDto.getPrdcodigo())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void whenDeleteNotValidProdutothenThrowException() throws Exception {

        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();

        //when

        doThrow(ProdutoNotFoundException.class)
                .when(produtoService).deleteByprdcodigo(produtoDto.getPrdcodigo());

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUTO_API_URL_PATH +
                "/deletebyId/" + produtoDto.getPrdcodigo())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenSellProdutothenReturnProdutoUpdated() throws Exception {

        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();
        QuantityDto quantityDto = QuantityDto.builder().build();

        quantityDto.setQuantityToSell(15);
        produtoDto.setPrdqtestoque(produtoDto.getPrdqtestoque() - quantityDto.getQuantityToSell());

        //when
        when(produtoService.sellProduto(produtoDto.getPrdcodigo(), quantityDto.getQuantityToSell()))
                .thenReturn(produtoDto);

        //then
        mockMvc.perform(MockMvcRequestBuilders.patch(PRODUTO_API_URL_PATH +
                "/sellProduto/" + produtoDto.getPrdcodigo())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prdtitulo", Matchers.is(produtoDto.getPrdtitulo())))
                .andExpect(jsonPath("$.prdfabricante", Matchers.is(produtoDto.getPrdfabricante())))
                .andExpect(jsonPath("$.prdqtestoque", Matchers.is(produtoDto.getPrdqtestoque())));

    }

    @Test
    void whenSellQuantityOfProdutoHigherThanStokedThenThrowException() throws Exception {

        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();
        QuantityDto quantityDto = QuantityDto.builder().build();

        quantityDto.setQuantityToSell(30);
        produtoDto.setPrdqtestoque(produtoDto.getPrdqtestoque() - quantityDto.getQuantityToSell());

        //when
        when(produtoService.sellProduto(produtoDto.getPrdcodigo(), quantityDto.getQuantityToSell()))
                .thenThrow(SoldQuantityHigherthanEstoquedException.class);

        //then
        mockMvc.perform(MockMvcRequestBuilders.patch(PRODUTO_API_URL_PATH +
                "/sellProduto/" + produtoDto.getPrdcodigo())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void whenSetValidPricetoProdutothenReturnUpdateProduto() throws Exception {

        ProdutoDto produtoDto = ProdutoDtoBuilder.builder().build().toDto();
        ProdutoDto expectedprodutoDto = produtoDto;

        PriceofSellDto priceofSellDto = new PriceofSellDto();
        priceofSellDto.setPriceOfSell(400);

        expectedprodutoDto.setPrdpreco(priceofSellDto.getPriceOfSell());

        //when

        when(produtoService.setProdutoPrice(produtoDto.getPrdcodigo(),
                priceofSellDto.getPriceOfSell())).thenReturn(expectedprodutoDto);

        //then
        mockMvc.perform(MockMvcRequestBuilders.patch(PRODUTO_API_URL_PATH +
                "/setPrice/" + produtoDto.getPrdcodigo())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(priceofSellDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prdpreco", Matchers.is(expectedprodutoDto.getPrdpreco())));

    }

}
