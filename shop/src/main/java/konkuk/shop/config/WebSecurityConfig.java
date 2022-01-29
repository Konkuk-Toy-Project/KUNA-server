package konkuk.shop.config;

import konkuk.shop.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http 시큐리티 빌더
        http.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정
                .and()
                .csrf().disable() // csrf는 현재 사용하지 않으므로 disable
                .httpBasic().disable() // token을 사용하므로 basic 인증 disable
                .formLogin().disable()
                .headers().frameOptions().disable()// h2 오류 방지
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session 기반이 아님을 선언
                .and()
                .authorizeRequests()
                  .antMatchers("/member/signup", "/member/duplication/email", "/member/login", "/member/find/**",
                          "/coupon", "/h2-console/**", "/review/**", "/qna/**", "/item/**").permitAll()
                .anyRequest().authenticated(); //이외의 모든 경로는 인증 해야 함

        // filter 등록
        // 매 요청마다 CorsFilter 실행한 후에 jwtAuthenticationFilter 실행된다.
        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );
    }
}

