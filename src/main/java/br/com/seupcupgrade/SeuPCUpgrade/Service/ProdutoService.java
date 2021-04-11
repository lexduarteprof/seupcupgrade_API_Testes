package br.com.seupcupgrade.SeuPCUpgrade.Service;

import br.com.seupcupgrade.SeuPCUpgrade.Dto.ProdutoDto;
import br.com.seupcupgrade.SeuPCUpgrade.Entity.Produto;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.PriceofSellIsLessThanThePermitedByMinimalProfitNecessary;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.ProdutoAlreadyExistException;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.ProdutoNotFoundException;
import br.com.seupcupgrade.SeuPCUpgrade.Exception.SoldQuantityHigherthanEstoquedException;
import br.com.seupcupgrade.SeuPCUpgrade.Mapper.ProdutoMapper;
import br.com.seupcupgrade.SeuPCUpgrade.Repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
//@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProdutoService {

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper = ProdutoMapper.INSTANCE;

    public ProdutoDto CriarProduto(ProdutoDto produtoDto) throws ProdutoAlreadyExistException {

        verifyifAlreadyRegistred(produtoDto.getPrdtitulo(), produtoDto.getPrdfabricante());

        Produto produto = produtoMapper.toModelProduto(produtoDto);
        Produto newProduto = produtoRepository.save(produto);

        return produtoMapper.toDTO(newProduto);

    }

    private void verifyifAlreadyRegistred(String prdtitulo, String prdfabricante)
            throws ProdutoAlreadyExistException {

        Optional<Produto> optSavedProduto = produtoRepository.findByprdtituloandfabricante(prdtitulo, prdfabricante);

        if (optSavedProduto.isPresent()) {
            throw new ProdutoAlreadyExistException(prdtitulo + " - " + prdfabricante);
        }

    }

    public Produto findbyProdutocodigo(Long prdcodigo)
            throws ProdutoNotFoundException {

        Optional<Produto> RegisteredProduto = produtoRepository.FindByProdutoByprdcodigo(prdcodigo);

        if (RegisteredProduto.isPresent() != true) {
            throw new ProdutoNotFoundException();
        }
        return RegisteredProduto.get();

    }


    public void verifyifExistByprdcodigo(Long prdcodigo)
            throws ProdutoNotFoundException {

        Optional<Produto> RegisteredProduto = produtoRepository.FindByProdutoByprdcodigo(prdcodigo);

        if (RegisteredProduto.isPresent() != true) {
            throw new ProdutoNotFoundException();
        }


    }

    public ProdutoDto findbyprdtituloeprdfabricante(String prdtitulo, String prdfabricante)
            throws ProdutoNotFoundException {

        Produto foundProduto =
                produtoRepository.findByprdtituloandfabricante(prdtitulo, prdfabricante)
                        .orElseThrow(() -> new ProdutoNotFoundException());
        return produtoMapper.toDTO(foundProduto);

    }

    public List<ProdutoDto> getAll() {

        List<Produto> produtos = produtoRepository.findAll();

        List<ProdutoDto> produtosDto;

        produtosDto = produtos.stream()
                .map(produtoMapper::toDTO)
                .collect(Collectors.toList());
        return produtosDto;
    }

    public void deleteByprdcodigo(Long prdcodigo) throws ProdutoNotFoundException {

        verifyifExistByprdcodigo(prdcodigo);
        produtoRepository.deleteProdutoByprdcodigo(prdcodigo);

    }


    public ProdutoDto sellProduto(Long prdcodigo, int quantitytoSold)
            throws ProdutoNotFoundException, SoldQuantityHigherthanEstoquedException {
        Produto foundProduto = findbyProdutocodigo(prdcodigo);

        if (quantitytoSold > foundProduto.getPrdqtestoque()) {

            throw new SoldQuantityHigherthanEstoquedException(prdcodigo, quantitytoSold);

        }
        foundProduto.setPrdqtestoque(foundProduto.getPrdqtestoque() - quantitytoSold);
        produtoRepository.save(foundProduto);

        return produtoMapper.toDTO(foundProduto);
    }

    public ProdutoDto setProdutoPrice(Long prdcodigo, double priceOfSell)
            throws ProdutoNotFoundException, SoldQuantityHigherthanEstoquedException, PriceofSellIsLessThanThePermitedByMinimalProfitNecessary {
        Produto foundProduto = findbyProdutocodigo(prdcodigo);

        //Gera um erro se a alteração do preço de venda do produto for menor que o preço de aquisição vezes o lucro mínimo
        //necessário de 20%
        if (priceOfSell < foundProduto.getPrdprecoaqusicao() * 1.2) {
            throw new PriceofSellIsLessThanThePermitedByMinimalProfitNecessary(prdcodigo, priceOfSell);
        }

        foundProduto.setPrdpreco(priceOfSell);
        produtoRepository.save(foundProduto);

        return produtoMapper.toDTO(foundProduto);
    }
}
