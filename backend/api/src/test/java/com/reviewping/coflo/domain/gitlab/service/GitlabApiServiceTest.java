// package com.reviewping.coflo.domain.gitlab.service;
//
// import static com.reviewping.coflo.global.error.ErrorCode.USER_GITLAB_ACCOUNT_NOT_EXIST;
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.mock;
//
// import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabSearchRequest;
// import com.reviewping.coflo.domain.gitlab.controller.dto.response.GitlabProjectPageResponse;
// import com.reviewping.coflo.domain.project.entity.Project;
// import com.reviewping.coflo.domain.project.repository.ProjectRepository;
// import com.reviewping.coflo.domain.user.entity.GitlabAccount;
// import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
// import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
// import com.reviewping.coflo.global.client.gitlab.GitLabClient;
// import com.reviewping.coflo.global.client.gitlab.response.GitlabProjectDetailContent;
// import com.reviewping.coflo.global.client.gitlab.response.GitlabProjectPageContent;
// import com.reviewping.coflo.global.common.entity.PageDetail;
// import com.reviewping.coflo.global.error.exception.BusinessException;
// import java.util.List;
// import java.util.Optional;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// @ExtendWith(MockitoExtension.class)
// class GitlabApiServiceTest {
//
//     @Mock private GitLabClient gitLabClient;
//     @Mock private ProjectRepository projectRepository;
//     @Mock private UserProjectRepository userProjectRepository;
//     @Mock private GitlabAccountRepository gitlabAccountRepository;
//     @InjectMocks private GitlabApiService gitlabApiService;
//
//     @Test
//     @DisplayName("프로젝트를 연동할 수 없는 경우, isLinkable은 false이다.")
//     public void testProjectNotLinked() {
//         // given
//         Long userId = 1L;
//         GitlabSearchRequest searchRequest = new GitlabSearchRequest("", 1, 10);
//         GitlabAccount gitlabAccount = mock(GitlabAccount.class);
//
//         given(gitlabAccountRepository.getFirstByUserId(userId)).willReturn(gitlabAccount);
//         given(gitLabClient.searchGitlabProjects(any(), any(), any()))
//                 .willReturn(createGitlabProjectPageContent());
//         given(projectRepository.findByGitlabProjectId(anyLong())).willReturn(Optional.empty());
//
//         // when
//         GitlabProjectPageResponse response =
//                 gitlabApiService.getGitlabProjects(userId, searchRequest);
//
//         // then
//         assertThat(response.gitlabProjectList()).hasSize(1);
//         assertThat(response.gitlabProjectList().getFirst().isLinkable()).isFalse();
//     }
//
//     @Test
//     @DisplayName("연동한 프로젝트의 경우, isLink가 true이다.")
//     public void testProjectLinked() {
//         // given
//         Long userId = 1L;
//         GitlabSearchRequest searchRequest = new GitlabSearchRequest("", 1, 10);
//         GitlabAccount gitlabAccount = mock(GitlabAccount.class);
//         Project project = mock(Project.class);
//
//         given(gitlabAccountRepository.getFirstByUserId(userId)).willReturn(gitlabAccount);
//         //        given(user.getGitlabAccounts()).willReturn(List.of(gitlabAccount));
//         given(gitlabAccount.getId()).willReturn(1L);
//         given(project.getId()).willReturn(1L);
//         given(gitLabClient.searchGitlabProjects(any(), any(), any()))
//                 .willReturn(createGitlabProjectPageContent());
//
//
// given(projectRepository.findByGitlabProjectId(anyLong())).willReturn(Optional.of(project));
//         given(userProjectRepository.existsByGitlabAccountIdAndProjectId(anyLong(), anyLong()))
//                 .willReturn(true);
//
//         // when
//         GitlabProjectPageResponse response =
//                 gitlabApiService.getGitlabProjects(userId, searchRequest);
//
//         // then
//         assertThat(response.gitlabProjectList()).hasSize(1);
//         assertThat(response.gitlabProjectList().getFirst().isLinked()).isTrue();
//     }
//
//     @Test
//     @DisplayName("조회하려는 유저의 깃랩 계정이 존재하지 않으면 예외가 발생한다.")
//     public void testUserGitlabAccountNotExist() {
//         // given
//         Long userId = 1L;
//         given(gitlabAccountRepository.getFirstByUserId(userId))
//                 .willThrow(new BusinessException(USER_GITLAB_ACCOUNT_NOT_EXIST));
//
//         // when & then
//         assertThatThrownBy(
//                         () ->
//                                 gitlabApiService.getGitlabProjects(
//                                         userId, any(GitlabSearchRequest.class)))
//                 .isInstanceOf(BusinessException.class)
//                 .hasMessageContaining(USER_GITLAB_ACCOUNT_NOT_EXIST.getMessage());
//     }
//
//     private GitlabProjectPageContent createGitlabProjectPageContent() {
//         GitlabProjectDetailContent detailContent =
//                 new GitlabProjectDetailContent(123L, "Test Project", "Description", "GIT URL");
//         return new GitlabProjectPageContent(List.of(detailContent), PageDetail.of(1L, 1, true,
// 1));
//     }
// }
