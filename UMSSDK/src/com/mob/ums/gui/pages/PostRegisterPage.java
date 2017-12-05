package com.mob.ums.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.ums.User;

public class PostRegisterPage extends Page<PostRegisterPage> {
	private User me;
	
	public PostRegisterPage(Theme theme) {
		super(theme);
	}
	
	public void setUser(User me) {
		this.me = me;
	}
	
	public User getUser() {
		return me;
	}
	
}
