package hlmio.oauth2.conf.security.other;

public enum ResultCode {

	/** 成功 */
	SUCCESS("200", "成功"),

	/** 数据为空 */
	SUCCESS_NO_DATA("201", "数据为空"),

	/** 用户不存在 */
	SUCCESS_NO_USER("202", "用户不存在"),

	/** 今天的量已经跑光了 */
	SUCCESS_ALL_GONE("203", "今天的量已经跑光了"),

	/** 失败，请重试 */
	SUCCESS_FAIL("204", "失败，请重试"),


	/** 鉴权未通过 */
	NO_AUTH("300", "鉴权未通过"),

	/** 登录未通过 */
	NO_LOGIN("310", "登录未通过"),
	NO_LOGIN_USERNAME("311", "用户名错误"),
	NO_LOGIN_PASSWORD("312", "密码错误"),

	/** 权限未通过 */
	NO_ACCESS("320", "权限未通过"),


	ERROR_UNKNOW("900", "服务器内部错误"),
	END_CODE("999","占位符");

	private ResultCode(String val, String msg) {
		this.val = val;
		this.msg = msg;
	}

	public String val() {
		return val;
	}

	public String msg() {
		return msg;
	}

	private String val;
	private String msg;

}
