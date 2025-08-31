package backend.greatjourney.domain.terms.repository;

import backend.greatjourney.domain.terms.domain.UserTermsAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTermsAgreementRepository extends JpaRepository<UserTermsAgreement, Long> {
    // 필요시 특정 유저의 동의 내역을 찾는 메서드 등을 추가할 수 있습니다.
    // boolean existsByUserAndTerms(User user, Terms terms);
}
