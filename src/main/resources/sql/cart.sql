#sql("listDetail")
	SELECT
	c.id id,
	g.name name,
	g.caption caption,
	p.sn product_id,
	p.specification_values specification_values,
	c.quantity quantity,
	p.price price,
  g.image image
FROM
	cart c
	LEFT JOIN product p ON c.product_id = p.sn
	LEFT JOIN goods g ON p.goods_sn = g.sn
WHERE
	c.user_id = #para(userId)
#end