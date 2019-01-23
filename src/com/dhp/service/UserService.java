package com.dhp.service;

import com.dhp.model.PasswordResetToken;
import com.dhp.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    User findByCpf(String cpf);
    
    User findById(long id);

    List<User> findByRef(String ref);

    User findByRefFinal(String refFinal);

    public void createPasswordResetTokenForUser(final User user, final String token);
    
    String validatePasswordResetToken(String cpf, String token);
    
    User getUser(String verificationToken);
    
    void changeUserPassword(User user, String password);
    
    boolean checkIfValidOldPassword(User user, String password);
    
}
