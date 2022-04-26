package com.unosquare.carmigo.util;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class SpecificationsBuilder<E>
{
    private final List<SearchCriteria> params = new ArrayList<>();

    public SpecificationsBuilder<E> with(final String key, final String operation, final Object value)
    {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<E> build()
    {
        if (params.size() == 0) {
            return null;
        }
        final List<Specification<E>> specs = params.stream()
                .map(EntitySpecification<E>::new)
                .collect(Collectors.toList());
        Specification<E> result = specs.get(0);
        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
