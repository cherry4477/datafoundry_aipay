package com.ldp.datahub.util;

import com.google.gson.JsonObject;

public class test {

	public static void main(String[] args) {
		JsonObject json = new JsonObject();
		json.addProperty("user", "aaaaa");
		System.out.println(json.toString());
	}

}
