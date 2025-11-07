package com.heloucomunicacao.ecommerce.service;

import com.heloucomunicacao.ecommerce.model.Produto;
import com.heloucomunicacao.ecommerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository; //Instanciando repository

    public Produto salvarProduto(Produto produto) { //Salvamento de novo produto criado
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() { //Listagem de todos os produtos
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) { //Tratamento para lidar com id não correspondido no banco de dados
        return produtoRepository.findById(id);
    }

    public Produto atualizarStatus(Long id, String novoStatus) { //Substituindo deletarProduto para inativação (Soft Delete)
        Optional<Produto> produtoOpt = produtoRepository.findById(id);

        if (produtoOpt.isEmpty()) {
            return null;
        }

        Produto produto = produtoOpt.get();
        produto.setStatus(novoStatus);
        return produtoRepository.save(produto);
    }

//    public void deletarProduto(Long id) {
//        produtoRepository.deleteById(id);
//    }
}