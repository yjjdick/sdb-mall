#sql("findList")
  SELECT * FROM #(tableName) where 1=1
  #for(filter:filters)

    #if(!filter.getProperty()??)
      #continue
    #end

    #if(filter.getOperator().name() == "eq")
      #if(filter.getValue()??)
        #(filter.getWhereOptStr()) #(filter.getProperty()) = #para(filter.getValue())
      #else
        #(filter.getWhereOptStr()) #(filter.getProperty()) IS NULL
      #end
    #elseif(filter.getOperator().name() == "ne")
      #if(filter.getValue()??)
        #(filter.getWhereOptStr()) #(filter.getProperty()) != #para(filter.getValue())
      #else
        #(filter.getWhereOptStr()) #(filter.getProperty()) IS NOT NULL
      #end
    #elseif(filter.getOperator().name() == "gt")
      #(filter.getWhereOptStr()) #(filter.getProperty()) > #para(filter.getValue())
    #elseif(filter.getOperator().name() == "lt")
      #(filter.getWhereOptStr()) #(filter.getProperty()) < #para(filter.getValue())
    #elseif(filter.getOperator().name() == "ge")
      #(filter.getWhereOptStr()) #(filter.getProperty()) >= #para(filter.getValue())
    #elseif(filter.getOperator().name() == "le")
      #(filter.getWhereOptStr()) #(filter.getProperty()) <= #para(filter.getValue())
    #elseif(filter.getOperator().name() == "like")
      #(filter.getWhereOptStr()) #(filter.getProperty()) like concat('%', #para(filter.getValue()), '%')
    #elseif(filter.getOperator().name() == "isNull")
      #(filter.getWhereOptStr()) #(filter.getProperty()) IS NULL
    #elseif(filter.getOperator().name() == "isNotNull")
      #(filter.getWhereOptStr()) #(filter.getProperty()) IS NOT NULL
    #elseif(filter.getOperator().name() == "in")
      #(filter.getWhereOptStr()) #(filter.getProperty()) in (
      #for(v:filter.getValue())
        #para(v)
        #if(!for.last)
        ,
        #end
      #end
      )
    #end
  #end

  #for(order: orders)
      #if(for.first)
        order by
      #end
      #if(!for.first)
        ,
      #end
      #(order.getProperty()??) #(order.getDirection().name()??)
    #end
#end

#sql("deleteBatch")
  DELETE FROM #(tableName) where
  #for(i = 0;i<primaryKeys.length;i++)
    #if(primaryKeys[i])
      #(primaryKeys[i]) in (
        #for(id:ids[i])
          #if(!for.first)
            ,
          #end
          #(id)
        #end
      )
    #end
  #end
#end

#sql("findByModel")
  SELECT * FROM #(tableName) WHERE 1=1
  #for(v:record)
    AND #(v.key) = #para(v.value)
  #end
#end

#sql("deleteByModel")
  DELETE FROM #(tableName) WHERE 1=1
  #for(v:record)
    AND #(v.key) = #para(v.value)
  #end
#end