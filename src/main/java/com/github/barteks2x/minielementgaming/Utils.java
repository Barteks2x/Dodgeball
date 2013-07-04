package com.github.barteks2x.minielementgaming;

import java.util.List;

public class Utils {

	@SuppressWarnings("unchecked")
	public static List addArrayToList(List list, Object[] obj) {
		for (int i = 0; i < obj.length; ++i) {
			list.add(obj[i]);
		}
		return list;
	}
}
