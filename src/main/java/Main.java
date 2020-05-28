import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        System.out.println("running......");
        String id = UUID.randomUUID().toString().replace("-", "");
        String secret = "liP5s7lYoIWj9fMkckzGanrnvB1SFNFLVvl3rTUFCVU";
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000*10)); // 30 seconds
        String phonenumber = "2547xxxxxxxxx";
        String token;
        Jws<Claims> jws = null;

        // use this to generate key for the first time to save it somewhere
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("****** Secret string *** " + secretString + "************************");
        // end of key string generator


        // ###################  encode  ###########################
        try {
            token = Jwts.builder()
                    .setIssuer("SOMEISSUER")
                    .setSubject("PROJECT")
                    .claim("name", "staff")
                    .claim("phone", phonenumber)
                    .setIssuedAt(now)
                    .setExpiration(exp)
                    .signWith(
                            SignatureAlgorithm.HS256, secret
                    )
                    .compact();

        } catch (Exception e) {
            System.err.println("Unable to create CSRf JWT: " + e.getMessage());
            token = id;
        }

        System.out.println("Token generated : " + token);

        // ###################  decode  ###########################
        // To test expiration
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);

        } catch (ExpiredJwtException e) {
            System.err.println("jws token decode expiration error : " + e.getMessage());
        } catch (JwtException ex) {
            System.err.println("jws token decode error : " + ex.getMessage());
        }

        System.out.println(jws.toString());
    }
}
