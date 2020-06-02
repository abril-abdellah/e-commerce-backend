package com.abril.ecommerce.config;

import com.abril.ecommerce.entity.Product;
import com.abril.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        HttpMethod[] unsupportedMethods = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};
        //Désactiver les méthodes Post, Put, Delete pour Product
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods));
        //Désactiver les méthodes Post, Put, Delete pour ProductCategory
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods));

        //Appeler intenal helper
        exposeIds(config);
    }
    private void exposeIds(RepositoryRestConfiguration config){
        //getting entities classes from entityManager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        // create array of entities type
        List<Class> entityClasses = new ArrayList<>();
        //get entity types for entities
        for (EntityType et: entities){
            entityClasses.add(et.getJavaType());
        }
        //expose entity ids for array of entities and domain type
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
