package com.programmers.smrtstore.domain.orderManagement.delivery.infrastructure;

import com.programmers.smrtstore.domain.orderManagement.delivery.entity.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryInfoJpaRepository extends JpaRepository<DeliveryInfo, Long> {

}
