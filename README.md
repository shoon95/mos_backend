# MOS_BACKEND


## Demo
![ezgif com-animated-gif-maker](https://github.com/user-attachments/assets/7cdd35df-ce20-4dd8-bb03-c91381f5f295)


## 💡 기술 스택

- ![Java](https://img.shields.io/badge/Java17-%23ED8B00.svg?style=square&logo=openjdk&logoColor=white) <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=square&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=square&logo=Spring Security&logoColor=white"> ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=square&logo=Spring&logoColor=white) <br>
![JWT](https://img.shields.io/badge/JWT-black?style=square&logo=JSON%20web%20tokens) ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=square&logo=Gradle&logoColor=white)
- ![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?style=square&logo=PostgreSQL&logoColor=white) ![Rds](https://img.shields.io/badge/AWS%20RDS-527fff.svg?style=square&logo=amazonrds&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-FF4438.svg?style=square&logo=amazonrds&logoColor=white)
- <img src="https://img.shields.io/badge/Docker-%230db7ed.svg?style=square&logo=docker&logoColor=white">  ![AWS-ec2](https://img.shields.io/badge/AWS%20EC2-FF9900.svg?style=square&logo=amazonec2&logoColor=white) ![AWS ALB](https://img.shields.io/badge/AWS%20ALB-8c4fff.svg?style=square&logo=awselasticloadbalancing&logoColor=white) ![AWS Action](https://img.shields.io/badge/Git%20Action-2088ff.svg?style=square&logo=githubactions&logoColor=white)
- ![GitHub](https://img.shields.io/badge/Github-%23121011.svg?style=square&logo=github&logoColor=white)  ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?style=square&logo=intellij-idea&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=square&logo=postman&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=square&logo=notion&logoColor=white)


  
<br> <br/>
## 📘 프로젝트 개요
#### 🚚 [프로젝트 노션 바로가기](https://fir-turkey-016.notion.site/MOS-19de2b2fe1ba80b5950df271276f45fc?pvs=4)
### 프로젝트 소개
* 스터디를 구하는 것부터 스터디 진행 및 관리를 한 곳에서!
* [기획 링크](https://shoon95.tistory.com/10)


<br> <br/>
## 🗺️ 인프라 설계도
![image](https://github.com/user-attachments/assets/d90ffbdb-32d7-4c34-8636-480d1ca5ee20)


<br> <br/>
## 🪄 나의 개발 기능
#### 🚚 [API 명세서 보러가기!](https://fir-turkey-016.notion.site/API-1aae2b2fe1ba8069ae79dba9ce6a190e?pvs=4)

> 프로젝트 전체 기획
- 모든 페이지 및 기능 직접 기획
- 모든 페이지 샘플 용(Demo) 프론트 엔드개발(Svelte)

> RestAPI 개발
- 스터디 도메인 CRUD
- 좋아요
- 스터디 혜택 CRUD
- 스터디 지원 질문 CRUD
- 스터디 커리큘럼 CRUD
- 스터디 규칙 CRUD
- 스터디 지원 자격 CRUD
- 파일 로그
- 알림 로그

> 알림 서비스
- FCM 기반  Web Push Notification

> 이벤트 드리븐 아키텍처 설계
- 핵심 도메인과 하위 도메인 의존성을 이벤트로 분리
- 비동기 파일 업로드, 인기글 갱신 등 트랜잭션 단위가 다른 작업을 이벤트로 처리
- handler/dispatcher 타입을 통해 확장성 있는 이벤트 처리 설계

> 인기글 서비스
- Redis Sorted Set을 활용해 인기글 순위 제공
- Event 기반 처리
- 팩토리 메서드 패턴 적용

> 조회수 처리
- IP 기반 조회수 어뷰징 방지
- Redis를 사용하여 RDB 접근 최소화
- 원자성 쿼리를 통한 비관적 락으로 동시성 문제 해결 (테스트 시 낙관적락과 동일 성능)

> 파일 업로드 기능
- S3 업로드 동기 | 비동기로 나누어 개발
- 모집 글 작성 페이지에서 이미지 drag & drop 시 동기로 S3 업로드 후 경로 반환
- 모집 글 작성 완료 시, 직렬화된 HTML과 이미지 경로 매핑 후 영구 저장 폴더로 이동, 사용되지 않는 이미지는 바로 삭제
- 자료실에서 파일 업로드 시, 서버에서 전달 받은 파일 임시 저장 후 S3 비동기 업로드

> CICD + 테스트 환경 격리
- Github Action + AWS CodeDeploy를 활용한 CICD 구축
- Docker 를 사용하여 Container 이미지 관리
- 테스트 자동화 과정에서 Testcontainers 도입하여 격리된 테스트 환경 적용 

<br> <br/>
## 💬 기술적 의사결정
### [인기글 구현](https://shoon95.tistory.com/15)
> Redis Sorted Set을 사용하여 인기글 구현
### [조회 수 어뷰징 방지 구현](https://shoon95.tistory.com/14)
> Redis + IP 기반 조회 수 어뷰징 방지
### [게시글 목록 조회 성능 개선](https://shoon95.tistory.com/12)
> 커버링 인덱스를 적용하여 페이징 쿼리 최적화를 통한 10배 성능 개선


<br> <br/>
## 🚨 트러블슈팅
### [JPA 테스트 시 TransactionRequiredException](https://shoon95.tistory.com/13)
> Spring Data JPA에서 Transaction 자동 적용과 범위
### [스프링 제네릭 이벤트와 타입 소거: ResolvableType으로 해결](https://shoon95.tistory.com/16)
> 런타임 시 제네릭 이벤트 타입 소거, ResolvableType으로 구체 타입 확인
### [Testcontainers 환경에서 커넥션 유실 문제]()
> mysql과 hibernate 사이의 커넥션 설정 충돌로 인한 커넥션 유실 문제


