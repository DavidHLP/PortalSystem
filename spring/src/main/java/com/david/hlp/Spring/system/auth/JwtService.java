package com.david.hlp.Spring.system.auth;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.david.hlp.Spring.system.mapper.TokenMapper;
import com.david.hlp.Spring.system.token.Token;
/**
 * JWT 服务类，提供生成、解析和验证功能。
 */
@Service
@RequiredArgsConstructor
public class JwtService {
  private final TokenMapper tokenMapper;

  public void invalidateUserTokens(Long userId) {
    List<Token> tokens = tokenMapper.listValidTokensByUser(userId);
    tokens.forEach(token ->
      {
        token.setRevoked(true);
        token.setExpired(true);
      }
    );
    tokenMapper.updateBatch(tokens);
  }
}
