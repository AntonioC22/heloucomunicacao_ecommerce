package com.heloucomunicacao.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private double preco;
    private String linkDownload; //Adicionando atributo para link de download do produto
    private String status = "Ativo"; //Adicionando atributo de status para produto
    private String imagemUrl; //Adicionando atributo de imagem para o produto
}