package br.com.seupcupgrade.SeuPCUpgrade.Repository;

import br.com.seupcupgrade.SeuPCUpgrade.Entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    //Optional<Beer> findByName(String name);

    @Query("SELECT p FROM Produto p WHERE p.prdtitulo = :prdtitulo and p.prdfabricante = :prdfabricante")
    Optional<Produto> findByprdtituloandfabricante(
            @Param("prdtitulo") String prdtitulo, @Param("prdfabricante") String prdfabricante);

    @Modifying
    @Query("DELETE FROM Produto p WHERE p.prdcodigo= :prdcodigo")
    void deleteProdutoByprdcodigo(@Param("prdcodigo") long prdcodigo);

    @Query("SELECT p FROM Produto p WHERE p.prdcodigo= :prdcodigo")
    Optional<Produto> FindByProdutoByprdcodigo(@Param("prdcodigo") Long prdcodigo);


}
