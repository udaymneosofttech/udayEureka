package vio.live.zuulServer.config;


import org.springframework.beans.factory.annotation.Value;

public class JwtConfig {

    @Value("${security.jwt.uri:/auth/**}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;

    public String getUri() {
        return Uri;
    }

    public JwtConfig setUri(String uri) {
        Uri = uri;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public JwtConfig setHeader(String header) {
        this.header = header;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public JwtConfig setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public int getExpiration() {
        return expiration;
    }

    public JwtConfig setExpiration(int expiration) {
        this.expiration = expiration;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public JwtConfig setSecret(String secret) {
        this.secret = secret;
        return this;
    }
}

