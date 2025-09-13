package com.estoque.controle.controller;

import com.estoque.controle.exceptions.FornecedorNaoEncontradoException;
import com.estoque.controle.model.fornecedor.Fornecedor;
import com.estoque.controle.dto.FornecedorDTO;
import com.estoque.controle.repository.FornecedorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedor")
@Tag(name = "Fornecedor", description = "Gerenciamento dos fornecedores.")
public class FornecedorController {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorController(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @Operation(summary = "Cadastrar fornecedor", description = "cadastrar fornecedor no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor cadastrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @PostMapping
    public Fornecedor cadastrar(@RequestBody FornecedorDTO fornecedorDTO) {
        FornecedorDTO dtoFormatado = FornecedorDTO.of(fornecedorDTO.nomeFornecedor(), fornecedorDTO.cpfOuCnpj(), fornecedorDTO.tipoFornecedor());

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeFornecedor(dtoFormatado.nomeFornecedor());
        fornecedor.setCpfOuCnpj(dtoFormatado.cpfOuCnpj());
        fornecedor.setTipoFornecedor(dtoFormatado.tipoFornecedor());

        fornecedorRepository.save(fornecedor);

        return fornecedor;
    }

    @Operation(summary = "Deletar fornecedor", description = "deletar fornecedor no banco de dados")
    @ApiResponses(value =  {
            @ApiResponse(responseCode = "200", description = "Fornecedor deletado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @DeleteMapping("{id}")
    public void deletar(@PathVariable("id") Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(FornecedorNaoEncontradoException::new);

        fornecedorRepository.delete(fornecedor);
    }

    @Operation(summary = "Atualizar fornecedor", description = "atualizar fornecedor no banco de dados")
    @ApiResponses(value =  {
            @ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @PutMapping("{id}")
    public Fornecedor atualizar(@PathVariable("id") Long id, @RequestBody FornecedorDTO fornecedorDTO) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(FornecedorNaoEncontradoException::new);

        FornecedorDTO dtoFormatado = FornecedorDTO.of(fornecedorDTO.nomeFornecedor(), fornecedorDTO.cpfOuCnpj(), fornecedorDTO.tipoFornecedor());

        fornecedor.setId(id);
        fornecedor.setNomeFornecedor(dtoFormatado.nomeFornecedor());
        fornecedor.setCpfOuCnpj(dtoFormatado.cpfOuCnpj());
        fornecedor.setTipoFornecedor(dtoFormatado.tipoFornecedor());

        return fornecedorRepository.save(fornecedor);
    }

    @Operation(summary = "Buscar todos os fornecedores", description = "buscar todos os fornecedores no banco de dados")
    @ApiResponses(value =  {
            @ApiResponse(responseCode = "200", description = "Fornecedores buscado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping
    public List<Fornecedor> buscarTodos() {
        return fornecedorRepository.findAll();
    }
}
