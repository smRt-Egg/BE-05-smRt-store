package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointDetailJpaRepository extends JpaRepository<PointDetail, Long>, PointDetailRepositoryCustom {

    @Query("SELECT pd FROM PointDetail pd WHERE pd.pointId = :pointId")
    List<PointDetail> findUsedDetailByPointId(Long pointId);
}
