#sql("queryAllNews")
	SELECT * FROM news WHERE enable = 1 ORDER BY create_date desc
#end

#sql("queryHeadNews")
	SELECT * FROM news WHERE enable = 1 and head = 1 ORDER BY create_date desc limit 3
#end

#sql("deleteById")
  UPDATE news SET enable = 0 WHERE id = #para(0)
#end