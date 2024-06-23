package com.example.roupas.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.roupas.domain.dto.roupa.RoupaRequestDTO;
import com.example.roupas.domain.dto.roupa.RoupaResponseDTO;
import com.example.roupas.domain.exception.ResourceNotFoundException;
import com.example.roupas.domain.model.Roupa;
import com.example.roupas.domain.model.Usuario;
import com.example.roupas.domain.repository.RoupaRepository;

@Service
public class RoupaService implements ICRUDService<RoupaRequestDTO,RoupaResponseDTO> {
    @Autowired
    private RoupaRepository roupaRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<RoupaResponseDTO> obterTodos() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Roupa> lista = roupaRepository.findByUsuario(usuario);
        return lista.stream().map(roupa -> mapper.map(roupa, RoupaResponseDTO.class))
        .collect(Collectors.toList());
    }

    @Override
    public RoupaResponseDTO obterPorId(Long id) {
        Optional<Roupa> optRoupa = roupaRepository.findById(id);

        if(optRoupa.isEmpty()){
            throw new ResourceNotFoundException("Não foi possível encontrar a peça de roupa com o id: " + id);
        }
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(optRoupa.get().getId() != usuario.getId()){
            throw new ResourceNotFoundException("Essa peça de roupa não pertence a esse usuário.");
        }

        return mapper.map(optRoupa.get(), RoupaResponseDTO.class);
    }

    @Override
    public RoupaResponseDTO cadastrar(RoupaRequestDTO dto) {
        Roupa roupa = mapper.map(dto, Roupa.class);
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        roupa.setUsuario(usuario);
        roupa.setId(null);
        roupa = roupaRepository.save(roupa);
        return mapper.map(roupa, RoupaResponseDTO.class);
    }

    @Override
    public RoupaResponseDTO atualizar(Long id, RoupaRequestDTO dto) {
        obterPorId(id);
        Roupa roupa = mapper.map(dto, Roupa.class);
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        roupa.setUsuario(usuario);
        roupa.setId(id);
        roupa = roupaRepository.save(roupa);
        return mapper.map(roupa, RoupaResponseDTO.class);
    }
    @Override
    public void deletar(Long id) {
        obterPorId(id);
        roupaRepository.deleteById(id);
    }
    
}
