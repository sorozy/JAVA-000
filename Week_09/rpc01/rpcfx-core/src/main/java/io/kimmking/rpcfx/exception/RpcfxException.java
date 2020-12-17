package io.kimmking.rpcfx.exception;


public class RpcfxException extends Exception {

    private Integer code;

    public RpcfxException(Integer code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    public RpcfxException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public RpcfxException(String msg, Throwable e) {
        super(msg, e);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
