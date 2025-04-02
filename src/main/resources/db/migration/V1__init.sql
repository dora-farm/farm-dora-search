-- 사용자
DROP TABLE IF EXISTS `user` RESTRICT;

-- SNS로그인
DROP TABLE IF EXISTS `sns` RESTRICT;

-- SNS타입(마스터)
DROP TABLE IF EXISTS `sns_type` RESTRICT;

-- 은행정보(마스터)
DROP TABLE IF EXISTS `bank_type` RESTRICT;

-- 판매상품옵션
DROP TABLE IF EXISTS `option` RESTRICT;

-- 상품타입(대분류)(마스터)
DROP TABLE IF EXISTS `option_type_big` RESTRICT;

-- 상품타입(소분류)(마스터)
DROP TABLE IF EXISTS `option_type` RESTRICT;

-- 판매상품
DROP TABLE IF EXISTS `sale` RESTRICT;

-- 배송지정보
DROP TABLE IF EXISTS `depot` RESTRICT;

-- 찜한상품(별도테이블)
DROP TABLE IF EXISTS `like` RESTRICT;

-- 주문정보
DROP TABLE IF EXISTS `order` RESTRICT;

-- 판매문의
DROP TABLE IF EXISTS `question` RESTRICT;

-- 판매자정보
DROP TABLE IF EXISTS `seller` RESTRICT;

-- 이벤트/팝업/배너(별도테이블)
DROP TABLE IF EXISTS `popup` RESTRICT;

-- 방송
DROP TABLE IF EXISTS `broadcast` RESTRICT;

-- 권한(마스터)
DROP TABLE IF EXISTS `auth` RESTRICT;

-- 상품이미지
DROP TABLE IF EXISTS `sale_file` RESTRICT;

-- 리뷰사진
DROP TABLE IF EXISTS `review_file` RESTRICT;

-- 환불/교환
DROP TABLE IF EXISTS `refund` RESTRICT;

-- 환불/교환사진
DROP TABLE IF EXISTS `refund_file` RESTRICT;

-- 환불/교환 타입(마스터)
DROP TABLE IF EXISTS `refund_type` RESTRICT;

-- 결제정보
DROP TABLE IF EXISTS `pay` RESTRICT;

-- 이벤트/팝업/배너 타입(마스터)
DROP TABLE IF EXISTS `popup_type` RESTRICT;

-- 주문상태 타입(마스터)
DROP TABLE IF EXISTS `order_status` RESTRICT;

-- 결제상태(마스터)
DROP TABLE IF EXISTS `pay_status` RESTRICT;

-- 장바구니(별도)회원용
DROP TABLE IF EXISTS `basket` RESTRICT;

-- 리뷰
DROP TABLE IF EXISTS `review` RESTRICT;

-- 사용자
CREATE TABLE `user`
(
    `user_id`      INTEGER  NOT NULL COMMENT '사용자번호',                           -- 사용자번호
    `id`           VARCHAR(50) NULL     COMMENT '일반로그인사용자아이디',                  -- 일반로그인사용자아이디
    `pwd`          VARCHAR(255) NULL     COMMENT '일반로그인비밀번호',                   -- 일반로그인비밀번호
    `name`         VARCHAR(50) NULL     COMMENT '이름',                           -- 이름
    `email`        VARCHAR(40) NULL     COMMENT '이메일',                          -- 이메일
    `account_num`  VARCHAR(20) NULL     COMMENT '계좌번호',                         -- 계좌번호
    `post_num`     VARCHAR(5) NULL     COMMENT '우편번호',                          -- 우편번호
    `addr`         VARCHAR(255) NULL     COMMENT '기본주소',                        -- 기본주소
    `detail_addr`  VARCHAR(255) NULL     COMMENT '상세주소',                        -- 상세주소
    `birth`        DATE NULL     COMMENT '생일',                                  -- 생일
    `sex`          TINYINT NULL     COMMENT '성별',                               -- 성별
    `phone_num`    VARCHAR(30) NULL     COMMENT '휴대폰번호',                        -- 휴대폰번호
    `created_date` DATETIME NOT NULL DEFAULT current_timestamp() COMMENT '가입일', -- 가입일
    `is_expire`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '탈퇴여부',              -- 0:가입 1:탈퇴(중요정보 마스킹처리)
    `is_blind`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '차단여부',              -- 0:미차단 1:차단
    `auth_id`      SMALLINT NOT NULL COMMENT '권한번호',                            -- 권한번호
    `bank_id`      SMALLINT NULL     COMMENT '은행번호'                             -- 은행번호
) COMMENT '사용자';

-- 사용자
ALTER TABLE `user`
    ADD CONSTRAINT `PK_user` -- 사용자 기본키
        PRIMARY KEY (
                     `user_id` -- 사용자번호
            );

-- 사용자 유니크 인덱스
CREATE UNIQUE INDEX `UIX_user`
    ON `user` ( -- 사용자
               `email` ASC -- 이메일
        );

