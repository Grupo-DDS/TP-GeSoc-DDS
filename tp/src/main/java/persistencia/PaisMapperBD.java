package persistencia;

import java.util.List;

import javax.persistence.EntityManager;

import com.API.Pais;

public class PaisMapperBD {
	public void insert(Pais p) {
		EntityManager em = BDUtils.getEntityManager();
		BDUtils.comenzarTransaccion(em);
		em.persist(p);
		
		BDUtils.commit(em);		
	}
	public void insert(List<Pais> paises) {
		EntityManager em = BDUtils.getEntityManager();
		BDUtils.comenzarTransaccion(em);
		int index = 0;
		int size = paises.size();
		while(index<size) {
			Pais pais = paises.get(index);
			em.persist(pais);
			index++;
		}
		BDUtils.commit(em);		
	}
	public void update(Pais pais) {
		EntityManager em = BDUtils.getEntityManager();
		BDUtils.comenzarTransaccion(em);
		em.merge(pais);
		BDUtils.commit(em);		
	}
	
	public void delete(Pais pais) {
		EntityManager em = BDUtils.getEntityManager();
		BDUtils.comenzarTransaccion(em);
		em.remove(pais);
		BDUtils.commit(em);	
	}
	
	public Pais obtenerPais(Long id) {		
		EntityManager em = BDUtils.getEntityManager();
		BDUtils.comenzarTransaccion(em);
		return em.find(Pais.class,id);	
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Pais> obtenerObjetos() {		
		EntityManager em = BDUtils.getEntityManager();
		BDUtils.comenzarTransaccion(em);
		return em.createQuery("FROM Pais").getResultList();
	}
}
