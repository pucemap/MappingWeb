package com.map.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CodeGenerator {

	public static BigDecimal generate() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringBuilder strNum = new StringBuilder();
		Random random = new Random();
		BigDecimal number;
		
		for (int i = 1; i < 7; i++) {
			list.add(random.nextInt(9)+1);
		}
		
		Collections.shuffle(list);
		for (Integer integer : list) {
			strNum.append(integer);
		}
		
		//int finalInt = Integer.parseInt(strNum.toString());
		number = new BigDecimal(strNum.toString());
		return number;
	}

}
