package hu.readdeo.money.management.svc;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "test", path = "tests")
public interface TestRepository extends PagingAndSortingRepository<Test, Long> {}