ALTER TABLE `user`
    MODIFY COLUMN `user_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '사용자번호';

-- SNS로그인
CREATE TABLE `sns`
(
    `user_id` INTEGER      NOT NULL COMMENT '사용자번호', -- 사용자번호
    `type_id` SMALLINT     NOT NULL COMMENT 'SNS유형', -- SNS유형
    `sns_id`  VARCHAR(255) NOT NULL COMMENT 'SNS아이디' -- SNS아이디
) COMMENT 'SNS로그인';

-- SNS로그인
ALTER TABLE `sns`
    ADD CONSTRAINT `PK_sns` -- SNS로그인 기본키
        PRIMARY KEY (
                     `user_id`, -- 사용자번호
                     `type_id` -- SNS유형
            );

-- SNS로그인 유니크 인덱스
CREATE UNIQUE INDEX `UIX_sns`
    ON `sns` ( -- SNS로그인
              `sns_id` ASC -- SNS아이디
        );

-- SNS타입(마스터)
CREATE TABLE `sns_type`
(
    `type_id` SMALLINT    NOT NULL COMMENT 'SNS유형', -- SNS유형
    `name`    VARCHAR(50) NOT NULL COMMENT 'SNS이름'  -- SNS이름
) COMMENT 'SNS타입(마스터)';

-- SNS타입(마스터)
ALTER TABLE `sns_type`
    ADD CONSTRAINT `PK_sns_type` -- SNS타입(마스터) 기본키
        PRIMARY KEY (
                     `type_id` -- SNS유형
            );

-- 은행정보(마스터)
CREATE TABLE `bank_type`
(
    `type_id` SMALLINT    NOT NULL COMMENT '은행번호', -- 은행번호
    `name`    VARCHAR(50) NOT NULL COMMENT '은행명'   -- 은행명
) COMMENT '은행정보(마스터)';

-- 은행정보(마스터)
ALTER TABLE `bank_type`
    ADD CONSTRAINT `PK_bank_type` -- 은행정보(마스터) 기본키
        PRIMARY KEY (
                     `type_id` -- 은행번호
            );

-- 판매상품옵션
CREATE TABLE `option`
(
    `option_id`  INTEGER     NOT NULL COMMENT '옵션번호',            -- 옵션번호
    `type_id`    SMALLINT    NOT NULL COMMENT '타입번호(소분류)',       -- 타입번호(소분류)
    `sale_id`    INTEGER     NOT NULL COMMENT '판매글번호',           -- 판매글번호
    `name`       VARCHAR(50) NOT NULL COMMENT '상품명',             -- 상품명
    `price`      INTEGER     NOT NULL COMMENT '상품가격',            -- 상품가격
    `quantity`   INTEGER     NOT NULL COMMENT '상품개수',            -- 상품개수
    `is_soldout` TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '판매중지여부' -- 0:판매중 1:판매중지
) COMMENT '판매상품옵션';

-- 판매상품옵션
ALTER TABLE `option`
    ADD CONSTRAINT `PK_option` -- 판매상품옵션 기본키
        PRIMARY KEY (
                     `option_id` -- 옵션번호
            );

ALTER TABLE `option`
    MODIFY COLUMN `option_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '옵션번호';

-- 상품타입(대분류)(마스터)
CREATE TABLE `option_type_big`
(
    `type_big_id` SMALLINT    NOT NULL COMMENT '타입번호(대분류)',  -- 타입번호(대분류)
    `name`        VARCHAR(50) NOT NULL COMMENT '카테고리이름(대분류)' -- 카테고리이름(대분류)
) COMMENT '상품타입(대분류)(마스터)';

-- 상품타입(대분류)(마스터)
ALTER TABLE `option_type_big`
    ADD CONSTRAINT `PK_option_type_big` -- 상품타입(대분류)(마스터) 기본키
        PRIMARY KEY (
                     `type_big_id` -- 타입번호(대분류)
            );

-- 상품타입(대분류)(마스터) 유니크 인덱스
CREATE UNIQUE INDEX `UIX_option_type_big`
    ON `option_type_big` ( -- 상품타입(대분류)(마스터)
                          `name` ASC -- 카테고리이름(대분류)
        );

-- 상품타입(소분류)(마스터)
CREATE TABLE `option_type`
(
    `type_id`     SMALLINT    NOT NULL COMMENT '타입번호(소분류)',  -- 타입번호(소분류)
    `type_big_id` SMALLINT    NOT NULL COMMENT '타입번호(대분류)',  -- 타입번호(대분류)
    `name`        VARCHAR(50) NOT NULL COMMENT '카레고리이름(소분류)' -- 카레고리이름(소분류)
) COMMENT '상품타입(소분류)(마스터)';

-- 상품타입(소분류)(마스터)
ALTER TABLE `option_type`
    ADD CONSTRAINT `PK_option_type` -- 상품타입(소분류)(마스터) 기본키
        PRIMARY KEY (
                     `type_id` -- 타입번호(소분류)
            );

-- 상품타입(소분류)(마스터) 유니크 인덱스
CREATE UNIQUE INDEX `UIX_option_type`
    ON `option_type` ( -- 상품타입(소분류)(마스터)
                      `name` ASC -- 카레고리이름(소분류)
        );

