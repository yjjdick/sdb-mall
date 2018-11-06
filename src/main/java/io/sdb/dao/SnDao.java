package io.sdb.dao;

import com.jfinal.plugin.activerecord.Db;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.enums.SnEnum;
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
public class SnDao extends BaseDao<Sn> {
	
	/**
	 * 构造方法
	 */
	public SnDao() {
		super(Sn.class);
	}

	private String goodsPrefix = "yyyyMMdd";
	private int goodsMaxLo = 100;
	private String productPrefix = "yyyyMMdd";
	private int productMaxLo = 100;
	private String orderMasterPrefix = "yyyyMMdd";
	private int orderMasterMaxLo = 100;
	private String orderDetailPrefix = "yyyyMMdd";
	private int orderDetailMaxLo = 100;
	private String userPrefix = "yyyyMMdd";
	private int userMaxLo = 100;

	/** 货品编号生成器 */
	private HiloOptimizer goodsHiloOptimizer = new HiloOptimizer(SnEnum.GOODS, goodsPrefix, goodsMaxLo);
	private HiloOptimizer productHiloOptimizer = new HiloOptimizer(SnEnum.PRODUCT, productPrefix, productMaxLo);
	private HiloOptimizer orderMasterHiloOptimizer = new HiloOptimizer(SnEnum.ORDER_MASTER, orderMasterPrefix, orderMasterMaxLo);
	private HiloOptimizer orderDetailHiloOptimizer = new HiloOptimizer(SnEnum.ORDER_DETAIL, orderDetailPrefix, orderDetailMaxLo);
	private HiloOptimizer userHiloOptimizer = new HiloOptimizer(SnEnum.USER, userPrefix, userMaxLo);

	/**
	 * 生成序列号
	 * 
	 * @param type
	 *            类型
	 * @return 序列号
	 */
	public String generate(SnEnum type) {
		switch (type) {
			case GOODS:
				return goodsHiloOptimizer.generate();
			case PRODUCT:
				return productHiloOptimizer.generate();
			case ORDER_MASTER:
				return orderMasterHiloOptimizer.generate();
			case ORDER_DETAIL:
				return orderDetailHiloOptimizer.generate();
			case USER:
				return userHiloOptimizer.generate();
		}
		return null;
	}

	/**
	 * 获取末值
	 * 
	 * @param type
	 *            类型
	 * @return 末值
	 */
	@JFinalTx
	private long getLastValue(SnEnum type) {
		String sql = "SELECT * FROM sn WHERE type = ?";
		Sn sn = modelManager.findFirst(sql, type.getCode());
		long lastValue = sn.getLastValue();
		String updateSql = "UPDATE sn SET last_value = ? WHERE type = ? AND last_value = ?";
		int result = Db.update(updateSql, lastValue + 1, type.getCode(), lastValue);
		return 0 < result ? lastValue : getLastValue(type);
	}

	
	/**
	 * 高低位算法生成器
	 */
	private class HiloOptimizer {

		/** 类型 */
		private SnEnum type;

		/** 前缀 */
		private String prefix;

		/** 最大低位值 */
		private int maxLo;

		/** 低位值 */
		private int lo;

		/** 高位值 */
		private long hi;

		/** 末值 */
		private long lastValue;

		/**
		 * 构造方法
		 * 
		 * @param type
		 *            类型
		 * @param prefix
		 *            前缀
		 * @param maxLo
		 *            最大低位值
		 */
		public HiloOptimizer(SnEnum type, String prefix, int maxLo) {
			this.type = type;
			this.prefix = prefix != null ? prefix : "";
			this.maxLo = maxLo;
			this.lo = maxLo + 1;
		}

		public String getDate(String pattern) {
			return DateFormatUtils.format(new Date(), pattern);
		}

		/**
		 * 生成序列号
		 * 
		 * @return 序列号
		 */
		public synchronized String generate() {
			if (lo > maxLo) {
				lastValue = getLastValue(type);
				lo = lastValue == 0 ? 1 : 0;
				hi = lastValue * (maxLo + 1);
			}
			return this.getDate(prefix) + type.getCode() + (hi + lo++);
		}
	}
}