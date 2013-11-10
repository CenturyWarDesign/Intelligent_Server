package com.centurywar;

import java.util.Date;

public class BaseModel {
	public int getTime() {
		Date date = new Date();
		return (int) (date.getTime() / 1000);
	}
}
