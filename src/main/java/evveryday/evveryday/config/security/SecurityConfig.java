package evveryday.evveryday.config.security;

import evveryday.evveryday.config.CustomAuthenticationSuccessHandler;
import evveryday.evveryday.config.UserAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import evveryday.evveryday.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .mvcMatchers("/", "/join/**", "/login/**", "/join/mail/**").permitAll()
                    .mvcMatchers("/member/**", "/logout/**", "/group/**").hasRole("USER")
                    .mvcMatchers("/admin/**", "/logout/**", "/member/**", "/group/**").hasRole("ADMIN")
                    .antMatchers("/admin/**")
                    .hasAnyAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/member")
                    .successHandler(new CustomAuthenticationSuccessHandler())
                    .usernameParameter("email")
                    .failureUrl("/login/error")
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true) // 세션 무효화
                    .deleteCookies("JSESSIONID", "remember-me") // 쿠키 제거
                    .clearAuthentication(true) // 인증 정보 제거
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new UserAuthenticationEntryPoint())
                    .and()
                .csrf().ignoringAntMatchers("/join/mail/**") // csrf disable 설정
        ;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}