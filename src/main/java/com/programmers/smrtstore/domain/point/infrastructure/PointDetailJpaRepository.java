package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointDetailJpaRepository extends JpaRepository<PointDetail, Long>, PointDetailRepositoryCustom {

}