-- 판매상품
CREATE TABLE `sale`
(
    `sale_id`      INTEGER     NOT NULL COMMENT '판매글번호',                           -- 판매글번호
    `user_id`      INTEGER     NOT NULL COMMENT '판매자번호',                           -- 판매자번호
    `title`        VARCHAR(50) NOT NULL COMMENT '제목',                              -- 제목
    `content`      TEXT        NOT NULL COMMENT '내용',                              -- 내용
    `origin`       VARCHAR(50) NOT NULL COMMENT '원산지',                             -- 원산지
    `is_blind`     TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '차단여부',                  -- 0:미차단 1:차단
    `created_date` DATETIME    NOT NULL DEFAULT current_timestamp() COMMENT '생성일시' -- 생성일시
) COMMENT '판매상품';

-- 판매상품
ALTER TABLE `sale`
    ADD CONSTRAINT `PK_sale` -- 판매상품 기본키
        PRIMARY KEY (
                     `sale_id` -- 판매글번호
            );

ALTER TABLE `sale`
    MODIFY COLUMN `sale_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '판매글번호';

-- 배송지정보
CREATE TABLE `depot`
(
    `depot_id`      INTEGER      NOT NULL COMMENT '택배지번호',            -- 택배지번호
    `user_id`       INTEGER      NOT NULL COMMENT '사용자번호',            -- 사용자번호
    `delivery_name` VARCHAR(50)  NOT NULL COMMENT '배송지이름',            -- 배송지이름
    `name`          VARCHAR(50)  NOT NULL COMMENT '받는사람',             -- 받는사람
    `phone_num`     VARCHAR(30)  NOT NULL COMMENT '전화번호',             -- 전화번호
    `post_num`      VARCHAR(5)   NOT NULL COMMENT '우편번호',             -- 우편번호
    `addr`          VARCHAR(255) NOT NULL COMMENT '기본주소',             -- 기본주소
    `detail_addr`   VARCHAR(255) NOT NULL COMMENT '상세주소',             -- 상세주소
    `require`       VARCHAR(255) NULL     COMMENT '요구사항',             -- 요구사항
    `is_default`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '기본배송지여부' -- 0:기본 X, 1:기본
) COMMENT '배송지정보';

-- 배송지정보
ALTER TABLE `depot`
    ADD CONSTRAINT `PK_depot` -- 배송지정보 기본키
        PRIMARY KEY (
                     `depot_id` -- 택배지번호
            );

ALTER TABLE `depot`
    MODIFY COLUMN `depot_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '택배지번호';

-- 찜한상품(별도테이블)
CREATE TABLE `like`
(
    `user_id` INTEGER NOT NULL COMMENT '사용자번호', -- 사용자번호
    `sale_id` INTEGER NOT NULL COMMENT '판매글번호'  -- 판매글번호
) COMMENT '찜한상품(별도테이블)';

-- 주문정보
CREATE TABLE `order`
(
    `order_id`     INTEGER      NOT NULL COMMENT '주문번호',                             -- 주문번호
    `user_id`      INTEGER      NOT NULL COMMENT '사용자번호',                            -- 사용자번호
    `option_id`    INTEGER      NOT NULL COMMENT '옵션번호',                             -- 옵션번호
    `created_date` DATETIME     NOT NULL DEFAULT current_timestamp() COMMENT '주문일시', -- 주문일시
    `status_id`    SMALLINT     NOT NULL DEFAULT 1 COMMENT '주문상태',                   -- 주문상태
    `quantity`     INTEGER      NOT NULL COMMENT '주문개수',                             -- 주문개수
    `price`        INTEGER      NOT NULL COMMENT '주문가격',                             -- 주문가격
    `post_num`     VARCHAR(5)   NOT NULL COMMENT '우편번호',                             -- 우편번호
    `addr`         VARCHAR(255) NOT NULL COMMENT '기본주소',                             -- 기본주소
    `detail_addr`  VARCHAR(255) NOT NULL COMMENT '상세주소'                              -- 상세주소
) COMMENT '주문정보';

-- 주문정보
ALTER TABLE `order`
    ADD CONSTRAINT `PK_order` -- 주문정보 기본키
        PRIMARY KEY (
                     `order_id` -- 주문번호
            );

ALTER TABLE `order`
    MODIFY COLUMN `order_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '주문번호';

-- 판매문의
CREATE TABLE `question`
(
    `question_id`  INTEGER     NOT NULL COMMENT '문의번호',                             -- 문의번호
    `title`        VARCHAR(50) NOT NULL COMMENT '문의제목',                             -- 문의제목
    `content`      TEXT        NOT NULL COMMENT '문의내용',                             -- 문의내용
    `answer`       TEXT        NULL COMMENT '문의답변',                                        -- 문의답변
    `created_date` DATETIME    NOT NULL DEFAULT current_timestamp() COMMENT '작성시간', -- 작성시간
    `is_process`   TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '처리상태',                   -- 0:미처리 1:처리완료
    `is_blind`     TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '차단여부',                   -- 0:미차단 1:차단
    `user_id`      INTEGER     NOT NULL COMMENT '사용자번호',                            -- 사용자번호
    `sale_id`      INTEGER     NOT NULL COMMENT '판매글번호'                             -- 판매글번호
) COMMENT '판매문의';

-- 판매문의
ALTER TABLE `question`
    ADD CONSTRAINT `PK_question` -- 판매문의 기본키
        PRIMARY KEY (
                     `question_id` -- 문의번호
            );

ALTER TABLE `question`
    MODIFY COLUMN `question_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '문의번호';

