package br.com.seupcupgrade.SeuPCUpgrade.Mapper;

import br.com.seupcupgrade.SeuPCUpgrade.Dto.ProdutoDto;
import br.com.seupcupgrade.SeuPCUpgrade.Entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface ProdutoMapper {

    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);

    Produto toModelProduto(ProdutoDto produtoDto);

    ProdutoDto toDTO(Produto produto);


}
