package com.heloucomunicacao.ecommerce.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.heloucomunicacao.ecommerce.model.Produto;
import com.heloucomunicacao.ecommerce.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*") //Permite a requisição de quaisquer origens (somente antes do deploy; para implementação do front end)
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService; //Instanciando a service (regra de negócio para adição ou criação de um novo produto)

    @PostMapping //Método para a criação de um produto
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.salvarProduto(produto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    @GetMapping //Método para retornar uma lista produtos
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoService.listarTodos();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping("/{id}") //Método para auxiliar a abertura da tela de edição de um produto específico
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(produto -> new ResponseEntity<>(produto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}") //Método para setar a atualização dos dados de um produto
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        return produtoService.buscarPorId(id)
                .map(produtoExistente -> {
                    produtoExistente.setNome(produto.getNome());
                    produtoExistente.setDescricao(produto.getDescricao());
                    produtoExistente.setPreco(produto.getPreco());
                    produtoExistente.setLinkDownload(produto.getLinkDownload()); //Campo para link de download do produto
                    produtoExistente.setImagemUrl(produto.getImagemUrl()); //Adicionando campo para imagem do produto
                    produtoExistente.setCategoria(produto.getCategoria()); //Adicionando campo para categoria
                    Produto produtoAtualizado = produtoService.salvarProduto(produtoExistente); //Salva o produto com as novas informações no banco se ele existir
                    return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/status") //Adicionando método para atualizar o o status de um produto (soft delete) ao invés de deleta-lo
    public ResponseEntity<Produto> inativarProduto(@PathVariable Long id, @RequestParam String novoStatus) {
        Produto produtoAtualizado = produtoService.atualizarStatus(id, novoStatus);

        if (produtoAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
    }
}