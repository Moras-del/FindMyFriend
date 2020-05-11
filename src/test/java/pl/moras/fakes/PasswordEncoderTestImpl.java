package pl.moras.fakes;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTestImpl implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return "hashed";
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.equals(s);
    }
}
