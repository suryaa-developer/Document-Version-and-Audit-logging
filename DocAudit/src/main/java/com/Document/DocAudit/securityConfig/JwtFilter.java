    package com.Document.DocAudit.securityConfig;

    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.jspecify.annotations.NonNull;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.CredentialsExpiredException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;
    import java.util.ArrayList;

    @Component
    public class JwtFilter extends OncePerRequestFilter {
        @Autowired
        private JwtUtils jwtUtils;

        @Override
        protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtils.ValidateToken(token)) {
                    String username = jwtUtils.GetUsernameFromToken(token);

                    // 1. Create the Authentication Token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username, null, new ArrayList<>()
                    );

                    // 2. Set details (important for Spring to trust the request)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 3. Set the context manually
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("User Authenticated in SecurityContext: " + username);
                } else {
                    System.err.println("JWT Token was invalid or expired");
//                    throw new CredentialsExpiredException("Your session has expired. Please log in again.");

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");

                    response.getWriter().write("""
        {
          "success": false,
          "message": "JWT Token expired or invalid",
          "data": null
        }
        """);

                    return; // 🔥 stop execution
                }
            }

            // Always call this so the request moves to the Controller
            filterChain.doFilter(request, response);
        }
    }
