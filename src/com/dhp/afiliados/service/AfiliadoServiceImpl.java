package com.dhp.afiliados.service;

import com.dhp.afiliados.model.PasswordResetTokenAfiliado;
import com.dhp.afiliados.repository.AfiliadoRepository;
import com.dhp.afiliados.repository.PasswordResetTokenAfiliadoRepository;
import com.dhp.model.Role;
import com.dhp.afiliados.model.Afiliado;
import com.dhp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service
public class AfiliadoServiceImpl implements AfiliadoService {

    @Autowired
    private AfiliadoRepository afiliadoRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordResetTokenAfiliadoRepository passwordResetTokenAfiliadoRepository;

    @Override
    public void save(Afiliado afiliado) {
        afiliado.setSenha(bCryptPasswordEncoder.encode(afiliado.getSenha()));
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setId(3L); // 1 = User Role
        roles.add(role);

        //user.setRoles(new HashSet<>(roleRepository.findAll()));
        afiliado.setRoles(roles);
        afiliadoRepository.save(afiliado);
    }

    @Override
    public Afiliado findByEmail(String email) {
        return afiliadoRepository.findByEmail(email);
    }

    @Override
    public Afiliado findById(long id) {
        return afiliadoRepository.findById(id);
    }

    @Override
    public Afiliado findByRefFinal(String refFinal) {
        return afiliadoRepository.findByRefEndingWith(refFinal);
    }

    @Override
    public void changeUserPassword(final Afiliado afiliado, final String password) {
        afiliado.setSenha(bCryptPasswordEncoder.encode(password));
        afiliadoRepository.save(afiliado);
    }

    @Override
    public boolean checkIfValidOldPassword(final Afiliado afiliado, final String oldPassword) {
        return bCryptPasswordEncoder.matches(oldPassword, afiliado.getSenha());
    }


    @Override
    public String validatePasswordResetToken(String email, String token) {
        final PasswordResetTokenAfiliado passToken = passwordResetTokenAfiliadoRepository.findByToken(token);
        final Afiliado afiliado = afiliadoRepository.findByEmail(email);
        //System.out.println(passToken.getUser().getCpf());
        if ((passToken == null) || (!afiliado.getEmail().equals(email))) {
            return "invalidToken";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            return "expired";
        }

        //final User user = passToken.getUser();
        final Authentication auth = new UsernamePasswordAuthenticationToken(afiliado, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
        return null;
    }

    @Override
    public void createPasswordResetTokenForUser(final Afiliado afiliado, final String token) {
        final PasswordResetTokenAfiliado myToken = new PasswordResetTokenAfiliado(token, afiliado);
        passwordResetTokenAfiliadoRepository.save(myToken);
    }
}
