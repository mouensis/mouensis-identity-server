package com.mouensis.server.identity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Collection;
import java.util.Collections;

/**
 * 安全配置
 *
 * @author zhuyuan
 * @date 2020/12/13 22:43
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public AccessDeniedHandler apiAccessDeniedHandler() {
        return new ApiAccessDeniedHandler(this.objectMapper);
    }

    @Bean
    public AuthenticationEntryPoint apiAccessDeniedEntryPoint() {
        return new ApiAccessDeniedEntryPoint(apiAccessDeniedHandler());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return Collections.EMPTY_LIST;
                    }

                    @Override
                    public String getPassword() {
                        return "$2a$10$LUeaVf8gZYsUnk5jPhl0XeTDJcIl9DQJ.lFHwMeCenRHE2XQOUh8e";
                    }

                    @Override
                    public String getUsername() {
                        return "user";
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                };
            }
        });
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(Customizer -> Customizer
                        .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/**")
                        .permitAll())
                .authorizeRequests(customizer -> customizer
                        .antMatchers("/**")
                        .authenticated())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //自定义登录认证配置
                .apply(new LoginAuthenticationServerConfigurer<>())
                .and()
                //异常处理401
                .exceptionHandling()
                .accessDeniedHandler(apiAccessDeniedHandler())
                .authenticationEntryPoint(apiAccessDeniedEntryPoint());
    }
}