-- 판매자정보
CREATE TABLE `seller`
(
    `user_id`     INTEGER      NOT NULL COMMENT '판매자번호',         -- 판매자번호
    `name`        VARCHAR(50)  NOT NULL COMMENT '상호',            -- 상호
    `post_num`    VARCHAR(5)   NOT NULL COMMENT '우편번호',          -- 우편번호
    `addr`        VARCHAR(255) NOT NULL COMMENT '기본주소(사업장)',     -- 기본주소(사업장)
    `detail_addr` VARCHAR(255) NOT NULL COMMENT '상세주소(사업장)',     -- 상세주소(사업장)
    `company_num` VARCHAR(255) NOT NULL COMMENT '사업자번호',         -- 사업자번호
    `phone_num`   VARCHAR(30)  NOT NULL COMMENT '사업자전화번호',       -- 사업자전화번호
    `save_file`   VARCHAR(255) NOT NULL COMMENT '첨부파일(변경)',      -- 첨부파일(변경)
    `origin_file` VARCHAR(255) NOT NULL COMMENT '첨부파일(기존)',      -- 첨부파일(기존)
    `is_approve`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '승인여부' -- 0:미승인 1:승인
) COMMENT '판매자정보';

-- 판매자정보
ALTER TABLE `seller`
    ADD CONSTRAINT `PK_seller` -- 판매자정보 기본키
        PRIMARY KEY (
                     `user_id` -- 판매자번호
            );

-- 판매자정보 유니크 인덱스
CREATE UNIQUE INDEX `UIX_seller`
    ON `seller` ( -- 판매자정보
                 `company_num` ASC -- 사업자번호
        );

-- 이벤트/팝업/배너(별도테이블)
CREATE TABLE `popup`
(
    `popup_id`     INTEGER      NOT NULL COMMENT '팝업번호',                             -- 팝업번호
    `title`        VARCHAR(50)  NOT NULL COMMENT '제목',                               -- 제목
    `type_id`      SMALLINT     NOT NULL COMMENT '타입번호',                             -- 타입번호
    `save_file`    VARCHAR(255) NOT NULL COMMENT '변경파일',                             -- 변경파일
    `origin_file`  VARCHAR(255) NOT NULL COMMENT '기존파일',                             -- 기존파일
    `created_date` DATETIME     NOT NULL DEFAULT current_timestamp() COMMENT '생성일시', -- 생성일시
    `start_date`   DATETIME     NOT NULL COMMENT '시작일',                              -- 시작일
    `end_date`     DATETIME     NOT NULL COMMENT '종료일'                               -- 종료일
) COMMENT '이벤트/팝업/배너(별도테이블)';

-- 이벤트/팝업/배너(별도테이블)
ALTER TABLE `popup`
    ADD CONSTRAINT `PK_popup` -- 이벤트/팝업/배너(별도테이블) 기본키
        PRIMARY KEY (
                     `popup_id` -- 팝업번호
            );

ALTER TABLE `popup`
    MODIFY COLUMN `popup_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '팝업번호';

-- 방송
CREATE TABLE `broadcast`
(
    `broadcast_id` INTEGER     NOT NULL COMMENT '방송번호',                             -- 방송번호
    `user_id`      INTEGER     NOT NULL COMMENT '판매자번호',                            -- 판매자번호
    `title`        VARCHAR(50) NOT NULL COMMENT '제목',                               -- 제목
    `content`      TEXT        NOT NULL COMMENT '내용',                               -- 내용
    `created_date` DATETIME    NOT NULL DEFAULT current_timestamp() COMMENT '생성일시', -- 생성일시
    `is_blind`     TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '차단여부'                    -- 0:미차단 1:차단
) COMMENT '방송';

-- 방송
ALTER TABLE `broadcast`
    ADD CONSTRAINT `PK_broadcast` -- 방송 기본키
        PRIMARY KEY (
                     `broadcast_id` -- 방송번호
            );

ALTER TABLE `broadcast`
    MODIFY COLUMN `broadcast_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '방송번호';

-- 권한(마스터)
CREATE TABLE `auth`
(
    `auth_id` SMALLINT    NOT NULL COMMENT '권한번호', -- 권한번호
    `role`    VARCHAR(50) NOT NULL COMMENT '역할'    -- 역할
) COMMENT '권한(마스터)';

-- 권한(마스터)
ALTER TABLE `auth`
    ADD CONSTRAINT `PK_auth` -- 권한(마스터) 기본키
        PRIMARY KEY (
                     `auth_id` -- 권한번호
            );

-- 상품이미지
CREATE TABLE `sale_file`
(
    `file_id`     INTEGER      NOT NULL COMMENT '파일번호',  -- 파일번호
    `sale_id`     INTEGER      NOT NULL COMMENT '판매글번호', -- 판매글번호
    `save_file`   VARCHAR(255) NOT NULL COMMENT '변경파일',  -- 변경파일
    `origin_file` VARCHAR(255) NOT NULL COMMENT '기존파일',  -- 기존파일
    `is_main`     TINYINT(1)   NOT NULL COMMENT '메인사진여부' -- 0:메인,1:서브
) COMMENT '상품이미지';

