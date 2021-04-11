package br.com.seupcupgrade.SeuPCUpgrade.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "prdcodigo")
    private Long prdcodigo;

    @Column(name = "prdtitulo")
    private String prdtitulo;

    @Column(name = "prdfabricante")
    private String prdfabricante;

    @Column(name = "prdpreco")
    private Double prdpreco;

    @Column(name = "prdprecoaqusicao")
    private Double prdprecoaqusicao;

    @Column(name = "prdqtestoque")
    private int prdqtestoque;

    @Column(name = "prddescricao")
    private String prddescricao;

}
