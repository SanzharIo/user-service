package kz.flurent.predicate;

import com.querydsl.core.types.dsl.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isNumeric;


@Data
@AllArgsConstructor
public class Predicate {

    private SearchCriteria criteria;

    public <T> BooleanExpression getPredicate(Class<T> c) {
        PathBuilder<T> entityPath = new PathBuilder<>(c, c.getSimpleName().toLowerCase());
        String criteriaValue = criteria.getValue().toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date date;
        Field field = null;
        try {
            field = c.getDeclaredField(criteria.getKey());
        } catch (NoSuchFieldException ignored) {
        }
        try {
            date = df.parse(criteriaValue);
        } catch (ParseException e) {
            date = null;
        }
        if (isNumeric(criteriaValue)) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            int value = Integer.parseInt(criteriaValue);
            switch (criteria.getOperation()) {
                case ":" -> {
                    return path.eq(value);
                }
                case ">" -> {
                    return path.goe(value);
                }
                case "<" -> {
                    return path.loe(value);
                }
            }
        } else if (date != null) {
            DateTimePath<Date> path = entityPath.getDateTime(criteria.getKey(), Date.class);
            switch (criteria.getOperation()) {
                case ">" -> {
                    return path.after(date);
                }
                case "<" -> {
                    return path.before(getEndDayTime(date));
                }
                case ":" -> {
                    return path.eq(date);
                }
            }
        } else if (criteriaValue.equalsIgnoreCase("true") || criteriaValue.equalsIgnoreCase("false")) {
            BooleanPath path = entityPath.getBoolean(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.eq(Boolean.parseBoolean(criteriaValue));
            }
        } else {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteriaValue);
            }
        }
        return null;
    }

    public Date getEndDayTime(Date date) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.HOUR, 23);
        cl.add(Calendar.MINUTE, 59);
        cl.add(Calendar.SECOND, 59);
        return cl.getTime();
    }

    private <T, E extends Enum<E>> BooleanExpression handleEnumField(PathBuilder<T> entityPath, Field field, String criteriaValue) {
        @SuppressWarnings("unchecked")
        Class<E> enumType = (Class<E>) field.getType();
        EnumPath<E> path = entityPath.getEnum(field.getName(), enumType);
        E value = Enum.valueOf(enumType, criteriaValue.toUpperCase());
        return path.eq(value);
    }
}
