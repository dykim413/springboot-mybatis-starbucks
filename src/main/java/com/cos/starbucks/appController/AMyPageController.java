package com.cos.starbucks.appController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cos.starbucks.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cos.starbucks.repository.CoffeeRepository;
import com.cos.starbucks.repository.MenuRepository;
import com.cos.starbucks.repository.MypageRepository;
import com.cos.starbucks.repository.UserRepository;
import com.cos.starbucks.security.MyUserDetails;

@RestController
@RequestMapping("/android/mypage")
public class AMyPageController {

	@Autowired
	private MypageRepository mRepo;
	@Autowired
	private UserRepository uRepo;
	@Autowired
	private CoffeeRepository cRepo;
	@Autowired
	private MenuRepository menuRepo;
	
	
	@GetMapping("")
	public HashMap<String, Object> mypage(@AuthenticationPrincipal MyUserDetails userDetail) {
		if(userDetail != null) {
			// 내 카드
			User_card myCard = mRepo.findByUserIdCard(userDetail.getUser().getId());
			
			// MyMenu - 커피
			List<MyCoffee> myCoffeeList = mRepo.findByUserId(userDetail.getUser().getId());
			List<Coffee> coffeeList = new ArrayList<>();
			if(myCoffeeList.size() > 0) {
				List<Integer> myCoffeeIdList = new ArrayList<>();
				for (MyCoffee myCoffee : myCoffeeList) {
					myCoffeeIdList.add(myCoffee.getCoffeeId());
				}
				coffeeList = cRepo.findByIds(myCoffeeIdList);
				coffeeList.forEach( item -> item.setKind("coffee") );
			}
			
			// MyMenu - 음료
			List<MyBeverage> myBeverageList = mRepo.findByUserIdBev(userDetail.getUser().getId());
			List<Beverage> beverageList = new ArrayList<>();
			if(myBeverageList.size() > 0) {
				List<Integer> myBeverageIdList = new ArrayList<>();
				for (MyBeverage myBeverage : myBeverageList) {
					myBeverageIdList.add(myBeverage.getBeverageId()); 
				}
				beverageList = menuRepo.findByIds(myBeverageIdList);
				beverageList.forEach( item -> item.setKind("beverage"));
			}

            // MyMenu - 음식
            List<MyFood> myFoodList = mRepo.findByUserIdFood(userDetail.getUser().getId());
            List<Food> foodList = new ArrayList<>();
            if(myFoodList.size() > 0) {
                List<Integer> myFoodIdList = new ArrayList<>();
                for (MyFood myFood : myFoodList) {
                    myFoodIdList.add(myFood.getFoodId());
                }
                foodList = menuRepo.findByFoodIds(myFoodIdList);
                foodList.forEach( item -> item.setKind("food") );
            }

            // 구매내역
			List<Trade> tradeList = mRepo.findTradeLog(userDetail.getUser().getId());
			
			HashMap<String, Object> mypageDTO = new HashMap<>();
			mypageDTO.put("myCard", myCard);
			mypageDTO.put("myCoffeeList", coffeeList);
			mypageDTO.put("myBeverageList", beverageList);
			mypageDTO.put("myFoodList", foodList);
			mypageDTO.put("tradeList", tradeList);
			
			return mypageDTO;
		}
		
		return null;
	}
		
	
	@PostMapping("/delete_card")
	public String mypageDeleteCard(@AuthenticationPrincipal MyUserDetails userDetail, @RequestParam int id) {
		if(userDetail != null) {
			int userId = mRepo.checkMyCard(id);
			if (userId == userDetail.getUser().getId())
				mRepo.deleteCard(id);
			
			return "1";	
		}
		return "notlogin";
	}
	
	@PostMapping("/save_beverage")
	public String mypageSaveBeverage(@AuthenticationPrincipal MyUserDetails userDetail, MyBeverage myBeverage) {
		if(userDetail != null) {
			myBeverage.setUserId(userDetail.getUser().getId());
			int num = mRepo.AfindMyBeverage(myBeverage);
			if(num>0) {
				return "0";
			}			
			mRepo.bevSave(myBeverage.getBeverageId(), userDetail.getUser().getId(), myBeverage.getBeverageName(), myBeverage.getPrice());
			return "1";
		}
				
		return "notlogin";
	}
	
	@PostMapping("/delete_beverage")
	public String mypageDeleteBeverage(@AuthenticationPrincipal MyUserDetails userDetail, MyBeverage myBeverage) {
		if(userDetail != null) {
			myBeverage.setUserId(userDetail.getUser().getId());
			mRepo.AdeleteMyBeverage(myBeverage);
			return "1";
		}
		
		return "notlogin";
	}
	
	@PostMapping("/save_coffee")
	public String mypageSaveCoffee(@AuthenticationPrincipal MyUserDetails userDetail, MyCoffee myCoffee) {
		if(userDetail != null) {
			myCoffee.setUserId(userDetail.getUser().getId());
			int num = mRepo.AfindMyCoffee(myCoffee);
			if(num>0) {
				return "0";
			}	
			mRepo.coffeeSave(myCoffee.getCoffeeId(), userDetail.getUser().getId(), myCoffee.getCoffeeName(), myCoffee.getPrice());
			return "1";
		}
				
		return "notlogin";
	}
	
	@PostMapping("/delete_coffee")
	public String mypageDeleteCoffee(@AuthenticationPrincipal MyUserDetails userDetail, MyCoffee myCoffee) {
		if(userDetail != null) {
			myCoffee.setUserId(userDetail.getUser().getId());
			mRepo.AdeleteMyCoffee(myCoffee);
			return "1";
		}
		
		return "notlogin";
	}

    @PostMapping("/save_food")
    public String mypageSaveFood(@AuthenticationPrincipal MyUserDetails userDetail, MyFood myFood) {
        if(userDetail != null) {
            myFood.setUserId(userDetail.getUser().getId());
            int num = mRepo.AfindMyFood(myFood);
            if(num>0) {
                return "0";
            }
            mRepo.foodSave(myFood.getFoodId(), userDetail.getUser().getId(), myFood.getFoodName(), myFood.getPrice());
            return "1";
        }

        return "notlogin";
    }
	
	
}
