package hlmio.oauth2.conf.security.other;


import lombok.Data;

@Data
public class UserTipException extends RuntimeException {

    protected String code;
    protected String msg;

    public UserTipException() {
        super();
        this.code = "600";
    }

    public UserTipException(String code, String msg) {
        super(msg);
        if(code==null||"".equals(code)){
            code = "690";
        }
        this.code = code;
        this.code = msg;
    }

    public UserTipException(String msg) {
        super(msg);
        this.code = "690";
        this.msg = msg;
    }

}
