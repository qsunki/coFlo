package com.reviewping.coflo.domain.badge.service;

import static com.reviewping.coflo.domain.badge.entity.BadgeType.*;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.badge.repository.BadgeCodeRepository;
import com.reviewping.coflo.domain.badge.repository.UserBadgeRepository;
import com.reviewping.coflo.domain.customprompt.repository.PromptHistoryRepository;
import com.reviewping.coflo.domain.mergerequest.repository.BestMrHistoryRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.LoginHistoryRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectScoreRepository;
import com.reviewping.coflo.global.util.ProjectDateUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeEventService {

    private static final long PROJECT_LINK_TARGET_COUNT = 20L;
    private static final long LOGIN_TARGET_COUNT = 100L;
    private static final long PROMPT_UPDATE_TARGET_COUNT = 100L;
    private static final int PERCENT = 3;
    private static final long AI_REWARD_TARGET_SCORE = 55L;
    private static final long BEST_MERGE_REQUEST_TARGET_COUNT = 50L;
    private static final int TOTAL_BADGES = 8;
    private static final Random random = new Random();

    private final UserRepository userRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final BadgeCodeRepository badgeCodeRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final UserProjectRepository userProjectRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final PromptHistoryRepository promptHistoryRepository;
    private final BestMrHistoryRepository bestMrHistoryRepository;
    private final UserProjectScoreRepository userProjectScoreRepository;
    private final ProjectDateUtil projectDateUtil;

    private UserBadge userBadge;
    private BadgeCode badgeCode;

    // 첫 모험가 - 처음 서비스 가입 시 획득
    public void eventFirstLogin(User user) {
        badgeCode = badgeCodeRepository.getById(FIRST_ADVENTURER.getId());
        if (userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) return;

        Integer count = gitlabAccountRepository.countByUser(user);
        if (count == 0) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
        }
    }

    // 프로젝트 마스터 - 연동한 프로젝트 개수 n개 이상 시 획득
    public void eventProjectLinkAchievement(User user) {
        badgeCode = badgeCodeRepository.getById(PROJECT_MASTER.getId());
        if (userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) return;

        List<GitlabAccount> gitlabAccounts = gitlabAccountRepository.findAllByUser(user);
        List<Long> gitlabAccountIds = gitlabAccounts.stream().map(GitlabAccount::getId).toList();
        Long projectCount = userProjectRepository.countByGitlabAccountIds(gitlabAccountIds);

        if (projectCount == PROJECT_LINK_TARGET_COUNT) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
            eventAllBadgeUnlocked(user);
        }
    }

    // 단골 손님 - 서비스 로그인 n회 이상 (1일 1회)
    public void eventLoginCount(User user) {
        badgeCode = badgeCodeRepository.getById(REGULAR_CUSTOMER.getId());
        if (userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) return;

        long loginCount = loginHistoryRepository.countByUserId(user.getId());
        if (loginCount >= LOGIN_TARGET_COUNT) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
            eventAllBadgeUnlocked(user);
        }
    }

    // 프롬프트 창조자 - 커스텀 프롬프트 수정 n회 이상
    public void eventUpdateCustomPrompt(User user) {
        long promptCount = promptHistoryRepository.countByUserId(user.getId());
        if (promptCount == PROMPT_UPDATE_TARGET_COUNT) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
            eventAllBadgeUnlocked(user);
        }
    }

    // 행운의 발견 - 접속 시 1% 확률로 랜덤 획득
    @Transactional
    public void eventRandom(User user) {
        int value = random.nextInt(100);
        if (value >= PERCENT) return;

        badgeCode = badgeCodeRepository.getById(LUCKY_FIND.getId());
        if (!userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
            eventAllBadgeUnlocked(user);
        }
    }

    // 정복자 - 베스트 MR에 n회 이상 선정 시 획득
    @Transactional
    public void eventBestMrCount() {
        // BestMrHistory가 N회 이상인 사용자 ID 조회
        List<Long> userIds =
                bestMrHistoryRepository.findUsersWithAtLeastNHistory(
                        BEST_MERGE_REQUEST_TARGET_COUNT);

        List<Long> newBadgeUserIds =
                userBadgeRepository.findUserIdsWithoutBadge(userIds, CONQUEROR.getId());

        badgeCode = badgeCodeRepository.getById(CONQUEROR.getId());

        List<User> users = userRepository.findAllByIds(newBadgeUserIds);
        List<UserBadge> userBadges =
                users.stream().map(user -> UserBadge.of(user, badgeCode)).toList();

        userBadgeRepository.saveAll(userBadges);
        users.forEach(this::eventAllBadgeUnlocked);
    }

    // 코드 마스터 - AI 리뷰평가 리워드 합이 n점 이상 시 획득
    @Transactional
    public void eventAiRewardScore() {
        badgeCode = badgeCodeRepository.getById(CODE_MASTER.getId());
        userProjectRepository
                .findAll()
                .forEach(userProject -> processUserProject(userProject, badgeCode));
    }

    // 리뷰 탐색자 - 첫 AI 리뷰 재생성
    public void eventFirstAiReviewRegenerate(User user) {
        badgeCode = badgeCodeRepository.getById(REVIEW_FINDER.getId());
        if (!userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
            eventAllBadgeUnlocked(user);
        }
    }

    // 전설의 모험가 - 모든 뱃지를 획득한 경우 획득
    private void eventAllBadgeUnlocked(User user) {
        if (userBadgeRepository.countByUser(user) == TOTAL_BADGES) {
            badgeCode = badgeCodeRepository.getById(LEGEND_ADVENTURER.getId());
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
        }
    }

    private void processUserProject(UserProject userProject, BadgeCode badgeCode) {
        Project project = userProject.getProject();
        User user = userProject.getGitlabAccount().getUser();

        int week =
                projectDateUtil.calculateWeekNumber(
                        project.getCreatedDate().toLocalDate(), LocalDate.now());

        long totalScore = calculateTotalScore(userProject, week - 1);
        if (totalScore >= AI_REWARD_TARGET_SCORE
                && !userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
            eventAllBadgeUnlocked(user);
        }
    }

    private long calculateTotalScore(UserProject userProject, int week) {
        return userProjectScoreRepository.findAllByUserProjectAndWeek(userProject, week).stream()
                .mapToLong(UserProjectScore::getTotalScore)
                .sum();
    }
}
