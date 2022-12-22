# Spring Framework Security 활용 예제

### 1. 설정
```
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/hello")
                .successHandler(loginSuccessHandler)
                .and();
                // .csrf().disable();
    }
```
- http 요청의 보안설정, login 화면을 제외한 모든 화면은 로그인(인증) 후 접근 가능하게 한다.

- CSRF는 현재 로그인 인증 예제에서는 사용하지않는다.
  - **추후  POST, PUT 등 요청마다 발행된 토큰을 확인할때 사용**
    - `HttpSessionCsrfTokenRepository` 또는 `CookieCsrfTokenRepository` 등
    - 사용시 클라이언트에서 요청할때마다 csrf 토큰값을 같이 전달해야한다.
  
- 로그인 성공 시 기본적으로 /hello 로 리다이렉트 설정
  - `SimpleUrlAuthenticationSuccessHandler` 상속받은 클래스에서  
  `defaultSuccessUrl` 메서드를 override 하고 다른 기능을 추가하거나
  - 또는 (스프링 Security 기본 로그인 페이지를 사용하지 않을때) defaultSuccessUrl 로 지정이 가능


### 2. 로그인 (인증)
##### 1. 회원정보는 DB에 저장하며 로그인시 JPA로 DB에서 유저정보를 조회한다.
```
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class MemberDetails implements UserDetails {

    private String username;
    private String password;
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private Collection<? extends GrantedAuthority> authorities;
}
```
- 스프링에서 제공하는 `UserDetails` 인터페이스를 위 예시처럼 VO 용도로 구현하고  
  `UserDetailsService` 인터페이스를 구현하는 클래스를 통해 DB에서 사용자를 조회할 수 있다.
  - 이 경우 `UserDetailsService` 인터페이스를 구현하는 클래스를 명시해주어야함
```
ex)
    @Autowired
    LoginService loginService;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService);
    }
```

##### 2. 로그인 정합성 확인
- `UserDetailsService` 구현 예시
```
ex) 
import org.springframework.security.core.userdetails.UserDetailsService;

public class LoginService implements UserDetailsService {

    @Autowired
    MemberRepo memberRepo; /* Jpa Interface */

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member;
        try {
            * DB에 아이디가 존재하지 않는다면 Jpa에서 오류를 뱉음
            member = memberRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("아이디를 찾을 수 없습니다.");
        }

        return setUserDetails(member);
    } ...
}    
```

- 로그인시 입력한 패스워드가 DB의 암호화된 패스워드가 다를 경우 
```
ex)
{
    "error": "Unauthorized",
    "message": "Bad credentials",
    "path": "/login",
    "status": 401,
    "timestamp": "2022-12-19T10:53:13+0000"
}
```

- Spring Security 의 인증 과정 ([설명](https://100100e.tistory.com/387))


1. 비밀번호 저장형식 ([공식문서](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html))
   - 비밀번호는 암호화된 비밀번호가 PasswordEncoder 의 ID값을 명시한 채로 DB에 저장하게 됨.  
     특정 PasswordEncoder를 빈으로 주입했다면 DB에 ID값 없이 주입된 PasswordEncoder로 감별 
   ```
   ex)
   이 아이디 값별로 Spring 이 내장하고 있는 PasswordEncoder 로 감별 
   PASSWORD_COLUMN : {bcrypt}$2a$10$d....
   ```


