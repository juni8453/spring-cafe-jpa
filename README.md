#### 배포url : http://3.36.169.168/
---
### `CodeSquad 3번쩨 프로젝트` ⚒
#### 
- `기간` : 2022.05.09 ~ 2022.05.20
- `팀 멤버` : BC, Tany (BE / 2)
- `기술 스택` : Spring Boot, MySQL, Spring Data JPA, Boot Strap, Git, AWS EC2, Docker, React
---

#### `Git Flow`
- 개발할 기능이 생기면 `issue` 를 작성한 뒤 feature(개발), fix(수정) 브랜치를 생성해 개발을 진행한다.
- 개발이 끝난 브랜치는 depoly 브랜치로 머지한 뒤 AWS 서버에 배포한다.
1. `feat` - 새 기능 추가
2. `fix` - 버그 수정
3. `docs` - 문서 수정
4. `test` - 테스트 코드 추가
5. `refact` - 코드 리팩터링
6. `chore` - 간단한 수정 (src, test 파일 변동 X)

``` text
main
 | - deploy (배포)
 | - develop-FE (개발)
 |     |
 |     | - feature (issue 에 맞춰 생성)
 |     | - fix
 |
 | - develop-BE (개발)
       |
       | - feature (issue 에 맞춰 생성)
       | - fix
      
```
---
#### `Template`
1. `issue Template`
```text
제목: [FEAT] (BE) 기능제목
---

## 💡 issue
CollectionView 구현

## 📝 todo
[ ] 작업1
[ ] 작업2
[ ] 작업3
```


2. `commit Template`
- 제목에 이슈 번호 표기
```text
:sparkles: feat : 제목내용 (#1)

- 내용 1
- 내용 2
```

--- 

### ERD

![image](https://user-images.githubusercontent.com/92678400/167776725-8c85b43f-df0c-494c-8bf4-c0fd1bfc6e31.png)

---
### API 명세

| Method |       URI       | Description                 |
|:------:|:---------------:|-----------------------------|
| GET    | /articles/{page} | 지정된 페이지의 게시글 조회 |
| POST   |    /articles    | 게시글 추가                 |
| PATCH  | /articles/{id}  | 지정된 게시글 수정          |
| DELETE | /articles/{id}  | 지정된 게시글 삭제          |
|   ---  |       ---       |             ----            |
| POST   |    /members     | 가입                        |
| GET    |                 | ???                         |

* 검색 기능?
