package backend.greatjourney.domain.terms.repository;


import backend.greatjourney.domain.terms.domain.Terms;
import backend.greatjourney.domain.terms.domain.TermsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermsRepository extends JpaRepository<Terms, Long> {
    // 특정 타입의 약관 중 가장 최신 버전을 조회 (적용일과 버전으로 정렬)
    Optional<Terms> findFirstByTypeOrderByEffectiveDateDescVersionDesc(TermsType type);
}