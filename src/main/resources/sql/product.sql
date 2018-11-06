#sql("listDetailByProductIds")
	SELECT
	g.`name` name,
	g.`model` model,
	g.`sn` goods_sn,
	g.caption caption,
	g.image image,
  p.specification_values specification_values,
  p.stock stock,
	p.sn sn,
	p.price price,
	p.`enable` `enable`,
	p.create_date create_date
FROM
	product p
	LEFT JOIN goods g ON p.goods_sn = g.sn
	WHERE p.sn in (
	    #for(v:productIds)
        #para(v)
        #if(!for.last)
        ,
        #end
      #end
	)
#end