-- 상품이미지
ALTER TABLE `sale_file`
    ADD CONSTRAINT `PK_sale_file` -- 상품이미지 기본키
        PRIMARY KEY (
                     `file_id` -- 파일번호
            );

ALTER TABLE `sale_file`
    MODIFY COLUMN `file_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '파일번호';

-- 리뷰사진
CREATE TABLE `review_file`
(
    `file_id`     INTEGER      NOT NULL COMMENT '파일번호', -- 파일번호
    `review_id`   INTEGER      NOT NULL COMMENT '리뷰번호', -- 리뷰번호
    `origin_file` VARCHAR(255) NOT NULL COMMENT '기존파일', -- 기존파일
    `save_file`   VARCHAR(255) NOT NULL COMMENT '저장파일'  -- 저장파일
) COMMENT '리뷰사진';

-- 리뷰사진
ALTER TABLE `review_file`
    ADD CONSTRAINT `PK_review_file` -- 리뷰사진 기본키
        PRIMARY KEY (
                     `file_id` -- 파일번호
            );

ALTER TABLE `review_file`
    MODIFY COLUMN `file_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '파일번호';

-- 환불/교환
CREATE TABLE `refund`
(
    `refund_id`    INTEGER  NOT NULL COMMENT '환불ID',                                 -- 환불ID
    `order_id`     INTEGER  NOT NULL COMMENT '주문번호',                                 -- 주문번호
    `type_id`      SMALLINT NOT NULL COMMENT '환불카테고리ID',                             -- 환불카테고리ID
    `content`      TEXT     NOT NULL COMMENT '환불/교환사유',                              -- 환불/교환사유
    `created_date` DATETIME NOT NULL DEFAULT current_timestamp() COMMENT '환불/교환신청시간' -- 환불/교환신청시간
) COMMENT '환불/교환';

-- 환불/교환
ALTER TABLE `refund`
    ADD CONSTRAINT `PK_refund` -- 환불/교환 기본키
        PRIMARY KEY (
                     `refund_id` -- 환불ID
            );

ALTER TABLE `refund`
    MODIFY COLUMN `refund_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '환불ID';

-- 환불/교환사진
CREATE TABLE `refund_file`
(
    `file_id`     INTEGER      NOT NULL COMMENT '파일번호', -- 파일번호
    `refund_id`   INTEGER      NOT NULL COMMENT '환불ID', -- 환불ID
    `origin_file` VARCHAR(255) NOT NULL COMMENT '기존파일', -- 기존파일
    `save_file`   VARCHAR(255) NOT NULL COMMENT '저장파일'  -- 저장파일
) COMMENT '환불/교환사진';

-- 환불/교환사진
ALTER TABLE `refund_file`
    ADD CONSTRAINT `PK_refund_file` -- 환불/교환사진 기본키
        PRIMARY KEY (
                     `file_id` -- 파일번호
            );

ALTER TABLE `refund_file`
    MODIFY COLUMN `file_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '파일번호';

-- 환불/교환 타입(마스터)
CREATE TABLE `refund_type`
(
    `type_id` SMALLINT    NOT NULL COMMENT '환불ID', -- 환불ID
    `name`    VARCHAR(50) NOT NULL COMMENT '환불종류'  -- 환불종류
) COMMENT '환불/교환 타입(마스터)';

-- 환불/교환 타입(마스터)
ALTER TABLE `refund_type`
    ADD CONSTRAINT `PK_refund_type` -- 환불/교환 타입(마스터) 기본키
        PRIMARY KEY (
                     `type_id` -- 환불ID
            );

-- 결제정보
CREATE TABLE `pay`
(
    `pay_id`       INTEGER      NOT NULL COMMENT '결제번호',                             -- 결제번호
    `order_id`     INTEGER      NOT NULL COMMENT '주문번호',                             -- 주문번호
    `method`       VARCHAR(50)  NOT NULL COMMENT '결제수단',                             -- 결제수단
    `status_id`    SMALLINT NULL     COMMENT '결제상태ID',                               -- 결제상태ID
    `amount`       INTEGER      NOT NULL COMMENT '결제금액',                             -- 결제금액
    `created_date` DATETIME     NOT NULL DEFAULT current_timestamp() COMMENT '결제일시', -- 결제일시
    `pay_num`      VARCHAR(255) NOT NULL COMMENT '결제 거래 고유번호',                       -- 결제 거래 고유번호
    `card`         VARCHAR(50) NULL     COMMENT '카드사',                               -- 카드사
    `card_number`  VARCHAR(20) NULL     COMMENT '카드번호',                              -- 카드번호
    `account_num`  VARCHAR(20) NULL     COMMENT '계좌번호',                              -- 계좌번호
    `bank_name`    VARCHAR(50) NULL     COMMENT '은행명'                                -- 은행명
) COMMENT '결제정보';

-- 결제정보
ALTER TABLE `pay`
    ADD CONSTRAINT `PK_pay` -- 결제정보 기본키
        PRIMARY KEY (
                     `pay_id` -- 결제번호
            );

