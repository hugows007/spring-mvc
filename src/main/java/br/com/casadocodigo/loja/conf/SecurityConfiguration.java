package br.com.casadocodigo.loja.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.casadocodigo.loja.dao.UsuarioDAO;
import org.springframework.web.filter.CharacterEncodingFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioDAO usuarioDao;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        http.addFilterBefore(encodingFilter, CsrfFilter.class);

        http.authorizeRequests()
                .antMatchers("/produtos/form").hasRole("ADMIN")
                .antMatchers("/produtos").hasRole("ADMIN")
                .antMatchers("/produtos/").hasRole("ADMIN")
                .antMatchers("/produtos/**").permitAll()
                .antMatchers("/carrinho/**").permitAll()
                .antMatchers("/pagamento/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/adiciona-usuario-senha-master").permitAll()
                .anyRequest().authenticated()
                .and()
                .	formLogin().loginPage("/login").defaultSuccessUrl("/produtos").permitAll()
                .and()
                	.logout()
                	.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                	.logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioDao)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    // Forma recomendada de ignorar no filtro de segurança as requisições para recursos estáticos
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
}
