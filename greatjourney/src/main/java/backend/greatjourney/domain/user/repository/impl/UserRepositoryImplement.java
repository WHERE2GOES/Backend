package backend.greatjourney.domain.user.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import backend.greatjourney.domain.user.entity.QUser;
import backend.greatjourney.domain.user.repository.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImplement implements UserRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	QUser quesr = QUser.user;

	@Override
	public boolean existsByEmail(String email){
		return queryFactory
			.selectOne()
			.from(quesr)
			.where(
				quesr.email.eq(email)
			)
			.fetchFirst() != null;
	}


}
