package com.xiaomi.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.TransportUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
public class BeanConfiguration {

    @Bean
    public ElasticsearchClient elasticsearchClient() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, KeyManagementException {
        ElasticsearchClient esClient = ElasticsearchClient.of(b -> b
                .host("http://47.109.104.160:9200/")
                .usernameAndPassword("admin", "123456")
        );

        return esClient;
    }
}