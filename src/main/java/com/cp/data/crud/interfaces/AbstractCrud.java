package com.cp.data.crud.interfaces;

import java.util.Collections;
import java.util.List;
import com.cp.util.AppLog;

import jakarta.persistence.EntityManager;

public abstract class AbstractCrud<T> {

    private Class<T> entityClass;

    protected AbstractCrud(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    protected abstract void close();

    // Interface funcional para operações com transação
    @FunctionalInterface
    private interface TransactionalOperation {
        void execute(EntityManager em) throws Exception;
    }

    private Exception executeTransaction(TransactionalOperation operation, String messageReturn) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            operation.execute(em);
            em.flush();
            em.getTransaction().commit();
            
            AppLog.getInstance().info(messageReturn);
            return null;
        } 
        
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            AppLog.getInstance().warn("Erro no banco de dados: " + this.getClass().getName() + " ==> " + e.getMessage());
            return e;
        }
    }

    public Exception persist(T entity) {
        return executeTransaction(
            em -> em.persist(entity),
            "Inserção de dados feito com sucesso"
        );
    }

    public Exception merge(T entity) {
        return executeTransaction(
            em -> em.merge(entity),
            "Alteração de dados feito com sucesso"
        );
    }

    public Exception remove(T entity) {
        return executeTransaction(
            em -> em.remove(em.merge(entity)),
            "Remoção de dados feito com sucesso"
        );
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> getAll() {
        try {
            EntityManager em = getEntityManager();
            var cb = em.getCriteriaBuilder();
            var cq = cb.createQuery(entityClass);
            cq.select(cq.from(entityClass));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            AppLog.getInstance().warn("Erro ao buscar todos registros: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<T> findRange(int[] range) {
        try {
            EntityManager em = getEntityManager();
            var cb = em.getCriteriaBuilder();
            var cq = cb.createQuery(entityClass);
            cq.select(cq.from(entityClass));
            var q = em.createQuery(cq);
            q.setFirstResult(range[0]);
            q.setMaxResults(range[1] - range[0] + 1);
            return q.getResultList();
        } catch (Exception e) {
            AppLog.getInstance().warn("Erro ao buscar intervalo: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public int count() {
        EntityManager em = getEntityManager();
        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Long.class);
        var rt = cq.from(entityClass);
        cq.select(cb.count(rt));
        var q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
