package uz.dachatop.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.JwtDTO;
import uz.dachatop.enums.ProfileRole;

import java.util.Date;
import java.util.List;

@Service
public class JwtUtil {
    private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day
    private static final String secretKey = "test-key";

    public static String encode(String phone, List<ProfileRole> role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS512, secretKey);
        jwtBuilder.claim("phone", phone);
        jwtBuilder.claim("role", role);
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.setIssuer("dashatop");
        return jwtBuilder.compact();
    }

    public static JwtDTO decode(String token) {
        JwtParser jwtParser = Jwts.parser();
        jwtParser.setSigningKey(secretKey);
        Jws<Claims> jws = jwtParser.parseClaimsJws(token);
        Claims claims = jws.getBody();
        String phone = (String) claims.get("phone");
//        List<String> role = (List<String>) claims.get("role");
//        List<ProfileRole> profileRole = new LinkedList<>();
//        role.forEach(s -> {
//            profileRole.add(ProfileRole.valueOf(s));
//        });


        return new JwtDTO(phone);
    }

    public static Integer decodeForEmailVerification(String token) {
        JwtParser jwtParser = Jwts.parser();
        jwtParser.setSigningKey(secretKey);
        Jws<Claims> jws = jwtParser.parseClaimsJws(token);
        Claims claims = jws.getBody();
        return (Integer) claims.get("id");
    }
}
