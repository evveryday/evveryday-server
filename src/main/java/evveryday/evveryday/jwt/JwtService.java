package evveryday.evveryday.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "YOUR_SECRET_KEY";
    private final long EXPIRATION_TIME = 864_000_000; // 10일

    public String generateEmailVerificationToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public String verifyEmailToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
