package com.effective_mobile.test_project.specifications;

import com.effective_mobile.test_project.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecifications {

    public static Specification<User> withFilters(LocalDate birthDate, String phone, String name, String email) {
        return (root, query, builder) -> {
            // Создаем список предикатов для добавления в запрос
            var predicates = builder.conjunction();

            // Добавляем условия фильтрации, если соответствующие параметры были переданы
            if (birthDate != null) {
                predicates = builder.and(predicates, builder.greaterThanOrEqualTo(root.get("birthDate"), birthDate));
            }

            if (phone != null && !phone.isEmpty()) {
                predicates = builder.and(predicates, builder.like(root.get("phone"), "%" + phone + "%"));
            }

            if (name != null && !name.isEmpty()) {
                predicates = builder.and(predicates, builder.like(root.get("name"), name + "%"));
            }

            if (email != null && !email.isEmpty()) {
                predicates = builder.and(predicates, builder.equal(root.get("email"), email));
            }

            return predicates;
        };
    }
}
