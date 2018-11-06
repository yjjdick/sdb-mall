#sql("queryNotButtonList")
	SELECT * FROM sys_menu WHERE TYPE != 2 ORDER BY order_num asc
#end