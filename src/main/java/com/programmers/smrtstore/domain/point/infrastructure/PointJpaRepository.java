package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointJpaRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {

}
