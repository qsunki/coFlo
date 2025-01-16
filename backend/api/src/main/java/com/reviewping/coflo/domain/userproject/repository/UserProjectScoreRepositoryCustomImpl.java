package com.reviewping.coflo.domain.userproject.repository;

import static com.reviewping.coflo.domain.user.entity.QGitlabAccount.gitlabAccount;
import static com.reviewping.coflo.domain.userproject.entity.QUserProject.userProject;
import static com.reviewping.coflo.domain.userproject.entity.QUserProjectScore.userProjectScore;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserProjectScoreRepositoryCustomImpl implements UserProjectScoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<UserProjectScore> findUserProjectScores(Long userId, Long projectId, int week) {
        return queryFactory
                .selectFrom(userProjectScore)
                .join(userProjectScore.userProject, userProject)
                .join(userProject.gitlabAccount, gitlabAccount)
                .where(
                        userProjectScore
                                .userProject
                                .project
                                .id
                                .eq(projectId)
                                .and(userProjectScore.week.eq(week))
                                .and(gitlabAccount.user.id.eq(userId)))
                .fetch();
    }

    @Override
    public List<UserProjectScore> findTopUserProjectScores(
            Long userId, Long projectId, int week, int limit) {
        List<Long> topUserProjectIds =
                queryFactory
                        .select(userProjectScore.userProject.id)
                        .from(userProjectScore)
                        .join(userProjectScore.userProject, userProject)
                        .join(userProject.gitlabAccount, gitlabAccount)
                        .where(
                                userProject
                                        .project
                                        .id
                                        .eq(projectId)
                                        .and(userProjectScore.week.eq(week))
                                        .and(gitlabAccount.user.id.ne(userId)))
                        .groupBy(userProjectScore.userProject.id)
                        .orderBy(userProjectScore.totalScore.sum().desc())
                        .limit(limit)
                        .fetch();

        if (topUserProjectIds.isEmpty()) {
            return Collections.emptyList();
        }

        return queryFactory
                .selectFrom(userProjectScore)
                .join(userProjectScore.userProject, userProject)
                .fetchJoin()
                .where(
                        userProjectScore
                                .userProject
                                .id
                                .in(topUserProjectIds)
                                .and(userProjectScore.week.eq(week)))
                .fetch();
    }
}
