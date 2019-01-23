package com.dhp.afiliados.repository;

import com.dhp.afiliados.model.Afiliado;
import com.dhp.afiliados.model.PasswordResetTokenAfiliado;
import com.dhp.model.PasswordResetToken;
import com.dhp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.stream.Stream;

public interface PasswordResetTokenAfiliadoRepository extends JpaRepository<PasswordResetTokenAfiliado, Long> {

    PasswordResetTokenAfiliado findByToken(String token);

    PasswordResetTokenAfiliado findByAfiliado(Afiliado afiliado);

    Stream<PasswordResetTokenAfiliado> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from PasswordResetTokenAfiliado t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}