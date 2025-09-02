package com.estoque.controle.repository;

import com.estoque.controle.dto.VendaIndicadoresDTO;
import com.estoque.controle.dto.VendedorRankingDTO;
import com.estoque.controle.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByEmail(String email);

    List<Usuario> findByNomeUsuario(String nomeUsuario);

    @Query("SELECT new com.estoque.controle.dto.UsuarioVendaDTO(u.nomeUsuario, SUM(v.quantidade), SUM(v.valorTotalVendido)) " +
            "FROM Venda v " +
            "JOIN v.usuario u " +
            "WHERE v.dataVenda BETWEEN :inicio AND :fim " +
            "GROUP BY u.nomeUsuario " +
            "ORDER BY SUM(v.quantidade) DESC")
    List<VendedorRankingDTO> quemVendeuMais(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
