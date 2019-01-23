package com.dhp.service;

import com.dhp.afiliados.model.Afiliado;
import com.dhp.afiliados.repository.AfiliadoRepository;
import com.dhp.model.Role;
import com.dhp.model.User;
import com.dhp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AfiliadoRepository afiliadoRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String cpfOuEmail) throws UsernameNotFoundException {

        if(cpfOuEmail.matches("(.*)[a-zA-Z](.*)")){
            Afiliado afiliado = afiliadoRepository.findByEmail(cpfOuEmail);
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for (Role role : afiliado.getRoles()){
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return new CustomUser(afiliado.getEmail(), afiliado.getSenha(), afiliado.getNome(), afiliado.getId(), 0, grantedAuthorities);
        }

        if(!(cpfOuEmail.matches("(.*)[a-zA-Z](.*)"))){
            User user = userRepository.findByCpf(cpfOuEmail);
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for (Role role : user.getRoles()){
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return new CustomUser(user.getCpf(), user.getSenha(), user.getNome(), user.getId(), user.getVerificado(), grantedAuthorities);
        }

        return null;
    }
}

