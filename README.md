<div align="center">
  <h1 style="display: flex; align-items: center; justify-content: center; width: 100%;">
    <img src="./resources/img/logo.png" width="22px" style="margin-right: 15px"/> 코플로, CoFlo
  </h1>
  <img src="./resources/img/main.png"/>
  <h3>🏆 SSAFY 11기 자율 프로젝트 우수상 수상 🏆</h3>
  <h4>AI 기반 맞춤형 코드 리뷰 플랫폼</h4>
  <p>2024.10.14 ~ 2024.11.19</p>  
    
  <a href="https://www.coflo.co.kr/">코플로</a>
  &nbsp; | &nbsp; 
  <a href="https://jmxx219.notion.site/115ced9ff3788076bdd1d0c94a1ecf18?pvs=74">리뷰핑 팀 Notion</a>
  &nbsp; | &nbsp; 
   <a href="exec/최종발표.pdf">발표 PPT</a>
</div>

<br>

## 목차

1. [개요](#개요)
2. [핵심 기능 소개](#기능-소개)
3. [서비스 소개](#서비스-소개)
4. [프로젝트 설계](#프로젝트-설계)
5. [개발 환경 및 기술 스택](#개발-환경-및-기술-스택)
6. [팀원 소개](#팀원-소개)

<br/>

## [개요](#목차)

코드리뷰는 개발자가 가장 선호하는 개발문화입니다. 장점이 많지만 현실적인 문제에 부딫혀 제대로 하지 쉽지 않습니다. 많은 양의 코드리뷰를 하는 것, 리뷰를 기다리는 것 모두 개발 속도를 늦추는 원인이 되기 때문입니다. 그렇기에 이런 시간적 비용을 단축시키고 개발자의 편의를 돕기 위해 맞춤형 코드 분석을 통해 코드 리뷰를 작성하는 AI 리뷰어, coFlo를 기획하게 되었습니다.

<br>
 
### **코플로란?**

code, flow가 합쳐진 합성어로 코드가 물 흐르듯 자연스럽게 리뷰되고 개선되는 것을 표현했습니다.

<br>
<br>

## [핵심 기능 소개](#목차)

### 1. AI 코드 리뷰

- **프로젝트 문맥**을 반영한 코드 리뷰: 프로젝트 코드를 메소드, 함수 단위로 청크하여 저장, 리뷰 생성 시 RAG의 검색데이터로 활용
- **커스텀 프롬프트** 설정: 원하는 리뷰 생성을 위한 프롬프트 수정가능

<br>

### 2. 코드 리뷰 데이터

- **사용자 코드 평가 지표**(6가지): 가독성, 일관성, 재사용성, 신뢰성, 보안성, 유지보수성에 따른 AI의 MR평가

- **주차별 개인 성장 그래프:** 6가지 지표에 대해 주간 점수 통계

- **코드 뱃지 획득:** 도전과제 달성 시 귀여운 뱃지 획득

- **주간 최고 MR:** AI가 평가한 좋은 MR TOP3

<br/>
<br/>

## [서비스 소개](#목차)

### 1️⃣ <b>로그인 페이지</b>

|                      **Lading Page**                      |
| :-------------------------------------------------------: |
| <img src="./resources/gif/landing_page.gif" height="400"> |

|                     **Login Page**                      |                     **SignUp Page**                      |
| :-----------------------------------------------------: | :------------------------------------------------------: |
| <img src="./resources/gif/login_page.gif" height="400"> | <img src="./resources/gif/signup_page.gif" height="400"> |

<br>

### 2️⃣ <b>메인 페이지</b>

> 프로젝트 팀 정보와 함께 6가지 지표에 따른 팀 육각형 그래프와 개인 성장 그래프를 조회할 수 있다.

|                     **Main Page**                      |
| :----------------------------------------------------: |
| <img src="./resources/gif/main_page.gif" height="400"> |

|                       **Project Page**                        |                       **Project Page**                        |
| :-----------------------------------------------------------: | :-----------------------------------------------------------: |
| <img src="./resources/gif/repository_start.gif" height="400"> | <img src="./resources/gif/repository_token.gif" height="400"> |

<br>

### 3️⃣ <b>MR 페이지</b>

> MR 리스트를 조회하고, AI 리뷰를 조회하고 재생성한다.

|             **Merge Request Search Page**              |                        **Review Page**                         |
| :----------------------------------------------------: | :------------------------------------------------------------: |
| <img src="./resources/gif/mr_search.gif" height="400"> | <img src="./resources/gif/review_regenerate.gif" height="400"> |

<br>

### 4️⃣ <b>커스텀 프롬프트 페이지</b>

> AI 리뷰 생성을 위한 커스텀 프롬프트를 등록한다.

|                   **Custom Prompt Page**                   |
| :--------------------------------------------------------: |
| <img src="./resources/gif/custom_prompt.gif" height="400"> |

<br>

### 5️⃣ <b>뱃지 & 설정 페이지</b>

|                    **My Badge Page**                    |                     **Setting Page**                      |
| :-----------------------------------------------------: | :-------------------------------------------------------: |
| <img src="./resources/gif/badge_page.gif" height="400"> | <img src="./resources/gif/setting_page.gif" height="400"> |

<br/>
<br/>

## [프로젝트 설계](#목차)

### 시스템 아키텍쳐

<img width="600px" src="./resources/img/아키텍쳐.png">

<br/>

### ERD

<img width="600px" src="./resources/img/ERD.png">

<br>

### [코플로 API 명세서](./resources/img/api_specification.png)

<br/>

### [리뷰핑팀 JIRA](./resources/img/jira)

<br/>
<br/>

## [개발 환경 및 기술 스택](#목차)

|      개발 환경      | 기술 스택                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| :-----------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|    **Frontend**     | ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white) ![CSS](https://img.shields.io/badge/CSS-1572b6?style=for-the-badge&logo=css3&logoColor=white) ![react](https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=white) ![typescript](https://img.shields.io/badge/typescript-3178C6?style=for-the-badge&logo=typescript&logoColor=white) ![axios](https://img.shields.io/badge/axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white) ![tailwind](https://img.shields.io/badge/tailwind-css-06B6D4.svg?style=for-the-badge&logo=tailwindcss&logoColor=white) ![jotai](https://img.shields.io/badge/jotai-000000?style=for-the-badge&logo=&logoColor=white)                                                                                                                                                                                                                                                                                                        |
|     **Backend**     | ![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) ![Spring Batch](https://img.shields.io/badge/Spring_Batch-609926?style=for-the-badge&logo=spring&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-1E8E3E?style=for-the-badge&logo=spring-security&logoColor=white) ![OAuth2.0](https://img.shields.io/badge/OAuth2.0-575757?style=for-the-badge&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-02B78F?style=for-the-badge&logo=Spring_Data_JPA&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white) ![GraphQL](https://img.shields.io/badge/GraphQL-E10098?style=for-the-badge&logo=graphql&logoColor=white) |
|       **DB**        | ![PostgreSQL](https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white) ![redis](https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
|       **AI**        | ![spring](https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![openai](https://img.shields.io/badge/openai-412991?style=for-the-badge&logo=openai&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
|      **Infra**      | ![amazonec2](https://img.shields.io/badge/amazon_ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white) ![docker](https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![Nginx Proxy Manager](https://img.shields.io/badge/Nginx%20Proxy%20Manager-F15833?style=for-the-badge&logo=nginxproxymanager&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
|   **Monitoring**    | ![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white) ![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=Grafana&logoColor=white) ![Promtail](https://img.shields.io/badge/Promtail-FF9C42?style=for-the-badge&logoColor=white) ![Loki](https://img.shields.io/badge/Loki-F9DC3E?style=for-the-badge&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| **Management Tool** | ![Jira](https://img.shields.io/badge/jira-0052CC?style=for-the-badge&logo=jira&logoColor=white) ![Gitlab](https://img.shields.io/badge/GitLab-FC6D26?style=for-the-badge&logo=GitLab&logoColor=white) ![Mattermost](https://img.shields.io/badge/mattermost-001A4B?style=for-the-badge&logo=mattermost&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000.svg?style=for-the-badge&logo=notion&logoColor=white) ![Figma](https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white) ![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                   |

<br/>
<br/>

## [팀원 소개](#목차)

<table align="center">
  <tr>
    <th style="text-align: center;"><a href="https://github.com/anjs124">구승석</a></th>
    <th style="text-align: center;"><a href="https://github.com/btothey99">이보연</a></th>
    <th style="text-align: center;"><a href="https://github.com/hyooun">유승현</a></th>
  </tr>
  <tr>
    <td style="text-align: center;"><img src="./resources/img/member/구승석.png" alt="" width="150px"/></td>
    <td style="text-align: center;"><img src="./resources/img/member/이보연.png" alt="" width="150px"/></td>
    <td style="text-align: center;"><img src="./resources/img/member/유승현.png" alt="" width="150px" /></td>
  </tr>
  <tr>
    <td style="text-align: center;"><b>Frontend</b></td>
    <td style="text-align: center;"><b>Frontend</b></td>
    <td style="text-align: center;"><b>Infra/Backend</b></td>
  </tr>
</table>

<table align="center">
  <tr>
    <th style="text-align: center;"><a href="https://github.com/jmxx219">손지민</a></th>
    <th style="text-align: center;"><a href="https://github.com/fkgnssla">김형민</a></th>
    <th style="text-align: center;"><a href="https://github.com/qsunki">홍선기</a></th>
  </tr>
  <tr>
    <td style="text-align: center;"><img src="./resources/img/member/손지민.png" alt="" width="150px" /></td>
    <td style="text-align: center;"><img src="./resources/img/member/김형민.png" alt="" width="150px"/></td>
    <td style="text-align: center;"><img src="./resources/img/member/홍선기.png" alt="" width="150px" /></td>
  </tr>
  <tr>
    <td style="text-align: center;"><b>Backend</b></td>
    <td style="text-align: center;"><b>Backend</b></td>
    <td style="text-align: center;"><b>Backend/AI</b></td>
  </tr>
</table>

<br>
