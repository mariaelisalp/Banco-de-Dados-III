package com.example.roupas.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.roupas.domain.dto.usuario.UsuarioRequestDTO;
import com.example.roupas.domain.dto.usuario.UsuarioResponseDTO;
import com.example.roupas.domain.exception.ResourceBadRequestException;
import com.example.roupas.domain.exception.ResourceNotFoundException;
import com.example.roupas.domain.model.Usuario;
import com.example.roupas.domain.repository.UsuarioRepository;

@Service
public class UsuarioService implements ICRUDService<UsuarioRequestDTO, UsuarioResponseDTO>{
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioResponseDTO> obterTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(usuario -> mapper.map(
            usuario, UsuarioResponseDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO obterPorId(Long id) {
        Optional<Usuario> optUsuario = 
                usuarioRepository.findById(id);
        if(optUsuario.isEmpty()){
           throw new ResourceNotFoundException
           ("não foi possível encontar o usuário com o id" +
           id);
        }
        return mapper.map(optUsuario.get(), 
        UsuarioResponseDTO.class);
    }

    @Override
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if(dto.getEmail() == null || dto.getSenha() == null){
            throw new ResourceBadRequestException("Email e Senha são Obrigatórios!");
        }//criar método para não repetir código
        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(dto.getEmail());
        if(optUsuario.isPresent()){
            throw new ResourceBadRequestException("Já existe um usuário cadastro com esse email: " + dto.getEmail());
        }
        Usuario usuario = mapper.map(dto, Usuario.class);
        //encriptografar senha 
        String senha = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senha);
        usuario.setId(null);
        usuario.setDataCadastro(new Date());  
        usuario = usuarioRepository.save(usuario);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Override
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
       UsuarioResponseDTO usuarioBanco = obterPorId(id); // se ele não existir o obterPorId vai lançar a exceção
         if(dto.getEmail() == null || dto.getSenha() == null){
            throw new ResourceBadRequestException("Email e Senha são Obrigatórios!");
        }
        Usuario usuario = mapper.map(dto, Usuario.class);
        //cryp senha
        usuario.setSenha(dto.getSenha());
        usuario.setId(id);
        usuario.setDataInativacao(usuarioBanco.getDataInativacao());
        usuario.setDataCadastro(usuarioBanco.getDataCadastro());
        usuario = usuarioRepository.save(usuario);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Override
    public void deletar(Long id) {
      Optional<Usuario> optUsuario = 
      usuarioRepository.findById(id);
      if(optUsuario.isEmpty()){
        throw new 
        ResourceNotFoundException("não foi possível encontrar o usuário");
      }
      Usuario usuario = optUsuario.get();
      usuario.setDataInativacao(new Date());
      usuarioRepository.save(usuario);
    }
    
}
