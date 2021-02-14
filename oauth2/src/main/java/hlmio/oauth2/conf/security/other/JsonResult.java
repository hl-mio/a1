package hlmio.oauth2.conf.security.other;

import lombok.Getter;

import java.nio.file.AccessDeniedException;

@Getter
public class JsonResult {

	private String code;
	private String msg;
	private Object data;

	// -- 构造函数
	public JsonResult() {
		this.setCode(ResultCode.SUCCESS);
		this.setMsg(ResultCode.SUCCESS.msg());
	}
	public JsonResult(ResultCode code) {
		this.setCode(code);
		this.setMsg(code.msg());
	}
	public JsonResult(ResultCode code, Object data) {
		this.setCode(code);
		this.setMsg(code.msg());
		this.setData(data);
	}

	// -- 属性方法
	public JsonResult setCode(ResultCode code) {
		this.code = code.val();
		return this;
	}
	public JsonResult setCode(String code) {
		this.code = code;
		return this;
	}

	public JsonResult setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public JsonResult setData(Object data) {
		this.data = data;
		return this;
	}

	public JsonResult setEnum(ResultCode code) {
		this.setCode(code)
				.setMsg(code.msg());
		return this;
	}
	public JsonResult setEnum(ResultCode code, Object data) {
		this.setCode(code)
				.setMsg(code.msg())
				.setData(data);
		return this;
	}

	public JsonResult setException(UserTipException e) {
		this.setCode(e.getCode())
				.setMsg(e.getMsg());
		return this;
	}

	public JsonResult setException(AccessDeniedException e) {
		this.setCode(ResultCode.NO_ACCESS)
				.setMsg(e.getMessage());
		return this;
	}

}
