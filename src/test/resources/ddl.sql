CREATE TABLE auth_tb
(
    id       bigint       NOT NULL AUTO_INCREMENT,
    password varchar(255) NOT NULL,
    username varchar(50)  NOT NULL,
    user_id  bigint DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_auth_username (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE cart_tb
(
    id                       bigint      NOT NULL AUTO_INCREMENT,
    created_at               datetime(6) NOT NULL,
    price                    int         NOT NULL,
    product_detail_option_id bigint      NOT NULL,
    quantity                 int         NOT NULL,
    updated_at               datetime(6) DEFAULT NULL,
    product_id               bigint      DEFAULT NULL,
    user_id                  bigint      NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE coupon_available_product_tb
(
    id         bigint NOT NULL AUTO_INCREMENT,
    coupon_id  bigint NOT NULL,
    product_id bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE coupon_available_user_tb
(
    id          bigint NOT NULL AUTO_INCREMENT,
    issue_count int    NOT NULL,
    use_yn      bit(1) NOT NULL,
    coupon_id   bigint NOT NULL,
    user_id     bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE coupon_common_transaction_tb
(
    id            bigint                                                       NOT NULL AUTO_INCREMENT,
    coupon_status enum ('ALLOCATED','DOWNLOAD','RE_DOWNLOAD','USED','EXPIRED') NOT NULL,
    create_at     datetime(6) DEFAULT NULL,
    coupon_id     bigint      DEFAULT NULL,
    user_id       bigint      DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE coupon_quantity_tb
(
    id      bigint NOT NULL AUTO_INCREMENT,
    value   int    NOT NULL,
    version int DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE coupon_tb
(
    id                           bigint                             NOT NULL AUTO_INCREMENT,
    available_yn                 bit(1)                             NOT NULL,
    benefit_unit_type            enum ('PERCENT','AMOUNT')          NOT NULL,
    coupon_publication_type      enum ('ALLOCATE','DOWNLOAD')       NOT NULL,
    coupon_type                  enum ('PRODUCT','CART','DELIVERY') NOT NULL,
    benefit_value                int                                NOT NULL,
    content                      varchar(255)                       NOT NULL,
    id_per_issuable_count        int                                NOT NULL,
    max_discount_value           int                                NOT NULL,
    min_order_price              int                                NOT NULL,
    name                         varchar(255)                       NOT NULL,
    created_at                   datetime(6)                        NOT NULL,
    customer_manage_benefit_type enum ('ALL','NEW','REPURCHASE')    NOT NULL,
    duplication_yn               bit(1)                             NOT NULL,
    membership_coupon_yn         bit(1)                             NOT NULL,
    valid_period_end_date        datetime(6)                        NOT NULL,
    valid_period_start_date      datetime(6)                        NOT NULL,
    coupon_quantity_id           bigint DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_coupon_quantity_coupon (coupon_quantity_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE coupon_usage_transaction_tb
(
    id            bigint                                                       NOT NULL AUTO_INCREMENT,
    coupon_status enum ('ALLOCATED','DOWNLOAD','RE_DOWNLOAD','USED','EXPIRED') NOT NULL,
    create_at     datetime(6)  DEFAULT NULL,
    coupon_id     bigint       DEFAULT NULL,
    user_id       bigint       DEFAULT NULL,
    order_id      varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_order_coupon_usage_transaction (order_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE delivery_info_tb
(
    id               bigint NOT NULL AUTO_INCREMENT,
    address_1depth   varchar(255) DEFAULT NULL,
    address_2depth   varchar(255) DEFAULT NULL,
    zip_code         varchar(255) DEFAULT NULL,
    delivery_request varchar(255) DEFAULT NULL,
    receiver_name    varchar(255) DEFAULT NULL,
    receiver_phone   varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE keep_tb
(
    id         bigint NOT NULL AUTO_INCREMENT,
    created_at datetime(6) DEFAULT NULL,
    product_id bigint      DEFAULT NULL,
    user_id    bigint      DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE order_sheet_tb
(
    id         bigint NOT NULL AUTO_INCREMENT,
    created_at datetime(6) DEFAULT NULL,
    user_id    bigint      DEFAULT NULL,
    delivery_method             enum ('DELIVERY','VISIT') DEFAULT NULL,
    delivery_fee                int     DEFAULT NULL,
    applied_delivery_fee_coupon tinyint DEFAULT false,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE order_tb
(
    id               varchar(255) NOT NULL,
    created_at       datetime(6)                                                                                                                                                                                                                        DEFAULT NULL,
    deleted_at       datetime(6)                                                                                                                                                                                                                        DEFAULT NULL,
    updated_at       datetime(6)                                                                                                                                                                                                                        DEFAULT NULL,
    order_date       datetime(6)                                                                                                                                                                                                                        DEFAULT NULL,
    order_status     enum ('PAYMENT_WAITING','PAYMENT_COMPLETED','DELIVERY_PREPARING','DELIVERING_BEFORE','DELIVERING','DELIVERED','CANCELLED','PURCHASE_CONFIRMED','REFUND_REQUESTED','REFUND_COMPLETED','CANCELLED_BY_NO_PAYMENT','DISPATCH_DELAYED') DEFAULT NULL,
--     payment_date     datetime(6)                                                                                                                                                                                                                        DEFAULT NULL,
--     payment_method   enum ('SAMSUNG_PAY','PAY_LATER','CARD','CARD_EASY_PAY','RECHARGE_POINT','CHARGE_POINT','BANK_EASY_PAY','MOBILE','MOBILE_EASY_PAY','REWARD_POINT')                                                                                  DEFAULT NULL,
    total_price      int                                                                                                                                                                                                                                DEFAULT NULL,
    delivery_info_id bigint                                                                                                                                                                                                                             DEFAULT NULL,
    order_sheet_id   bigint                                                                                                                                                                                                                             DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE ordered_product_tb
(
    id                 bigint NOT NULL AUTO_INCREMENT,
    coupon_discount    int    DEFAULT NULL,
    extra_price        int    DEFAULT NULL,
    immediate_discount int    DEFAULT NULL,
    org_price          int    DEFAULT NULL,
    point_discount     int    DEFAULT NULL,
    quantity           int    DEFAULT NULL,
    total_price        int    DEFAULT NULL,
    order_sheet_id     bigint DEFAULT NULL,
    product_id         bigint DEFAULT NULL,
    product_option_id  bigint DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE point_detail_transaction_tb
(
    id                 bigint NOT NULL AUTO_INCREMENT,
    ordered_product_id bigint DEFAULT NULL,
    origin_acm_id      bigint NOT NULL,
    point_amount       int    NOT NULL,
    point_id           bigint DEFAULT NULL,
    user_id            bigint DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE point_transaction_tb
(
    id                  bigint                                                                     NOT NULL AUTO_INCREMENT,
    expired_at          datetime(6)  DEFAULT NULL,
    issued_at           datetime(6)                                                                NOT NULL,
    membership_apply_yn bit(1)                                                                     NOT NULL,
    order_id            varchar(255) DEFAULT NULL,
    point_label         enum ('ORDER','REVIEW','EVENT')                                            NOT NULL,
    point_status        enum ('ACCUMULATED','USED','ACCUMULATE_CANCELED','USE_CANCELED','EXPIRED') NOT NULL,
    point_value         int                                                                        NOT NULL,
    user_id             bigint       DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE product_answer_tb
(
    id                  bigint       NOT NULL AUTO_INCREMENT,
    content             varchar(255) NOT NULL,
    created_at          datetime(6)  NOT NULL,
    updated_at          datetime(6) DEFAULT NULL,
    product_question_id bigint      DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE product_option_tb
(
    id                  bigint                        NOT NULL AUTO_INCREMENT,
    deleted_at          datetime(6) DEFAULT NULL,
    option_name_1       varchar(50) DEFAULT NULL,
    option_name_2       varchar(50) DEFAULT NULL,
    option_name_3       varchar(50) DEFAULT NULL,
    option_type         enum ('SINGLE','COMBINATION') NOT NULL,
    price               int                           NOT NULL,
    register_date       datetime(6)                   NOT NULL,
    product_id          bigint                        NOT NULL,
    product_quantity_id bigint      DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_product_quantity_product_option (product_quantity_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE product_quantity_tb
(
    id             bigint NOT NULL AUTO_INCREMENT,
    stock_quantity int    NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE product_question_tb
(
    id         bigint       NOT NULL AUTO_INCREMENT,
    content    varchar(255) NOT NULL,
    created_at datetime(6)  NOT NULL,
    product_id bigint       NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    user_id    bigint       NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE product_tb
(
    id                  bigint                                          NOT NULL AUTO_INCREMENT,
    category            enum ('CLOTHES','FOOD','ELECTRIC','HOUSE','IT') NOT NULL,
    combination_yn      tinyint                                         NOT NULL,
    content_image       varchar(255) DEFAULT NULL,
    created_at          datetime(6)                                     NOT NULL,
    deleted_at          datetime(6)  DEFAULT NULL,
    discount_ratio      int                                             NOT NULL,
    discount_yn         tinyint                                         NOT NULL,
    name                varchar(100)                                    NOT NULL,
    option_name_type_1  varchar(50)  DEFAULT NULL,
    option_name_type_2  varchar(50)  DEFAULT NULL,
    option_name_type_3  varchar(50)  DEFAULT NULL,
    size                tinyint      DEFAULT NULL,
    price               int                                             NOT NULL,
    product_status_type enum ('SALE','OUT_OF_STOCK','NOT_SALE')         NOT NULL,
    release_date        date         DEFAULT NULL,
    thumbnail           varchar(255)                                    NOT NULL,
    updated_at          datetime(6)  DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE refresh_token_tb
(
    id                        bigint NOT NULL AUTO_INCREMENT,
    refresh_token             varchar(255) DEFAULT NULL,
    refresh_token_expiry_date datetime(6)  DEFAULT NULL,
    auth_id                   bigint NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_auth_refresh_token (auth_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE review_like_tb
(
    id        bigint NOT NULL AUTO_INCREMENT,
    review_id bigint NOT NULL,
    user_id   bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE review_tb
(
    id           bigint                                   NOT NULL AUTO_INCREMENT,
    content      tinytext                                 NOT NULL,
    created_at   datetime(6)                              NOT NULL,
    review_score enum ('ONE','TWO','THREE','FOUR','FIVE') NOT NULL,
    title        varchar(100)                             NOT NULL,
    updated_at   datetime(6)                              NOT NULL,
    product_id   bigint DEFAULT NULL,
    user_id      bigint DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE shipping_address_tb
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    address1depth varchar(50) NOT NULL,
    address2depth varchar(30) NOT NULL,
    default_yn    bit(1)      NOT NULL,
    name          varchar(10) DEFAULT NULL,
    phone_num1    varchar(15) NOT NULL,
    phone_num2    varchar(15) DEFAULT NULL,
    recipient     varchar(10) NOT NULL,
    zip_code      varchar(10) NOT NULL,
    user_id       bigint      DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE user_tb
(
    id              bigint                          NOT NULL AUTO_INCREMENT,
    age             int         DEFAULT NULL,
    birth           varchar(64)                     NOT NULL,
    created_at      datetime(6)                     NOT NULL,
    deleted_at      datetime(6) DEFAULT NULL,
    email           varchar(64)                     NOT NULL,
    gender          enum ('MALE','FEMALE')          NOT NULL,
    marketing_agree bit(1)                          NOT NULL,
    membership_yn   bit(1)                          NOT NULL,
    nick_name       varchar(10)                     NOT NULL,
    phone           varchar(255)                    NOT NULL,
    point           int                             NOT NULL,
    role            enum ('ROLE_USER','ROLE_ADMIN') NOT NULL,
    thumbnail       blob,
    updated_at      datetime(6) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