ALTER TABLE `pay`
    MODIFY COLUMN `pay_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '결제번호';

-- 이벤트/팝업/배너 타입(마스터)
CREATE TABLE `popup_type`
(
    `type_id` SMALLINT    NOT NULL COMMENT '타입번호', -- 타입번호
    `name`    VARCHAR(50) NOT NULL COMMENT '타입이름'  -- 타입이름
) COMMENT '이벤트/팝업/배너 타입(마스터)';

-- 이벤트/팝업/배너 타입(마스터)
ALTER TABLE `popup_type`
    ADD CONSTRAINT `PK_popup_type` -- 이벤트/팝업/배너 타입(마스터) 기본키
        PRIMARY KEY (
                     `type_id` -- 타입번호
            );

-- 주문상태 타입(마스터)
CREATE TABLE `order_status`
(
    `status_id` SMALLINT    NOT NULL COMMENT '주문상태ID', -- 주문상태ID
    `name`      VARCHAR(50) NOT NULL COMMENT '주문상태종류'  -- 주문상태종류
) COMMENT '주문상태 타입(마스터)';

-- 주문상태 타입(마스터)
ALTER TABLE `order_status`
    ADD CONSTRAINT `PK_order_status` -- 주문상태 타입(마스터) 기본키
        PRIMARY KEY (
                     `status_id` -- 주문상태ID
            );

-- 결제상태(마스터)
CREATE TABLE `pay_status`
(
    `status_id` SMALLINT NOT NULL COMMENT '결제상태ID',   -- 결제상태ID
    `name`      VARCHAR(50) NULL     COMMENT '결제상태이름' -- 결제상태이름
) COMMENT '결제상태(마스터)';

-- 결제상태(마스터)
ALTER TABLE `pay_status`
    ADD CONSTRAINT `PK_pay_status` -- 결제상태(마스터) 기본키
        PRIMARY KEY (
                     `status_id` -- 결제상태ID
            );

-- 장바구니(별도)회원용
CREATE TABLE `basket`
(
    `user_id`   INTEGER NOT NULL COMMENT '사용자번호', -- 사용자번호
    `option_id` INTEGER NOT NULL COMMENT '옵션번호'   -- 옵션번호
) COMMENT '장바구니(별도)회원용';

-- 장바구니(별도)회원용 유니크 인덱스
CREATE UNIQUE INDEX `UIX_basket`
    ON `basket` ( -- 장바구니(별도)회원용
                 `option_id` ASC, -- 옵션번호
                 `user_id` ASC -- 사용자번호
        );

-- 리뷰
CREATE TABLE `review`
(
    `review_id`    INTEGER  NOT NULL COMMENT '리뷰번호',                              -- 리뷰번호
    `order_id`     INTEGER  NOT NULL COMMENT '주문번호',                              -- 주문번호
    `content`      TEXT     NOT NULL COMMENT '리뷰내용',                              -- 리뷰내용
    `score`        TINYINT  NOT NULL COMMENT '만족도도',                              -- 만족도
    `created_date` DATETIME NOT NULL DEFAULT current_timestamp() COMMENT '리뷰작성시간' -- 리뷰작성시간
) COMMENT '리뷰';

-- 리뷰
ALTER TABLE `review`
    ADD CONSTRAINT `PK_review` -- 리뷰 기본키
        PRIMARY KEY (
                     `review_id` -- 리뷰번호
            );

ALTER TABLE `review`
    MODIFY COLUMN `review_id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '리뷰번호';

-- 사용자
ALTER TABLE `user`
    ADD CONSTRAINT `FK_auth_TO_user` -- 권한(마스터) -> 사용자
        FOREIGN KEY (
                     `auth_id` -- 권한번호
            )
            REFERENCES `auth` ( -- 권한(마스터)
                               `auth_id` -- 권한번호
                );

-- 사용자
ALTER TABLE `user`
    ADD CONSTRAINT `FK_bank_type_TO_user` -- 은행정보(마스터) -> 사용자
        FOREIGN KEY (
                     `bank_id` -- 은행번호
            )
            REFERENCES `bank_type` ( -- 은행정보(마스터)
                                    `type_id` -- 은행번호
                );

-- SNS로그인
ALTER TABLE `sns`
    ADD CONSTRAINT `FK_sns_type_TO_sns` -- SNS타입(마스터) -> SNS로그인
        FOREIGN KEY (
                     `type_id` -- SNS유형
            )
            REFERENCES `sns_type` ( -- SNS타입(마스터)
                                   `type_id` -- SNS유형
                );

-- SNS로그인
ALTER TABLE `sns`
    ADD CONSTRAINT `FK_user_TO_sns` -- 사용자 -> SNS로그인
        FOREIGN KEY (
                     `user_id` -- 사용자번호
            )
            REFERENCES `user` ( -- 사용자
                               `user_id` -- 사용자번호
                );

-- 판매상품옵션
ALTER TABLE `option`
    ADD CONSTRAINT `FK_sale_TO_option` -- 판매상품 -> 판매상품옵션
        FOREIGN KEY (
                     `sale_id` -- 판매글번호
            )
            REFERENCES `sale` ( -- 판매상품
                               `sale_id` -- 판매글번호
                );

