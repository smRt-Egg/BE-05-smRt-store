package com.programmers.smrtstore.domain.keep.infrastructure;

import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeepJpaRepository extends JpaRepository<Keep, Long>, KeepRepositoryCustom {
}
