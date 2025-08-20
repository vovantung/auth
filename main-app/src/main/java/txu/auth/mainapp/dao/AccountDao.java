package txu.auth.mainapp.dao;

import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import txu.auth.mainapp.base.AbstractDao;
import txu.auth.mainapp.entity.AccountEntity;

@Repository
public class AccountDao extends AbstractDao<AccountEntity> {
    @Transactional
    public AccountEntity save(AccountEntity accountEntity) {

            if (accountEntity.getId() == null || accountEntity.getId() == 0) {
            persist(accountEntity);
            return accountEntity;
        } else {
            return merge(accountEntity);
        }
    }

    @Override
    public AccountEntity findById(Object Id) {
        return super.findById(Id);
    }

    @Transactional
    public void remove(AccountEntity accountEntity) {
        accountEntity = merge(accountEntity);
        getEntityManager().remove(accountEntity);
    }

    public AccountEntity getByUsername(String username){
        StringBuilder queryString = new StringBuilder("SELECT A FROM AccountEntity AS A WHERE username=:username");
        Query query = getEntityManager().createQuery(queryString.toString());
        query.setParameter("username", username);
        return getSingle(query);
    }

    public AccountEntity findByUsername(String username){
        StringBuilder queryString = new StringBuilder("SELECT A FROM AccountEntity AS A WHERE username=:username");
        Query query = getEntityManager().createQuery(queryString.toString());
        query.setParameter("username", username);
        return getSingle(query);
    }

}
