package com.hust.baseweb.rest.user;

import java.util.UUID;

import com.hust.baseweb.entity.PartyType;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true, excerptProjection = UserRestBriefProjection.class)
//@RepositoryRestResource(exported = true)
public interface UserRestRepository extends PagingAndSortingRepository<DPerson, UUID>,  QuerydslPredicateExecutor<DPerson>, QuerydslBinderCustomizer<QDPerson>  
{
	
	public Page<DPerson> findByType(PartyType type,Pageable page);
	
    default void customize(QuerydslBindings bindings, QDPerson store) {
//		bindings.bind(store.address.city).single((path, value) -> path.startsWith(value));
        //bindings.bind(String.class).s
        //single((StringPath path, String value) -> path.contains(value));

        bindings.bind(String.class)
          .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
	}
}
