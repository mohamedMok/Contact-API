package com.ebiz.connection;

import com.ebiz.connection.ESConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.ws.rs.Produces;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * Created by ebiz on 12/02/2015.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ebiz.dao.EsDAO.repository")
public class ConfigSpringES {

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        System.out.println("ES Connection");
        return new ElasticsearchTemplate(ESConnection.getInstance().getDB());
    }
}
