package kz.flurent.predicate;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
public class PredicatesBuilder {
    private List<SearchCriteria> params;

    public PredicatesBuilder() {
        params = new ArrayList<>();
    }
    public PredicatesBuilder with(
            String key, String operation, Object value) {

        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public <T> BooleanExpression build(Class<T> c) {
        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        if (params.size() == 0) {
            return result;
        }

        List<BooleanExpression> predicates = params.stream().map(param -> {
            Predicate predicate = new Predicate(param);
            return predicate.getPredicate(c);
        }).filter(Objects::nonNull).toList();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}
