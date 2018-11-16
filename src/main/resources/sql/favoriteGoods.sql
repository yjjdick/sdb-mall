#sql("list")
	SELECT
	g.sn goodsId,
  g.name name,
	g.caption caption,
	g.price price,
  g.image image
FROM
	favorite_goods f
	LEFT JOIN goods g ON f.favorite_goods = g.sn
WHERE
	f.favorite_user = #para(userId)
#end