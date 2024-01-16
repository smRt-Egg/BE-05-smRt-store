package com.programmers.smrtstore.domain.orderManagement.order.infrastructure;

import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.OrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    @Query("SELECT count(o) FROM Order o JOIN OrderSheet os ON o.orderSheet.id = os.id WHERE os.user.id = :userId AND o.orderStatus IN :statuses AND o.deletedAt IS NULL")
    Long findOrderCountByStatusesAndUserId(Long userId, List<OrderStatus> statuses);

    @Query("SELECT o FROM Order o JOIN OrderSheet os ON o.orderSheet.id = os.id WHERE os.user.id = :userId AND o.deletedAt IS NULL")
    List<Order> findByUserId(Long userId);

    // TODO: user 가 탈퇴한 경우 해당 메소드 어떻게 처리할 것인가?
    /**
     * 단일 Order를 조회합니다. 이 메소드는 삭제 된 Order 엔티티는 제외 하고 반환합니다. 이 메소드는 'orderSheet'와 'orderSheet 의 user'
     * 필드를 Eager로 로딩합니다.
     *
     * @param orderId 조회하고자 하는 Order의 ID입니다.
     * @return Optional<Order> - 해당 ID를 가진 Order 객체. 만약 Order가 존재하지 않거나 'deletedAt'이 null이 아닐 경우, 비어
     * 있는 Optional을 반환합니다.
     */
    @EntityGraph(attributePaths = {"orderSheet", "orderSheet.user"})
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.deletedAt IS NULL")
    Optional<Order> findByIdWithOrderSheetAndUser(Long orderId);

    /**
     * 단일 Order를 조회합니다. 이 메소드는 삭제 된 Order 엔티티는 제외 하고 반환합니다. 이 메소드는 'orderSheet' 필드를 Eager로 로딩합니다.
     *
     * @param orderId 조회하고자 하는 Order의 ID입니다.
     * @return Optional<Order> - 해당 ID를 가진 Order 객체. 만약 Order가 존재하지 않거나 'deletedAt'이 null이 아닐 경우, 비어
     * 있는 Optional을 반환합니다.
     */
    @EntityGraph(attributePaths = {"orderSheet"})
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.deletedAt IS NULL")
    Optional<Order> findByIdWithOrderSheet(Long orderId);

    /**
     * 단일 Order를 조회합니다. 이 메소드는 삭제 된 Order 엔티티는 제외 하고 반환합니다.
     *
     * @param orderId 조회하고자 하는 Order의 ID입니다.
     * @return Optional<Order> - 해당 ID를 가진 Order 객체. 만약 Order가 존재하지 않거나 'deletedAt'이 null이 아닐 경우, 비어
     * 있는 Optional을 반환합니다.
     */
    @Override
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.deletedAt IS NULL")
    Optional<Order> findById(Long orderId);

    /**
     * 단일 Order를 조회합니다. 이 메소드는 삭제 된 Order 엔티티도 포함하여 결과를 반환합니다. 이 메소드는 'orderSheet' 필드를 Eager로
     * 로딩합니다.
     *
     * @param orderId 조회하고자 하는 Order의 ID입니다.
     * @return Optional<Order> - 해당 ID를 가진 Order 객체. 만약 Order가 존재하지 않으면, 비어 있는 Optional을 반환합니다.
     */
    @EntityGraph(attributePaths = {"orderSheet"})
    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> findByIdWithOrderSheetIncludeDeleted(String orderId);

    /**
     * 단일 Order를 조회합니다. 이 메소드는 삭제 된 Order 엔티티도 포함하여 결과를 반환합니다.
     *
     * @param orderId 조회하고자 하는 Order의 ID입니다.
     * @return Optional<Order> - 해당 ID를 가진 Order 객체. 만약 Order가 존재하지 않으면, 비어
     * 있는 Optional을 반환합니다.
     */
    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> findByIdIncludeDeleted(String orderId);

}
