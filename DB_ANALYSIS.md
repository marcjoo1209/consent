# DB_ANALYSIS.md

## 데이터베이스 분석 (동의서 관련 테이블)

### 연결 정보
- **데이터베이스**: allppou1 (MariaDB)
- **호스트**: cila1.cafe24.com:3306
- **사용자**: root

### 분석 대상 테이블

#### 1. CONSENT_INFO (메인 동의서 정보 테이블)
**레코드 수**: 37,563건

**핵심 필드**:
- **APT_CD** (varchar(5), NOT NULL) - 아파트 코드
- **FORM_CD** (varchar(4), NOT NULL) - 양식 코드
- **APT_DONG** (varchar(20), NOT NULL) - 아파트 동
- **APT_HO** (varchar(20), NOT NULL) - 아파트 호수

**사용자 정보** (최대 3명 + 추가 1명):
- **USER_NM1** (varchar(100), NOT NULL) - 주 사용자명
- **USER_BIRTH_DT1** (varchar(8)) - 주 사용자 생년월일
- **USER_NUM1** (varchar(50), NOT NULL) - 주 사용자 연락처
- **USER_NM11** (varchar(100)) - 추가 사용자명
- **USER_BIRTH_DT11** (varchar(8)) - 추가 사용자 생년월일
- **USER_NUM11** (varchar(50)) - 추가 사용자 연락처
- **USER_NM2, USER_NM3** - 2, 3번째 사용자 정보
- **EMRG_NUM** (varchar(50)) - 비상연락처

**개인정보 동의**:
- **AGREE_PRIVACY_YN** (varchar(1)) - 개인정보 동의 여부
- **AGREE_PRIVACY_YN2** (varchar(1)) - 개인정보 동의 여부 2

**주소 정보** (2세트):
- **POSTCODE1/2** (varchar(10)) - 우편번호
- **ROADADDRESS1/2** (varchar(100)) - 도로명주소
- **JIBUNADDRESS1/2** (varchar(100)) - 지번주소
- **DETAILADDRESS1/2** (varchar(100)) - 상세주소
- **EXTRAADDRESS1/2** (varchar(100)) - 참고항목

**파일 업로드**:
- **ORGFILENAME/ORGFILENAME2** (varchar(1000)) - 원본 파일명
- **SERFILENAME/SERFILENAME2** (varchar(1000)) - 서버 파일명
- **FILESIZE/FILESIZE2** (varchar(50)) - 파일 크기

**추가 정보**:
- **CYEAR1~5** (varchar(100)) - 연도 정보 (5개)
- **CNAME1~5** (varchar(100)) - 이름 정보 (5개)
- **RADIO1~10** (varchar(20)) - 라디오 버튼 선택값 (10개)
- **ETC1~10** (varchar(500)) - 기타 정보 (10개)

**관리 필드**:
- **USE_YN** (varchar(1)) - 사용 여부
- **JUN_YN** (varchar(1)) - 준공 여부
- **RGST_ID** (varchar(20)) - 등록자 ID
- **FST_RGST_DTM** (datetime) - 최초 등록일시
- **DELYN** (varchar(2)) - 삭제 여부
- **DELDTM** (datetime) - 삭제일시

#### 2. CONSENT_INFO_20200122 (백업/이력 테이블)
**레코드 수**: 103건

**특징**:
- CONSENT_INFO와 유사한 구조이나 일부 필드 누락
- APT_CD가 varchar(2)로 더 짧음
- USER_NM11, USER_BIRTH_DT11, USER_NUM11 필드 없음
- AGREE_PRIVACY_YN2 필드 없음
- 파일 관련 필드들 없음
- 2020년 1월 22일 백업으로 추정

#### 3. FORM_INFO (양식 정보 테이블)
**레코드 수**: 6건

**필드 구조**:
- **FORMCD** (varchar(2), PRIMARY KEY) - 양식 코드
- **FORMNM** (varchar(100), NOT NULL) - 양식명
- **USEYN** (varchar(1)) - 사용 여부
- **URL** (varchar(200)) - 관련 URL
- **TEAMCD** (varchar(4)) - 팀 코드
- **RGSTID** (varchar(20)) - 등록자 ID
- **FSTRGSTDTM** (date) - 최초 등록일
- **AUDITID** (varchar(20)) - 수정자 ID
- **AUDITDTM** (date) - 수정일

### 테이블 관계
- **CONSENT_INFO.FORM_CD** → **FORM_INFO.FORMCD** (양식 정보 참조)
- **CONSENT_INFO.APT_CD** - 아파트 코드로 아파트 정보와 연관

### 주요 특징
1. **다중 사용자 지원**: 한 세대당 최대 4명의 사용자 정보 저장 가능
2. **이중 주소**: 2개의 주소 정보 저장 (현재/이전 주소로 추정)
3. **파일 업로드**: 2개의 파일 첨부 가능
4. **유연한 확장**: ETC1~10, RADIO1~10 필드로 다양한 추가 정보 수용
5. **이력 관리**: 삭제 플래그와 삭제일시로 soft delete 지원
6. **백업 테이블**: 과거 데이터 보존을 위한 별도 테이블 존재

### 권장사항
1. **정규화 개선**: ETC1~10, RADIO1~10 같은 반복 필드는 별도 테이블로 분리 고려
2. **인덱스 추가**: APT_CD, FORM_CD, FST_RGST_DTM 등에 인덱스 추가 권장
3. **데이터 타입 최적화**: 날짜 필드는 DATE/DATETIME 타입 사용 권장
4. **제약조건 추가**: 외래키 제약조건으로 데이터 무결성 보장