package Autocrypt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Autocrypt.dto.Member;
import lombok.Data;

@Data
@Service
public class memberServiceImpl implements MemberService {
	
	@Autowired
	private Member member;
	
	@PersistenceContext
	private EntityManager entityManager;
	 
	 

	@Override
	public Map<String, Object> checkLoginAvailable(Map<String, Object> param) {

		Map<String, Object> rs = new HashMap<String, Object>();
		
		String loginId = (String) param.get("loginId");
		String loginPw = (String)param.get("loginPw");

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAProject");
		//EntityManager em = emf.createEntityManager();
		
		//Member member = em.find(member, id);
		

		Query query = entityManager.createNativeQuery("SELECT id, password FROM Member");
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List r = query.getResultList();
		 
		for(Object obj : r) {
		        Map row = (Map) obj;
		             
		        for(Object key: row.keySet()) {                    
		            System.out.println("key : " + key +"value : " + row.get(key));
		        }      
		}
		
		
		//로그인 성공 또는 실패 여부를 controller로 보냄 
		if(member == null) {
			rs.put("resultCode", "F1");
			rs.put("msg", "해당 회원이 존재하지 않습니다.");
		}else if (Member.password.equals(loginPw) == false) {
		//입력 비밀번호와 DB 비밀번호가 동일하지 않는 경우 
			rs.put("resultCode", "F2");
			rs.put("msg", "비밀번호가 일치하지 않습니다.");
		} else {
			rs.put("resultCode", "S1");
			rs.put("msg", "로그인 성공");
		}
		
		return rs;

}}
