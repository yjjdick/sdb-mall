package io.sdb.dao;

import com.jfinal.plugin.activerecord.Db;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.enums.SnEnum;
import io.sdb.model.DeliveryCorp;
import io.sdb.model.Sn;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Dao - 序列号
 * 
 * 
 */
@Component
public class DeliveryCorpDao extends BaseDao<DeliveryCorp> {

	/**
	 * 构造方法
	 */
	public DeliveryCorpDao() {
		super(DeliveryCorp.class);
	}
}