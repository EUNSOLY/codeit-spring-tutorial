package com.demo.demo.configuration;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.demo.demo.repository.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 아이디/비밀번호 로그인을 직접 처리하는 커스텀 AuthenticationProvider
 *
 * <p>원래는 시큐리티가 기본 구현체인 {@link DaoAuthenticationProvider}를 자동으로 만들어서
 * UserDetailsService로 유저를 찾고 비밀번호 비교까지 알아서 해 준다.
 * 이 클래스를 빈으로 등록하면 그 기본 Provider 대신 이 클래스가 로그인을 처리한다.</p>
 *
 * <p>이렇게 직접 만드는 경우</p>
 * <ul>
 *   <li>비밀번호 확인 말고도 추가로 확인할 게 있을 때 (예: 계정 잠금, 사용 기간 만료, OTP)</li>
 *   <li>로그인 실패 메시지나 실패 처리를 직접 정하고 싶을 때</li>
 *   <li>우리 DB가 아니라 외부 서버에 물어봐서 로그인을 확인해야 할 때</li>
 * </ul>
 *
 * <p>처리 순서</p>
 * <ol>
 *   <li>UserDetailsService로 아이디에 맞는 유저를 찾는다</li>
 *   <li>PasswordEncoder로 입력한 비밀번호와 저장된 비밀번호를 비교한다</li>
 *   <li>맞으면 권한을 넣은 인증 완료 토큰을 돌려주고, 틀리면 예외를 던진다</li>
 * </ol>
 *
 * @see DaoAuthenticationProvider
 * @see CustomUserDetailsService
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 요청이 들어오면 아이디/비밀번호가 맞는지 직접 확인하는 메서드
     * - 맞으면: 인증 완료된 토큰을 돌려줌
     * - 틀리면: 예외를 던져서 로그인 실패 처리
     */
    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 로그인 폼에서 넘어온 값이 담긴 토큰 (아직 인증 전 상태)
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authentication;

        log.info("커스텀 로그인 진입");
        String username = token.getName();                  // 입력한 아이디
        String password = (String)token.getCredentials();   // 입력한 비밀번호(평문)

        // 아이디로 저장된 유저를 찾음 (없으면 UsernameNotFoundException 발생)
        User loggedUser = (User)userDetailsService.loadUserByUsername(username);

        // 입력한 비밀번호와 저장된 암호화 비밀번호가 같은지 비교(matches가 입력값을 같은 방식으로 암호화해서 비교해준다.)
        if (!passwordEncoder.matches(password, loggedUser.getPassword())) {
            throw new BadCredentialsException(loggedUser.getUsername() + " : Invalid password");
        }

        // 인증 성공: 권한을 넣어서 "인증 완료" 토큰을 새로 만들어 돌려준다 (권한을 넣는 생성자를 쓰면 Authenticated=true 된다.)
        return new UsernamePasswordAuthenticationToken(username, password, loggedUser.getAuthorities());
    }

    /**
     * 이 Provider가 어떤 종류의 인증 요청을 처리할지 정하는 메서드
     * - true를 돌려주면 authenticate()가 호출됨
     * - 이 Provider는 아이디/비밀번호 로그인(UsernamePasswordAuthenticationToken)만 담당
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
        // return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        // - equals: 정확히 같은 클래스일 때만 true
        // - isAssignableFrom: 같은 클래스거나 그걸 상속한 자식 클래스여도 true
        //   (instanceof는 "객체"로 판단하고, isAssignableFrom은 "클래스"로 판단함)
    }
}
