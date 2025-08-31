package backend.greatjourney.domain.terms.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Enum 이름을 DB에 문자열로 저장
    @Column(nullable = false)
    private TermsType type; // 약관 종류

    @Column(nullable = false)
    private String version; // 버전 (예: "1.0", "1.1")

    @Lob // CLOB, TEXT 등 DB의 대용량 텍스트 타입과 매핑
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 약관 내용 (HTML)

    @Column(nullable = false)
    private boolean isRequired; // 필수 동의 여부

    private String effectiveDate;

}