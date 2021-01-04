package kr.co.nologaja.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class BuyerDAO implements IBuyerDAO{

	@Override
	public boolean blogin(String uid, String upw) {
		boolean result=false;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("one", uid);
		map.put("two", upw);
		
		int count = sqlSession.selectOne("member.blogin", map);
		if(count==1) {
			result = true;
			
		}
		
		return result;

	}

	@Override
	public String read_bgrd(String uid, String upw) {
		String bgrd="";
		
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("bgrd_uid", uid);
		map1.put("bgrd_upw", upw);
		bgrd = sqlSession.selectOne("member.read_bgrd", map1);
		
		return bgrd;
	}

	@Inject //<-사용자가 직접 만들지 않고 스프링에서 생성해서 주입을 시킴
	SqlSession sqlSession;
	
	@Override
	public List<BuyerDTO> list() {
		return sqlSession.selectList("member.blist");
	}

	@Override
	public void insert(BuyerDTO dto) {
		sqlSession.insert("member.binsert",dto);
		
	}

	@Override
	public BuyerDTO detail(String uid) {
		return sqlSession.selectOne("member.bdetail", uid);
	}

	@Override
	public void delete(String uid) {
		sqlSession.delete("member.bdelete",uid);
		
	}

	@Override
	public void update(BuyerDTO dto) {
		sqlSession.update("member.bupdate", dto);
		
	}

	@Override
	public boolean checkpasswd(String uid, String upw) {
		boolean result=false;
		//mapper.xml에 전달한 값이 2개 ㅇ상인 경우 주로 DTO또는 MAP으로 전달
		Map<String, String> map = new HashMap<String, String>();
		map.put("one", uid);
		map.put("two", upw);
		
		int count = sqlSession.selectOne("member.bcheck_passwd", map);
		if(count==1) result = true;
		
		return result;
	}

	@Override
	public int duplecateID(String uid) {
		int count = sqlSession.selectOne("member.bduplicateID", uid);
		return count;
	}
	
}
