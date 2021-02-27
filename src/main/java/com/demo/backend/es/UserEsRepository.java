package com.demo.backend.es;

import com.demo.backend.esModel.UserEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserEsRepository extends ElasticsearchRepository<UserEs, Integer> {
}
