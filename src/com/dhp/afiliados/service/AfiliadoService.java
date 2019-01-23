package com.dhp.afiliados.service;

import com.dhp.afiliados.model.Afiliado;

public interface AfiliadoService {

    void save(Afiliado afiliado);

    Afiliado findByEmail(String email);

    Afiliado findById(long id);

    Afiliado findByRefFinal(String refFinal);

    void changeUserPassword(Afiliado afiliado, String password);

    boolean checkIfValidOldPassword(Afiliado afiliado, String password);

    String validatePasswordResetToken(String email, String token);

    void createPasswordResetTokenForUser(final Afiliado afiliado, final String token);

    }
