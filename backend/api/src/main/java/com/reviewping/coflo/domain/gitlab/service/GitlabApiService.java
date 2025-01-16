package com.reviewping.coflo.domain.gitlab.service;

import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.gitlab.controller.dto.response.GitlabProjectPageResponse;
import com.reviewping.coflo.domain.gitlab.controller.dto.response.GitlabProjectResponse;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabBranchContent;
import com.reviewping.coflo.global.client.gitlab.response.GitlabProjectSearchContent;
import com.reviewping.coflo.global.client.gitlab.response.GitlabProjectSearchContent.GitlabProjectSimpleContent;
import com.reviewping.coflo.global.util.GraphQlUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GitlabApiService {

    private final GitLabClient gitLabClient;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final UserRepository userRepository;

    public GitlabProjectPageResponse getGitlabProjects(
            Long userId, GitlabSearchRequest gitlabSearchRequest) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(userId);
        GitlabProjectSearchContent gitlabProjectPage =
                gitLabClient.searchGitlabProjects(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        gitlabSearchRequest);

        List<GitlabProjectResponse> gitlabProjects =
                buildGitlabProjectResponses(gitlabProjectPage, gitlabAccount);

        return new GitlabProjectPageResponse(gitlabProjects, gitlabProjectPage.pageInfo());
    }

    public List<String> getGitlabProjectBranches(Long userId, Long gitlabProjectId) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(userId);
        List<GitlabBranchContent> allBranches =
                gitLabClient.getAllBranchNames(
                        gitlabAccount.getDomain(), gitlabAccount.getUserToken(), gitlabProjectId);
        allBranches.sort(
                (branch1, branch2) -> {
                    if (Boolean.TRUE.equals(branch1.isDefault())
                            && Boolean.FALSE.equals(branch2.isDefault())) {
                        return -1;
                    } else if (Boolean.FALSE.equals(branch1.isDefault())
                            && Boolean.TRUE.equals(branch2.isDefault())) {
                        return 1;
                    }
                    return Boolean.compare(
                            Boolean.TRUE.equals(branch2.isProtected()),
                            Boolean.TRUE.equals(branch1.isProtected()));
                });

        return allBranches.stream().map(GitlabBranchContent::name).collect(Collectors.toList());
    }

    public Boolean validateUserToken(String domain, String userToken) {
        try {
            gitLabClient.getUserInfo(domain, userToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean validateBotToken(Long userId, Long gitlabProjectId, String botToken) {
        try {
            User user = userRepository.getById(userId);
            gitLabClient.getSingleProject(
                    user.getGitlabAccounts().getFirst().getDomain(), botToken, gitlabProjectId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private List<GitlabProjectResponse> buildGitlabProjectResponses(
            GitlabProjectSearchContent gitlabProjects, GitlabAccount gitlabAccount) {
        return gitlabProjects.nodes().stream()
                .map(project -> createGitlabProjectResponse(project, gitlabAccount.getId()))
                .toList();
    }

    private GitlabProjectResponse createGitlabProjectResponse(
            GitlabProjectSimpleContent content, Long gitlabAccountId) {
        return projectRepository
                .findByGitlabProjectId(GraphQlUtil.extractIdFromId(content.id()))
                .map(
                        project -> {
                            boolean isLinked = isProjectLinked(gitlabAccountId, project.getId());
                            return GitlabProjectResponse.ofLinkable(content, isLinked);
                        })
                .orElseGet(() -> GitlabProjectResponse.ofNonLinkable(content));
    }

    private boolean isProjectLinked(Long gitlabAccountId, Long projectId) {
        return userProjectRepository.existsByGitlabAccountIdAndProjectId(
                gitlabAccountId, projectId);
    }
}
