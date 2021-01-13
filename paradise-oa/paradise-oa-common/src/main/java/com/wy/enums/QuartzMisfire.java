package com.wy.enums;

/**
 * 定时任务失败时的策略
 * 
 * @author ParadiseWY
 * @date 2020年4月7日 下午3:45:16
 */
public enum QuartzMisfire {

	/** 默认 */
	MISFIRE_DEFAULT(-1),
	/** 立即触发执行 */
	MISFIRE_FIRE_AND_PROCEED(0),
	/** 触发一次执行 */
	MISFIRE_DO_NOTHING(1),
	/** 不触发 */
	MISFIRE_IGNORE_MISFIRES(2);

	private int policy;

	private QuartzMisfire(int policy) {
		this.policy = policy;
	}

	public int getPolicy() {
		return policy;
	}

	public static QuartzMisfire getValue(int ordinal) {
		for (QuartzMisfire quartzMisfire : QuartzMisfire.values()) {
			if (quartzMisfire.ordinal() == ordinal) {
				return quartzMisfire;
			}
		}
		return null;
	}
}