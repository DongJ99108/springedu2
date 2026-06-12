package com.example.springedu2.controller;

import com.example.springedu2.entity.Visitor;
import com.example.springedu2.repository.VisitorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VisitorController {


    // 1. @Autowired 이용 생성자 주입
    // @Autowired
    // private VisitorRepository visitorRepository;

    // 2. 생성자 주입 : 요즘 방식
    // private VisitorRepository visitorRepository;

    /*
    public VisitorController(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }
    */

    // 3. 생성자 주입 다른 방법, final 가능
    // 이거 쓸라면 맨 위에 Public class 에서 @RequiredArgsConstructor 써야함 : Lombok 이 필수임
    // final이 붙는 변수는 한번만 초기화 할 수 있는 변수이다.
    private final VisitorRepository visitorRepository;


    // 방명록 조회
    @GetMapping("/vlist")
    public ModelAndView vlist() {
        List<Visitor> visitors = visitorRepository.findAll(); // 목록 조회
        return visitorView(visitors, null);
    }

    private ModelAndView visitorView(List<Visitor> visitors, String buttonText) {
        ModelAndView mv = new ModelAndView("/visitorView"); // setViewName 안넣고 싶으면 이런식으로 파라미터 자리에 넣으면 됨
        // mv.setViewName("visitorView"); // visitorView.html(Model 사용) - thymeleaf
        if( visitors.isEmpty() ) {
            mv.addObject("msg", "조회된 결과가 없습니다.");
        } else {
            mv.addObject("vList", visitors);
        }
        if( buttonText != null ) {
            mv.addObject("buttonText", buttonText);
        }
        return mv;
    }

    @GetMapping("/vsearch")
    public ModelAndView vsearch() {

        return null;
    }

    // @Valid : form 에서 넘어온 자료를 @Entity 에 있는 설정(@Id, @NotBlank, @Column(nullable=false))과 비교해서 입력 data 를 검증
    @PostMapping("vinsert")
    @Transactional
    public String vinsert(@Valid Visitor visitor,
                  BindingResult bindingResult,
                  Model model) {

        System.out.println("visitor: " + visitor);
        System.out.println("bindingResult: " + bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("msg", "이름과 내용을 모두 입력하세요");
            return "visitorView"; // visitorView.html
        }
        visitorRepository.save(visitor); // insert 실행 entity type
        // entity 객체를 사용해야한다. save() 여기 괄호안에 적는건 entity 타입만 적을 수 있음

        return "redirect:/vlist";

    }

}
