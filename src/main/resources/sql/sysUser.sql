#sql("queryAllPerms")
  select m.perms from sys_user_role ur
			LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id
			LEFT JOIN sys_menu m on rm.menu_id = m.menu_id
	where ur.user_id = #para(userId)
#end

#sql("queryAllMenuId")
  select distinct rm.menu_id from sys_user_role ur
			LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id
		where ur.user_id = #para(userId)
#end

#sql("queryByUserName")
  select * from sys_user where username = #para(username)
#end