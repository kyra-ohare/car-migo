package com.unosquare.carmigo.configuration;

import com.unosquare.carmigo.service.CarMigoUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private CarMigoUserDetailsService userDetailsService;

    //    @Override
    //    protected void configure(final AuthenticationManagerBuilder auth) throws Exception
    //    {
    //        auth.userDetailsService(userDetailsService);
    //    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }
}
