#sql("updateValueByKey")
  update sys_config set param_value = #para(paramValue) where param_key = #para(paramKey)
#end