package br.com.seupcupgrade.SeuPCUpgrade.Controller;

import br.com.seupcupgrade.SeuPCUpgrade.Dto.PriceofSellDto;
import br.com.seupcupgrade.SeuPCUpgrade.Dto.ProdutoDto;
import br.com.seupcupgrade.SeuPCUpgrade.Dto.QuantityDto;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.PriceofSellIsLessThanThePermitedByMinimalProfitNecessary;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.ProdutoAlreadyExistException;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.ProdutoNotFoundException;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.SoldQuantityHigherthanEstoquedException;
import br.com.seupcupgrade.SeuPCUpgrade.Service.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/seupcupgrade")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping("/buscarportitulofabricante")
    public ProdutoDto buscarporTituloEFabricante(@Param("titulo") String titulo,
                                                 @Param("fabricante") String fabricante) throws Exception {

        return produtoService.findbyprdtituloeprdfabricante(titulo, fabricante);

    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoDto criarProd(@RequestBody @Valid ProdutoDto produtoDto) throws ProdutoAlreadyExistException {

        return produtoService.CriarProduto(produtoDto);

    }

    @GetMapping("/buscartodos")
    public List<ProdutoDto> buscarTodos() throws Exception {

        return produtoService.getAll();

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/deletebyId/{prdcodigo}")
    public void deleteByID(@PathVariable Long prdcodigo) throws ProdutoNotFoundException {

        produtoService.deleteByprdcodigo(prdcodigo);

    }

    @PatchMapping("/sellProduto/{prdcodigo}")
    public ProdutoDto SellProduto(@PathVariable Long prdcodigo, @RequestBody @Valid QuantityDto quantityDto)
            throws SoldQuantityHigherthanEstoquedException, ProdutoNotFoundException {

        return produtoService.sellProduto(prdcodigo, quantityDto.getQuantityToSell());

    }

    @PatchMapping("/setPrice/{prdcodigo}")
    public ProdutoDto setPrice(@PathVariable Long prdcodigo, @RequestBody @Valid PriceofSellDto priceofSellDto)
            throws SoldQuantityHigherthanEstoquedException, ProdutoNotFoundException, PriceofSellIsLessThanThePermitedByMinimalProfitNecessary {

        return produtoService.setProdutoPrice(prdcodigo, priceofSellDto.getPriceOfSell());

    }

}
