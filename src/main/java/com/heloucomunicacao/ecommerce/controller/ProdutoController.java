package com.heloucomunicacao.ecommerce.controller;

import com.heloucomunicacao.ecommerce.model.Produto;
import com.heloucomunicacao.ecommerce.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //Informando o Java que essa classe é uma controller
@RequestMapping("/produtos") //Padronizando rota base como "/produtos"
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService; //Realizando injeção de dependência; injetando a interface service na controller para...

    @PostMapping //Metodo post para criar um novo produto
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) { //Utilizando ResponseEntity<> para lidar com respostas HTTP e @RequestBody para receber JSON do corpo das requisições
        Produto novoProduto = produtoService.salvarProduto(produto); //Quando chamado, o Post irá criar uma nova instância da Model Produto, executando o método salvarProduto da produtoService
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED); //Retorna se o novo produto foi criado
    }

    @GetMapping //Método get para listar os produtos
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoService.listarTodos();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping("/{id}") //Método get para retornar um produto específico com base no seu Id
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) { //@áthVariable para receber o Id da URL
        return produtoService.buscarPorId(id)
                .map(produto -> new ResponseEntity<>(produto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}") //Método put para atualizar o produto com base no seu Id
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) { //@PathVariable para receber o Id da URL e @RequestBody para receber os campos atualizados do Body do JSON
        return produtoService.buscarPorId(id)
                .map(produtoExistente -> { //Inicio da estrutura optional para manipulação dos dados do produto
                    produtoExistente.setNome(produto.getNome());
                    produtoExistente.setDescricao(produto.getDescricao());
                    produtoExistente.setPreco(produto.getPreco());
                    produtoExistente.setLinkDownload(produto.getLinkDownload());
                    Produto produtoAtualizado = produtoService.salvarProduto(produtoExistente);
                    return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        if (produtoService.buscarPorId(id).isPresent()) {
            produtoService.deletarProduto(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