-- 판매상품옵션
ALTER TABLE `option`
    ADD CONSTRAINT `FK_option_type_TO_option` -- 상품타입(소분류)(마스터) -> 판매상품옵션
        FOREIGN KEY (
                     `type_id` -- 타입번호(소분류)
            )
            REFERENCES `option_type` ( -- 상품타입(소분류)(마스터)
                                      `type_id` -- 타입번호(소분류)
                );

-- 상품타입(소분류)(마스터)
ALTER TABLE `option_type`
    ADD CONSTRAINT `FK_option_type_big_TO_option_type` -- 상품타입(대분류)(마스터) -> 상품타입(소분류)(마스터)
        FOREIGN KEY (
                     `type_big_id` -- 타입번호(대분류)
            )
            REFERENCES `option_type_big` ( -- 상품타입(대분류)(마스터)
                                          `type_big_id` -- 타입번호(대분류)
                );

-- 판매상품
ALTER TABLE `sale`
    ADD CONSTRAINT `FK_seller_TO_sale` -- 판매자정보 -> 판매상품
        FOREIGN KEY (
                     `user_id` -- 판매자번호
            )
            REFERENCES `seller` ( -- 판매자정보
                                 `user_id` -- 판매자번호
                );

-- 배송지정보
ALTER TABLE `depot`
    ADD CONSTRAINT `FK_user_TO_depot` -- 사용자 -> 배송지정보
        FOREIGN KEY (
                     `user_id` -- 사용자번호
            )
            REFERENCES `user` ( -- 사용자
                               `user_id` -- 사용자번호
                );

-- 주문정보
ALTER TABLE `order`
    ADD CONSTRAINT `FK_user_TO_order` -- 사용자 -> 주문정보
        FOREIGN KEY (
                     `user_id` -- 사용자번호
            )
            REFERENCES `user` ( -- 사용자
                               `user_id` -- 사용자번호
                );

-- 주문정보
ALTER TABLE `order`
    ADD CONSTRAINT `FK_option_TO_order` -- 판매상품옵션 -> 주문정보
        FOREIGN KEY (
                     `option_id` -- 옵션번호
            )
            REFERENCES `option` ( -- 판매상품옵션
                                 `option_id` -- 옵션번호
                );

-- 주문정보
ALTER TABLE `order`
    ADD CONSTRAINT `FK_order_status_TO_order` -- 주문상태 타입(마스터) -> 주문정보
        FOREIGN KEY (
                     `status_id` -- 주문상태
            )
            REFERENCES `order_status` ( -- 주문상태 타입(마스터)
                                       `status_id` -- 주문상태ID
                );

-- 판매문의
ALTER TABLE `question`
    ADD CONSTRAINT `FK_user_TO_question` -- 사용자 -> 판매문의
        FOREIGN KEY (
                     `user_id` -- 사용자번호
            )
            REFERENCES `user` ( -- 사용자
                               `user_id` -- 사용자번호
                );

-- 판매문의
ALTER TABLE `question`
    ADD CONSTRAINT `FK_sale_TO_question` -- 판매상품 -> 판매문의
        FOREIGN KEY (
                     `sale_id` -- 판매글번호
            )
            REFERENCES `sale` ( -- 판매상품
                               `sale_id` -- 판매글번호
                );

-- 판매자정보
ALTER TABLE `seller`
    ADD CONSTRAINT `FK_user_TO_seller` -- 사용자 -> 판매자정보
        FOREIGN KEY (
                     `user_id` -- 판매자번호
            )
            REFERENCES `user` ( -- 사용자
                               `user_id` -- 사용자번호
                );

-- 이벤트/팝업/배너(별도테이블)
ALTER TABLE `popup`
    ADD CONSTRAINT `FK_popup_type_TO_popup` -- 이벤트/팝업/배너 타입(마스터) -> 이벤트/팝업/배너(별도테이블)
        FOREIGN KEY (
                     `type_id` -- 타입번호
            )
            REFERENCES `popup_type` ( -- 이벤트/팝업/배너 타입(마스터)
                                     `type_id` -- 타입번호
                );

-- 방송
ALTER TABLE `broadcast`
    ADD CONSTRAINT `FK_seller_TO_broadcast` -- 판매자정보 -> 방송
        FOREIGN KEY (
                     `user_id` -- 판매자번호
            )
            REFERENCES `seller` ( -- 판매자정보
                                 `user_id` -- 판매자번호
                );

-- 상품이미지
ALTER TABLE `sale_file`
    ADD CONSTRAINT `FK_sale_TO_sale_file` -- 판매상품 -> 상품이미지
        FOREIGN KEY (
                     `sale_id` -- 판매글번호
            )
            REFERENCES `sale` ( -- 판매상품
                               `sale_id` -- 판매글번호
                );

-- 리뷰사진
ALTER TABLE `review_file`
    ADD CONSTRAINT `FK_review_TO_review_file` -- 리뷰 -> 리뷰사진
        FOREIGN KEY (
                     `review_id` -- 리뷰번호
            )
            REFERENCES `review` ( -- 리뷰
                                 `review_id` -- 리뷰번호
                );

