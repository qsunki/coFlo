package com.reviewping.coflo.domain.project.entity;

import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.webhookchannel.entity.WebhookChannel;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import com.reviewping.coflo.global.converter.CryptoConverter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = CryptoConverter.class)
    @Column(nullable = false, unique = true)
    private String botToken;

    @Column(nullable = false, unique = true)
    private Long gitlabProjectId;

    private String name;

    private String gitUrl;

    private String fullPath;

    @OneToMany(mappedBy = "project")
    private List<UserProject> userProjects = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<MrInfo> mrInfos = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<WebhookChannel> webhookChannels = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Branch> branches = new ArrayList<>();

    @Builder
    public Project(
            String botToken, Long gitlabProjectId, String name, String gitUrl, String fullPath) {
        this.botToken = botToken;
        this.gitlabProjectId = gitlabProjectId;
        this.name = name;
        this.gitUrl = gitUrl;
        this.fullPath = fullPath;
    }

    public void addBranch(Branch branch) {
        branches.add(branch);
    }
}
