package jpaproject.cafe.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jpaproject.cafe.exception.AuthorizationException;
import jpaproject.cafe.login.dto.Token;
import jpaproject.cafe.member.Member;
import jpaproject.cafe.member.MemberRepository;
import jpaproject.cafe.member.MemberType;
import jpaproject.cafe.member.dto.OauthMemberInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Service
@Transactional
public class LoginService {

    private final MemberRepository memberRepository;
    private final String clientId;
    private final String loginUri;
    private final String redirectUri;
    private final String tokenUri;
    private final String userUri;
    private final String secret;
    private final String state;
    private final RestTemplate restTemplate;

    public LoginService(
        MemberRepository memberRepository,
        @Value("${oauth2.user.github.client-id}") String clientId,
        @Value("${oauth2.user.github.login-uri}") String loginUri,
        @Value("${oauth2.user.github.redirect-uri}") String redirectUri,
        @Value("${oauth2.user.github.user-uri}") String userUri,
        @Value("${oauth2.user.github.token-uri}") String tokenUri,
        @Value("${oauth2.user.github.client-secret}") String secret,
        RestTemplate restTemplate) {
        this.memberRepository = memberRepository;
        this.clientId = clientId;
        this.loginUri = loginUri;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userUri = userUri;
        this.secret = secret;
        this.state = UUID.randomUUID().toString();
        this.restTemplate = restTemplate;
    }

    public RedirectView requestOauthId(RedirectAttributes attributes) {
        attributes.addAttribute("client_id", clientId);
        attributes.addAttribute("redirect_uri", redirectUri);
        attributes.addAttribute("state", state);

        return new RedirectView(loginUri);
    }

    public Token getAccessToken(String code, String state) {
        Map<String, String> body = new HashMap<>();

        body.put("client_id", clientId);
        body.put("client_secret", secret);
        body.put("code", code);
        body.put("state", state);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // Http ?????? ?????? ????????? ???????????? HttpHeader, HttpBody ??? ???????????? ?????????.
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // postForEntity() ???????????? ????????? JSON ?????? ????????? ?????????.
        ResponseEntity<Token> response = restTemplate.postForEntity(tokenUri, request, Token.class);

        return response.getBody();
    }

    public OauthMemberInfo getOauthMemberInfo(Token accessTokenInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessTokenInfo.getAccessToken());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        // getForEntity() -> Header ??? ?????? ??? ?????? ????????? exchange() ??????
        return restTemplate
            .exchange(userUri, HttpMethod.GET, request, OauthMemberInfo.class)
            .getBody();
    }


    public void saveMember(OauthMemberInfo memberInfo) {
        Member member = new Member(memberInfo, MemberType.USER);

        // ?????? ????????? ?????? login ????????? true??? ????????????, ???????????? ????????? ?????? ??????
        memberRepository.findByMemberName(member.getMemberName())
            .ifPresentOrElse(findMember -> findMember.setLogin(true),
                () -> memberRepository.save(member));

    }

    public void saveMember(OauthMemberInfo memberInfo, String sessionId) {
        Member member = new Member(memberInfo, MemberType.USER, sessionId);

        // ?????? ????????? ?????? login ????????? true??? ????????????, ???????????? ????????? ?????? ??????
        memberRepository.findByMemberName(member.getMemberName())
            .ifPresentOrElse(findMember -> findMember.setSessionId(sessionId),
                () -> memberRepository.save(member));

    }

    public void validateState(String receivedState) {
            if (!receivedState.equals(this.state)) {
                throw new AuthorizationException("CSRF ????????????!");
            }
    }
}