-- 환불/교환
ALTER TABLE `refund`
    ADD CONSTRAINT `FK_order_TO_refund` -- 주문정보 -> 환불/교환
        FOREIGN KEY (
                     `order_id` -- 주문번호
            )
            REFERENCES `order` ( -- 주문정보
                                `order_id` -- 주문번호
                );

-- 환불/교환
ALTER TABLE `refund`
    ADD CONSTRAINT `FK_refund_type_TO_refund` -- 환불/교환 타입(마스터) -> 환불/교환
        FOREIGN KEY (
                     `type_id` -- 환불카테고리ID
            )
            REFERENCES `refund_type` ( -- 환불/교환 타입(마스터)
                                      `type_id` -- 환불ID
                );

-- 환불/교환사진
ALTER TABLE `refund_file`
    ADD CONSTRAINT `FK_refund_TO_refund_file` -- 환불/교환 -> 환불/교환사진
        FOREIGN KEY (
                     `refund_id` -- 환불ID
            )
            REFERENCES `refund` ( -- 환불/교환
                                 `refund_id` -- 환불ID
                );

-- 결제정보
ALTER TABLE `pay`
    ADD CONSTRAINT `FK_order_TO_pay` -- 주문정보 -> 결제정보
        FOREIGN KEY (
                     `order_id` -- 주문번호
            )
            REFERENCES `order` ( -- 주문정보
                                `order_id` -- 주문번호
                );

-- 결제정보
ALTER TABLE `pay`
    ADD CONSTRAINT `FK_pay_status_TO_pay` -- 결제상태(마스터) -> 결제정보
        FOREIGN KEY (
                     `status_id` -- 결제상태ID
            )
            REFERENCES `pay_status` ( -- 결제상태(마스터)
                                     `status_id` -- 결제상태ID
                );

-- 리뷰
ALTER TABLE `review`
    ADD CONSTRAINT `FK_order_TO_review` -- 주문정보 -> 리뷰
        FOREIGN KEY (
                     `order_id` -- 주문번호
            )
            REFERENCES `order` ( -- 주문정보
                                `order_id` -- 주문번호
                );


-- pay_status (주문산태 타입(마스터)) 테이블 마스터 데이터
INSERT INTO `pay_status` (`status_id`, `name`)
VALUES (1, '결제대기'),
       (2, '결제완료'),
       (3, '결제실패'),
       (4, '결제취소');

-- auth (권한(마스터)) 테이블 마스터 데이터
INSERT INTO `auth` (`auth_id`, `role`)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_SELLER'),
       (3, 'ROLE_USER');

-- sns_type (SNS타입(마스터)) 테이블 마스터 데이터
INSERT INTO `sns_type` (`type_id`, `name`)
VALUES (1, 'KAKAO'),
       (2, 'NAVER');

-- bank_type (은행정보(마스터)) 테이블 마스터 데이터
INSERT INTO `bank_type` (`type_id`, `name`)
VALUES (1, '신한은행'),
       (2, '국민은행'),
       (3, '우리은행'),
       (4, '하나은행'),
       (5, '농협은행'),
       (6, '카카오뱅크'),
       (7, '토스뱅크');

-- option_type_big (상품타입(대분류)(마스터)) 테이블 마스터 데이터 (농산물 관련)
INSERT INTO `option_type_big` (`type_big_id`, `name`)
VALUES (1, '과일'),
       (2, '채소'),
       (3, '곡류'),
       (4, '견과류'),
       (5, '버섯류');

-- option_type (상품타입(소분류)(마스터)) 테이블 마스터 데이터 (농산물 관련)
INSERT INTO `option_type` (`type_id`, `type_big_id`, `name`)
VALUES (1, 1, '사과'),
       (2, 1, '배'),
       (3, 1, '딸기'),
       (4, 1, '포도'),
       (5, 1, '감귤'),
       (6, 2, '상추'),
       (7, 2, '깻잎'),
       (8, 2, '고추'),
       (9, 2, '마늘'),
       (10, 2, '양파'),
       (11, 3, '쌀'),
       (12, 3, '현미'),
       (13, 3, '보리'),
       (14, 3, '콩'),
       (15, 4, '호두'),
       (16, 4, '아몬드'),
       (17, 4, '땅콩'),
       (18, 4, '잣'),
       (19, 5, '새송이버섯'),
       (20, 5, '표고버섯'),
       (21, 5, '느타리버섯');

-- refund_type (환불/교환 타입(마스터)) 테이블 마스터 데이터
INSERT INTO `refund_type` (`type_id`, `name`)
VALUES (1, '단순 변심'),
       (2, '상품 파손'),
       (3, '상품 불량'),
       (4, '오배송'),
       (5, '상품 정보 상이'),
       (6, '배송 지연'),
       (7, '기타');

-- popup_type (이벤트/팝업/배너 타입(마스터)) 테이블 마스터 데이터
INSERT INTO `popup_type` (`type_id`, `name`)
VALUES (1, '이벤트'),
       (2, '팝업'),
       (3, '배너');

-- order_status (주문산태 타입(마스터)) 테이블 마스터 데이터
INSERT INTO `order_status` (`status_id`, `name`)
VALUES (1, '배송준비'),
       (2, '배송중'),
       (3, '배송완료'),
       (4, '취소'),
       (5, '반품'),
       (6, '교환');
