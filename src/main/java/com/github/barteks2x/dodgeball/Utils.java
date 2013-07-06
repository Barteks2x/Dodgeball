package com.github.barteks2x.dodgeball;

import java.util.List;

public class Utils {

	@SuppressWarnings("unchecked")
	public static <T extends Object> List<T> addArrayToList(List<T> list, T[] obj) {
		for (int i = 0; i < obj.length; ++i) {
			list.add(obj[i]);
		}
		return list;
	}
}
