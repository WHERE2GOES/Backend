package backend.greatjourney.domain.terms.domain;


import backend.greatjourney.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // @CreatedDate 자동 생성을 위해 필요
public class UserTermsAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", nullable = false)
    private Terms terms;

    @CreatedDate // 엔티티가 생성될 때 시간이 자동으로 저장됩니다.
    @Column(nullable = false, updatable = false)
    private LocalDateTime agreedAt; // 동의한 시간

    @Builder
    public UserTermsAgreement(User user, Terms terms) {
        this.user = user;
        this.terms = terms;
    }
}
