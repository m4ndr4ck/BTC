package com.dhp.service;

import java.util.*;

import com.dhp.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;	
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dhp.model.PasswordResetToken;
import com.dhp.model.User;
import com.dhp.model.VerificationToken;
import com.dhp.repository.PasswordResetTokenRepository;
import com.dhp.repository.RoleRepository;
import com.dhp.repository.UserRepository;
import com.dhp.repository.VerificationTokenRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setSenha(bCryptPasswordEncoder.encode(user.getSenha()));
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setId(1L); // 1 = User Role
        roles.add(role);

        //user.setRoles(new HashSet<>(roleRepository.findAll()));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public User findByCpf(String cpf) {
        return userRepository.findByCpf(cpf);
    }
    
    @Override
    public User findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findByRef(String ref) {
        return userRepository.findByRef(ref);
    }

    @Override
    public User findByRefFinal(String refFinal) {
        return userRepository.findByRefEndingWith(refFinal);
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }
    
    @Override
    public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }
    
    @Override
    public String validatePasswordResetToken(String cpf, String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        final User user = userRepository.findByCpf(cpf);
        //System.out.println(passToken.getUser().getCpf());
        if ((passToken == null) || (!user.getCpf().equals(cpf))) {
            return "invalidToken";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
            return "expired";
        }

        //final User user = passToken.getUser();
        final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext()
            .setAuthentication(auth);
        return null;
    }
    
    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setSenha(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
    
    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return bCryptPasswordEncoder.matches(oldPassword, user.getSenha());
    }
    
}
