package com.viratech.specification;

import com.viratech.dto.MovieFilterRequest;
import com.viratech.model.Movie;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecification {

    public static Specification<Movie> filter(MovieFilterRequest filter){

        return(root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();
            if(filter.genre() != null && !filter.genre().isBlank()){
                predicates.add(
                        cb.like(cb.lower(root.get("genre")),
                                "%" + filter.genre().toLowerCase() + "%")
                );
            }

            if(filter.minRating() != null){
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("rating"),
                                filter.minRating()
                        )
                );
            }

            if(filter.releaseYear() != null){
                predicates.add(
                        cb.equal(
                                root.get("releaseYear"),
                                filter.releaseYear()
                        )
                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
