package ru.lipnin.itmohomework.security.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.proc.BadJWTException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.lipnin.itmohomework.security.model.CustomUserDetails;

import java.io.IOException;
import java.text.ParseException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtSecurityService jwtSecurityService;
    private final UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        /*
        Я обратил внимание, что много так где делается,
        но почему мы начинаем с символа после пробела, мы же проверяем, что заголовок начинается на Bearer
        без пробела, то есть в теории если будет опечатка и не придет пароль, то токен будет неверный
        На сколько правильно использовать authHeader.substring(6).trim()?
         */

        String jwt = authHeader.substring(7);

        String userName = null;
        try {
            userName = jwtSecurityService.getSubject(jwt);
        } catch (BadJOSEException | ParseException | JOSEException e) {
            throw new IOException(e);
        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetailService.loadUserByUsername(userName);
            try {
                if (jwtSecurityService.isTokenValid(jwt, customUserDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUserDetails,
                            null, customUserDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (BadJOSEException | ParseException | JOSEException e) {
                throw new IOException(e);
            }
        }
        filterChain.doFilter(request, response);

    }
}
