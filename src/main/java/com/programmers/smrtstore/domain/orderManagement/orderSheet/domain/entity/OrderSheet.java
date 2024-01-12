package com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity;

import static com.programmers.smrtstore.core.properties.ErrorCode.INVALID_USER;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.exception.OrderSheetException;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_sheet_TB")
@Entity
public class OrderSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    // TODO: orderedProduct 를 먼저 생성하고 orderSheet 에 넣어주는 것에 대한 고민. (setter 를 사용하기 싫음)
    @OneToMany(mappedBy = "orderSheet", cascade = CascadeType.ALL)
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public OrderSheet(
        Long id, User user, List<OrderedProduct> orderedProducts, LocalDateTime createdAt
    ) {
        this.id = id;
        this.user = user;
        this.orderedProducts = orderedProducts;
        orderedProducts.forEach(orderedProduct -> orderedProduct.setOrderSheet(this));
        this.createdAt = createdAt;
    }

    public void validateOwnerOfOrderSheet(User user) {
        if (!this.user.equals(user)) {
            throw new OrderSheetException(INVALID_USER, String.valueOf(user.getId()));
        }
    }

}
