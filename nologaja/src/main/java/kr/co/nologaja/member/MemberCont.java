package kr.co.nologaja.member;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class MemberCont {
	
	@Inject
	BuyerDAO bdao;
	
	@Inject
	SellerDAO sdao;
	
	public MemberCont() {
		System.out.println("==MemberCont()==");
	}
	
	//회원가입 페이지(배너 상의 회원가입 버튼)
	@RequestMapping(value = "/memberform.do")
	   public ModelAndView form() {
	      ModelAndView mav = new ModelAndView();
	      mav.setViewName("member/memberform");
	      return mav;
	   }
	
	//회원가입 구매자 dto
	@RequestMapping(value = "/buyerjoin.do")
	public String insert(@ModelAttribute BuyerDTO dto) {
		bdao.insert(dto);
		return "member/login";
	}//insert() end
	
	//회원가입 판매자 dto
	@RequestMapping(value = "/sellerjoin.do")
	public String insert(@ModelAttribute SellerDTO dto) {
		sdao.insert(dto);
		return "member/login";
	}//insert() end
	
	@RequestMapping(value = "/login.do")
	   public ModelAndView login() {
	      ModelAndView mav = new ModelAndView();
	      mav.setViewName("member/login");
	      return mav;
	   }
	
	//회원정보수정 폼 불러오기
	@RequestMapping(value = "updateform.do")
	public ModelAndView update(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String ugrd = (String)session.getAttribute("ugrd");
		System.out.println(ugrd);
	//B1은 판매회원, C1는 일반회원
		if(ugrd.equals("B1")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("member/s_updateform");
			String suid = (String)session.getAttribute("suid");
			mav.addObject("dto", sdao.detail(suid));
			mav.addObject("ugrd",ugrd);
			return mav;
		} else if(ugrd.equals("C1")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("member/b_updateform");
			String uid = (String)session.getAttribute("uid");
			mav.addObject("dto", bdao.detail(uid));
			mav.addObject("ugrd",ugrd);
			return mav;
		}
		return null;
	}
	
	//회원정보수정하기
	@RequestMapping(value = "updateproc.do")
	public ModelAndView update(@ModelAttribute BuyerDTO bdto, SellerDTO sdto, HttpServletRequest req) {
		HttpSession session = req.getSession();
		String ugrd = (String)session.getAttribute("ugrd");
	//B1은 판매회원, C1는 일반회원
		if(ugrd.equals("B1")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("index");
			sdao.update(sdto);
			return mav;
		} else if(ugrd.equals("C1")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("index");
			bdao.update(bdto);
			return mav;
		}
		return null;
	}

	
	@RequestMapping("/blogin.do")
	public ModelAndView blogin(String uid, String upw, HttpServletRequest request) {
		boolean result = bdao.blogin(uid, upw);
		ModelAndView mav = new ModelAndView();
		if(result==true) {
			String ugrd=bdao.read_bgrd(uid, upw);
			mav.setViewName("index");
			HttpSession session=request.getSession();
			session.setAttribute("uid", uid);
			session.setAttribute("ugrd", ugrd);
			session.setMaxInactiveInterval(20*60*24);
		}else {
			mav.setViewName("member/login");
		}
		return mav;
	}//blogin() end
	
	
	//판매자 로그인처리
	@RequestMapping("/slogin.do")
	public ModelAndView slogin(String suid, String supw, HttpServletRequest request) {
		boolean result = sdao.slogin(suid, supw);
		ModelAndView mav = new ModelAndView();
		if(result==true) {
			String ugrd=sdao.read_sgrd(suid, supw);
			mav.setViewName("index");
			HttpSession session=request.getSession();
			session.setAttribute("suid", suid);
			session.setAttribute("ugrd", ugrd);
			session.setMaxInactiveInterval(20*60*24);
		}else {
			mav.setViewName("member/login");
		}
		return mav;
	}//slogin() end
	
	//로그아웃
	@RequestMapping("/logout.do")
    public ModelAndView logout(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.removeAttribute("suid");
		session.removeAttribute("uid");
		ModelAndView mav = new ModelAndView();
	    mav.setViewName("member/login");
	    return mav;
	}
}//class end
