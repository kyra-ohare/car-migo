package com.unosquare.carmigo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class EntitySpecification<E> implements Specification<E>
{
    private static final long serialVersionUID = 5065608184266278333L;
    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(final Root<E> root, final CriteriaQuery<?> query, final CriteriaBuilder builder)
    {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else {
            if (criteria.getOperation().equalsIgnoreCase("<")) {
                return builder.lessThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            } else {
                if (criteria.getOperation().equalsIgnoreCase(":")) {
                    if (root.get(criteria.getKey()).getJavaType() == String.class) {
                        return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
                    } else {
                        return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                    }
                }
            }
        }
        return null;
    }

    public SearchCriteria getCriteria()
    {
        return criteria;
    }
}
