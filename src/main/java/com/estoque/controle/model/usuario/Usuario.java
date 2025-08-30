package com.estoque.controle.model.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario_tb")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "nome")
    private String nomeUsuario;

    @Column(name = "senha")
    private String senhaUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_do_usuario")
    private NivelDeUsuario nivelDeUsuario;

    public Usuario(String email, String encryptedSenha, NivelDeUsuario nivelDeUsuario, String nomeUsuario) {
        this.email = email;
        this.senhaUsuario = encryptedSenha;
        this.nivelDeUsuario = nivelDeUsuario;
        this.nomeUsuario = nomeUsuario;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.nivelDeUsuario == NivelDeUsuario.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_GERENTE"),
                new SimpleGrantedAuthority("ROLE_USUARIO"),
                new SimpleGrantedAuthority("ROLE_ESTOQUE"));
        else if (this.nivelDeUsuario == NivelDeUsuario.GERENTE)
            return List.of(new SimpleGrantedAuthority("ROLE_GERENTE"),
                    new SimpleGrantedAuthority("ROLE_USUARIO"),
                    new SimpleGrantedAuthority(("ROLE_ESTOQUE")));
        else if (this.nivelDeUsuario == NivelDeUsuario.ESTOQUE)
            return List.of(new SimpleGrantedAuthority("ROLE_USUARIO"),
                    new SimpleGrantedAuthority("ROLE_ESTOQUE"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USUARIO"));
    }

    public String getPassword() {
        return this.senhaUsuario;
    }

    public String getUsername() {
        return this.email;
    }

    //Verificação para ver se a conta não expirou
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Verificação para ver se a conta não está bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Verificação para ver se a credencial não está expirada
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Verificar se a conta está abilitada
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }


    public String getSenhaUsuario() {
        return senhaUsuario;
    }


    public NivelDeUsuario getNivelDeUsuario() {
        return nivelDeUsuario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public void setNivelDeUsuario(NivelDeUsuario nivelDeUsuario) {
        this.nivelDeUsuario = nivelDeUsuario;
    }
}
