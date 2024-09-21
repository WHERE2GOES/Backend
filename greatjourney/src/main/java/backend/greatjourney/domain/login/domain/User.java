package backend.greatjourney.domain.login.domain;

import backend.greatjourney.domain.community.entity.Posting;
import backend.greatjourney.domain.login.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String email;

    private String password;

    private String residence;

    private boolean gender;

    //양방향 하게 되면 쓸라고 달아둠 일단 쓸 수도?
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<Posting> postings;


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String phone;


    private boolean sns;

    @Enumerated(EnumType.STRING)
    private Role role;    // 권한

    private String profileImageUrl;

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    public void setPassword(String encode) {
    }
}
