package com.example.roupas.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.roupas.domain.model.Roupa;
import com.example.roupas.domain.model.Usuario;

public interface RoupaRepository extends JpaRepository<Roupa, Long>{
    List<Roupa> findByUsuario(Usuario usuario);
    
}
