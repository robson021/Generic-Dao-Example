package com.example.dao;

import com.example.entity.BaseJpaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
public abstract class GenericDao<T extends BaseJpaEntity> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    private final String className = clazz.getSimpleName();

    private final EntityManager em;

    protected GenericDao(EntityManager em) {
        this.em = em;
    }

    public void save(T entity) {
        saveAll(Collections.singletonList(entity));
    }

    public void saveAll(Iterable<T> entities) {
        for (T entity : entities) {
            em.persist(entity);
        }
        log.debug("Saved entities: {}", entities);
    }

    public void delete(Long id) {
        em.createQuery("delete from " + className + " c where c.id = :id")
                .setParameter("id", id)
                .executeUpdate();

        log.debug("Deleted {} with id: {}", className, id);
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(em.find(clazz, id));
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return em.createQuery("from " + className, clazz).getResultList();

    }

    @Transactional(readOnly = true)
    public List<T> findByField(String fieldName, Object fieldValue) {
        return findByFields(Collections.singletonMap(fieldName, fieldValue));
    }

    @Transactional(readOnly = true)
    public List<T> findByFields(Map<String, Object> fieldValueMap) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);

        Root<T> root = criteriaQuery.from(clazz);
        CriteriaQuery<T> select = criteriaQuery.select(root);

        for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            select.where(builder.equal(root.get(field), value));
        }

        return em.createQuery(criteriaQuery).getResultList();
    }

}