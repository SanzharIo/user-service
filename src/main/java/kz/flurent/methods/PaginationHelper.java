package kz.flurent.methods;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PaginationHelper {

    private CallApi callApi;

    public static PageRequest paginate(Optional<Integer> page,
                            Optional<Integer> size,
                            Optional<String[]> sortBy) {
        Sort sort = Sort.by("id");
        if(sortBy.isPresent()){
            String[] sorters = sortBy.get();
            List<Sort.Order> sorts = Arrays.stream(sorters)
                    .map(s -> s.split("-")[0].trim().equalsIgnoreCase("asc")
                            ? Sort.Order.asc(s.split("-")[1]) : Sort.Order.desc(s.split("-")[1]))
                    .collect(Collectors.toList());
            sort = Sort.by(sorts);
        }
        return PageRequest.of(page.orElse(0),size.orElse(5),sort);
    }
}
