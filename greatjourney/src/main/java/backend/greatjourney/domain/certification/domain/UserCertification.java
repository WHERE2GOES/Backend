package backend.greatjourney.domain.certification.domain;

import backend.greatjourney.domain.course.domain.Place;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_certification")
public class UserCertification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_center_id")
    private Place certificationCenter;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime certifiedAt;

    @Column(nullable = false)
    private Integer courseId;

    @Builder
    public UserCertification(User user, Place certificationCenter, Integer courseId) {
        this.user = user;
        this.certificationCenter = certificationCenter;
        this.courseId = courseId;
    }
}