package Autocrypt.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import Autocrypt.service.MemberService;

@SessionAttributes("user")
@RestController
public class MemberController {

	@Autowired	
	private MemberService memberService ; 
		
		String showLogin() {
			
			return "Login";
		}

		@RequestMapping("member/Login.do")
		String doLogin(HttpSession session, Model model, @RequestParam Map<String, Object> param) {
			
			//로그인이 가능한지 알아보도록 한다 
			Map<String, Object> rs = memberService.checkLoginAvailable(param);
			
			//로그인 성공인지 실패인지 받아줌 
			String resultCode =(String) rs.get("resultCode");
			String msg = (String) rs.get("msg");
			
			
			if (resultCode.startsWith("S")) {
				//브라우저마다 로그인을 한 계정을 알기 위해 Session 사용 
				//id가 들어감으로써 로그인 
				int  loginedMemberId = (int) rs.get("id");
				//로그인 처리 (이 브라우저에 대한 로그인 저장)
				session.setAttribute("loginedMemberId", loginedMemberId);
				msg =  "로그인 되었습니다";
				
				//로그인된 후 보일 화면 
				String redirectUrl = "/article/list";
				
				model.addAttribute("jsAlertMsg", msg);
				model.addAttribute("jsLocationReplaceUrl", redirectUrl);
			}else {
				model.addAttribute("jsAlertMsg", msg);
				model.addAttribute("jsHistoryBack", true);
				
			}
			
			
			return "redirect";
		}
}