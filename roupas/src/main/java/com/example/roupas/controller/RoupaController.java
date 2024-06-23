package com.example.roupas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.roupas.domain.dto.roupa.RoupaRequestDTO;
import com.example.roupas.domain.dto.roupa.RoupaResponseDTO;
import com.example.roupas.domain.service.RoupaService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/roupas")
public class RoupaController {
    @Autowired
    private RoupaService roupaService;

    @GetMapping
    public ResponseEntity<List<RoupaResponseDTO>> obterTodos(){
        return ResponseEntity.ok(roupaService.obterTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoupaResponseDTO> obterPorId(@PathVariable Long id){
        return ResponseEntity.ok(roupaService.obterPorId(id));
    }

    @PostMapping
    public ResponseEntity<RoupaResponseDTO> cadastrar(@RequestBody RoupaRequestDTO dto){
        RoupaResponseDTO roupa = roupaService.cadastrar(dto);
        return new ResponseEntity<>(roupa, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoupaResponseDTO> atualizar(@PathVariable Long id, @RequestBody RoupaRequestDTO dto){
        RoupaResponseDTO responseDTO = roupaService.atualizar(id, dto);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id){
        roupaService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

